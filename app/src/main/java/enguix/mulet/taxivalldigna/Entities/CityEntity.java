package enguix.mulet.taxivalldigna.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;


public class CityEntity implements Parcelable {

    private int id;
    private String name;
    private LatLng coordenates;
    private int id_cia;
    private String company;
    private String phone;


    public CityEntity(){

    }

    public CityEntity(int id, String name, LatLng coordenates, int id_cia, String company, String phone) {
        this.id = id;
        this.name = name;
        this.coordenates = coordenates;
        this.id_cia = id_cia;
        this.company = company;
        this.phone = phone;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(LatLng coordenates) {
        this.coordenates = coordenates;
    }

    public int getId_cia() {
        return id_cia;
    }

    public void setId_cia(int id_cia) {
        this.id_cia = id_cia;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString(){
        return name;
    }

    protected CityEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        coordenates = (LatLng) in.readValue(LatLng.class.getClassLoader());
        id_cia = in.readInt();
        company = in.readString();
        phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeValue(coordenates);
        dest.writeInt(id_cia);
        dest.writeString(company);
        dest.writeString(phone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CityEntity> CREATOR = new Parcelable.Creator<CityEntity>() {
        @Override
        public CityEntity createFromParcel(Parcel in) {
            return new CityEntity(in);
        }

        @Override
        public CityEntity[] newArray(int size) {
            return new CityEntity[size];
        }
    };
}