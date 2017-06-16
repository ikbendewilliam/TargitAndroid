package be.howest.nmct.targit.views.gameover;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.models.HighscoreEntry;
import be.howest.nmct.targit.views.highscore.HighscoreFragment;
import be.howest.nmct.targit.views.settings.AboutFragment;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class GameOverActivity extends AppCompatActivity implements SaveScoreFragment.SaveScoreTransitionListener {
    private HighscoreFragment mHighscoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        //set activity to full screen
        findViewById(R.id.activity_game_over_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        //check if the activity recieved all the params (EXTRA_GAME,EXTRA_SCORE,EXTRA_CATEGORY
        if (getIntent().hasExtra(EXTRA_GAME) && getIntent().hasExtra(EXTRA_SCORE) && getIntent().hasExtra(EXTRA_CATEGORY)) {
            //get arguments
            String gamemode = getIntent().getStringExtra(EXTRA_GAME);
            int score = getIntent().getIntExtra(EXTRA_SCORE, 0);
            String category = getIntent().getStringExtra(EXTRA_CATEGORY);

            mHighscoreFragment = HighscoreFragment.newInstance(gamemode, category, null);
            showHighscoreFragment();
            if (score == 0) {
                showFragmentInGameOverLayoutRight(new AboutFragment()); // TODO: Show game over fragment
            } else {
                //make fragment
                Fragment saveScoreFragment = SaveScoreFragment.newInstance();
                //put arguments for fragment
                saveScoreFragment.setArguments(getIntent().getExtras());
                //show the fragment
                showFragmentInGameOverLayoutRight(saveScoreFragment);
            }
        }
    }

    private void showFragmentInGameOverLayoutRight(Fragment newFragment) {
        //Log.i(Constants.TAG, "showFragment: " + newFragment.toString());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_game_over_framelayout_right, newFragment);
        transaction.commit();
    }

    private void showHighscoreFragment() {
        //Log.i(Constants.TAG, "showFragment: " + newFragment.toString());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_game_over_framelayout_highscore, mHighscoreFragment);
        transaction.commit();
    }

    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_game_over_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void saveScore(HighscoreEntry newEntry) {
        mHighscoreFragment.addEntry(newEntry);
        showFragmentInGameOverLayoutRight(new AboutFragment()); // TODO: Show game over fragment
    }
}
