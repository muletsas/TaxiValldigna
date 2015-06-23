package enguix.mulet.taxivalldigna.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by root on 17/05/15.
 */
public class CustomAddress implements Parcelable {
    private String streets_name;
    private int id_city;
    private String city;
    private String c_p;
    private LatLng coordenates;

    public CustomAddress() {
        streets_name = "#";
        city = "#";
        c_p="#";
        coordenates=null;
    }

    public CustomAddress(String streets_name, String city, String c_p, LatLng coordenates) {
        this.streets_name = streets_name;
        this.city = city;
        this.c_p = c_p;
        this.coordenates = coordenates;
    }

    public CustomAddress(String streets_name, String city, LatLng coordenates) {
        this.streets_name = streets_name;
        this.city = city;
        this.coordenates = coordenates;
        this.c_p = "#";
    }

    public CustomAddress(String streets_name, int id_city, LatLng coordenates) {
        this.streets_name = streets_name;
        this.id_city = id_city;
        this.coordenates = coordenates;
    }

    public CustomAddress(int id_city, String city) {
        this.city = city;
        this.id_city = id_city;
    }

public boolean checkDirection(String stt, String cty){

    if(!this.streets_name.equals(stt)){
        return false;
    }
    if(!this.city.equals(cty)){
        return false;
    }
    if(coordenates==null){
        return false;
    }

    return true;

}


    public CustomAddress(LatLng coordenates) {
        this.coordenates = coordenates;
    }

    public String getStreets_name() {
        return streets_name;
    }

    public int getId_city() {
        return id_city;
    }

    public void setId_city(int id_city) {
        this.id_city = id_city;
    }

    public void setStreets_name(String streets_name) {
        this.streets_name = streets_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getC_p() {
        return c_p;
    }

    public void setC_p(String c_p) {
        this.c_p = c_p;
    }

    public LatLng getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(LatLng coordenates) {
        this.coordenates = coordenates;
    }

    public void setCoordenates(double lat, double lgn) {
        LatLng coor = new LatLng(lat, lgn);
        this.coordenates = coor;
    }

    public String toString() {
        if(c_p !=null) {
            return streets_name + " - " + city + "\n" + c_p;//+ " " + coordenates;
        }else return streets_name + " - " + city;
    }

    public String getString(){
        return streets_name+", "+city;
    }



    protected CustomAddress(Parcel in) {
        streets_name = in.readString();
        id_city = in.readInt();
        city = in.readString();
        c_p = in.readString();
        coordenates = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(streets_name);
        dest.writeInt(id_city);
        dest.writeString(city);
        dest.writeString(c_p);
        dest.writeValue(coordenates);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomAddress> CREATOR = new Parcelable.Creator<CustomAddress>() {
        @Override
        public CustomAddress createFromParcel(Parcel in) {
            return new CustomAddress(in);
        }

        @Override
        public CustomAddress[] newArray(int size) {
            return new CustomAddress[size];
        }
    };
}