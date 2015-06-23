package enguix.mulet.taxivalldigna;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import enguix.mulet.taxivalldigna.Entities.CityEntity;
import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.Entities.TripEntity;
import enguix.mulet.taxivalldigna.Views.SignInActivity;

/**
 * Created by root on 17/05/15.
 */
public class TaxiDataBase {
    private final String TAG= "SQLite";

    private Context context;
    private SQLiteDatabase db;
    private TaxiOpenHelper dbHelper;
private String userTable;


    public TaxiDataBase(Context context) {

        dbHelper = new TaxiOpenHelper(context);
        this.context=context;
        SharedPreferences         prefs_user = context.getSharedPreferences(
                SignInActivity.PREFERENCES_USER,
                Context.MODE_PRIVATE);

        String user = prefs_user.getString(SignInActivity.PROPERTY_REG_USER, "");
        this.userTable = returnUser(user);
    }

    public void newUser(String nameUser){

        db = dbHelper.getWritableDatabase();
        dbHelper.newUser(nameUser,db);
    }

    public void changeUser(String oldUser, String newUser){
        db = dbHelper.getWritableDatabase();
        dbHelper.changeUser(db, oldUser, newUser);
    }

    public void deleteUser(String user){
        db = dbHelper.getWritableDatabase();
        dbHelper.leaveUser(user, db);
    }

    public boolean updateTableUser(String user){
        try {
            db = dbHelper.getWritableDatabase();
            dbHelper.updateTableTrips(db, user);
        }catch (SQLiteException ex){
            return false;
        }
        return true;
    }
    public boolean updateTableUser(){
        try {
            db = dbHelper.getWritableDatabase();
            dbHelper.updateTableTrips(db, userTable);
        }catch (SQLiteException ex){
            return false;
        }
        return true;
    }

    private Calendar convertDate(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal  = Calendar.getInstance();

        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return Calendar.getInstance();
        }

