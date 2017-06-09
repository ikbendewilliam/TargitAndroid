package be.howest.nmct.targit.views.highscore;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.ingame.GameActivity;
import be.howest.nmct.targit.views.ingame.MemoritGameFragment;
import be.howest.nmct.targit.views.ingame.SmashitGameFragment;
import be.howest.nmct.targit.views.ingame.ZenitGameFragment;

public class HighscoreActivity extends AppCompatActivity {
    public static String EXTRA_GAME = "gameMode";
    public static String EXTRA_SCORE = "score";
    public static String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.highscore_button_to_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(MainActivity.class);
            }
        });

        Intent intent = getIntent();
//        Log.i(Constants.TAG, "got " + intent.getExtras());
//        Log.i(Constants.TAG, "need " + EXTRA_GAME + " and " + EXTRA_SCORE);
//        Log.i(Constants.TAG, "result " + intent.hasExtra(EXTRA_GAME) + " and " + intent.hasExtra(EXTRA_SCORE));
        showFragment(HighscoreFragment.newInstance(GameActivity.EXTRA_GAME_MEMORIT, GameActivity.EXTRA_DURATION_MEDIUM, null));

        if (intent.hasExtra(EXTRA_GAME) && intent.hasExtra(EXTRA_SCORE) && intent.hasExtra(EXTRA_CATEGORY)) {
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            int gameScore = intent.getIntExtra(EXTRA_SCORE, 0);
            String category = intent.getStringExtra(EXTRA_CATEGORY);
            ((TextView) (findViewById(R.id.highscore_textview_score))).setText("score: " + gameScore + " in " + gameMode + " " + category);
//                Log.i(Constants.TAG, "score: " + gameScore + " in " + gameMode + " " + category);
            showFragment(HighscoreFragment.newInstance(gameMode, category, null));
        }

    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_highscoreactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
