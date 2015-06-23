package enguix.mulet.taxivalldigna.Views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import enguix.mulet.taxivalldigna.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveTimeFragment extends Fragment implements View.OnClickListener{
    TextView date;
    TextView hour;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveTimeFragment newInstance(String param1, String param2) {
        ReserveTimeFragment fragment = new ReserveTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReserveTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_reserve_time, container, false);

        date = (TextView)rootView.findViewById(R.id.date_reserve);
        hour = (TextView)rootView.findViewById(R.id.hour_reserve);

        date.setOnClickListener(this);
        hour.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {


        if(v.getId()== R.id.hour_reserve){
            mListener.getHour(v);
        }else{
            mListener.getDate(v);
        }


    }



    public Calendar getTime(){

        Calendar time = GregorianCalendar.getInstance();


        return null;
    }

    public void setDate(Calendar calendar){

        String d = date.getText().toString();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void getHour(View v);
        public void getDate(View v);
    }

    public void setHour(String hour){
        this.hour.setText(hour);
    }
    public void setDate(String date){
        this.date.setText(date);
    }

    public String getDate() {
        return date.getText().toString();
    }

    public String getHour() {
        return hour.getText().toString();
    }
}