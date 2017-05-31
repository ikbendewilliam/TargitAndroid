package be.howest.nmct.bluetoothtesting;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import be.howest.nmct.bluetoothtesting.Constants;
import me.aflak.bluetooth.Bluetooth;

/**
 */
public class MainActivityFragment extends Fragment implements BluetoothConnection.OnConnectionListener {
    BluetoothConnection mBluetoothConnection;
    ProgressBar toolbarProgressCircle;
    private int mButtonPressed = 0;

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

        return view;
    }

    private void connectDevices() {
        for (String deviceName : Constants.DEVICE_NAMES) {
            mBluetoothConnection.addConnection(deviceName);
        }
    }

    @Override
    public void incomingMessage(String deviceName, String message) {
        mButtonPressed++;
    }

    @Override
    public void finishConnecting(BluetoothDevice device) {
        mBluetoothConnection.sendMessageToDevice(device.getName(), Constants.COMMAND_LED_FLASH_SLOW);
    }
}
