package enguix.mulet.taxivalldigna;

import android.provider.BaseColumns;

/**
 * Created by root on 17/05/15.
 */
public final class TaxiContract {





    public TaxiContract() {
    }


    public static abstract class CitiesTaxis implements BaseColumns {
        public static final String TABLE_NAME = "City";
        public static final String COLUMN_NAME_ID = "id_city";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_ID_CIA = "id_cia";
        public static final String COLUMN_NAME_COMPANY = "company";
        public static final String COLUMN_NAME_PHONE = "phone";


    }

    public static abstract class UserTrips implements BaseColumns {
        public static final String TABLE_NAME = "Trip";
        public static final String COLUMN_NAME_ID = "id_trip";

        public static final String COLUMN_NAME_DATE = "date";

        public static final String COLUMN_NAME_STREET_FROM = "street_from";
        public static final String COLUMN_NAME_CITY_FROM = "city_from";
        public static final String COLUMN_NAME_LAT_FROM = "lat_from";
        public static final String COLUMN_NAME_LNG_FROM = "lng_from";
        public static final String COLUMN_NAME_STREET_TO = "street_to";
        public static final String COLUMN_NAME_CITY_TO = "city_to";
        public static final String COLUMN_NAME_LAT_TO = "lat_to";
        public static final String COLUMN_NAME_LNG_TO = "lng_to";
        public static final String COLUMN_NAME_COMENTS = "coments";
        public static final String COLUMN_NAME_KMS = "kms";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_TYPE = "type";

    }






}
