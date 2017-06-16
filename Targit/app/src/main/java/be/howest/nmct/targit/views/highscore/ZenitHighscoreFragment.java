package be.howest.nmct.targit.views.highscore;


import android.app.FragmentTransaction;
import android.graphics.Typeface;
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
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_LONG;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;


public class ZenitHighscoreFragment extends Fragment {
    private HighscoreListFragment mHighscoreListFragment;
    private Button btnHighscoreEasy;
    private Button btnHighscoreMedium;
    private Button btnHighscoreHard;
    
    public ZenitHighscoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zenit_highscore, container, false);

        showlistFragment(EXTRA_DURATION_LONG);

        btnHighscoreEasy = (Button) view.findViewById(R.id.fragment_zenit_highscore_easy);
        btnHighscoreMedium = (Button) view.findViewById(R.id.fragment_zenit_highscore_medium);
        btnHighscoreHard = (Button) view.findViewById(R.id.fragment_zenit_highscore_hard);

        //set listeners on the buttons
        btnHighscoreEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_EASY);
                mHighscoreListFragment.changeList(EXTRA_GAME_ZENIT,EXTRA_DURATION_LONG);
            }
        });
        btnHighscoreMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_MEDIUM);
                mHighscoreListFragment.changeList(EXTRA_GAME_ZENIT,EXTRA_DURATION_MEDIUM);
            }
        });
        btnHighscoreHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(BUTTON_HARD);
                mHighscoreListFragment.changeList(EXTRA_GAME_ZENIT,EXTRA_DURATION_SHORT);
            }
        });

        //set the fonts
        Typeface font = Typeface.createFromAsset( getActivity().getAssets(), "font/BRLNSDB.TTF");
        btnHighscoreEasy.setTypeface(font);
        btnHighscoreMedium.setTypeface(font);
        btnHighscoreHard.setTypeface(font);

        return view;
    }

    //change bg of the button selected
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

    //change the list for another category
    private void showlistFragment(int category) {
        HighscoreListFragment highscoreListFragment = HighscoreListFragment.newInstance(EXTRA_GAME_ZENIT, category, null);
        mHighscoreListFragment = highscoreListFragment;
        showFragment(highscoreListFragment);
    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_fragment_zenit_highscore, newFragment);
        transaction.commit();
    }

}
