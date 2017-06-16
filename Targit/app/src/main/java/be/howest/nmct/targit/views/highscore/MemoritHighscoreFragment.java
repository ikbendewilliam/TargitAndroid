package be.howest.nmct.targit.views.highscore;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.BUTTON_EASY;
import static be.howest.nmct.targit.Constants.BUTTON_HARD;
import static be.howest.nmct.targit.Constants.BUTTON_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_HARD;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MANY;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MEDIUM;


public class MemoritHighscoreFragment extends Fragment {
    private HighscoreListFragment mHighscoreListFragment;
    private Button btnHighscoreEasy;
    private Button btnHighscoreMedium;
    private Button btnHighscoreHard;

    public MemoritHighscoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_highscore, container, false);

        showlistFragment(EXTRA_LIVES_MANY);

        btnHighscoreEasy = (Button) view.findViewById(R.id.fragment_memorit_highscore_easy);
        btnHighscoreMedium = (Button) view.findViewById(R.id.fragment_memorit_highscore_medium);
        btnHighscoreHard = (Button) view.findViewById(R.id.fragment_memorit_highscore_hard);

        btnHighscoreEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_EASY);
                mHighscoreListFragment.changeList(EXTRA_GAME_MEMORIT, EXTRA_LIVES_MANY);
            }
        });
        btnHighscoreMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_MEDIUM);
                mHighscoreListFragment.changeList(EXTRA_GAME_MEMORIT, EXTRA_LIVES_MEDIUM);
            }
        });
        btnHighscoreHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_HARD);
                mHighscoreListFragment.changeList(EXTRA_GAME_MEMORIT, EXTRA_LIVES_FEW);
            }
        });

        return view;
    }

    private void showlistFragment(int category) {
        HighscoreListFragment highscoreListFragment = HighscoreListFragment.newInstance(EXTRA_GAME_MEMORIT, category, null);
        mHighscoreListFragment = highscoreListFragment;
        showFragment(highscoreListFragment);
    }

    private void selectButton(int buttonId) {
        btnHighscoreEasy.setBackgroundResource(R.drawable.green_postit);
        btnHighscoreMedium.setBackgroundResource(R.drawable.orange_postit);
        btnHighscoreHard.setBackgroundResource(R.drawable.red_postit);

        switch (buttonId) {
            case BUTTON_EASY:
                btnHighscoreEasy.setBackgroundResource(R.drawable.green_postit_selected);
                break;
            case BUTTON_MEDIUM:
                btnHighscoreMedium.setBackgroundResource(R.drawable.orange_postit_selected);
                break;
            case BUTTON_HARD:
                btnHighscoreHard.setBackgroundResource(R.drawable.red_postit_selected);
                break;
        }
    }


    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_fragment_memorit_highscore, newFragment);
        transaction.commit();
    }

}
