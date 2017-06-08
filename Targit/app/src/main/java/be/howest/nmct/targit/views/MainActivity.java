package be.howest.nmct.targit.views;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.infogamemode.InfoGameModeActivity;
import be.howest.nmct.targit.views.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements BluetoothConnection.OnConnectionListener {
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
        findViewById(R.id.button_highscore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(HighscoreActivity.class);
            }
        });

        mBluetoothConnection = BluetoothConnection.initiate(this, this);
        try{
            if (mBluetoothConnection.getConnectedDevicesSize() == 0)
                connectDevices();
        }catch(Exception ex){
            System.out.println(ex);
        }

        //set activity to full screen
        findViewById(R.id.activity_main_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showInfoGameModeActivity(String gameMode) {
        Intent intent = new Intent(this, InfoGameModeActivity.class);
        intent.putExtra(InfoGameModeActivity.EXTRA_GAMEMODE, gameMode);
        startActivity(intent);
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public void incomingMessage(String deviceName, String message) {

    }

    @Override
    public void finishConnecting(BluetoothDevice device) {

    }
}
