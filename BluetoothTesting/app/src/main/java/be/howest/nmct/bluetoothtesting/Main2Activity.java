package be.howest.nmct.bluetoothtesting;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.bluetoothtesting.models.ArduinoButton;

public class Main2Activity extends AppCompatActivity implements BluetoothConnection.OnConnectionListener {
    ProgressBar toolbarProgressCircle;
    private List<ArduinoButton> arduinoButtons = new ArrayList<>();
    private BluetoothConnection mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();

        toolbarProgressCircle = (ProgressBar) this.findViewById(R.id.toolbar_progress_circle);
//        toolbarProgressCircle.setVisibility(View.VISIBLE);

        Button button_leds_on = (Button) findViewById(R.id.button_leds_on);
        Button button_leds_flash_slow = (Button) findViewById(R.id.button_leds_flash_slow);
        Button button_leds_flash_medium = (Button) findViewById(R.id.button_leds_flash_medium);
        Button button_leds_flash_fast = (Button) findViewById(R.id.button_leds_flash_fast);
        Button button_leds_off = (Button) findViewById(R.id.button_leds_off);
        Button button_change_activity = (Button) findViewById(R.id.button_change_activity);

        button_leds_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_ON);
            }
        });
        button_leds_flash_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_FLASH_SLOW);
            }
        });
        button_leds_flash_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_FLASH_MEDIUM);
            }
        });
        button_leds_flash_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_FLASH_FAST);
            }
        });
        button_leds_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
            }
        });
        button_change_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        callAsynchronousTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBluetoothConnection.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBluetoothConnection.unregisterListener();
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask checkButtons = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        int pressed = 0;
                        for (ArduinoButton arduinoButton : arduinoButtons) {
                            pressed += arduinoButton.getPressedCount();
                        }
                        TextView textview_connected_devices = (TextView) findViewById(R.id.textview_connected_devices);
                        TextView textview_clicked_buttons = (TextView) findViewById(R.id.textview_clicked_buttons);

                        textview_connected_devices.setText("#Connected devices: " + mBluetoothConnection.getConnectedDevices().size());
                        textview_clicked_buttons.setText("#clicked buttons: " + pressed);
                    }
                });
            }
        };
        timer.schedule(checkButtons, 0, 100);
    }

    @Override
    public void incomingMessage(String deviceName, String message) {
        for (ArduinoButton arduinoButton : arduinoButtons) {
            if (arduinoButton.getDeviceName().equals(deviceName)) {
                arduinoButton.incomingMessage(message);
            }
        }
    }

    @Override
    public void finishConnecting(BluetoothDevice device) {
        mBluetoothConnection.sendMessageToDevice(device.getName(), Constants.COMMAND_LED_FLASH_SLOW);
    }
}

