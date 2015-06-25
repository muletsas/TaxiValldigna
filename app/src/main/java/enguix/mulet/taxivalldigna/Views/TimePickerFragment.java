package enguix.mulet.taxivalldigna.Views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import enguix.mulet.taxivalldigna.Entities.TripEntity;

/**
 * Created by root on 11/06/15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TripEntity trip;
    //Times activity;
    private TextView hora;
    private int minute;
    private int hour;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        if(c.compareTo(trip.getTime())>=0){
            c.add(Calendar.HOUR,2);
            minute = c.get(Calendar.MINUTE);
            hour = c.get(Calendar.HOUR_OF_DAY);
        }else {
            minute = trip.getTime().get(Calendar.MINUTE);
            hour = trip.getTime().get(Calendar.HOUR_OF_DAY);
        }

        /*minute = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR_OF_DAY);*/


        //  minute =
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user


        setText(hourOfDay,minute);

    }

    public boolean isToday(){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String today = format.format(new Date());
        String dayTrip = trip.date();


        return today.equals(dayTrip);

    }

    public void setText(int hourOfDay, int minute){


        if(isToday()){
            Calendar c = Calendar.getInstance();
            if(hourOfDay - c.get(Calendar.HOUR_OF_DAY) < 2 ){

                trip.setClock(hour,minute);
            }else {
                trip.setClock(hourOfDay,minute);
            }


        } else {
            trip.setClock(hourOfDay,minute);
        }




        hora.setText("  "+trip.clock()+"  ");
    }

    public void setTrip(TripEntity trip){
        this.trip = trip;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }
}
