package be.howest.nmct.targit.views.ingame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.gameover.GameOverActivity;
import be.howest.nmct.targit.views.highscore.HighscoreActivity;
import be.howest.nmct.targit.views.MainActivity;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MANY;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

// The activity that plays the game
public class GameActivity extends AppCompatActivity
        implements ZenitGameFragment.OnZenitGameListener,
        MemoritGameFragment.OnMemoritGameListener,
        SmashitGameFragment.OnSmashitGameListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //set activity to full screen
        findViewById(R.id.activity_activitygame_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // get the intent
        Intent intent = getIntent();
        // If this intent is sent from info_game
        if (intent.hasExtra(EXTRA_GAME)) {
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            // get the mode from the intent
            if (gameMode == null) {
                // If the gamemode is empty, return to MainActivity
                showActivity(MainActivity.class);
            } else if (gameMode.equals(EXTRA_GAME_SMASHIT)) {
                // Game mode Smashit
                if (intent.hasExtra(EXTRA_DIFFICULTY)) {
                    String difficulty = intent.getStringExtra(EXTRA_DIFFICULTY);
                    if (difficulty == null) {
                        showActivity(MainActivity.class);
                    } else {
                        // Start the game on the given difficulty
                        showFragment(SmashitGameFragment.newInstance(difficulty));
                    }
                }
            } else if (gameMode.equals(EXTRA_GAME_ZENIT)) {
                if (intent.hasExtra(EXTRA_DURATION)) {
                    int duration = intent.getIntExtra(EXTRA_DURATION, EXTRA_DURATION_SHORT);
                    // Start the game with the given duration
                    showFragment(ZenitGameFragment.newInstance(duration));
                }
            } else if (gameMode.equals(EXTRA_GAME_MEMORIT)) {
                if (intent.hasExtra(EXTRA_LIVES)) {
                    int lives = intent.getIntExtra(EXTRA_LIVES, EXTRA_LIVES_MANY);
                    // Start the game with the given lives
                    showFragment(MemoritGameFragment.newInstance(lives));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable the back pressed
    }

    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_activitygame_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

    // show a fragment
    // @param newFragment: the fragment to show

    }
    private void showFragment(Fragment newFragment) {
        //Log.i(Constants.TAG, "showFragment: " + newFragment.toString());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_gameactivity, newFragment);
        transaction.commit();
    }

    // Change to an activity
    // @param activity: the class of the activity you want to show
    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    // @param gameMode: the mode that was played
    // @param score: the achieved score
    // @param category: The played category
    void showGameOverActivity(String gameMode, int score, String category) {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(EXTRA_GAME, gameMode);
        intent.putExtra(EXTRA_SCORE, score);
        intent.putExtra(EXTRA_CATEGORY, category);
        startActivity(intent);
        //Log.i(Constants.TAG, "showHighscoreActivity: " + intent.getExtras().toString());
    }

    // When the game is stopped
    // @param gameMode: the mode that was played
    // @param score: the achieved score
    // @param category: The played category
    @Override
    public void stopGame(String gameMode, int score, String category) {
        showGameOverActivity(gameMode, score, category);
    }
}
