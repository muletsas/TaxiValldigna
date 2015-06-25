package enguix.mulet.taxivalldigna.Views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import enguix.mulet.taxivalldigna.Entities.TripEntity;

/**
 * Created by Mulet on 11/06/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int year;
    private int month;
    private int day;

    private TextView data;
    private TripEntity trip;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void setData(TextView data) {
        this.data = data;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user


        Calendar c = GregorianCalendar.getInstance();

            c.set(year,month,day);

        if(c.compareTo(trip.getTime()) < 0){
            trip.setDate(this.year, this.month, this.day);
        }else {
            trip.setDate(year,month,day);
        }

        setText(trip.date());

    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }

    public void setText(String date){

        data.setText("   "+date+"  ");
    }


}

