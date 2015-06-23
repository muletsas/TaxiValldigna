package enguix.mulet.taxivalldigna.Views;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import enguix.mulet.taxivalldigna.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReserveNowFragment extends Fragment {
    Button now;
    Button reserve;

    FragmentNowReserveListener mListener;
    public ReserveNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_reserve_now, container,false);

        now = (Button)rootView.findViewById(R.id.now_button);
        reserve = (Button)rootView.findViewById(R.id.reserve_button);


        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.nowActivity();
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.reserveActivity();
            }
        });

        return rootView;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentNowReserveListener) activity;



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


    public interface FragmentNowReserveListener {



        void nowActivity();
        void reserveActivity();
    }


}
