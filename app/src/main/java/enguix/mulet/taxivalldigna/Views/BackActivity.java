package enguix.mulet.taxivalldigna.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import enguix.mulet.taxivalldigna.Entities.CityEntity;
import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.Entities.TripEntity;
import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.TaxiDataBase;
import enguix.mulet.taxivalldigna.UtilsRequest;

public class BackActivity extends AppCompatActivity implements View.OnClickListener, UtilsRequest.ListenerRequest {
    public static final String BACK_ACT = "BackActivity";
    private TripEntity trip_go;
    private TripEntity trip;

    private EditText street_from;
    private EditText city_from;
    private EditText street_to;
    private EditText city_to;

    private EditText coments;

    private TextView hour;
    private TextView date;


    private Button submit;
    private Button map_from;
    private Button map_to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);

        Bundle extra = getIntent().getExtras();
if(savedInstanceState==null) {
    trip_go = extra.getParcelable("trip_go");
    trip = new TripEntity();

    trip.setFrom(trip_go.getTo());
    trip.setTo(trip_go.getFrom());

    trip.setTime(trip_go.getTime());
}else {
    trip= savedInstanceState.getParcelable("trip");
    trip_go= savedInstanceState.getParcelable("trip_go");
}

        street_from= (EditText)findViewById(R.id.address_from);

        city_from = (EditText)findViewById(R.id.city_from);
        street_to= (EditText)findViewById(R.id.address_to);
        city_to = (EditText)findViewById(R.id.city_to);
        coments = (EditText)findViewById(R.id.coments);

        date = (TextView)findViewById(R.id.date_back);
        hour = (TextView)findViewById(R.id.hour_back);
if(savedInstanceState==null) {
    date.setText("  "+trip.date()+"  ");
}else{
    date.setText("  "+savedInstanceState.getString("date")+"  ");
    hour.setText("  "+savedInstanceState.getString("hour")+"  ");
}

try{

    city_from.setText(trip.getFrom().getCity());
    city_to.setText(trip.getTo().getCity());
    street_from.setText(trip.getFrom().getStreets_name());
    street_to.setText(trip.getTo().getStreets_name());

    city_to.setEnabled(false);
    city_from.setEnabled(false);

}catch (Exception e){
    Log.e("ERRor trip_go", "Error setText()");
}

        submit = (Button)findViewById(R.id.submit_trip);
        map_from = (Button)findViewById(R.id.Btnmap_from);
        map_to = (Button)findViewById(R.id.Btnmap_to);

        submit.setOnClickListener(this);
        map_from.setOnClickListener(this);
        map_to.setOnClickListener(this);

        hour.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
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

    public void showTimePickerDialog(View v){
        TimePickerFragment newFragment = new TimePickerFragment();
        Log.v("RESERVA", "timepiker():" + Thread.currentThread().getId());
        newFragment.setHora((TextView) v);
        // newFragment.setActivity(this);
        newFragment.setTrip(trip);
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){


            case R.id.hour_back:
                showTimePickerDialog(v);

                break;

            case R.id.Btnmap_from:

                intent = new Intent(this,MapsActivity.class);
                intent.putExtra(GoActivity.KEY_MAP,GoActivity.ACC_FROM);
                intent.putExtra(GoActivity.VALUE_FROM, trip.getFrom());
                intent.putExtra(GoActivity.VALUE_TO, trip.getTo());
                intent.putExtra(GoActivity.KEY_CAMERA, trip.getFrom().getCoordenates());
                intent.putExtra(GoActivity.FROM_ACTIVITY,BACK_ACT);
                startActivityForResult(intent, GoActivity.CLICK_FROM);
                // this.startActivity(intent);
                break;

            case R.id.Btnmap_to:
                intent = new Intent(this,MapsActivity.class);
                intent.putExtra(GoActivity.KEY_MAP,GoActivity.ACC_TO);
                intent.putExtra(GoActivity.VALUE_FROM, trip.getFrom());
                intent.putExtra(GoActivity.VALUE_TO, trip.getTo());
                intent.putExtra(GoActivity.KEY_CAMERA, trip.getTo().getCoordenates());
                intent.putExtra(GoActivity.FROM_ACTIVITY,BACK_ACT);
                startActivityForResult(intent, GoActivity.CLICK_TO);
                //this.startActivity(intent);
                break;
            case R.id.submit_trip:
                submit();
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("trip_go", trip_go);
        outState.putParcelable("trip", trip);
        outState.putString("hour", hour.getText().toString());
        outState.putString("date", date.getText().toString());
    }

    public void submit(){

        UtilsRequest request = new UtilsRequest(this);
        request.setResponses(this);
        trip.setComents(coments.getText().toString());

        TaxiDataBase dataBase = new TaxiDataBase(this);
        int id_c = dataBase.getIdCity(trip_go.getFrom().getCity());

        trip.getTo().setCity("" + id_c);
        trip_go.getFrom().setCity("" + id_c);
        request.sendTripData(trip_go,trip);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if ((requestCode == GoActivity.CLICK_FROM)) {
                CustomAddress address  = extras.getParcelable(GoActivity.VALUE_FROM);
                if (trip == null) {
                    trip = new TripEntity();
                }
                /*if(go_back==1){

                    TaxiDataBase dataBase = new TaxiDataBase(this);
                    int id_c = dataBase.getIdCity(address.getCity());

                    address.setCity(""+id_c);
                }*/

                trip.setFrom(address);
                street_from.setText(trip.getFrom().getStreets_name());
            } else if ((requestCode == GoActivity.CLICK_TO)) {

                CustomAddress address  = extras.getParcelable(GoActivity.VALUE_TO);
                if (trip == null) {
                    trip = new TripEntity();
                }
                trip.setTo(address);



                street_to.setText(trip.getTo().getStreets_name());
            }
        }
        // Ocultar el teclat
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(street_from.getWindowToken(), 0);
    }

    @Override
    public void response(JSONObject response)  {

        try {
            int su_go = response.getInt("success_go");
            int su_back = response.getInt("success_back");
            if (su_go == 1 && su_back == 1) {

                Toast.makeText(getApplicationContext(), getString(R.string.save_ok), Toast.LENGTH_LONG).show();

            }else if(su_go == 1 && su_back == 0){
                Toast.makeText(getApplicationContext(), getString(R.string.save_one) + getString(R.string.trip_go), Toast.LENGTH_LONG).show();

            }else {

                Toast.makeText(getApplicationContext(),getString(R.string.save_no), Toast.LENGTH_LONG).show();
            }




        }catch (JSONException e){
            Log.e("JSON_TRIPS", "Exception all trips", e);
            Toast.makeText(getApplicationContext(),getString(R.string.error)+e, Toast.LENGTH_LONG).show();
        }finally {
            finish();
        }
    }

    @Override
    public void response(JSONObject response, String mode) {

    }
}
