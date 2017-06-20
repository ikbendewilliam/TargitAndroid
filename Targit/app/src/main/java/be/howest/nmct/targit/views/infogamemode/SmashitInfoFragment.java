package be.howest.nmct.targit.views.infogamemode;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_HARD;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_SMASHIT;
import static be.howest.nmct.targit.Constants.TEXT_SIZE;

// The information shown for smashit
public class SmashitInfoFragment extends Fragment {

    private OnSmashitInfoListener mListener; // A listener to start the game

    // Required empty public constructor
    public SmashitInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smashit_info, container, false);

        // Put listeners on every button that start the game
        view.findViewById(R.id.smashit_info_button_play_easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(EXTRA_DIFFICULTY_EASY);
            }
        });
        view.findViewById(R.id.smashit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(EXTRA_DIFFICULTY_MEDIUM);
            }
        });
        view.findViewById(R.id.smashit_info_button_play_hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(EXTRA_DIFFICULTY_HARD);
            }
        });

        //back button listener
        view.findViewById(R.id.smashit_info_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //fab listener
        view.findViewById(R.id.smashit_info_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });

        //instellen fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        Button btnSnel = (Button)view.findViewById(R.id.smashit_info_button_play_hard);
        btnSnel.setTypeface(font);
        Button btnMatig = (Button)view.findViewById(R.id.smashit_info_button_play_medium);
        btnMatig.setTypeface(font);
        Button btnTraag = (Button)view.findViewById(R.id.smashit_info_button_play_easy);
        btnTraag.setTypeface(font);

        TextView txtDot = (TextView) view.findViewById(R.id.smashit_info_tekst_dot);
        txtDot.setTypeface(font);
        TextView txtDot1 = (TextView) view.findViewById(R.id.smashit_info_tekst_dot_1);
        txtDot1.setTypeface(font);
        TextView txtDot2 = (TextView) view.findViewById(R.id.smashit_info_tekst_dot_2);
        txtDot2.setTypeface(font);
        TextView txtDot3 = (TextView) view.findViewById(R.id.smashit_info_tekst_dot_3);
        txtDot3.setTypeface(font);

        TextView txtLevens = (TextView) view.findViewById(R.id.smashit_info_tekst_levens);
        txtLevens.setTypeface(font);
        TextView txt1 = (TextView) view.findViewById(R.id.smashit_info_tekst_1);
        txt1.setTypeface(font);
        TextView txt2 = (TextView) view.findViewById(R.id.smashit_info_tekst_2);
        txt2.setTypeface(font);
        TextView txt3 = (TextView) view.findViewById(R.id.smashit_info_tekst_3);
        txt3.setTypeface(font);

        //instellen autosize textfields
        long textSize = Math.round(TEXT_SIZE/getResources().getDisplayMetrics().densityDpi);
        txtDot.setTextSize(textSize);
        txtDot1.setTextSize(textSize);
        txtDot2.setTextSize(textSize);
        txtDot3.setTextSize(textSize);
        txtLevens.setTextSize(textSize);
        txt1.setTextSize(textSize);
        txt2.setTextSize(textSize);
        txt3.setTextSize(textSize);

        return view;
    }

    private void showHelpDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = InfoGameHelpFragment.newInstance(EXTRA_GAMEMODE_SMASHIT);
        newFragment.show(ft, "dialog");
    }

    // A standard implementation when using a listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSmashitInfoListener) {
            mListener = (OnSmashitInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSmashitInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // The listener defined
    public interface OnSmashitInfoListener {
        void playSmashit(String cmdDifficulty);
    }
}
