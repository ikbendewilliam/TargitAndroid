package be.howest.nmct.targit.views.highscore;


import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.BUTTON_EASY;
import static be.howest.nmct.targit.Constants.BUTTON_HARD;
import static be.howest.nmct.targit.Constants.BUTTON_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
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

        //init buttons
        btnHighscoreEasy = (Button) view.findViewById(R.id.fragment_memorit_highscore_easy);
        btnHighscoreMedium = (Button) view.findViewById(R.id.fragment_memorit_highscore_medium);
        btnHighscoreHard = (Button) view.findViewById(R.id.fragment_memorit_highscore_hard);

        //set listeners on the buttons
        btnHighscoreEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
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

        //set the fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        btnHighscoreEasy.setTypeface(font);
        btnHighscoreMedium.setTypeface(font);
        btnHighscoreHard.setTypeface(font);

        return view;
    }

    //change the list for another category
    private void showlistFragment(int category) {
        HighscoreListFragment highscoreListFragment = HighscoreListFragment.newInstance(EXTRA_GAME_MEMORIT, category, null, true);
        mHighscoreListFragment = highscoreListFragment;
        showFragment(highscoreListFragment);
    }

    //change bg of the button selected
    private void selectButton(int buttonId) {
        btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green);
        btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange);
        btnHighscoreHard.setBackgroundResource(R.drawable.postit_red);
        removeSelectButtonLayout(btnHighscoreEasy);
        removeSelectButtonLayout(btnHighscoreMedium);
        removeSelectButtonLayout(btnHighscoreHard);

        switch (buttonId) {
            case BUTTON_EASY:
                btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green_selected);
                setSelectButtonLayout(btnHighscoreEasy);
                break;
            case BUTTON_MEDIUM:
                btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange_selected);
                setSelectButtonLayout(btnHighscoreMedium);
                break;
            case BUTTON_HARD:
                btnHighscoreHard.setBackgroundResource(R.drawable.postit_red_selected);
                setSelectButtonLayout(btnHighscoreHard);
                break;
        }
    }

    //change layout params for button to look selected
    private void setSelectButtonLayout(Button btn) {
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) btn.getLayoutParams();
        params.removeRule(RelativeLayout.RIGHT_OF);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.fragment_memorit_highscore_view_selected);
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.widthPercent = 0.13f;
        btn.setLayoutParams(params);
    }

    //change layout params for button to look unselected
    private void removeSelectButtonLayout(Button btn) {
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) btn.getLayoutParams();
        params.removeRule(RelativeLayout.RIGHT_OF);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.fragment_memorit_highscore_view);
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.widthPercent = 0.12f;
        btn.setLayoutParams(params);
    }


    //show a fragment in the framelayout for the list
    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_fragment_memorit_highscore, newFragment);
        transaction.commit();
    }

}
