package enguix.mulet.taxivalldigna.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.Entities.UserEntity;
import enguix.mulet.taxivalldigna.TaxiDataBase;
import enguix.mulet.taxivalldigna.TaxiOpenHelper;
import enguix.mulet.taxivalldigna.UtilsRequest;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener, UtilsRequest.ListenerRequest {
    public static final String PREFERENCES_USER = "user_prefs";
    public static final String PROPERTY_REG_USER = "user";
    public static final String PROPERTY_REG_PASSWORD = "password";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String SENDER_ID = "770418799048";

    private boolean times;
    private int go_back;

    private EditText mail;
    private EditText name;
    private EditText phone;
    private EditText password;
    private Button submit;
    private UserEntity user;

    private GoogleCloudMessaging gcm;

    String regid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Bundle extra = getIntent().getExtras();

        times = extra.getBoolean(MainActivity.TAG_TIME);
        go_back = extra.getInt(MainActivity.TAG_GO_BACK);

        mail = (EditText)findViewById(R.id.etMail);
        name = (EditText)findViewById(R.id.etUserName);
        phone = (EditText)findViewById(R.id.etPhone);
        password = (EditText)findViewById(R.id.etPass);
        submit = (Button)findViewById(R.id.btnSingIn);



        user = new UserEntity();

        registerNotifications();

        submit.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log) {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.putExtra(MainActivity.TAG_TIME, times);
            intent.putExtra(MainActivity.TAG_GO_BACK, go_back);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void registerNotifications(){


        //Chequemos si está instalado Google Play Services
        if(checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(SignInActivity.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId();

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("") || regid.isEmpty()) {
                RegisterTaskGCM tarea = new RegisterTaskGCM();
               tarea.execute("");
                Toast.makeText(getApplicationContext(), "Executa registre", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No se ha encontrado Google Play Services.",Toast.LENGTH_LONG).show();
            Log.i("checkPlayServices", "No se ha encontrado Google Play Services.");
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i("checkPlayServices", "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId() {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(UtilsRequest.PROPERTY_REG_ID, "");


        if (registrationId.length() == 0) {
            //Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        return registrationId;
    }

    @Override
    public void response(JSONObject response) {

        int succes = -1;
        String respon ="";
        try {
            succes = response.getInt("success");
            if(succes==1) {

                SharedPreferences prefs = getSharedPreferences(
                        PREFERENCES_USER,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PROPERTY_REG_USER, user.getUser());
                editor.putString(PROPERTY_REG_PASSWORD, user.getPassword());
                editor.commit();


                TaxiDataBase db = new TaxiDataBase(getApplicationContext());
                db.newUser(user.getUser());


                String key = response.getString("key");
                if(key != null || !key.equals("")) {

                    SharedPreferences prefss = getSharedPreferences(
                            LogInActivity.PREFS_SESSION,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editors = prefss.edit();
                    editor.putString(LogInActivity.PREFS_SESSION_USER, response.getString("user"));
                    editor.putString(LogInActivity.PREFS_SESSION_KEY, response.getString("key"));
                    editors.commit();
                }




                Toast.makeText(getApplicationContext(), getString(R.string.save_ok), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this,GoActivity.class);

                intent.putExtra("action", true);
                intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.startActivity(intent);
                finish();
            }else {

                    Toast.makeText(getApplicationContext(),getString(R.string.save_no),Toast.LENGTH_LONG).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON ","error "+e);
        }


    }

    @Override
    public void response(JSONObject response, String mode) {

    }

    private class RegisterTaskGCM extends AsyncTask<String,Integer,String> {



        // GoogleCloudMessaging gcm;
        @Override
        protected String doInBackground(String... params) {
            String msg = "";

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d("GCM MEssaging", "Registrado en GCM: registration_id=" + regid);
                //  Toast.makeText(getApplicationContext(),"Registrado en GCM: registration_id=" + regid,Toast.LENGTH_LONG).show();
                setRegistrationId(regid);

            } catch (IOException ex) {
                Log.d("RegistreGCM", "Error registro en GCM:" + ex.getMessage());
            }

            return regid;
        }

        protected void onPostExecute(String result) {

            user.setCode_gcm(result);
            // new enviarJson().execute(result);

        }


        private void setRegistrationId(String regId) {
            SharedPreferences prefs = getSharedPreferences(
                    MainActivity.class.getSimpleName(),
                    Context.MODE_PRIVATE);

       //     int appVersion = getAppVersion(context);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(UtilsRequest.PROPERTY_REG_ID, regId);
          //  editor.putInt(UtilsRequest.PROPERTY_APP_VERSION, appVersioncontext);
         //   editor.putLong(PROPERTY_EXPIRATION_TIME, System.currentTimeMillis() );//+ EXPIRATION_TIME_MS

            editor.commit();
        }
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
        Log.i("SignIn", "click Button");
        UtilsRequest utils = new UtilsRequest(getApplicationContext());
        utils.setResponses(this);
        user.setUser(mail.getText().toString());
        user.setContact_name(name.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setPassword(password.getText().toString());

        if(user.validateEmail()){
            utils.sendUsersData(user);
        }else {
            Toast.makeText(this,R.string.mail_no,Toast.LENGTH_LONG).show();
        }




    }
}
