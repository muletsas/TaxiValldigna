package enguix.mulet.taxivalldigna.Views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import enguix.mulet.taxivalldigna.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpinnerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinnerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_CITIES = "cities_to";
    private static final String ARG_CITY = "city_to";
    // TODO: Rename and change types of parameters

    private ArrayList<String> cities;
    private OnFragmentInteractionListener mListener;
    private Spinner spinner;
    private int index;
    private ArrayAdapter<String> dataAdapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param cities Parameter 2.
     * @return A new instance of fragment SpinnerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpinnerFragment newInstance( ArrayList<String> cities, int index) {
        SpinnerFragment fragment = new SpinnerFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);StringArrayListt
        args.putStringArrayList(ARG_CITIES, cities);
        args.putInt(ARG_CITY, index);
        fragment.setArguments(args);
        return fragment;
    }

    public SpinnerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cities = getArguments().getStringArrayList(ARG_CITIES);
            index = getArguments().getInt(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_spinner, container, false);

        spinner = (Spinner) v.findViewById(R.id.to_spinner);





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //  Toast.makeText(getApplicationContext(), cities_from.get(position).toString(), Toast.LENGTH_LONG);
                Log.i("listener Spinner onItem", position + " " + cities.get(position).toString());
                index = position;

                mListener.city_to(position, cities.get(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        if(cities!=null){
            dataAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, cities);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);

            spinner.setSelection(index);
        }


        return v;
    }     // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public int getIndex(){
        return this.index;
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
        public void city_to(int pos, String name);
    }

}
