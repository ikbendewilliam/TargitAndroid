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
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.HighscoreActivity;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;
import be.howest.nmct.targit.views.infogamemode.SmashitInfoFragment;
import be.howest.nmct.targit.views.infogamemode.ZenitInfoFragment;

public class GameActivity extends AppCompatActivity
        implements ZenitGameFragment.OnZenitGameListener,
        MemoritGameFragment.OnMemoritGameListener,
        SmashitGameFragment.OnSmashitGameListener {
    public static String EXTRA_GAME = "gameMode";
    public static String EXTRA_GAME_SMASHIT = "smashit";
    public static String EXTRA_GAME_ZENIT = "zenit";
    public static String EXTRA_GAME_MEMORIT = "memorit";
    public static String EXTRA_DIFFICULTY = "difficulty";
    public static String EXTRA_DIFFICULTY_EASY = "easy";
    public static String EXTRA_DIFFICULTY_MEDIUM = "medium";
    public static String EXTRA_DIFFICULTY_HARD = "hard";
    public static String EXTRA_DURATION = "duration";
    public static int EXTRA_DURATION_SHORT = 30;
    public static int EXTRA_DURATION_MEDIUM = 60;
    public static int EXTRA_DURATION_LONG = 90;
    public static String EXTRA_LIVES = "lives";
    public static int EXTRA_LIVES_MANY = 5;
    public static int EXTRA_LIVES_MEDIUM = 3;
    public static int EXTRA_LIVES_FEW = 1;
    public static int STEP_TIME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GAME)) {
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            if (gameMode == null) {
                showActivity(MainActivity.class);
            } else if (gameMode.equals(EXTRA_GAME_SMASHIT)) {
                if (intent.hasExtra(EXTRA_DIFFICULTY)) {
                    String difficulty = intent.getStringExtra(EXTRA_DIFFICULTY);
                    if (difficulty == null) {
                        showActivity(MainActivity.class);
                    } else {
                        showFragment(SmashitGameFragment.newInstance(difficulty));
                    }
                }
            } else if (gameMode.equals(EXTRA_GAME_ZENIT)) {
                if (intent.hasExtra(EXTRA_DURATION)) {
                    int duration = intent.getIntExtra(EXTRA_DURATION, EXTRA_DURATION_SHORT);
                    showFragment(ZenitGameFragment.newInstance(duration));
                }
            } else if (gameMode.equals(EXTRA_GAME_MEMORIT)) {
                if (intent.hasExtra(EXTRA_LIVES)) {
                    int lives = intent.getIntExtra(EXTRA_LIVES, EXTRA_LIVES_MANY);
                    showFragment(MemoritGameFragment.newInstance(lives));
                }
            }
        }
    }

    private void showFragment(Fragment newFragment) {
        //Log.i(Constants.TAG, "showFragment: " + newFragment.toString());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_gameactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void showHighscoreActivity(String gameMode, int score, String category) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        intent.putExtra(HighscoreActivity.EXTRA_GAME, gameMode);
        intent.putExtra(HighscoreActivity.EXTRA_SCORE, score);
        intent.putExtra(HighscoreActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
        //Log.i(Constants.TAG, "showHighscoreActivity: " + intent.getExtras().toString());
    }

    @Override
    public void stopGame(String gameMode, int score, String category) {
        showHighscoreActivity(gameMode, score, category);
    }
}
