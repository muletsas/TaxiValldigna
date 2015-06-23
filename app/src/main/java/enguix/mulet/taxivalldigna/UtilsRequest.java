package enguix.mulet.taxivalldigna;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import enguix.mulet.taxivalldigna.Entities.CityEntity;
import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.Entities.CustomRequest;
import enguix.mulet.taxivalldigna.Entities.TripEntity;
import enguix.mulet.taxivalldigna.Entities.UserEntity;
import enguix.mulet.taxivalldigna.Views.LogInActivity;
import enguix.mulet.taxivalldigna.Views.MainActivity;
import enguix.mulet.taxivalldigna.Views.MapsActivity;


/**
 * Created by root on 17/05/15.
 */
public class UtilsRequest {
private AddressResponse respAddress;
    private ListenerRequest responses;
    private Context context;

    private RequestQueue mRequestQueue;

    private static final String URL_BASE = "http://www.taxivalldigna.esy.es/tax/appTaxi/API/ApiRest.php";
    private static final String URL_USER = "?accio=registreUsuari";//"tax/registrarUsuari.php";
    private static final String URL_USER_VALIDATE = "?accio=validarUsuari";
    private static final String URL_CITIES = "?accio=donarCiutats";
    private static final String URL_TRIP = "?accio=registraViatge";
    private static final String URL_TRIPS = "?accio=registraViatges";
    private static final String URL_TRIPS_USER = "?accio=donarViatgesUsuari";
    public static final String PREFERENCES_USER = "user_prefs";

    public static final String PROPERTY_SESSION_USER = "user";
    public static final String PROPERTY_SESSION_KEY = "key";
    public static final String PROPERTY_REG_ID = "registration_id";

    private static final String PARAMS_MAIL = "user";
    private static final String PARAMS_CONTACT = "contact_name";
    private static final String PARAMS_PHONE = "phone";
    private static final String PARAMS_PASSWORD = "password";
    private static final String PARAMS_LANGUAGE = "lang";
    private static final String PARAMS_GCM = "code_gcm";

    public static final String PARAMS_STREET_FROM = "street_from";
    public static final String PARAMS_CITY_FROM = "city_from";
    public static final String PARAMS_LAT_FROM = "lat_from";
    public static final String PARAMS_LNG_FROM = "lng_from";
    public static final String PARAMS_STREET_TO = "street_to";
    public static final String PARAMS_CITY_TO = "city_to";
    public static final String PARAMS_LAT_TO = "lat_to";
    public static final String PARAMS_LNG_TO = "lng_to";
    public static final String PARAMS_COMENTS = "coments";
    public static final String PARAMS_PASSENGERS = "passengers";
    public static final String PARAMS_TIME = "time";
    public static final String PARAMS_DATE = "date";
    public static final String PARAMS_HOUR = "hour";
    public static final String PARAMS_KMS = "kms";
    public static final String PARAMS_PRICE = "price";

    private static final String JSONARRAY_CITIES = "cities";


    public UtilsRequest(Context context) {
        this.context = context;
        mRequestQueue =  Volley.newRequestQueue(context);
    }

    public void setResponses(ListenerRequest responses) {
        this.responses = responses;
    }


    public HashMap<String,String> putKey(){

        HashMap <String,String> params = new HashMap<>();

        SharedPreferences prefs = context.getSharedPreferences(
                LogInActivity.PREFS_SESSION,
                Context.MODE_PRIVATE);

        String user = prefs.getString(LogInActivity.PREFS_SESSION_USER, "demo");
        String key = prefs.getString(LogInActivity.PREFS_SESSION_KEY, null);

        params.put(PROPERTY_SESSION_USER, user);//user);
        if(key != null){
            params.put(PROPERTY_SESSION_KEY,key);
        }

        return params;
    }


    public void sendUsersData(UserEntity user){
        // Mapeo de los pares clave-valor
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAMS_MAIL, user.getUser());
        params.put(PARAMS_CONTACT, user.getContact_name());
        params.put(PARAMS_PHONE, user.getPhone());
        params.put(PARAMS_PASSWORD, user.getPassword());

