package be.howest.nmct.targit.views;

import android.app.Activity;
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


        findViewById(R.id.highscore_button_to_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(MainActivity.class);
            }
        });

        //set activity to full screen
        findViewById(R.id.activity_highscore_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        Intent intent = getIntent();
        Log.i(Constants.TAG, "got " + intent.getExtras());
        Log.i(Constants.TAG, "need " + EXTRA_GAME + " and " + EXTRA_SCORE);
        Log.i(Constants.TAG, "result " + intent.hasExtra(EXTRA_GAME) + " and " + intent.hasExtra(EXTRA_SCORE));

        if (intent.hasExtra(EXTRA_GAME) && intent.hasExtra(EXTRA_SCORE)) {
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            int gameScore = intent.getIntExtra(EXTRA_SCORE, 0);
            Log.i(Constants.TAG, "score: " + gameScore);
            if (intent.hasExtra(EXTRA_CATEGORY)) {
                String category = intent.getStringExtra(EXTRA_CATEGORY);
                ((TextView) (findViewById(R.id.highscore_textview_score))).setText("score: " + gameScore + " in " + gameMode + " " + category);
                Log.i(Constants.TAG, "score: " + gameScore + " in " + gameMode + " " + category);
            } else {
                ((TextView) (findViewById(R.id.highscore_textview_score))).setText("score: " + gameScore + " in " + gameMode);
            }
        }
    }

    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_highscore_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}


    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
