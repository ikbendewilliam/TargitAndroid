package be.howest.nmct.targit.views.settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;


public class StatusFragment extends Fragment implements BluetoothConnection.OnConnectionListener {
    private BluetoothConnection mBluetoothConnection;
    private List<ArduinoButton> arduinoButtons = new ArrayList<>();

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();
        mBluetoothConnection.registerListener(this);

        for (String deviceName : Constants.DEVICE_NAMES)
            arduinoButtons.add(new ArduinoButton(deviceName));

        updateUIAsync(view);

        return view;
    }

    public void updateUIAsync(final View view) {
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
                        TextView textview_connected_devices = (TextView) view.findViewById(R.id.settings_status_number);
                        TextView textview_clicked_buttons = (TextView) view.findViewById(R.id.settings_status_pressed);

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

    }
}
