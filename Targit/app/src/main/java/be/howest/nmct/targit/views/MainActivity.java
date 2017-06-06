package be.howest.nmct.targit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.infogamemode.InfoGameModeActivity;
import be.howest.nmct.targit.views.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.image_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(SettingsActivity.class);
            }
        });
        findViewById(R.id.button_info_smashit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(InfoGameModeActivity.EXTRA_GAMEMODE_SMASHIT);
            }
        });
        findViewById(R.id.button_info_zenit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(InfoGameModeActivity.EXTRA_GAMEMODE_ZENIT);
            }
        });
        findViewById(R.id.button_info_memorit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(InfoGameModeActivity.EXTRA_GAMEMODE_MEMORIT);
            }
        });
        findViewById(R.id.image_highscore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(HighscoreActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showInfoGameModeActivity(String gameMode)
    {
        Intent intent = new Intent(this, InfoGameModeActivity.class);
        intent.putExtra(InfoGameModeActivity.EXTRA_GAMEMODE, gameMode);
        startActivity(intent);
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
