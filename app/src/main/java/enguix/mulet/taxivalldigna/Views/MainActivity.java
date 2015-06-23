package enguix.mulet.taxivalldigna.Views;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import enguix.mulet.taxivalldigna.Entities.CityEntity;
import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.TaxiDataBase;
import enguix.mulet.taxivalldigna.UtilsRequest;


public class MainActivity extends AppCompatActivity implements ReserveNowFragment.FragmentNowReserveListener,View.OnClickListener {
public static final String PROPERTY_REG_DB= "DataBase";
    public static final String TAG_TIME="times";
    public static final String TAG_GO_BACK ="go_back";
    ArrayList<CityEntity> cities;
    Button call;
    boolean newuser;//a true va a registre usuari, a false va a validar usuari
    String user;
    String pass;
    private Toolbar toolbar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call = (Button)findViewById(R.id.Btncall);
        call.setOnClickListener(this);

        if(checkConection(this)) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ReserveNowFragment fragment = new ReserveNowFragment();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }else{
           /* FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ConnectionFragment fragment = new ConnectionFragment();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();*/
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_taxi_car);

        SharedPreferences prefs =
                getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        boolean checkDB = prefs.getBoolean(PROPERTY_REG_DB, false);


        SharedPreferences prefsUser = getSharedPreferences(
                SignInActivity.PREFERENCES_USER,
                Context.MODE_PRIVATE);

        user = prefsUser.getString(SignInActivity.PROPERTY_REG_USER, "NO");
        pass = prefsUser.getString(SignInActivity.PROPERTY_REG_PASSWORD, "NO");

        if(user.equals("NO")){
            newuser = true;
        }else{
            newuser = false;
        }


       // TaxiOpenHelper connection = new TaxiOpenHelper(this);
        if(!checkDB) {
            UtilsRequest connect = new UtilsRequest(this);
            connect.getAllCities();
        }
        cities = new ArrayList<>();

    }

    private void call(String phone) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing", "Call failed", activityException);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lists) {
            Intent inte = new Intent(this,TripsActivity.class);
            startActivity(inte);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean checkConection(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < redes.length; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    @Override
    public void nowActivity() {

        if(newuser) {
            intent = new Intent(this, SignInActivity.class);
        }else {

            SharedPreferences prefsUser = getSharedPreferences(
                    LogInActivity.PREFS_SESSION,
                    Context.MODE_PRIVATE);

            String userr = prefsUser.getString(LogInActivity.PREFS_SESSION_USER, "NO");
            String key = prefsUser.getString(LogInActivity.PREFS_SESSION_KEY, "NO");

            Log.i("PREF_SESSION","user="+userr+" key="+key);


            if(userr.equals("NO") || key.equals("NO")){
                intent = new Intent(this,LogInActivity.class);
            }else {
                intent = new Intent(this,GoActivity.class);
            }

        }

        dialogTaxi(true);







        Toast.makeText(this,"Now",Toast.LENGTH_SHORT).show();



    }

    @Override
    public void reserveActivity() {
        Toast.makeText(this,"Reserve",Toast.LENGTH_SHORT).show();

        if(newuser) {
            intent = new Intent(this, SignInActivity.class);
        }else {


            SharedPreferences prefsUser = getSharedPreferences(
                    LogInActivity.PREFS_SESSION,
                    Context.MODE_PRIVATE);

            String userr = prefsUser.getString(LogInActivity.PREFS_SESSION_USER, "NO");
            String key = prefsUser.getString(LogInActivity.PREFS_SESSION_KEY, "NO");

            Log.i("PREF_SESSION","user="+userr+" key="+key);


            if(userr.equals("NO") || key.equals("NO")){
                intent = new Intent(this,LogInActivity.class);
            }else {
                intent = new Intent(this,GoActivity.class);
            }





        }

        dialogTaxi(false);


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"Call",Toast.LENGTH_SHORT).show();

        if(cities.isEmpty() || cities==null){
            TaxiDataBase db = new TaxiDataBase(this);
            cities = db.getAllCitiesFrom();
        }



        dialogCall();


/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_city_from));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                Toast.makeText(getApplicationContext(), cities.get(item).getName(), Toast.LENGTH_SHORT).show();
                if (cities.get(item).getId_cia() == 0) {
                    AlertDialog.Builder built = new AlertDialog.Builder(MainActivity.this);
                    built.setTitle(getString(R.string.choose_city_to));

                    int no_cia = 0;
                    for (CityEntity c : cities) {
                        if (c.getId_cia() == 0) {
                            no_cia++;
                        }
                    }

                    final CharSequence[] cities_to = new CharSequence[cities.size() - no_cia];
                    for (int i = 0; i < cities_to.length; i++) {
                        CharSequence city = cities.get(i).getName();
                        cities_to[i] = city;
                    }


                    built.setItems(cities_to, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            Toast.makeText(getApplicationContext(), cities.get(item).getPhone(), Toast.LENGTH_SHORT).show();
                            call(cities.get(item).getPhone());

                        }
                    });
                    AlertDialog alert = built.create();
                    alert.show();
                } else {
                    call(cities.get(item).getPhone());
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/
    }

    public void dialogTaxi(boolean times){
        final boolean act = times;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.questionTrip));

        builder.setNegativeButton(R.string.oneway, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                intent.putExtra(TAG_TIME, act);
                intent.putExtra(TAG_GO_BACK, 1);
                MainActivity.this.startActivity(intent);
            }
        });

        builder.setPositiveButton(R.string.roundtrip, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                intent.putExtra(TAG_TIME, act);
                intent.putExtra(TAG_GO_BACK, 2);
                MainActivity.this.startActivity(intent);
            }
        }).setInverseBackgroundForced(true);
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences prefs = getSharedPreferences(
                LogInActivity.PREFS_SESSION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(LogInActivity.PREFS_SESSION_USER);
        editor.remove(LogInActivity.PREFS_SESSION_KEY);
        editor.commit();



    }

    public void dialogCall(){

        int no_cia=0;
        CharSequence[] items = new CharSequence[cities.size()];

        for(int i = 0; i < cities.size();i++){

            CharSequence item = cities.get(i).toString();
            items[i]=item;


        }



      AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_city_from));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                Toast.makeText(getApplicationContext(), cities.get(item).getName(), Toast.LENGTH_SHORT).show();
                if (cities.get(item).getId_cia() == 0) {
                    AlertDialog.Builder built = new AlertDialog.Builder(MainActivity.this);
                    built.setTitle(getString(R.string.choose_city_to));

                    int no_cia = 0;
                    for (CityEntity c : cities) {
                        if (c.getId_cia() == 0) {
                            no_cia++;
                        }
                    }

                    final CharSequence[] cities_to = new CharSequence[cities.size() - no_cia];
                    for (int i = 0; i < cities_to.length; i++) {
                        CharSequence city = cities.get(i).getName();
                        cities_to[i] = city;
                    }


                    built.setItems(cities_to, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            Toast.makeText(getApplicationContext(), cities.get(item).getPhone(), Toast.LENGTH_SHORT).show();
                            call(cities.get(item).getPhone());

                        }
                    });
                    AlertDialog alert = built.create();
                    alert.show();
                } else {
                    call(cities.get(item).getPhone());
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