       String lang = Locale.getDefault().getLanguage();
        params.put(PARAMS_LANGUAGE, lang);
        SharedPreferences prefs =
                context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String gcm = prefs.getString(PROPERTY_REG_ID, "NO");


        params.put(PARAMS_GCM, gcm);

        final UserEntity usser = user;

        CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
                URL_BASE + URL_USER, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Log.d("Response: ", response.toString());

                    responses.response(response);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int succes = -1;
/*

                String respon ="";
                try {
                    succes = response.getInt("success");
                    if(succes==1) {

                        SharedPreferences prefs = context.getSharedPreferences(
                                PREFERENCES_USER,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(PROPERTY_REG_USER, usser.getUser());
                        editor.putString(PROPERTY_REG_PASSWORD, usser.getPassword());
                        editor.commit();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON ","error "+e);
                }
*/
                Log.i("JSON ", "success="+succes);
                if(succes == 1){
                    Toast.makeText(context, "Guardat correctament", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"No s'ha guardat",Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("eRResponse: ", response.toString());
                Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        //mRequestQueue =  Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);

    }

    public void userValidate(UserEntity user){
            // Mapeo de los pares clave-valor
            HashMap<String, String> params = new HashMap<>();
            params.put(PARAMS_MAIL, user.getUser());
            params.put(PARAMS_PASSWORD, user.getPassword());

            String lang = Locale.getDefault().getLanguage();
            params.put(PARAMS_LANGUAGE, lang);
            SharedPreferences prefs =
                    context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
            String gcm = prefs.getString(PROPERTY_REG_ID,"NO");


            params.put(PARAMS_GCM, gcm);

            final UserEntity usser = user;

            CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
                    URL_BASE + URL_USER_VALIDATE, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        responses.response(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError response) {
                    Log.d("ErroResponse: ", response.toString());
                    Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                }
            });
            //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
            //mRequestQueue =  Volley.newRequestQueue(context);
            mRequestQueue.add(jsObjRequest);
    }




    public void sendTripData(TripEntity trip){
        // Mapeo de los pares clave-valor
        HashMap<String, String> params = putKey();

                trip.prepareParameters(params);
        //new HashMap<>();
       /* params.put(PARAMS_STREET_FROM, trip.getFrom().getStreets_name());
        params.put(PARAMS_CITY_FROM, trip.getFrom().getCity());
        params.put(PARAMS_LAT_FROM, ""+trip.getFrom().getCoordenates().latitude);
        params.put(PARAMS_LNG_FROM, ""+trip.getFrom().getCoordenates().longitude);
        params.put(PARAMS_STREET_TO, trip.getTo().getStreets_name());
        params.put(PARAMS_CITY_TO, trip.getTo().getCity());
        params.put(PARAMS_LAT_TO, ""+trip.getTo().getCoordenates().latitude);
        params.put(PARAMS_LNG_TO, ""+trip.getTo().getCoordenates().longitude);
        params.put(PARAMS_COMENTS, trip.getComents());
        params.put(PARAMS_PASSENGERS, ""+trip.getPassengers());
        params.put(PARAMS_DATE,trip.getDate().toString());
        params.put(PARAMS_HOUR, trip.getHour());
        //params.put(PARAMS_KMS, ""+trip.getKms());
        //params.put(PARAMS_PRICE, ""+trip.getPrice());*/


        Log.i("PARAMStrip","size()="+params.size());

        Log.i("VALU", params.values().toString());

        CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
                URL_BASE + URL_TRIP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.d("Response: ","success== "+ response.getString("success"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                int succes = -1;
                String respon ="";
                try {
                    succes = response.getInt("success");


                    responses.response(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON ", "error " + e);
                }

                /*Log.i("JSON ", "success=" + succes);
                if(succes == 1){
                    Toast.makeText(context, "Guardat correctament", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"No s'ha guardat",Toast.LENGTH_LONG).show();
                }*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("ERR_TRIP: ", response.toString() + " msg=" + response.getMessage());


            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue =  Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);

    }

    public void sendTripData(TripEntity trip_go, TripEntity trip_back){
        // Mapeo de los pares clave-valor
       // HashMap<String, String> params = trip.prepareParameters();
        //new HashMap<>();
       /* params.put(PARAMS_STREET_FROM, trip.getFrom().getStreets_name());
        params.put(PARAMS_CITY_FROM, trip.getFrom().getCity());
        params.put(PARAMS_LAT_FROM, ""+trip.getFrom().getCoordenates().latitude);
        params.put(PARAMS_LNG_FROM, ""+trip.getFrom().getCoordenates().longitude);
        params.put(PARAMS_STREET_TO, trip.getTo().getStreets_name());
        params.put(PARAMS_CITY_TO, trip.getTo().getCity());
        params.put(PARAMS_LAT_TO, ""+trip.getTo().getCoordenates().latitude);
        params.put(PARAMS_LNG_TO, ""+trip.getTo().getCoordenates().longitude);
        params.put(PARAMS_COMENTS, trip.getComents());
        params.put(PARAMS_PASSENGERS, ""+trip.getPassengers());
        params.put(PARAMS_DATE,trip.getDate().toString());
        params.put(PARAMS_HOUR, trip.getHour());
        //params.put(PARAMS_KMS, ""+trip.getKms());
        //params.put(PARAMS_PRICE, ""+trip.getPrice());*/
        HashMap<String,String> params = putKey();

        trip_go.prepareParameters("go", params);
        trip_back.prepareParameters("back", params);



        CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
                URL_BASE + URL_TRIPS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.d("Response: ", response.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                try {



                    responses.response(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON ","error "+e);
                }

               /* Log.i("JSON ","success="+succes);
                if(succes == 1){
                    Toast.makeText(context, "Guardat correctament", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"No s'ha guardat",Toast.LENGTH_LONG).show();
                }*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue =  Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);

    }

    public void getTripsUser(){

        HashMap<String,String> params = putKey();



        CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
                URL_BASE + URL_TRIPS_USER, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<TripEntity> journeys = new ArrayList<>();
                try {
                    JSONArray trips = response.getJSONArray("trips");

                    int id_before = 0;

                    for (int i = 0; i < trips.length(); i++) {

                        JSONObject tripObj = trips.getJSONObject(i);
                        TripEntity tripEnt = new TripEntity();

                        tripEnt.set_id(tripObj.getInt("id"));

                        if(tripEnt.get_id()==id_before){
                            tripEnt.setType(2);
                        }else {
                            tripEnt.setType(1);
                        }

                        id_before = tripEnt.get_id();

                        tripEnt.setTime(tripObj.getString("date"));

                        CustomAddress from = new CustomAddress();
                        from.setStreets_name(tripObj.getString("street_from"));
                        from.setCity(tripObj.getString("city_from"));
                        from.setCoordenates(new LatLng(tripObj.getDouble("lat_from"), tripObj.getDouble("lng_from")));

                        tripEnt.setFrom(from);

                        CustomAddress to = new CustomAddress();
                        to.setStreets_name(tripObj.getString("street_to"));
                        to.setCity(tripObj.getString("city_to"));
                        to.setCoordenates(new LatLng(tripObj.getDouble("lat_to"), tripObj.getDouble("lng_to")));

                        tripEnt.setTo(to);

                        tripEnt.setComents(tripObj.getString("coments"));

                        String kms = tripObj.getString("kms");
                        String price = tripObj.getString("price");

                        Log.i("INSERT_TRIPS", kms + " kms "+ price + " euros");
                        tripEnt.setKms(Float.parseFloat(kms));
                        tripEnt.setPrice(Float.parseFloat(price));

                        journeys.add(tripEnt);
                    }

            } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON ","error "+e);
                }

                TaxiDataBase db = new TaxiDataBase(context);
                if(!journeys.isEmpty()) {
                    db.insertAllTrips(journeys);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue =  Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);





    }



public void getAllCities(){







    CustomRequest jsObjRequest = new CustomRequest( Request.Method.POST,
            URL_BASE + URL_CITIES, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            try {

                Log.d("Response: ", response.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            int succes = -1;
            String respon ="";
            try {
                succes = response.getInt("success");
                if(succes==1) {
                    JSONArray array = response.getJSONArray(JSONARRAY_CITIES);
                    ArrayList<CityEntity> cities = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        CityEntity city = new CityEntity();

                        JSONObject cityObj = array.getJSONObject(i);


                        city.setId(cityObj.getInt(TaxiContract.CitiesTaxis.COLUMN_NAME_ID));
                        city.setName(cityObj.getString(TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME));
                        double lat = cityObj.getDouble(TaxiContract.CitiesTaxis.COLUMN_NAME_LAT);
                        double lng = cityObj.getDouble(TaxiContract.CitiesTaxis.COLUMN_NAME_LNG);

                        city.setCoordenates(new LatLng(lat, lng));
                        city.setId_cia(cityObj.getInt(TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA));
                        city.setCompany(cityObj.getString(TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY));
                        city.setPhone(cityObj.getString(TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE));

                        cities.add(city);
                    }


                    TaxiDataBase db = new TaxiDataBase(context);

                    try {
                        db.insertAllCities(cities);
                    }catch(SQLiteException e){
                        Log.e("InsertAllCities", ""+e);
                    }
                    SharedPreferences prefs = context.getSharedPreferences(
                            MainActivity.class.getSimpleName(),
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(MainActivity.PROPERTY_REG_DB, true);

                    editor.commit();
                }



            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON ","error "+e);
            }

            Log.i("JSON ","success="+succes);
            if(succes == 1){
                Toast.makeText(context, "ciutats guardades", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"No s'ha guardat",Toast.LENGTH_LONG).show();
            }

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError response) {
            Log.d("Response: ", response.toString());
        }
    });
    //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
    mRequestQueue =  Volley.newRequestQueue(context);
    mRequestQueue.add(jsObjRequest);

}

    public void getLocationInfo(LatLng coor) {

        CustomRequest jsObjRequest = new CustomRequest(
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + coor.latitude + "," + coor.longitude + "&sensor=true",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.d("Response: ", response.toString());

                    responses.response(response, MapsActivity.TAG_CLICK);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);


    }

    public void getLocationInfo(String address) {
        String place=null;
        try{
             place = URLEncoder.encode(address, "utf-8");
        }catch (Exception e){

        }


        CustomRequest jsObjRequest = new CustomRequest(
                "http://maps.googleapis.com/maps/api/geocode/json?address="+ place + "&sensor=true&region=ES",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.d("Response: ", response.toString());

                    responses.response(response, MapsActivity.TAG_SEARCH);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);


    }

    public void getLocationInfo(String address , final int type) {
        String place=null;
        try{
            place = URLEncoder.encode(address, "utf-8");
        }catch (Exception e){

        }




        final CustomRequest jsObjRequest = new CustomRequest(
                "http://maps.googleapis.com/maps/api/geocode/json?address="+ place + "&sensor=true&region=ES",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                double lat,lng;

                String number = "";
                String street = "";
                String city = "";

                String c_p = "";

                if(respAddress==null){
                    respAddress = (AddressResponse)context;
                }

                try {
                    String status = response.getString("status").toString();
                    Log.i("status", status);

                    if (status.equalsIgnoreCase("OK")) {
                        JSONArray Results = response.getJSONArray("results");
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
                        String addrees = "-";
                        if(!street.equals("")){
                            addrees = street + ", " + number;
                        }



                        respAddress.getAddress( new CustomAddress(addrees,city,c_p, new LatLng(lat,lng)),type);



                    }
                } catch (Exception e) {
                  respAddress.getAddress(null,type);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        //mRequestQueue.getInstance().addToRequestQueue(jsObjRequest);
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(jsObjRequest);


    }




    public interface ListenerRequest{
        public void response(JSONObject response) throws JSONException;
        public void response(JSONObject response, String mode);

    }

    public interface AddressResponse{
       public void getAddress(CustomAddress address, int type);
    }

}
