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

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MANY;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MEDIUM;

// The information shown for memorit
public class MemoritInfoFragment extends Fragment {

    private OnMemoritInfoListener mListener; // A listener to start the game

    // Required empty public constructor
    public MemoritInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_info, container, false);

        // Put listeners on every button that start the game
        view.findViewById(R.id.memorit_info_button_play_easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(EXTRA_LIVES_MANY);
            }
        });
        view.findViewById(R.id.memorit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(EXTRA_LIVES_MEDIUM);
            }
        });
        view.findViewById(R.id.memorit_info_button_play_hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(EXTRA_LIVES_FEW);
            }
        });

        //back button listener
        view.findViewById(R.id.memorit_info_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //instellen fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        Button btnSnel = (Button)view.findViewById(R.id.memorit_info_button_play_hard);
        btnSnel.setTypeface(font);
        Button btnMatig = (Button)view.findViewById(R.id.memorit_info_button_play_medium);
        btnMatig.setTypeface(font);
        Button btnTraag = (Button)view.findViewById(R.id.memorit_info_button_play_easy);
        btnTraag.setTypeface(font);

        TextView txtDot = (TextView) view.findViewById(R.id.memorit_info_tekst_dot);
        txtDot.setTypeface(font);
        TextView txtDot1 = (TextView) view.findViewById(R.id.memorit_info_tekst_dot_1);
        txtDot1.setTypeface(font);
        TextView txtDot2 = (TextView) view.findViewById(R.id.memorit_info_tekst_dot_2);
        txtDot2.setTypeface(font);
        TextView txtDot3 = (TextView) view.findViewById(R.id.memorit_info_tekst_dot_3);
        txtDot3.setTypeface(font);

        TextView txt = (TextView) view.findViewById(R.id.memorit_info_tekst_1);
        txt.setTypeface(font);
        TextView txt1 = (TextView) view.findViewById(R.id.memorit_info_tekst_2);
        txt1.setTypeface(font);
        TextView txt2 = (TextView) view.findViewById(R.id.memorit_info_tekst_3);
        txt2.setTypeface(font);
        TextView txt3 = (TextView) view.findViewById(R.id.memorit_info_tekst_4);
        txt3.setTypeface(font);

        return view;
    }

    // A standard implementation when using a listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMemoritInfoListener) {
            mListener = (OnMemoritInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMemoritInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // The listener defined
    public interface OnMemoritInfoListener {
        void playMemorit(int cmdLives);
    }
}