        return cal;
    }


    public void insertTrip(TripEntity trip , boolean one ) {

        Log.i("INSERT_TRIP", trip.toString());
        if(one) {
            db = dbHelper.getWritableDatabase();
        }
        if(db != null){
            ContentValues valors = new ContentValues();
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_ID, trip.get_id());
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_DATE,trip.dataSQL());//format
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_STREET_FROM, trip.getFrom().getStreets_name());
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_CITY_FROM, trip.getFrom().getCity());
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_LAT_FROM, trip.getFrom().getCoordenates().latitude);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_LNG_FROM, trip.getFrom().getCoordenates().longitude);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_STREET_TO, trip.getTo().getStreets_name());
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_CITY_TO, trip.getTo().getCity());
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_LAT_TO, trip.getTo().getCoordenates().latitude);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_LNG_TO, trip.getTo().getCoordenates().longitude);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_COMENTS, trip.getComents());
            double kms =(double)trip.getKms();double price = (double) trip.getPrice();
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_KMS, kms);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_PRICE, price);
            valors.put(TaxiContract.UserTrips.COLUMN_NAME_TYPE, trip.getType());
            Log.i("INSERT_TRIP "+trip.get_id(), "k="+kms+" p="+price);
            db.insert(this.userTable + "_" + TaxiContract.UserTrips.TABLE_NAME, null, valors);

            if(one) {
                db.close();
            }
        }
    }

    public boolean insertAllTrips(ArrayList<TripEntity> trips){



        db = dbHelper.getWritableDatabase();
        if(db != null) {
            for (TripEntity trip : trips) {
                insertTrip(trip, false );
            }
            db.close();
            return true;
        }else {
            return false;
        }


    }

    public ArrayList<TripEntity> getAllTrips() {
        TripEntity trip=null;
        ArrayList<TripEntity> trips = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM "+this.userTable+"_"+TaxiContract.UserTrips.TABLE_NAME;

        Cursor c = db.rawQuery(query, null);

 /*       String[] get_values = { //TaxiContract.UserTrips.COLUMN_NAME_ID,
                TaxiContract.UserTrips.COLUMN_NAME_ID,
                TaxiContract.UserTrips.COLUMN_NAME_DATE,
                TaxiContract.UserTrips.COLUMN_NAME_STREET_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_CITY_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_LAT_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_LNG_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_STREET_TO,
                TaxiContract.UserTrips.COLUMN_NAME_CITY_TO,
                TaxiContract.UserTrips.COLUMN_NAME_LAT_TO,
                TaxiContract.UserTrips.COLUMN_NAME_LNG_TO,
                TaxiContract.UserTrips.COLUMN_NAME_COMENTS,
        };
        Cursor c = db.query(this.userTable+"_"+TaxiContract.UserTrips.TABLE_NAME,
                get_values,
                null,
                null, null, null, null, null);

                 public static final String COLUMN_NAME_ID = "id_trip";1

        public static final String COLUMN_NAME_DATE = "date";2

        public static final String COLUMN_NAME_STREET_FROM = "street_from";3
        public static final String COLUMN_NAME_CITY_FROM = "city_from";4
        public static final String COLUMN_NAME_LAT_FROM = "lat_from";5
        public static final String COLUMN_NAME_LNG_FROM = "lng_from";6
        public static final String COLUMN_NAME_STREET_TO = "street_to";7
        public static final String COLUMN_NAME_CITY_TO = "city_to";8
        public static final String COLUMN_NAME_LAT_TO = "lat_to";9
        public static final String COLUMN_NAME_LNG_TO = "lng_to";10
        public static final String COLUMN_NAME_COMENTS = "coments";11
        public static final String COLUMN_NAME_KMS = "kms";12
        public static final String COLUMN_NAME_PRICE = "price";13





                */
        if(c != null) {
            c.moveToFirst();

            do {

                CustomAddress add_from = new CustomAddress(c.getString(3),c.getString(4),new LatLng(c.getDouble(5),c.getDouble(6)));
                CustomAddress add_to = new CustomAddress(c.getString(7),c.getString(8),new LatLng(c.getDouble(9),c.getDouble(10)));


                trip = new TripEntity(c.getInt(1),  add_from,  add_to, c.getString(11),convertDate(c.getString(2)));
                trip.setKms(c.getFloat(12));
               /* float pr = c.getFloat(13);
                int aux = (int)pr/100;
                pr = aux/100;*/
                trip.setPrice(c.getFloat(13));
                trip.setType(c.getInt(14));
                if(!trips.isEmpty()){
                    if(trip.getTime().compareTo(trips.get(0).getTime()) >= 0){
                        trips.add(0,trip);
                    }else {
                        trips.add(trip);
                    }
                }else {
                    trips.add(trip);
                }

                Log.i("GET_ALL_TRIPS", "id="+trip.get_id()+" "+trip.toString());

            } while (c.moveToNext());

        }
        db.close();
        c.close();
        return trips;
    }




    public ArrayList<TripEntity> getTrip(int id) {
        TripEntity trip=null;
ArrayList<TripEntity> trips = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] get_values = { //TaxiContract.UserTrips.COLUMN_NAME_ID,
                TaxiContract.UserTrips.COLUMN_NAME_ID,
                TaxiContract.UserTrips.COLUMN_NAME_DATE,
                TaxiContract.UserTrips.COLUMN_NAME_STREET_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_CITY_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_LAT_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_LNG_FROM,
                TaxiContract.UserTrips.COLUMN_NAME_STREET_TO,
                TaxiContract.UserTrips.COLUMN_NAME_CITY_TO,
                 TaxiContract.UserTrips.COLUMN_NAME_LAT_TO,
                TaxiContract.UserTrips.COLUMN_NAME_LNG_TO,
                 TaxiContract.UserTrips.COLUMN_NAME_COMENTS,
        };
        Cursor c = db.query(this.userTable + "_" + TaxiContract.UserTrips.TABLE_NAME,
                get_values,
                TaxiContract.CitiesTaxis.COLUMN_NAME_ID + "=" + id,
                null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();

            do {

                CustomAddress add_from = new CustomAddress(c.getString(2),c.getString(3),new LatLng(c.getDouble(4),c.getDouble(5)));
                CustomAddress add_to = new CustomAddress(c.getString(6),c.getString(7),new LatLng(c.getDouble(8),c.getDouble(9)));


                trip = new TripEntity(c.getInt(0),  add_from,  add_to, c.getString(10),convertDate(c.getString(1)));
                trips.add(trip);
            } while (c.moveToNext());

        }
        db.close();
        c.close();
        return trips;
    }

    private String returnUser(String user){
        String name[] = user.split("@");
        if(name != null && name.length >1){
            return name[0];
        }
        return user;
    }


    public boolean insertAllCities(ArrayList<CityEntity> cities) {



       // marca.setId(ficaId());
        db = dbHelper.getWritableDatabase();

        try {


        for(CityEntity city : cities){
        if (db != null) {

            Log.i("city", city.getId()+" "+city.getName()+" "+city.getCompany());

            ContentValues valors = new ContentValues();
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_ID, city.getId());
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME, city.getName());
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_LAT, city.getCoordenates().latitude);
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_LNG, city.getCoordenates().longitude);
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA, city.getId_cia());
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY, city.getCompany());
            valors.put(TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE, city.getPhone());
            db.insert(TaxiContract.CitiesTaxis.TABLE_NAME, null, valors);

        }

        }

            db.close();
            return true;
        }catch (SQLiteException e){
            Log.e(TAG,"AllCities()"+e);
            return false;
        }

    }

    public int getIdCity(String city){
        db = dbHelper.getReadableDatabase();
        int id=-1;
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT "+ TaxiContract.CitiesTaxis.COLUMN_NAME_ID+" FROM "+TaxiContract.CitiesTaxis.TABLE_NAME+
                " WHERE "+TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME+" = \""+city +"\"", null);
        if(c != null) {
            c.moveToFirst();
            id= c.getInt(0);
        }

        Log.i("IDCITY", "id="+id);
        return id;
    }

    private int ficaId(){
        int id;
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT max(_id) FROM Markers ", null);
        if(c != null) {
            c.moveToFirst();
        }
        id= c.getInt(0);
        id++;
        return id;
    }

    public boolean modificarMarca(int id, String nom, String tipus, String comentari, double lat, double lon){

        boolean correcte=false;

        db = dbHelper.getWritableDatabase();

        ContentValues valors = new ContentValues();
        valors.put("tipus",tipus);
        valors.put("nom", nom);
        valors.put("comentari", comentari);
        valors.put("lon", lon);
        valors.put("lat", lat);
        int a=db.update("Markers", valors, "_id=" + id, null);
        if(a==1){
            correcte=true;
        }
        db.close();
        return correcte;
    }

    public void borrarMarca(int id) {


        db = dbHelper.getWritableDatabase();
        db.delete("Markers", "_id=" + id, null);
        db.close();
    }

    public CityEntity getCity(int id) {
        CityEntity city=null;

        db = dbHelper.getReadableDatabase();
        String[] get_values = { TaxiContract.CitiesTaxis.COLUMN_NAME_ID
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LAT
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LNG
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE};
        Cursor c = db.query(TaxiContract.CitiesTaxis.TABLE_NAME,
                            get_values,
                            TaxiContract.CitiesTaxis.COLUMN_NAME_ID+ "=" + id,
                            null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();

        city = new CityEntity(c.getInt(0), c.getString(1), new LatLng(c.getDouble(2),c.getDouble(3)), c.getInt(4), c.getString(5),c.getString(6));
        }
        db.close();
        c.close();
        return city;
    }

    public CityEntity getCitiesCompany(int id_cia) {
        CityEntity city=null;

        db = dbHelper.getReadableDatabase();
        String[] get_values = { TaxiContract.CitiesTaxis.COLUMN_NAME_ID
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LAT
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LNG
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE};
        Cursor c = db.query(TaxiContract.CitiesTaxis.TABLE_NAME,
                get_values,
                TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA+ "=" + id_cia,
                null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();

            city = new CityEntity(c.getInt(0), c.getString(1), new LatLng(c.getDouble(2),c.getDouble(3)), c.getInt(4), c.getString(5),c.getString(6));
        }
        db.close();
        c.close();
        return city;
    }

    public ArrayList<CityEntity> getCitiesTo() {
        CityEntity city=null;
ArrayList<CityEntity>cities=new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] get_values = { TaxiContract.CitiesTaxis.COLUMN_NAME_ID
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LAT
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LNG
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE};
        Cursor c = db.query(TaxiContract.CitiesTaxis.TABLE_NAME,
                get_values,
                TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA+ " > 0" ,
                null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();

            do {

                city = new CityEntity(c.getInt(0), c.getString(1), new LatLng(c.getDouble(2),c.getDouble(3)), c.getInt(4), c.getString(5),c.getString(6));
                cities.add(city);
            } while (c.moveToNext());

        }
        db.close();
        c.close();
        return cities;
    }
    public ArrayList<CityEntity> getAllCitiesFrom() {
        CityEntity city=null;
        ArrayList<CityEntity>cities=new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] get_values = { TaxiContract.CitiesTaxis.COLUMN_NAME_ID
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LAT
                , TaxiContract.CitiesTaxis.COLUMN_NAME_LNG
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY
                ,TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE};


        Cursor c = db.query(TaxiContract.CitiesTaxis.TABLE_NAME,
                get_values,
                null,
                null, null, null, null, null);
        if(c != null && c.moveToFirst()) {
           // c.moveToFirst();

            do {

                city = new CityEntity(c.getInt(0), c.getString(1), new LatLng(c.getDouble(2),c.getDouble(3)), c.getInt(4), c.getString(5),c.getString(6));
                cities.add(city);
            } while (c.moveToNext());

        }
        db.close();
        c.close();
        return cities;
    }


