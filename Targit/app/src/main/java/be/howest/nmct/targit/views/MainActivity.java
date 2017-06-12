package be.howest.nmct.targit.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the buttons to do their function
        findViewById(R.id.image_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(SettingsActivity.class);
            }
        });
        findViewById(R.id.button_info_smashit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_SMASHIT);
            }
        });
        findViewById(R.id.button_info_zenit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_ZENIT);
            }
        });
        findViewById(R.id.button_info_memorit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoGameModeActivity(EXTRA_GAMEMODE_MEMORIT);
            }
        });
        findViewById(R.id.button_highscore).setOnClickListener(new View.OnClickListener() {
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
