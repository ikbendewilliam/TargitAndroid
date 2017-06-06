package be.howest.nmct.targit.views.settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.models.ArduinoButton;


public class StatusFragment extends Fragment {
    private BluetoothConnection mBluetoothConnection;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();

        TextView settings_status_number = (TextView)view.findViewById(R.id.settings_status_number);
        settings_status_number.setText("#Connected devices: " + mBluetoothConnection.getConnectedDevices().size());

        return view;
    }

    public void updateUIAsync() {
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

}
