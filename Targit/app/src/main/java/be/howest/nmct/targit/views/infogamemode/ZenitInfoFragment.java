package be.howest.nmct.targit.views.infogamemode;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_DURATION_LONG;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.TEXT_SIZE;

// The information shown for zenit
public class ZenitInfoFragment extends Fragment {

    private OnZenitInfoListener mListener; // A listener to start the game

    // Required empty public constructor
    public ZenitInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zenit_info, container, false);

        // Put listeners on every button that start the game
        view.findViewById(R.id.zenit_info_button_play_short).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(EXTRA_DURATION_SHORT);
            }
        });
        view.findViewById(R.id.zenit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(EXTRA_DURATION_MEDIUM);
            }
        });
        view.findViewById(R.id.zenit_info_button_play_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(EXTRA_DURATION_LONG);
            }
        });

        //back button listener
        view.findViewById(R.id.zenit_info_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //instellen fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        Button btnSnel = (Button)view.findViewById(R.id.zenit_info_button_play_long);
        btnSnel.setTypeface(font);
        Button btnMatig = (Button)view.findViewById(R.id.zenit_info_button_play_medium);
        btnMatig.setTypeface(font);
        Button btnTraag = (Button)view.findViewById(R.id.zenit_info_button_play_short);
        btnTraag.setTypeface(font);

        TextView txtDot = (TextView) view.findViewById(R.id.zenit_info_tekst_dot);
        txtDot.setTypeface(font);
        TextView txtDot1 = (TextView) view.findViewById(R.id.zenit_info_tekst_dot_1);
        txtDot1.setTypeface(font);
        TextView txtDot2 = (TextView) view.findViewById(R.id.zenit_info_tekst_dot_2);
        txtDot2.setTypeface(font);
        TextView txtDot3 = (TextView) view.findViewById(R.id.zenit_info_tekst_dot_3);
        txtDot3.setTypeface(font);

        TextView txt = (TextView) view.findViewById(R.id.zenit_info_tekst_1);
        txt.setTypeface(font);
        TextView txt1 = (TextView) view.findViewById(R.id.zenit_info_tekst_2);
        txt1.setTypeface(font);
        TextView txt2 = (TextView) view.findViewById(R.id.zenit_info_tekst_3);
        txt2.setTypeface(font);
        TextView txt3 = (TextView) view.findViewById(R.id.zenit_info_tekst_4);
        txt3.setTypeface(font);

        //instellen autosize textfields
        long textSize = Math.round(TEXT_SIZE/getResources().getDisplayMetrics().densityDpi);
        txtDot.setTextSize(textSize);
        txtDot1.setTextSize(textSize);
        txtDot2.setTextSize(textSize);
        txtDot3.setTextSize(textSize);
        txt.setTextSize(textSize);
        txt1.setTextSize(textSize);
        txt2.setTextSize(textSize);
        txt3.setTextSize(textSize);

        return view;
    }

    // A standard implementation when using a listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnZenitInfoListener) {
            mListener = (OnZenitInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnZenitInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // The listener defined
    public interface OnZenitInfoListener {
        void playZenit(int cmdDuration);
    }
}
