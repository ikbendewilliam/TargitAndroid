package be.howest.nmct.bluetoothtesting;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.bluetoothtesting.Constants;
import me.aflak.bluetooth.Bluetooth;

/**
 */
public class MainActivityFragment extends Fragment implements BluetoothConnection.OnFinishConnectingListener {
    BluetoothConnection mBluetoothConnection;
    ProgressBar toolbarProgressCircle;
    List<BluetoothDevice> mFoundDevices = new ArrayList<>();
    List<BluetoothDevice> mUsedDevices = new ArrayList<>();

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

        Button button_leds_on = (Button)view.findViewById(R.id.button_leds_on);
        Button button_leds_flash = (Button)view.findViewById(R.id.button_leds_flash);
        Button button_leds_off = (Button)view.findViewById(R.id.button_leds_off);

        button_leds_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_ON);
            }
        });
        button_leds_flash.setOnClickListener(new View.OnClickListener() {
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
    public void finishConnecting(BluetoothDevice device) {
        mBluetoothConnection.sendMessageToDevice(device.getName(), Constants.COMMAND_LED_FLASH_SLOW);
    }
}
