package be.howest.nmct.targit.views.ingame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import be.howest.nmct.targit.R;
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

public class GameActivity extends AppCompatActivity
        implements ZenitGameFragment.OnZenitGameListener,
        MemoritGameFragment.OnMemoritGameListener,
        SmashitGameFragment.OnSmashitGameListener {

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
        intent.putExtra(EXTRA_GAME, gameMode);
        intent.putExtra(EXTRA_SCORE, score);
        intent.putExtra(EXTRA_CATEGORY, category);
        startActivity(intent);
        //Log.i(Constants.TAG, "showHighscoreActivity: " + intent.getExtras().toString());
    }

    @Override
    public void stopGame(String gameMode, int score, String category) {
        showHighscoreActivity(gameMode, score, category);
    }
}
