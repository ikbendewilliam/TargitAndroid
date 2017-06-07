package be.howest.nmct.targit.views.infogamemode;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.ingame.GameActivity;

public class SmashitInfoFragment extends Fragment {

    private OnSmashitInfoListener mListener;

    public SmashitInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_info, container, false);

        view.findViewById(R.id.smashit_info_button_play_easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(GameActivity.EXTRA_DIFFICULTY_EASY);
            }
        });
        view.findViewById(R.id.smashit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(GameActivity.EXTRA_DIFFICULTY_MEDIUM);
            }
        });
        view.findViewById(R.id.smashit_info_button_play_hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playSmashit(GameActivity.EXTRA_DIFFICULTY_HARD);
            }
        });

        return view;
    }

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

    public interface OnSmashitInfoListener {
        void playSmashit(String cmdDifficulty);
    }
}
