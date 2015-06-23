package enguix.mulet.taxivalldigna;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import enguix.mulet.taxivalldigna.Entities.TripEntity;

/**
 * Created by root on 15/06/15.
 */
public class AdapterList extends BaseAdapter implements ListAdapter {
    private static final int C_NOW = Color.rgb(8,119,30);
    private static final int C_BEFORE_ONE = Color.rgb(181,66,0);
    private static final long ONE_HOUR = 3600000;
    private final Context context;
    private final ArrayList<TripEntity> trips;
    private int id_before;

private int errorsDate;
    public AdapterList(Context context, ArrayList<TripEntity> trips) {
        this.context = context;
        this.trips = trips;
        errorsDate=0;
        id_before=0;

    }

    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Object getItem(int position) {
        return trips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trips.get(position).get_id();
    }



    public LatLng[] getCoordenates(int position){

        LatLng from= trips.get(position).getFrom().getCoordenates();
        LatLng to= trips.get(position).getTo().getCoordenates();


        return new LatLng[]{from,to};

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Calendar current = GregorianCalendar.getInstance();
        Calendar tripDate = trips.get(position).getTime();

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list, null, true);

        TextView n_id = (TextView)view.findViewById(R.id.id_trip);

        n_id.setText("N: "+trips.get(position).get_id());

        if(trips.get(position).getType()==2){

            n_id.setBackgroundResource(R.drawable.ic_back);
        }
        id_before= trips.get(position).get_id();
        TextView date = (TextView)view.findViewById(R.id.date_list);
        date.setText(trips.get(position).date());

        if(current.getTime().compareTo(trips.get(position).getTime().getTime())==-1){
            date.setTextColor(Color.BLUE);
        }

        TextView hour = (TextView)view.findViewById(R.id.hour_list);
        hour.setText(trips.get(position).clock());

        if(isToday(current,tripDate )){//si es hui Data verda


            long dif = tripDate.getTimeInMillis() - current.getTimeInMillis();

            if(dif > 0) {

                if (dif <= 2*ONE_HOUR ) {//si queda menys d'un hora

                    hour.setTextColor(C_BEFORE_ONE);

                }
                if (dif <= ONE_HOUR ) {//si queda menys d'un hora
                    hour.setTextColor(C_NOW);

                }
                date.setTextColor(C_NOW);
            }
        }else if(tripDate.compareTo(current)==1){//si està reservat blava
            date.setTextColor(Color.BLUE);


        }


        TextView kms = (TextView)view.findViewById(R.id.kms_list);
        kms.setText(trips.get(position).getKms()+" kms.");

        TextView price = (TextView)view.findViewById(R.id.price_list);
        price.setText(trips.get(position).getPrice()+" €");

        TextView st_from = (TextView)view.findViewById(R.id.street_from_l);
        st_from.setText(trips.get(position).getFrom().getStreets_name());

        TextView city_from = (TextView)view.findViewById(R.id.city_from_l);
        city_from.setText(trips.get(position).getFrom().getCity());

        TextView st_to = (TextView)view.findViewById(R.id.street_to_l);
        st_to.setText(trips.get(position).getTo().getStreets_name());

        TextView city_to = (TextView)view.findViewById(R.id.city_to_l);
        city_to.setText(trips.get(position).getTo().getCity());



        return view;
    }

    public boolean isToday(Calendar current, Calendar dateTrip){

        return spanishDate(current).equals(spanishDate(dateTrip));


    }

    public String spanishDate(Calendar date){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

try {
    return format.format(date.getTime());
}catch (Exception e){
    Log.e("FORMAT_DATE", "error date="+errorsDate+"-->"+e);
    return ""+errorsDate++;
}
    }
}
