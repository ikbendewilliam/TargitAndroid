package be.howest.nmct.targit.views.gameover;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;

public class SaveScoreFragment extends Fragment {


    public SaveScoreFragment() {
        // Required empty public constructor
    }

    public static SaveScoreFragment newInstance() {
        SaveScoreFragment fragment = new SaveScoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String gamemode = getArguments().getString(EXTRA_GAME);
        View view = null;
        if(gamemode.equals(EXTRA_GAME_SMASHIT))
                view = inflater.inflate(R.layout.fragment_save_score, container, false);


        return view;

    }

}
