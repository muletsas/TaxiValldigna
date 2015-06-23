package enguix.mulet.taxivalldigna.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.R;
import enguix.mulet.taxivalldigna.UtilsRequest;

public class MapsActivity extends FragmentActivity implements UtilsRequest.ListenerRequest, View.OnClickListener {
    public static final String ACTION_MAP = "putAddress";
    public static final LatLng COORDENATES_DEFAULT = new LatLng(39.0733815,-0.2643516);
    public static final String TAG_SEARCH = "search";
    public static final String TAG_CLICK = "longClick";

    private CustomAddress currentLocation;
    private CustomAddress addres_from;
    private CustomAddress addres_to;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Button search;
    EditText text;
    private String action;
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


        search = (Button)findViewById(R.id.search);
        text = (EditText)findViewById(R.id.searchText);

        search.setOnClickListener(this);


        Bundle extra = null;

        extra = getIntent().getExtras();

        addres_from = extra.getParcelable(GoActivity.VALUE_FROM);
        addres_to = extra.getParcelable(GoActivity.VALUE_TO);
        action = extra.getString(GoActivity.KEY_MAP);
        activity = extra.getString(GoActivity.FROM_ACTIVITY);
        if(action.equals(GoActivity.ACC_FROM) || activity.equals(BackActivity.BACK_ACT)){

            //si es des de l'origen o ve de BackActivity es ficarà les coordenades de la ciutat

            LatLng coor = extra.getParcelable(GoActivity.KEY_CAMERA);
            if(coor != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor, 14));
            }
            try{
            if(addres_from.getCoordenates() != null && activity.equals(BackActivity.BACK_ACT)){
                mMap.addMarker(new MarkerOptions()
                                .position(addres_from.getCoordenates())
                                .title(getString(R.string.dir_from))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .draggable(true)
                );
            }
            if(addres_to.getCoordenates() != null && activity.equals(BackActivity.BACK_ACT)){
                mMap.addMarker(new MarkerOptions()
                                .position(addres_to.getCoordenates())
                                .title(getString(R.string.dir_to))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .draggable(true)
                );
            }
            }catch (NullPointerException np){
                Log.e("MAPS", "adreça null", np);
            }
            currentLocation = new CustomAddress();
            currentLocation.setCoordenates(coor);
        }else if(activity.equals(GoActivity.GO_ACT)) {

            //si es desti es buscarà les coordenades a google i es ficarà la ciutat de destí
            try {
                if (addres_from.getCoordenates() != null && action.equals(GoActivity.ACC_TO)) {
                    mMap.addMarker(new MarkerOptions()
                                    .position(addres_from.getCoordenates())
                                    .title(getString(R.string.dir_from))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                    );
                }
            }catch (NullPointerException np){
                Log.e("MAPS", "adreça null", np);
            }

            String poblation = extra.getString(GoActivity.KEY_CAMERA);
            UtilsRequest request = new UtilsRequest(getApplicationContext());
            request.setResponses(this);
            request.getLocationInfo(poblation);

        }



        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
               /* String address = getCurrentLocationViaJSON(latLng.latitude,latLng.longitude);
                toast(address);*/
                UtilsRequest request = new UtilsRequest(getApplicationContext());
                request.setResponses(MapsActivity.this);
                request.getLocationInfo(latLng);


            }
        });

        // Ocultar el teclat
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COORDENATES_DEFAULT, 15));
        //mMap.addMarker(new MarkerOptions().position(COORDENATES_DEFAULT).title("Marker"));
    }

    @Override
    public void response(JSONObject response) {


    }

    @Override
    public void response(JSONObject response, String mode) {

        currentLocation = setLocation(response);

        switch (mode){
            case TAG_CLICK:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MapsActivity.this);
                builder.setTitle(getString(R.string.want_save));
                builder.setMessage(currentLocation.toString());

                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "YES");

                                if (action.equals(GoActivity.ACC_FROM)) {
                                    addres_from = currentLocation;
                                    mMap.addMarker(new MarkerOptions()
                                                    .position(addres_from.getCoordenates())
                                                    .title(getString(R.string.dir_from))
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                    .snippet(currentLocation.toString())

                                    );

                                    Intent back = new Intent(MapsActivity.this, GoActivity.class);
                                    back.putExtra(GoActivity.VALUE_FROM, addres_from);
                                    //  back.putExtra(GoActivity.KEY_MAP, activity);
                                    //   back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    //back.setAction(ACTION_MAP);
                                    setResult(RESULT_OK, back);
                                    // setResult(1, back);
                                    // finish();

                                } else if (action.equals(GoActivity.ACC_TO)) {
                                    Intent back = new Intent(MapsActivity.this, GoActivity.class);
                                    addres_to = currentLocation;

                                    mMap.addMarker(new MarkerOptions()
                                                    .position(addres_from.getCoordenates())
                                                    .title(getString(R.string.dir_from))
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                    .snippet(currentLocation.toString())
                                    );




                                    back.putExtra(GoActivity.VALUE_TO, addres_to);
                                    //   back.putExtra(GoActivity.KEY_MAP, activity);
                                    //  back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    // back.setAction(ACTION_MAP);
                                    setResult(RESULT_OK, back);
                                    // setResult(2, back);
                                    // finish();
                                }

                            }
                        });
                builder.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "NO");
                                // toast("no");
                            }
                        });
                builder.show();
                break;


            default:
            case TAG_SEARCH:
                if(currentLocation.getCoordenates() != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation.getCoordenates(), 14));
                }


        }


    }


    public CustomAddress setLocation(JSONObject jsonObj){

        double lat,lng;

        String number = "";
        String street = "";
        String city = "";

        String c_p = "";

       currentLocation = null;

        try {
            String status = jsonObj.getString("status").toString();
            Log.i("status", status);

            if (status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero
                        .getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String type = mtypes.getString(0);

                    switch(type){
                        case "street_number":
                            number = long_name;
                            break;
                        case "route":
                            street =  long_name;
                            break;
                        case "locality":
                            city = long_name;
                            break;
                        case "postal_code":
                            c_p = long_name;
                            break;

                    }

                }
                JSONObject geometry = zero.getJSONObject("geometry");
                Log.i("Geometry string =>", geometry.toString());
                JSONObject location = geometry.getJSONObject("location");
                Log.i("Location string =>", location.toString());


                lat = location.getDouble("lat");
                lng = location.getDouble("lng");


                return new CustomAddress(street + ", " + number,city,c_p, new LatLng(lat,lng));



            }
        } catch (Exception e) {
                return null;
        }

        return null;
    }


    @Override
    public void onClick(View v) {//Search

        UtilsRequest request = new UtilsRequest(getApplicationContext());
        request.setResponses(MapsActivity.this);

        if(activity.equals(BackActivity.BACK_ACT)){

            if(action.equals(GoActivity.ACC_FROM)){
                request.getLocationInfo(text.getText().toString()+","+addres_from.getCity());
            }else {
                request.getLocationInfo(text.getText().toString()+","+addres_to.getCity());
            }


        }else {
            if(action.equals(GoActivity.ACC_FROM)){
                request.getLocationInfo(text.getText().toString()+","+addres_from.getCity());
            }else {
                request.getLocationInfo(text.getText().toString());
            }
        }






    }

    private class MoveCameraToCity extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... params) {


            UtilsRequest request = new UtilsRequest(getApplicationContext());
            request.getLocationInfo(params[0]);

           return null;
        }
    }
}
