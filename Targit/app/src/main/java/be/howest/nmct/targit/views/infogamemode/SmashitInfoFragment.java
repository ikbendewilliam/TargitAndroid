package be.howest.nmct.targit.views.infogamemode;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_HARD;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_MEDIUM;

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

        return view;
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