/*

    public Marca recuperarMarca(Marker marc) {

        Marca marker=null;


        db = dbHelper.getReadableDatabase();
        // String[] valores_recuperar = {"_id", "nom", "tipus", "comentari", "lat", "lon"};
        //Cursor c = db.query("Markers", valores_recuperar, "lat=" + marc.getPosition().latitude,//+" AND lon="+marc.getPosition().longitude,
        //   null, null, null, null, null);

        Cursor c =  db.rawQuery("select * from Markers", null);// where lat="+marc.getPosition().latitude, null );
        if(c != null) {
            c.moveToFirst();
            marker = new Marca(c.getInt(1), c.getString(2), c.getString(3),
                    c.getString(4), c.getDouble(5), c.getDouble(6));

        }
        Log.v("comença a arreplegar","comensem");
        int i=0;
        while(c.isAfterLast() == false){
            //    marker = new Marca(c.getInt(0), c.getString(1), c.getString(2),
            //           c.getString(3), c.getDouble(4), c.getDouble(5));
            i++;
            //Log.v("marquer "+i,marker.toString());

            //if(marc.getTitle().equals(marker.getNom()+": "+marker.getTipus())){
            //  break;
            //}

            c.moveToNext();
        }

        db.close();
        c.close();
        return marker;
    }

    public Marca donamMarca(Marker marker){
        ArrayList<Marca> markes = obtenirMarques();

        for(Marca marc : markes){
            if(marker.getTitle().equals(marc.getNom()+": "+marc.getTipus())){
                return marc;
            }
        }
        return null;
    }

    public ArrayList<Marca> obtenirMarques(){
        ArrayList<Marca> marques = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        String[] valores_recuperar = {"_id", "nom", "tipus", "comentari", "lat", "lon"};
        Cursor c = db.query("Markers", valores_recuperar,null,
                null, null, null, null, null);
        c.moveToFirst();
        do {

        } while (c.moveToNext());
        db.close();
        c.close();

        return marques;
    }
*/

}
