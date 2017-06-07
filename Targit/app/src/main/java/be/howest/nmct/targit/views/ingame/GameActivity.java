package be.howest.nmct.targit.views.ingame;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;
import be.howest.nmct.targit.views.infogamemode.SmashitInfoFragment;
import be.howest.nmct.targit.views.infogamemode.ZenitInfoFragment;

public class GameActivity extends AppCompatActivity {
    public static String EXTRA_GAME = "gameMode";
    public static String EXTRA_GAME_SMASHIT = "smashit";
    public static String EXTRA_GAME_ZENIT = "zenit";
    public static String EXTRA_GAME_MEMORIT = "memorit";
    public static String EXTRA_DIFFICULTY = "difficulty";
    public static String EXTRA_DIFFICULTY_EASY = "easy";
    public static String EXTRA_DIFFICULTY_MEDIUM = "medium";
    public static String EXTRA_DIFFICULTY_HARD = "hard";
    public static String EXTRA_DURATION = "duration";
    public static String EXTRA_DURATION_SHORT = "short";
    public static String EXTRA_DURATION_MEDIUM = "normal";
    public static String EXTRA_DURATION_LONG = "long";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(EXTRA_GAME)) {
            String gameMode = getIntent().getStringExtra(EXTRA_GAME);

            if (gameMode == null) {
                showActivity(MainActivity.class);
            } else if (gameMode.equals(EXTRA_GAME_SMASHIT)) {
                if (getIntent().hasExtra(EXTRA_DIFFICULTY)) {
                    String difficulty = getIntent().getStringExtra(EXTRA_DIFFICULTY);
                    if (difficulty == null) {
                        showActivity(MainActivity.class);
                    } else if (difficulty.equals(EXTRA_DIFFICULTY_EASY)) {
                        showFragment(new SmashitGameFragment());
                    } else if (difficulty.equals(EXTRA_DIFFICULTY_MEDIUM)) {
                        showFragment(new SmashitGameFragment());
                    } else if (difficulty.equals(EXTRA_DIFFICULTY_HARD)) {
                        showFragment(new SmashitGameFragment());
                    }
                }
            } else if (gameMode.equals(EXTRA_GAME_ZENIT)) {
                if (getIntent().hasExtra(EXTRA_DURATION)) {
                    String duration = getIntent().getStringExtra(EXTRA_DURATION);
                    if (duration == null) {
                        showActivity(MainActivity.class);
                    } else if (duration.equals(EXTRA_DURATION_SHORT)) {
                        showFragment(new ZenitGameFragment());
                    } else if (duration.equals(EXTRA_DURATION_MEDIUM)) {
                        showFragment(new ZenitGameFragment());
                    } else if (duration.equals(EXTRA_DURATION_LONG)) {
                        showFragment(new ZenitGameFragment());
                    }
                }
            } else if (gameMode.equals(EXTRA_GAME_MEMORIT)) {
                showFragment(new MemoritGameFragment());
            }
        }
    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_gameactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
