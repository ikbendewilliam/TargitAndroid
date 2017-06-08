package be.howest.nmct.targit.views.infogamemode;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.ingame.GameActivity;

public class InfoGameModeActivity extends AppCompatActivity
        implements SmashitInfoFragment.OnSmashitInfoListener,
        ZenitInfoFragment.OnZenitInfoListener,
        MemoritInfoFragment.OnMemoritInfoListener {
    public static String EXTRA_GAMEMODE = "gameMode";
    public static String EXTRA_GAMEMODE_SMASHIT = "smashit";
    public static String EXTRA_GAMEMODE_ZENIT = "zenit";
    public static String EXTRA_GAMEMODE_MEMORIT = "memorit";

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

        if (getIntent().hasExtra(EXTRA_GAMEMODE)) {
            String gameMode = getIntent().getStringExtra(EXTRA_GAMEMODE);

            if (gameMode == null) {
                showActivity(MainActivity.class);
            } else if (gameMode.equals(EXTRA_GAMEMODE_SMASHIT)) {
                showFragment(new SmashitInfoFragment());
            } else if (gameMode.equals(EXTRA_GAMEMODE_ZENIT)) {
                showFragment(new ZenitInfoFragment());
            } else if (gameMode.equals(EXTRA_GAMEMODE_MEMORIT)) {
                showFragment(new MemoritInfoFragment());
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


    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_info_game_modeactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void showGameActivity(String gameMode) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_GAME, gameMode);
        startActivity(intent);
    }

    void showGameActivity(String gameMode, String extraName, String extraValue) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_GAME, gameMode);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    @Override
    public void playMemorit() {
        showGameActivity(GameActivity.EXTRA_GAME_MEMORIT);
    }

    @Override
    public void playZenit(String cmdDuration) {
        showGameActivity(GameActivity.EXTRA_GAME_ZENIT, GameActivity.EXTRA_DURATION, cmdDuration);
    }

    @Override
    public void playSmashit(String cmdDifficulty) {
        showGameActivity(GameActivity.EXTRA_GAME_SMASHIT, GameActivity.EXTRA_DIFFICULTY, cmdDifficulty);
    }
}
