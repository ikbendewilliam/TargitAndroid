package be.howest.nmct.targit.views.settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.MyArduinoButtonRecyclerViewAdapter;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.BluetoothConnection.OnConnectionListener;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;


public class StatusFragment extends Fragment implements OnConnectionListener {
    private BluetoothConnection mBluetoothConnection;
    private OnStatusFragmentInteractionListener mListener;
    private MyArduinoButtonRecyclerViewAdapter myArduinoButtonRecyclerViewAdapter;
    private int mConnected = 0;
    private int mPressed = 0;
    private boolean mButtonPressed = false;
    List<ArduinoButton> mArduinoButtons = new ArrayList<>();

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();
//        mBluetoothConnection.registerListener(this);
        mArduinoButtons =  mBluetoothConnection.getArduinoButtons();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.settings_status_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myArduinoButtonRecyclerViewAdapter = new MyArduinoButtonRecyclerViewAdapter(mArduinoButtons, mListener);
        recyclerView.setAdapter(myArduinoButtonRecyclerViewAdapter);

        view.findViewById(R.id.settings_status_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.retryConnections();
            }
        });

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
                        int connected = 0;
                        boolean buttonPressed = false;
                        for (ArduinoButton arduinoButton : mArduinoButtons)
                        {
                            pressed += arduinoButton.getPressedCount();
                            if (arduinoButton.isConnected())
                                connected++;
                            if (arduinoButton.isPressed())
                                buttonPressed = true;
                        }

                        TextView textview_connected_devices = (TextView) view.findViewById(R.id.settings_status_number);
                        TextView textview_clicked_buttons = (TextView) view.findViewById(R.id.settings_status_pressed);

                        textview_connected_devices.setText("#Connected devices: " + connected);
                        textview_clicked_buttons.setText("#clicked buttons: " + pressed);

                        if (mConnected != connected) {
                            for (ArduinoButton arduinoButton : mArduinoButtons)
                                arduinoButton.setConnected(mBluetoothConnection.isDeviceConnected(arduinoButton.getDeviceName()));
                            myArduinoButtonRecyclerViewAdapter.notifyDataSetChanged();
                            mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_FLASH_SLOW);
                        } else if (mPressed != pressed || buttonPressed != mButtonPressed)
                            myArduinoButtonRecyclerViewAdapter.notifyDataSetChanged();

                        mConnected = connected;
                        mPressed = pressed;
                        mButtonPressed = buttonPressed;
                    }
                });
            }
        };
        timer.schedule(checkButtons, 0, 50);
    }

    @Override
    public void incomingMessage(String deviceName, String message) {

    }

    @Override
    public void finishConnecting(BluetoothDevice device) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStatusFragmentInteractionListener) {
            mListener = (OnStatusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStatusFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnStatusFragmentInteractionListener {
        void onListFragmentInteraction();
    }
}
