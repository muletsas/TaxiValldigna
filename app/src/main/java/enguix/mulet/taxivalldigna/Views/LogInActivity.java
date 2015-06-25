package enguix.mulet.taxivalldigna.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import enguix.mulet.taxivalldigna.Entities.UserEntity;
import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.TaxiDataBase;
import enguix.mulet.taxivalldigna.UtilsRequest;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener, UtilsRequest.ListenerRequest  {

    public static final String PREFS_SESSION = "session";
    public static final String PREFS_SESSION_USER = "user_s";
    public static final String PREFS_SESSION_KEY = "key_s";

    private boolean times;
    private int go_back;

    private EditText textUser;
    private EditText textPass;
    private Button submit;

    private SharedPreferences prefs_user;
    private String lastUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

       Bundle extra = getIntent().getExtras();

        times = extra.getBoolean(MainActivity.TAG_TIME);
        go_back = extra.getInt(MainActivity.TAG_GO_BACK);

        textUser = (EditText)findViewById(R.id.etMail);
        textPass = (EditText)findViewById(R.id.etPass);
        submit = (Button)findViewById(R.id.btnLogIn);


        prefs_user = getSharedPreferences(
                SignInActivity.PREFERENCES_USER,
                Context.MODE_PRIVATE);

        lastUser = prefs_user.getString(SignInActivity.PROPERTY_REG_USER, "");

        textUser.setText(lastUser);

        submit.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_user) {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra(MainActivity.TAG_TIME, times);
            intent.putExtra(MainActivity.TAG_GO_BACK, go_back);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void response(JSONObject response) {

        int succes = -1;

        try {
            succes = response.getInt("success");
            if(succes==1) {

                String ok_user = response.getString("user");
                SharedPreferences prefs = getSharedPreferences(
                        PREFS_SESSION,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREFS_SESSION_USER, ok_user);
                editor.putString(PREFS_SESSION_KEY, response.getString("key"));
                editor.commit();



                if(!lastUser.equals(ok_user)){
                    SharedPreferences.Editor edit_prefs = prefs_user.edit();
                   // edit_prefs.remove(SignInActivity.PROPERTY_REG_USER);
                    //edit_prefs.remove(SignInActivity.PROPERTY_REG_PASSWORD);

                    edit_prefs.putString(SignInActivity.PROPERTY_REG_USER, ok_user);
                    edit_prefs.putString(SignInActivity.PROPERTY_REG_PASSWORD, this.textPass.getText().toString());
                    edit_prefs.commit();
                    TaxiDataBase db = new TaxiDataBase(getApplicationContext());
                    db.changeUser(lastUser,ok_user);

                }else {
                    TaxiDataBase db = new TaxiDataBase(getApplicationContext());
                    db.updateTableUser(ok_user);
                }

                UtilsRequest gettrips = new UtilsRequest(this);
                gettrips.getTripsUser();


                Intent intent = new Intent(this, GoActivity.class);
                intent.putExtra(MainActivity.TAG_TIME, times);
                intent.putExtra(MainActivity.TAG_GO_BACK, go_back);
                startActivity(intent);
                finish();


            }else {
                textPass.setText("");
                if(!submit.isEnabled()){
                    submit.setEnabled(true);
                }

                Toast.makeText(this,getString(R.string.pass_no),Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON ", "error " + e);
        }



    }

    @Override
    public void response(JSONObject response, String mode) {

    }

    @Override
    public void onClick(View v) {
        submit.setEnabled(false);
        UtilsRequest request = new UtilsRequest(getApplicationContext());
        request.setResponses(this);

        String us = textUser.getText().toString();
        String pa = textPass.getText().toString();

        request.userValidate(new UserEntity(us,pa));


    }
}
