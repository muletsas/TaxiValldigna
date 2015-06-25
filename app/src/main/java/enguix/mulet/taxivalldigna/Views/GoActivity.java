package enguix.mulet.taxivalldigna.Views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import enguix.mulet.taxivalldigna.Entities.CityEntity;
import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.TaxiDataBase;
import enguix.mulet.taxivalldigna.Entities.TripEntity;
import enguix.mulet.taxivalldigna.UtilsRequest;


public class GoActivity extends AppCompatActivity implements View.OnClickListener,
        SpinnerFragment.OnFragmentInteractionListener, TextFragment.OnFragmentInteractionListener,
        ReserveTimeFragment.OnFragmentInteractionListener, NowTimeFragment.OnFragmentInteractionListener ,
        UtilsRequest.ListenerRequest, UtilsRequest.AddressResponse {


    public static final String ACC_FROM = "from";
    public static final String ACC_TO = "to";
    public static final String KEY_MAP = "map";
    public static final String FROM_ACTIVITY ="Activity";
    public static final int CLICK_FROM = 1;
    public static final int CLICK_TO = 2;

    public static final String GO_ACT = "GoActivity";

    public static final String VALUE_FROM = "address_from";
    public static final String VALUE_TO = "address_to";
    public static final String KEY_CAMERA = "camera";

    private boolean from_ok;
    private boolean to_ok;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextFragment text_to;
    private SpinnerFragment spinner_to;

    private NowTimeFragment nowFragment;
    private ReserveTimeFragment timFragment;

    private boolean times;//true = now false= reserve
    private int go_back;// 1 = simple 2 = go_back

    private EditText street_from;
    private int id_city_from;
    private Spinner city_from;

    private EditText street_to;
    private String city_to;

    private EditText coments;
    private EditText passengers;

    private ArrayAdapter<String> dataAdapter;
    private ArrayList<CityEntity>cities_from;
    private ArrayList<String> name_cities;

    private ArrayList<String> cities_to;
    private Button submit;
    private Button map_from;
    private Button map_to;

    private TripEntity trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        Bundle extra = null;

            extra = getIntent().getExtras();


        from_ok = false;

        to_ok = false;

        times = extra.getBoolean(MainActivity.TAG_TIME);
        go_back = extra.getInt(MainActivity.TAG_GO_BACK);


        street_from= (EditText)findViewById(R.id.address_from);

        city_from = (Spinner)findViewById(R.id.city_from);
        street_to= (EditText)findViewById(R.id.address_to);
        coments = (EditText)findViewById(R.id.coments);
        passengers=(EditText)findViewById(R.id.n_pas);



        submit = (Button)findViewById(R.id.btn_ok);
        map_from = (Button)findViewById(R.id.Btnmap_from);
        map_to = (Button)findViewById(R.id.Btnmap_to);

        if(go_back==2){
            submit.setText(getString(R.string.next));

        }




        map_from.setOnClickListener(this);
        map_to.setOnClickListener(this);
        submit.setOnClickListener(this);

        if(savedInstanceState == null){
            spinner_to = null;

            TaxiDataBase db = new TaxiDataBase(this);
            cities_from = db.getAllCitiesFrom();
            trip = new TripEntity();
            setSpinner();
            trip.getFrom().setCity(cities_from.get(id_city_from).getName());
        }else{
            trip = savedInstanceState.getParcelable("trip");
            name_cities = savedInstanceState.getStringArrayList("list_cities");
            cities_from = savedInstanceState.getParcelableArrayList("cities_from");
            id_city_from = savedInstanceState.getInt("id_city_from");
            cities_to = savedInstanceState.getStringArrayList("cities_to");
            setSpinner();
            city_from.setSelection(id_city_from);
            city_to =savedInstanceState.getString("city_to");
        }








        city_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                Log.i("listener Spinner onItem", position + " " + cities_from.get(position).toString());
                id_city_from = position;

                if (cities_from.get(position).getId_cia()==0 && text_to != null) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    spinner_to = SpinnerFragment.newInstance(cities_to,0);
                    fragmentTransaction.replace(R.id.container, spinner_to);
                    fragmentTransaction.commit();

                    text_to = null;
                } else if (spinner_to != null) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    text_to = new TextFragment();
                    fragmentTransaction.replace(R.id.container, text_to);
                    fragmentTransaction.commit();

                    spinner_to = null;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        try{

            if(savedInstanceState == null){
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                text_to = new TextFragment();
                fragmentTransaction.replace(R.id.container, text_to);
                fragmentTransaction.commit();

            }else{


                String fragment_to = savedInstanceState.getString("fragment");

                switch (fragment_to){
                    case "text":
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        text_to = TextFragment.newInstance(city_to);
                        fragmentTransaction.replace(R.id.container, text_to);
                        fragmentTransaction.commit();



                        break;

                    default:
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        spinner_to = SpinnerFragment.newInstance(cities_to,savedInstanceState.getInt("city_to"));
                        fragmentTransaction.replace(R.id.container, spinner_to);
                        fragmentTransaction.commit();
                }





            }



            //carrega de fragment horari
            if(!times) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                timFragment = new ReserveTimeFragment();
                transaction.replace(R.id.timerLayout, timFragment);
                transaction.commit();
            }else {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                nowFragment = NowTimeFragment.newInstance(trip.date());
                transaction.replace(R.id.timerLayout, nowFragment);
                transaction.commit();
            }
        }catch (Exception e){
            Log.e("Fragment", e.toString());
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstance) {
        super.onSaveInstanceState(saveInstance);
        //guardamos en la variable t el texto del campo EditText
       /* String dt = data.getText().toString();
        String hr = hora.getText().toString();*/


        //lo "guardamos" en el Bundle
        String dt ="";
        String hr="";
        if(timFragment != null){
            dt = timFragment.getDate();
            hr = timFragment.getHour();
        }else if(nowFragment != null){

            hr = nowFragment.getMinutes();
        }

        //saveInstance.putSerializable("text_to",text_to);

        if(text_to!=null){
            saveInstance.putString("city_to", text_to.getText_to());
            saveInstance.putString("fragment", "text");
        }
        if (spinner_to != null){
            saveInstance.putInt("city_to", spinner_to.getIndex());
            saveInstance.putString("fragment", "spinner");
        }
        saveInstance.putInt("id_city_from",id_city_from);
        saveInstance.putString("date", dt);
        saveInstance.putString("hour", hr);
        saveInstance.putParcelable("trip", trip);
        saveInstance.putStringArrayList("list_cities", name_cities);
        saveInstance.putParcelableArrayList("cities_from", cities_from);
        saveInstance.putStringArrayList("cities_to", cities_to);
    }

    @Override
    protected void onRestoreInstanceState(Bundle restoreInstance) {
        super.onRestoreInstanceState(restoreInstance);
        //recuperamos el String del Bundle

        if(timFragment != null){
           timFragment.setDate(restoreInstance.getString("date"));
            timFragment.setHour(restoreInstance.getString("hour"));
        }else if(nowFragment != null){
            nowFragment.setMinutes(restoreInstance.getString("hour"));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_go, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSpinner(){

        if(name_cities == null){

            name_cities = new ArrayList<>();
            cities_to = new ArrayList<>();

            for(CityEntity city : cities_from){

                if(go_back == 1){
                    name_cities.add(city.getName());
                    if(city.getId_cia() != 0) {
                        cities_to.add(city.getName());
                    }
                }else
                if(city.getId_cia() != 0) {
                    name_cities.add(city.getName());
                }

            }
        }



        if(!(cities_from.isEmpty() || cities_from ==null)  ) {
            dataAdapter = new ArrayAdapter<>(GoActivity.this,
                    android.R.layout.simple_spinner_item, name_cities);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            city_from.setAdapter(dataAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent= null;
        switch (v.getId()){
            case R.id.Btnmap_from:
                trip.getFrom().setCity(cities_from.get(id_city_from).getName());
                trip.getFrom().setStreets_name(street_from.getText().toString());
                intent = new Intent(this,MapsActivity.class);
                intent.putExtra(KEY_MAP,ACC_FROM);
                intent.putExtra(VALUE_FROM, trip.getFrom());
                intent.putExtra(VALUE_TO, trip.getTo());
                intent.putExtra(KEY_CAMERA, cities_from.get(id_city_from).getCoordenates());
                intent.putExtra(FROM_ACTIVITY,GO_ACT);
                startActivityForResult(intent, CLICK_FROM);
               // this.startActivity(intent);
                break;

            case R.id.Btnmap_to:

                intent = new Intent(this,MapsActivity.class);
                intent.putExtra(KEY_MAP,ACC_TO);
                intent.putExtra(VALUE_FROM, trip.getFrom());
                intent.putExtra(VALUE_TO, trip.getTo());
                intent.putExtra(FROM_ACTIVITY,GO_ACT);
                if(text_to != null){
                    city_to = text_to.getText_to();
                }
                intent.putExtra(KEY_CAMERA, city_to);
                startActivityForResult(intent, CLICK_TO);
                //this.startActivity(intent);
                break;
            case R.id.btn_ok:
                submit();



                break;
            default:
                Log.i("CLICK", "onClick()");
        }
        Toast.makeText(this, v.getId() + "", Toast.LENGTH_SHORT).show();

    }

    public void submit(){



        if( checkAdresses() && to_ok && from_ok) {

            sendData();
        }
try {
    Log.i("SUBMIT", trip.toString());
}catch(NullPointerException e){
    Log.e("SUBMIT", "trip.toString()", e);
}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            CustomAddress address=null;
            if ((requestCode == CLICK_FROM)) {
                address  = extras.getParcelable(VALUE_FROM);
                address.setCity(cities_from.get(id_city_from).getName());

            } else if ((requestCode == CLICK_TO)) {

                address  = extras.getParcelable(VALUE_TO);

            }
            putAddress(address,requestCode);
        }


    }

    public void sendData(){
        UtilsRequest request = new UtilsRequest(this);
        request.setResponses(this);

        trip.setComents(coments.getText().toString());

        if (passengers.getText().toString().length() < 1) {
            trip.setPassengers(1);
        } else {
            trip.setPassengers(Integer.parseInt(passengers.getText().toString()));
        }

        if (times) {
            trip.addMinutes(nowFragment.getMin_value());
        }

        if (go_back == 1) {

            TaxiDataBase dataBase = new TaxiDataBase(this);
            int id_c = dataBase.getIdCity(trip.getFrom().getCity());

            trip.getFrom().setCity("" + id_c);

            request.sendTripData(trip);
        } else {

            Intent intent = new Intent(this, BackActivity.class);
            intent.putExtra("trip_go", trip);
            startActivity(intent);
            finish();
        }
    }

public boolean checkAdresses(){
boolean ok_=true;
   // if(street_from.getText().length())

if(trip.getFrom() != null){
    if(!trip.getFrom().checkDirection(street_from.getText().toString(), cities_from.get(id_city_from).getName())){
        UtilsRequest req = new UtilsRequest(this);
        from_ok = false;
        String direction = street_from.getText().toString()+","+ cities_from.get(id_city_from).getName();
        Log.i("CHECK_ADDRESS","dirFrom="+direction);
        req.getLocationInfo(direction, 1);
        ok_= false;
        if(street_from.getText().length() < 4 && cities_from.get(id_city_from).getId_cia() != 0){

            Toast.makeText(this,getString(R.string.check_from),Toast.LENGTH_LONG).show();

        }
    }

    }
    if(spinner_to != null){
      if(!trip.getTo().checkDirection(street_to.getText().toString(), name_cities.get(spinner_to.getIndex()))){
            UtilsRequest req = new UtilsRequest(this);
            to_ok=false;
            String direction = street_to.getText().toString()+","+ name_cities.get(spinner_to.getIndex());
            Log.i("CHECK_ADDRESS","dirTo="+direction);
            req.getLocationInfo(direction, 2);
            ok_= false;
        }
    }else if(text_to != null){
         if(!trip.getTo().checkDirection(street_to.getText().toString(),text_to.getText_to() )){
            UtilsRequest req = new UtilsRequest(this);
            to_ok=false;
            String direction = street_to.getText().toString()+","+ text_to.getText_to();
            Log.i("CHECK_ADDRESS","dirTo="+direction);
            req.getLocationInfo(direction, 2);
            ok_= false;
         }
    }


    return ok_;
}


    @Override
    public void getMinutes(View v) {
        showNumberPikerDialog(v);
    }

    @Override
    public void getHour(View v) {


        showTimePickerDialog(v);
    }

    @Override
    public void getDate(View v) {
        showDatePickerDialog(v);
    }

    public void showTimePickerDialog(View v){
        TimePickerFragment newFragment = new TimePickerFragment();
        Log.v("RESERVA", "timepiker():" + Thread.currentThread().getId());
        newFragment.setHora((TextView) v);
        // newFragment.setActivity(this);
        newFragment.setTrip(trip);
        newFragment.show(getSupportFragmentManager(), getString(R.string.insert_time));

    }
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        Log.v("RESERVA", "datepiker():" + Thread.currentThread().getId());
        newFragment.setData((TextView) v);
        //  newFragment.setActivity(this);
        newFragment.setTrip(trip);
        newFragment.show(getSupportFragmentManager(), getString(R.string.insert_date));
    }

    public void showNumberPikerDialog(View v){
        NumberPickerFragment picker = new NumberPickerFragment();

        picker.setMin((TextView) v);
        picker.show(getSupportFragmentManager(), getString(R.string.insert_min));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void city_to(int pos, String name) {

        city_to = name;
        trip.getTo().setCity(name);
        Toast.makeText(getApplicationContext(),pos+" "+name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void response(JSONObject response) {

        try {
            int succes =  response.getInt("success");
            String msg = response.getString("message");

            if(succes==1){
                Toast.makeText(this,succes+" "+msg, Toast.LENGTH_SHORT).show();
                if(go_back==1){
                    finish();
                }/*else if(go_back==2){
                    Intent intent = new Intent(this,BackActivity.class);
                    intent.putExtra("trip_go", trip);
                    startActivity(intent);
                    finish();
                }*/
            }else {
                Toast.makeText(this,succes+" "+msg, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void response(JSONObject response, String mode) {


    }

    @Override
    public void getAddress(CustomAddress address, int type) {
        if(address != null) {

            putAddress(address, type);
        }
    }

    public void putAddress(CustomAddress address,int type) {
        if (trip == null) {
            trip = new TripEntity();

        }
        if ((type == CLICK_FROM)) {

            trip.setFrom(address);
            street_from.setText(trip.getFrom().getStreets_name());
            from_ok = true;
        } else if (type == CLICK_TO) {



            trip.setTo(address);


            if (text_to != null) {
                text_to.setText_to(trip.getTo().getCity());
            }
            street_to.setText(trip.getTo().getStreets_name());
            to_ok = true;
        }
    }
}
