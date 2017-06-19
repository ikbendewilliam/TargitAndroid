package be.howest.nmct.targit.views.gameover;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.models.HighscoreEntry;
import be.howest.nmct.targit.views.highscore.HighscoreListFragment;
import be.howest.nmct.targit.views.settings.AboutFragment;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class GameOverActivity extends AppCompatActivity implements SaveScoreFragment.SaveScoreTransitionListener {
    private HighscoreListFragment mHighscoreListFragment;

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

            mHighscoreListFragment = HighscoreListFragment.newInstance(gamemode, category, null);
            showHighscoreListFragment();

            //redirect player to game over instead of savescore if he has a score of 0
            if (score == 0) {
                //show game over fragment
                showFragmentInGameOverLayoutRight(GameOverFragment.newInstance(""+score,
                        gamemode,category));
            } else {
                //make fragment
                Fragment saveScoreFragment = new SaveScoreFragment();
                //put arguments for fragment
                saveScoreFragment.setArguments(getIntent().getExtras());
                //show the fragment
                showFragmentInGameOverLayoutRight(saveScoreFragment);
            }

            //set font
            Typeface font = Typeface.createFromAsset(getAssets(), "font/BRLNSDB.TTF");
            TextView txtTitle = (TextView) findViewById(R.id.fragment_save_score_textview_gametitle);
            txtTitle.setTypeface(font);

            //get the elements that need color change
            //check which game mode it was
            //TODO: insert category
            if (gamemode.equals(EXTRA_GAME_SMASHIT)) {
                //set title name
                txtTitle.setText("SMASH - iT");
            } else if (gamemode.equals(EXTRA_GAME_ZENIT)) {
                //set title name
                txtTitle.setText("ZEN - iT");
            } else if (gamemode.equals(EXTRA_GAME_MEMORIT)) {
                //set title name
                txtTitle.setText("MEMOR - iT");
            }
        }
    }

    //show a fragment on the rightside of the screen.
    private void showFragmentInGameOverLayoutRight(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_game_over_framelayout_right, newFragment);
        transaction.commit();
    }
    //show highscore fragment on the left side of the screen
    private void showHighscoreListFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_game_over_framelayout_highscore, mHighscoreListFragment);
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

    //used when the player filled in their name and saved the score
    @Override
    public void saveScore(HighscoreEntry newEntry) {
        //Save score
        mHighscoreListFragment.addEntry(newEntry);
        //show the game over fragment
        showFragmentInGameOverLayoutRight(GameOverFragment.newInstance(""+newEntry.getScore(),
                getIntent().getStringExtra(EXTRA_GAME),getIntent().getStringExtra(EXTRA_CATEGORY)));
    }

    //used when the player doesn't want to save the score
    @Override
    public void dismissScore(HighscoreEntry newEntry) {
        //show the game over fragment
        showFragmentInGameOverLayoutRight(GameOverFragment.newInstance(""+newEntry.getScore(),
                getIntent().getStringExtra(EXTRA_GAME),getIntent().getStringExtra(EXTRA_CATEGORY)));
    }
}
