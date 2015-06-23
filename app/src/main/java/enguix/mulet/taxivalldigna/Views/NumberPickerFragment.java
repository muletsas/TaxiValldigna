package enguix.mulet.taxivalldigna.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import enguix.mulet.taxivalldigna.R;

/**
 * Created by root on 11/06/15.
 */
public class NumberPickerFragment extends DialogFragment implements NumberPicker.OnValueChangeListener, DialogInterface.OnClickListener {
    private TextView min;
    private int minutes;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        minutes=5;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("NumberPicker");
        //d.setContentView(R.layout.dialog);




        final NumberPicker np = new NumberPicker(getActivity());
        np.setMaxValue(120); // max value 120
        np.setMinValue(5);   // min value 5
        try {
            if (min != null || !min.getText().equals("")) {
                String m = min.getText().toString();
                String min[]= m.split("[ ]+");

                minutes = Integer.parseInt(min[min.length-1]);
                np.setValue(minutes);
            }
        }catch(NumberFormatException e){
            Log.e("NumberPicker", e.toString());
        }

        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        RelativeLayout layout = new RelativeLayout(getActivity());

        LinearLayout linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout.LayoutParams linearParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);



       // Button bt = new Button(getActivity());
        //bt.setText("Fet");


        //bt.setOnClickListener(this);

        builder.setNeutralButton(getString(R.string.done), this);


        linearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);




        linear.addView(np);
      //  linear.addView(bt);


        layout.addView(linear, linearParams);

        builder.setView(layout);

      //  builder.setContentView(layout);






        return builder.create();
    }


    public void setMin(TextView min) {
        this.min = min;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        minutes = newVal;
    }




    @Override
    public void onClick(DialogInterface dialog, int which) {
        min.setText("  "+minutes+"  ");

        this.dismiss();
    }
}

