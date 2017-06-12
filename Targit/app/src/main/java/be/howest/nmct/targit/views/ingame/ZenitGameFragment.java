package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;

import static be.howest.nmct.targit.Constants.COMMAND_LED_OFF;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.STEP_TIME;


public class ZenitGameFragment extends Fragment {
    private int mDuration; // Duration of the game
    private int mMaxFrame; // Max frames (to check duration)
    private int mFrameCounter = 0; // A counter to keep count the frames
    private int mPressedOnFrame = 0; // When the button was pressed
    private int mScore = 0; // The current score
    private ArduinoButton mLitButton = null; // The button that is lit
    private ArduinoButton mPreviousLitButton = null; // The last lit button
    private Timer mTimer = new Timer(); // The game clock
    private List<ArduinoButton> mArduinoButtons; // All devices
    private BluetoothConnection mBluetoothConnection; // bt connection

    private OnZenitGameListener mListener; // Listener to stop the game

    public ZenitGameFragment() {
        // Required empty public constructor
    }

    // Create an instance of this fragment
    public static ZenitGameFragment newInstance(int duration) {
        ZenitGameFragment fragment = new ZenitGameFragment();
        fragment.mDuration = duration;
        fragment.mMaxFrame = (duration + 3) * 1000 / STEP_TIME; // Set the number of frames in this game
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zenit_game, container, false);

        view.findViewById(R.id.ingame_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });


        mBluetoothConnection = BluetoothConnection.getBluetoothConnection(); // Get the connection
        mArduinoButtons = mBluetoothConnection.getArduinoButtons(); // get the devices
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off

        startGameSteps(view); // configure the routine
        // initiate the textfield
        ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);

        return view;
    }

    // End the game
    private void stopGame() {
        if (mListener != null)
            mListener.stopGame(EXTRA_GAME_ZENIT, mScore, "" + mDuration); // Send the highscore
        mTimer.cancel(); // Stop the actual game
        mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF); // Turn all leds off
    }

    // Initiate the routine
    // this method create a loop for the method gameStep to run every STEP_TIME ms
    public void startGameSteps(final View view) {
        mTimer.cancel();
        mTimer = new Timer();
        final Handler handler = new Handler();
        TimerTask gamestep = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        gameStep(mFrameCounter++, view);
                    }
                });
            }
        };
        mTimer.scheduleAtFixedRate(gamestep, (long) STEP_TIME, (long) STEP_TIME);
    }

    private void gameStep(int frame, View view) {
        if (frame * STEP_TIME > 3000) // after 3 seconds
        {
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("tijd over: " + ((mMaxFrame - frame) * STEP_TIME / 1000));

            if ((frame - mPressedOnFrame) * STEP_TIME > 200 && mLitButton == null) {
                // wait 200ms and no button is lit
                // Get a new random button that is different from the previous one
                int i = 0;
                Random random = new Random();
                do {
                    mLitButton = mArduinoButtons.get(random.nextInt(mArduinoButtons.size()));
                    if (i++ > 100) {
                        stopGame();
                        break; // BREAK OUT OF LOOP AFTER 100 TRIES
                    }
                }
                while (!mLitButton.isEnabled() || !mLitButton.isConnected() || mPreviousLitButton == mLitButton);
                // Get a new random button that is different from the previous one

                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF); // Turn all leds off
                mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), Constants.COMMAND_LED_FLASH_FAST); // Flash the button to press
            } else if (mLitButton != null) {
                // If a button is lit
                if (mLitButton.isPressed() && mLitButton.isConnected() && mLitButton.isEnabled()) {
                    // If this button is pressed
                    mPreviousLitButton = mLitButton; // set the previous button
                    mLitButton = null; // unset the lit button
                    mScore++; // increment score
                    mPressedOnFrame = frame; // This frame it is pressed
                    ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);
                    mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF); // Turn all leds off
                }
            }

            if (frame >= mMaxFrame) {
                // Time's up
                stopGame();
            }
        } else
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("het spel start in: " + (3 - frame * STEP_TIME / 1000));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnZenitGameListener) {
            mListener = (OnZenitGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnZenitGameListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startGameSteps(getView());
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnZenitGameListener {
        void stopGame(String gameMode, int score, String category);
    }
}
