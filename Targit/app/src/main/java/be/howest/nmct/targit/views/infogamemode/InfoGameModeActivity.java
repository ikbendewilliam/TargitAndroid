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

public class InfoGameModeActivity extends AppCompatActivity
        implements SmashitInfoFragment.OnFragmentInteractionListener,
        ZenitInfoFragment.OnFragmentInteractionListener,
        MemoritInfoFragment.OnFragmentInteractionListener {
    public static String EXTRA_GAMEMODE = "gameMode";
    public static String EXTRA_GAMEMODE_SMASHIT = "smashit";
    public static String EXTRA_GAMEMODE_ZENIT = "zenit";
    public static String EXTRA_GAMEMODE_MEMORIT = "memorit";

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
