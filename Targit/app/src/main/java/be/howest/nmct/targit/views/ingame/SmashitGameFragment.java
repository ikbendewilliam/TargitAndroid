package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.targit.R;

public class SmashitGameFragment extends Fragment {
    String mDifficulty;

    private OnSmashitGameListener mListener;

    public SmashitGameFragment() {
        // Required empty public constructor
    }

    public static SmashitGameFragment newInstance(String difficulty) {
        SmashitGameFragment fragment = new SmashitGameFragment();
        fragment.mDifficulty = difficulty;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smashit_game, container, false);

        view.findViewById(R.id.ingame_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.stopGame(GameActivity.EXTRA_GAME_SMASHIT, 0, mDifficulty);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSmashitGameListener) {
            mListener = (OnSmashitGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSmashitGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSmashitGameListener {
        void stopGame(String gameMode, int score, String category);
    }
}
