package be.howest.nmct.targit.views.settings;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.ingame.ZenitGameFragment;

import static be.howest.nmct.targit.Constants.COMMAND_LED_FLASH_SLOW;
import static be.howest.nmct.targit.Constants.COMMAND_LED_OFF;


public class StatusFragment extends Fragment {
    private MyArduinoButtonRecyclerViewAdapter myArduinoButtonRecyclerViewAdapter; // The adapter that fills the list
    private int mConnected = 0; // how many connected devices there are
    private int mConnecting = 0; // how many devices are connecting
    private int mPressed = 0; // how many buttons are pressed
    private boolean mButtonPressed = false; // If any button is pressed
    private BluetoothConnection mBluetoothConnection; // bt connection
    List<ArduinoButton> mArduinoButtons; // All devices
    private StatusFragmentNavigateInterface mListener;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection(); // Get the connection
        mArduinoButtons = mBluetoothConnection.getArduinoButtons(); // get all devices

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.settings_status_recyclerview); // Retrieve the list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()); //create layoutmanager
        recyclerView.setLayoutManager(layoutManager); // Set the manager

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()); //create a divider between rows
        recyclerView.addItemDecoration(dividerItemDecoration); //set the divider

        myArduinoButtonRecyclerViewAdapter = new MyArduinoButtonRecyclerViewAdapter(mArduinoButtons); // create new adapter
        recyclerView.setAdapter(myArduinoButtonRecyclerViewAdapter); // set the adapter

        // Set the retry button
        view.findViewById(R.id.settings_status_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.retryConnections(); // Retry all failed attempts
            }
        });

        // Set the help fab to open helpfragment
        view.findViewById(R.id.settings_status_search_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.navigateToFragment(new HelpFragment());
            }
        });

        updateUIAsync(view); // Start updating the list

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
                        int pressed = 0; // local pressed counter
                        int connected = 0; // local connected devices counter
                        int connecting = 0; // local connecting devices counter
                        boolean buttonPressed = false; // local check any button pressed
                        for (ArduinoButton arduinoButton : mArduinoButtons) {
                            // Loop all devices to count
                            pressed += arduinoButton.getPressedCount();
                            if (arduinoButton.isConnected())
                                connected++;
                            if (arduinoButton.isConnecting())
                                connecting++;
                            if (arduinoButton.isPressed())
                                buttonPressed = true;
                        }

                        if (mConnected != connected || mConnecting != connecting || mPressed != pressed || buttonPressed != mButtonPressed) {
                            if (mConnected != connected) {
                                mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn them off so they flash in sync
                                mBluetoothConnection.sendMessageToAll(COMMAND_LED_FLASH_SLOW); // flash all leds
                            }
                            myArduinoButtonRecyclerViewAdapter.notifyDataSetChanged();

                            mConnected = connected;
                            mPressed = pressed;
                            mButtonPressed = buttonPressed;
                            mConnecting = connecting;
                        }
                    }
                });
            }
        };
        timer.schedule(checkButtons, 0, 50);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatusFragmentNavigateInterface) {
            mListener = (StatusFragmentNavigateInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatusFragmentNavigateInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface StatusFragmentNavigateInterface {
        void navigateToFragment(Fragment newFragment);
    }
}
