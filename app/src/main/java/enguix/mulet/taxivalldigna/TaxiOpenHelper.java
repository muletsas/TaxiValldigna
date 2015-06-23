package enguix.mulet.taxivalldigna;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 17/05/15.
 */
public class TaxiOpenHelper extends SQLiteOpenHelper{


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TaxiApp.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " NUMBER";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";


    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + TaxiContract.CitiesTaxis.TABLE_NAME + " (" +
                    TaxiContract.CitiesTaxis._ID + INT_TYPE+ " PRIMARY KEY," +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_CITY_NAME + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_LNG + REAL_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_ID_CIA + INT_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_COMPANY + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.CitiesTaxis.COLUMN_NAME_PHONE + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " ;

    private static final String SQL_CREATE_TRIPS =
                   //  "CREATE TABLE " + TaxiContract.UserTrips.TABLE_NAME +
                    " (" +
                    TaxiContract.UserTrips._ID + INT_TYPE+ " PRIMARY KEY," +
                    TaxiContract.UserTrips.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +

                    TaxiContract.UserTrips.COLUMN_NAME_STREET_FROM + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_CITY_FROM + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_LAT_FROM + REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_LNG_FROM + REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_STREET_TO + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_CITY_TO + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_LAT_TO + REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_LNG_TO + REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_COMENTS + TEXT_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_KMS+ REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_PRICE + REAL_TYPE + COMMA_SEP +
                    TaxiContract.UserTrips.COLUMN_NAME_TYPE + NUMBER_TYPE +
                    " );";

    private static final String SQL_DELETE_CITIES=
            "DROP TABLE IF EXISTS " + TaxiContract.CitiesTaxis.TABLE_NAME;
    private static final String SQL_DELETE_TRIPS=
            "DROP TABLE IF EXISTS " + TaxiContract.UserTrips.TABLE_NAME;
    private static final String SQL_DROP=
            "DROP TABLE IF EXISTS ";

    public TaxiOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    public void newUser(String nameUser, SQLiteDatabase db){

        String table = SQL_CREATE_TABLE + returnUser(nameUser) + "_" + TaxiContract.UserTrips.TABLE_NAME + SQL_CREATE_TRIPS;

        Log.i("CREATE_TABLE", table);
try {
    db.execSQL(table);
}catch (SQLiteException ex){
    Log.e("CREATE_TABLE", table, ex);
}
    }


    public void leaveUser(String nameUser, SQLiteDatabase db){

        db.execSQL(SQL_DROP + returnUser(nameUser) + "_" + TaxiContract.UserTrips.TABLE_NAME + ";");
    }

    public void changeUser(SQLiteDatabase db, String oldUser, String newUser){
        leaveUser(oldUser, db);
        newUser(newUser, db);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CITIES);
        //db.execSQL(SQL_CREATE_TABLE+SQL_CREATE_TRIPS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CITIES);
        //db.execSQL(SQL_DELETE_TRIPS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String returnUser(String user){
        String name[] = user.split("@");
        if(name != null && name.length >1){
            return name[0];
        }
        return user;
    }

    public void updateTableTrips(SQLiteDatabase db, String user){

        leaveUser(user,db);
        newUser(user,db);
    }
}
