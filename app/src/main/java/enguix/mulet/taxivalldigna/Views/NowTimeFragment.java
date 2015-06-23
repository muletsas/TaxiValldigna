package enguix.mulet.taxivalldigna.Views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import enguix.mulet.taxivalldigna.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NowTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NowTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowTimeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam;
    private String mParam2;

    TextView date;
    TextView minutes;
    private int min_;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     *
     * @return A new instance of fragment NowTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NowTimeFragment newInstance(String param) {
        NowTimeFragment fragment = new NowTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, param);
        fragment.setArguments(args);
        return fragment;
    }

    public NowTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_DATE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_now_time, container, false);

        date = (TextView)rootView.findViewById(R.id.date_now);
        minutes = (TextView)rootView.findViewById(R.id.min_now);

        date.setText("  "+mParam+"  ");
        minutes.setOnClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getMin_value() {

        String m = minutes.getText().toString();
        String min[]= m.split("[ ]+");

        Log.i("valueMIN", "value ="+min[min.length-1]+"length="+min.length);

try{
        return Integer.parseInt(min[min.length-1]);
    }catch(NumberFormatException e){
        Log.e("NumberPicker", e.toString());
    }
        return 5;
    }

    @Override
    public void onClick(View v) {
        mListener.getMinutes(v);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        // Guardamos el estado de la posicion del elemento
        // que estábamos consultando
        //outState.putInt(POSICION, position);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void getMinutes(View v);
    }



    public void setMinutes(String minutes) {
        this.minutes.setText(minutes);
    }



    public String getMinutes() {
        return minutes.getText().toString();
    }
}
