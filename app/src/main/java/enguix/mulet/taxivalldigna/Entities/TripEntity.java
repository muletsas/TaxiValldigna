package enguix.mulet.taxivalldigna.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import enguix.mulet.taxivalldigna.UtilsRequest;

/**
 * Created by root on 17/05/15.
 */
public class TripEntity implements Parcelable {

    private int _id;
    private CustomAddress from;
    private CustomAddress to;
    private String coments;
    private int passengers;
    private Date date;
    private int type;
    private Float kms;
    private Float price;
    private Calendar time;



    public TripEntity(int _id, CustomAddress from, CustomAddress to, String coments, Calendar time) {
        this._id = _id;
        this.from = from;
        this.to = to;
        this.coments = coments;
        this.time = time;

    }

    private Calendar convertDate(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Calendar cal  = Calendar.getInstance();

        try {
            this.date = df.parse(date);

            cal.setTime(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
            return Calendar.getInstance();
        }

        return cal;
    }

    public void setTime(String date){

        this.time = convertDate(date);
    }





    public TripEntity(){
        this.time = GregorianCalendar.getInstance();
        to = new CustomAddress();
        from = new CustomAddress();
        passengers=1;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public CustomAddress getFrom() {
        return from;
    }

    public void setFrom(CustomAddress from) {
        this.from = from;
    }

    public CustomAddress getTo() {
        return to;
    }

    public void setTo(CustomAddress to) {
        this.to = to;
    }

    public String getComents() {
        return coments;
    }

    public void setComents(String coments) {
        this.coments = coments;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {

        try {
            if(passengers <= 0 ){
                passengers = 1;
            }
            this.passengers = passengers;
        }catch (Exception e){
            this.passengers = 1;
        }

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Float getKms() {
        return kms;
    }

    public void setKms(Float kms) {
        this.kms = kms;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String date(){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


        return format.format(this.time.getTime());
    }
    public String clock(){

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");


        return format.format(this.time.getTime());
    }

    public String dataSQL(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        return format.format(this.time.getTime());

    }

    public void setClock(int hour, int min){

        this.time.set(Calendar.HOUR_OF_DAY, hour);
        this.time.set(Calendar.MINUTE, min);

    }

    public void setDate(int y, int m, int d){
        time.set( y,m,d);

    }



    public void addMinutes(int min){
        long milis = min*60000;
        // Calendar data = GregorianCalendar.getInstance();

        this.time = GregorianCalendar.getInstance();

        this.time.setTimeInMillis(this.time.getTimeInMillis() + milis);

    }

    public void prepareParameters(HashMap<String,String> params){

        if(params == null){
            params = new HashMap<>();
        }
        params.put(UtilsRequest.PARAMS_STREET_FROM, this.getFrom().getStreets_name());
        params.put(UtilsRequest.PARAMS_CITY_FROM, this.getFrom().getCity());
        params.put(UtilsRequest.PARAMS_LAT_FROM, ""+this.getFrom().getCoordenates().latitude);
        params.put(UtilsRequest.PARAMS_LNG_FROM, ""+this.getFrom().getCoordenates().longitude);
        params.put(UtilsRequest.PARAMS_STREET_TO, this.getTo().getStreets_name());
        params.put(UtilsRequest.PARAMS_CITY_TO, this.getTo().getCity());
        params.put(UtilsRequest.PARAMS_LAT_TO, ""+this.getTo().getCoordenates().latitude);
        params.put(UtilsRequest.PARAMS_LNG_TO, ""+this.getTo().getCoordenates().longitude);
        params.put(UtilsRequest.PARAMS_COMENTS, this.getComents());
        params.put(UtilsRequest.PARAMS_PASSENGERS, ""+this.getPassengers());
        params.put(UtilsRequest.PARAMS_TIME,dataSQL());



    }

    public void prepareParameters(String type , HashMap<String,String> params){

        if(params == null){
            params = new HashMap<>();
        }
        // HashMap<String, String> params = new HashMap<>();
        params.put(type+"_"+UtilsRequest.PARAMS_STREET_FROM, this.getFrom().getStreets_name());
        params.put(type+"_"+UtilsRequest.PARAMS_CITY_FROM, this.getFrom().getCity());
        params.put(type+"_"+UtilsRequest.PARAMS_LAT_FROM, ""+this.getFrom().getCoordenates().latitude);
        params.put(type+"_"+UtilsRequest.PARAMS_LNG_FROM, ""+this.getFrom().getCoordenates().longitude);
        params.put(type+"_"+UtilsRequest.PARAMS_STREET_TO, this.getTo().getStreets_name());
        params.put(type+"_"+UtilsRequest.PARAMS_CITY_TO, this.getTo().getCity());
        params.put(type+"_"+UtilsRequest.PARAMS_LAT_TO, ""+this.getTo().getCoordenates().latitude);
        params.put(type+"_"+UtilsRequest.PARAMS_LNG_TO, ""+this.getTo().getCoordenates().longitude);
        params.put(type+"_"+UtilsRequest.PARAMS_COMENTS, this.getComents());
        params.put(type+"_"+UtilsRequest.PARAMS_PASSENGERS, ""+this.getPassengers());
        params.put(type+"_"+UtilsRequest.PARAMS_TIME,dataSQL());


    }

public String toString(){

    return date()+" "+clock()+"\n"+from.getStreets_name()+", "+from.getCity()+"\n"+to.getStreets_name()+", "+to.getCity();

}

    protected TripEntity(Parcel in) {
        _id = in.readInt();
        from = (CustomAddress) in.readValue(CustomAddress.class.getClassLoader());
        to = (CustomAddress) in.readValue(CustomAddress.class.getClassLoader());
        coments = in.readString();
        passengers = in.readInt();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        type = in.readInt();
        kms = in.readByte() == 0x00 ? null : in.readFloat();
        price = in.readByte() == 0x00 ? null : in.readFloat();
        time = (Calendar) in.readValue(Calendar.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeValue(from);
        dest.writeValue(to);
        dest.writeString(coments);
        dest.writeInt(passengers);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeInt(type);
        if (kms == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(kms);
        }
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(price);
        }
        dest.writeValue(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TripEntity> CREATOR = new Parcelable.Creator<TripEntity>() {
        @Override
        public TripEntity createFromParcel(Parcel in) {
            return new TripEntity(in);
        }

        @Override
        public TripEntity[] newArray(int size) {
            return new TripEntity[size];
        }
    };
}