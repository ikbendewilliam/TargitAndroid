package be.howest.nmct.targit.views;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.highscore.HighscoreActivity;
import be.howest.nmct.targit.views.infogamemode.InfoGameModeActivity;
import be.howest.nmct.targit.views.settings.SettingsActivity;

import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_ZENIT;

public class MainActivity extends AppCompatActivity {
    private BluetoothConnection mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set activity to full screen
        findViewById(R.id.activity_main_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // Set the buttons to do their function
        findViewById(R.id.activity_main_imageview_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(SettingsActivity.class);
            }
        });

        //instellen font highscores
//        Typeface font = Typeface.createFromAsset(getAssets(), "font/BRLNSDB.TTF");
//        TextView txtHighscores = (TextView)findViewById(R.id.activity_main_button_highscore);
//        txtHighscores.setTypeface(font);


        findViewById(R.id.activity_main_imageview_smashit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_SMASHIT);
            }
        });
        findViewById(R.id.activity_main_imageview_zenit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_ZENIT);
            }
        });
        findViewById(R.id.activity_main_imageview_memorit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_MEMORIT);
            }
        });
        findViewById(R.id.activity_main_button_highscore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(HighscoreActivity.class);
            }
        });

        // Initialize bt connection
        mBluetoothConnection = BluetoothConnection.initiate(this);
        if (mBluetoothConnection.getArduinoButtons().size() == 0) {
            // if there are no devices yet
            connectDevices(); // Connect to the devices
        }
    }

    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_main_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}


    }


    private void connectDevices() {
        for (String deviceName : Constants.DEVICE_NAMES) {
            // Create an ArduinoButton for each devicename found in Constants
            mBluetoothConnection.addConnection(new ArduinoButton(deviceName), this);
        }
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showInfoGameModeActivity(String gameMode) {
        Intent intent = new Intent(this, InfoGameModeActivity.class);
        intent.putExtra(EXTRA_GAMEMODE, gameMode);
        startActivity(intent);
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
