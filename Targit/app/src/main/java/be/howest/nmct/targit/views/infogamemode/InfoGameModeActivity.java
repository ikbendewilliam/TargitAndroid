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

public class InfoGameModeActivity extends AppCompatActivity
        implements SmashitInfoFragment.OnSmashitInfoListener,
        ZenitInfoFragment.OnZenitInfoListener,
        MemoritInfoFragment.OnMemoritInfoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_game_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_info_game_modeactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void showGameActivity(String gameMode, String extraName, int extraValue) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_GAME, gameMode);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

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
