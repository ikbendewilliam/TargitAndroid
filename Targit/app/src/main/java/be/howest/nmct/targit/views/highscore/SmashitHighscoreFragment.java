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


public class SmashitHighscoreFragment extends Fragment {

    private HighscoreListFragment mHighscoreListFragment;
    private Button btnHighscoreEasy;
    private Button btnHighscoreMedium;
    private Button btnHighscoreHard;

    public SmashitHighscoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smashit_highscore, container, false);

        showlistFragment(EXTRA_DIFFICULTY_EASY);

       btnHighscoreEasy = (Button) view.findViewById(R.id.fragment_smashit_highscore_easy);
       btnHighscoreMedium = (Button) view.findViewById(R.id.fragment_smashit_highscore_medium);
       btnHighscoreHard = (Button) view.findViewById(R.id.fragment_smashit_highscore_hard);

        btnHighscoreEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_EASY);
                mHighscoreListFragment.changeList(EXTRA_GAME_SMASHIT,EXTRA_DIFFICULTY_EASY);
            }
        });
        btnHighscoreMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_MEDIUM);
                mHighscoreListFragment.changeList(EXTRA_GAME_SMASHIT,EXTRA_DIFFICULTY_MEDIUM);
            }
        });
        btnHighscoreHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_HARD);
                mHighscoreListFragment.changeList(EXTRA_GAME_SMASHIT,EXTRA_DIFFICULTY_HARD);
            }
        });



        return view;
    }

    private void selectButton(int buttonId) {
        btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green);
        btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange);
        btnHighscoreHard.setBackgroundResource(R.drawable.postit_red);

        switch (buttonId) {
            case BUTTON_EASY:
                btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green_selected);
                break;
            case BUTTON_MEDIUM:
                btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange_selected);
                break;
            case BUTTON_HARD:
                btnHighscoreHard.setBackgroundResource(R.drawable.postit_red_selected);
                break;
        }
    }

    private void showlistFragment(String category) {
        HighscoreListFragment highscoreListFragment = HighscoreListFragment.newInstance(EXTRA_GAME_SMASHIT, category, null);
        mHighscoreListFragment = highscoreListFragment;
        showFragment(highscoreListFragment);
    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_fragment_smashit_highscore, newFragment);
        transaction.commit();
    }

}
