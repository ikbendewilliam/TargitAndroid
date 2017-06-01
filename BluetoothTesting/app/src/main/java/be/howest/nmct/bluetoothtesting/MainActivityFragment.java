package be.howest.nmct.bluetoothtesting;

import android.app.Fragment;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import be.howest.nmct.bluetoothtesting.Constants;
import be.howest.nmct.bluetoothtesting.models.ArduinoButton;
import me.aflak.bluetooth.Bluetooth;

/**
 */
public class MainActivityFragment extends Fragment implements BluetoothConnection.OnConnectionListener {
    BluetoothConnection mBluetoothConnection;
    ProgressBar toolbarProgressCircle;
    private List<ArduinoButton> arduinoButtons = new ArrayList<>();
//    private int[] mButtonPressed = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbarProgressCircle = (ProgressBar) getActivity().findViewById(R.id.toolbar_progress_circle);
//        toolbarProgressCircle.setVisibility(View.VISIBLE);
        mBluetoothConnection = new BluetoothConnection(getActivity(), this);

        connectDevices();

        Button button_leds_on = (Button) view.findViewById(R.id.button_leds_on);
        Button button_leds_flash_slow = (Button) view.findViewById(R.id.button_leds_flash_slow);
        Button button_leds_flash_medium = (Button) view.findViewById(R.id.button_leds_flash_medium);
        Button button_leds_flash_fast = (Button) view.findViewById(R.id.button_leds_flash_fast);
        Button button_leds_off = (Button) view.findViewById(R.id.button_leds_off);

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

//        TextView textview_connected_devices = (TextView) view.findViewById(R.id.textview_connected_devices);
//        textview_connected_devices.setText("#Connected devices: " + mBluetoothConnection.getConnectedDevices().size());

        callAsynchronousTask(view);

        return view;
    }

    private void connectDevices() {
        for (String deviceName : Constants.DEVICE_NAMES) {
            mBluetoothConnection.addConnection(deviceName);
            arduinoButtons.add(new ArduinoButton(deviceName));
        }
    }

    public void callAsynchronousTask(final View view) {
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
                        TextView textview_connected_devices = (TextView) view.findViewById(R.id.textview_connected_devices);
                        TextView textview_clicked_buttons = (TextView) view.findViewById(R.id.textview_clicked_buttons);

                        textview_connected_devices.setText("#Connected devices: " + mBluetoothConnection.getConnectedDevices().size());
                        textview_clicked_buttons.setText("#clicked buttons: " + pressed);
                    }
                });
            }
        };
//        TimerTask checkUnconnectedDevices = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        TextView textview_connected_devices = (TextView) view.findViewById(R.id.textview_connected_devices);
//                        textview_connected_devices.setText("#Connected devices: " + mBluetoothConnection.getConnectedDevices().size());
//
//                        //mBluetoothConnection.retryConnections(Constants.DEVICE_NAMES);
//                    }
//                });
//            }
//        };
        timer.schedule(checkButtons, 0, 100);
//        timer.schedule(checkUnconnectedDevices, 5000, 3000);
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
