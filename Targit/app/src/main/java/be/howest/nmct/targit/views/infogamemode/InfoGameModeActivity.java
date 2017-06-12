package be.howest.nmct.targit.views.infogamemode;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.ingame.GameActivity;

import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES;

// The activity that shows the info about the gameMode
public class InfoGameModeActivity extends AppCompatActivity
        implements SmashitInfoFragment.OnSmashitInfoListener,
        ZenitInfoFragment.OnZenitInfoListener,
        MemoritInfoFragment.OnMemoritInfoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_game_mode);

        //set activity to full screen
        findViewById(R.id.activity_infogamemode_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // See if there is specified what to show
        if (getIntent().hasExtra(EXTRA_GAMEMODE)) {
            String gameMode = getIntent().getStringExtra(EXTRA_GAMEMODE);

            if (gameMode == null) {
                // If the gamemode is empty, return to MainActivity
                showActivity(MainActivity.class);
            } else if (gameMode.equals(EXTRA_GAMEMODE_SMASHIT)) {
                showFragment(new SmashitInfoFragment()); // Show smashit's info
            } else if (gameMode.equals(EXTRA_GAMEMODE_ZENIT)) {
                showFragment(new ZenitInfoFragment()); // Show zenit's info
            } else if (gameMode.equals(EXTRA_GAMEMODE_MEMORIT)) {
                showFragment(new MemoritInfoFragment()); // Show memorit's info
            }
        }
    }
    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_infogamemode_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}


    }


    // show a fragment
    // @param newFragment: the fragment to show
    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_info_game_modeactivity, newFragment);
        transaction.commit();
    }

    // Change to an activity
    // @param activity: the class of the activity you want to show
    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    // show the gameActivity
    // @param gameMode: the gameMode to play (as defined in Constants)
    // @param extraName: the name of the other parameter (as defined in Constants)
    // @param extraValue: the value of the other parameter (as defined in Constants)
    void showGameActivity(String gameMode, String extraName, int extraValue) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_GAME, gameMode);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    // show the gameActivity
    // @param gameMode: the gameMode to play (as defined in Constants)
    // @param extraName: the name of the other parameter (as defined in Constants)
    // @param extraValue: the value of the other parameter (as defined in Constants)
    void showGameActivity(String gameMode, String extraName, String extraValue) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_GAME, gameMode);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    @Override
    public void playMemorit(int cmdLives) {
        showGameActivity(EXTRA_GAME_MEMORIT, EXTRA_LIVES, cmdLives);
    }

    @Override
    public void playZenit(int cmdDuration) {
        showGameActivity(EXTRA_GAME_ZENIT, EXTRA_DURATION, cmdDuration);
    }

    @Override
    public void playSmashit(String cmdDifficulty) {
        showGameActivity(EXTRA_GAME_SMASHIT, EXTRA_DIFFICULTY, cmdDifficulty);
    }
}
