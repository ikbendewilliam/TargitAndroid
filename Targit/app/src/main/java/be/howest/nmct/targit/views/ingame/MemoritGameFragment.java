package be.howest.nmct.targit.views.ingame;

import android.app.DialogFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;

import static be.howest.nmct.targit.Constants.COMMAND_LED_OFF;
import static be.howest.nmct.targit.Constants.COMMAND_LED_ON;
import static be.howest.nmct.targit.Constants.COUNTDOWN_TIME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MANY;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MEDIUM;
import static be.howest.nmct.targit.Constants.STEP_TIME;
import static be.howest.nmct.targit.Constants.WAIT_TIME_MAX;
import static be.howest.nmct.targit.Constants.WAIT_TIME_MIN;
import static be.howest.nmct.targit.Constants.WAIT_TIME_RATE;

public class MemoritGameFragment extends Fragment {
    private int mLastFrameLit = 0; // The frame when the last device lit up
    private int mFrameCounter = 0; // A counter to keep count the frames
    private int mScore = 0; // The current score
    private int mCategory; // The played category
    private int mLives; // Current lives
    private int mIterator = 0; // An iterator to keep track of which button to press/which device to lit up
    private int mWaitTime = WAIT_TIME_MAX;
    private boolean mUserinput = true; // If it is the user its turn
    private boolean mIsPressed = false; // If a button is being pressed
    private ArduinoButton mLitButton = null; // Which button to lit
    private Timer mTimer = new Timer(); // A timer to execute the game
    private List<ArduinoButton> mArduinoButtons; // A list of the devices
    private BluetoothConnection mBluetoothConnection; // the connection with bt
    private List<ArduinoButton> mSequence = new ArrayList<>(); // The given sequence
    private MediaPlayer pointPlayer;
    private MediaPlayer failPlayer;
    private MediaPlayer gameoverPlayer;

    private OnMemoritGameListener mListener; // A listener to stop the game

    public MemoritGameFragment() {
        // Required empty public constructor
    }

    public static MemoritGameFragment newInstance(int lives) {
        MemoritGameFragment fragment = new MemoritGameFragment();
        fragment.mLives = lives;
        fragment.mCategory = lives;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_game, container, false);

        // Set the stop button to stop the game
        view.findViewById(R.id.fragment_memorit_game_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection(); // Get the connection
        mArduinoButtons = mBluetoothConnection.getArduinoButtons(); // get the devices
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off

        pointPlayer = MediaPlayer.create(getContext(), R.raw.point); // Set the point sound
        failPlayer = MediaPlayer.create(getContext(), R.raw.fail); // Set the fail sound
        gameoverPlayer = MediaPlayer.create(getContext(), R.raw.gameover); // Set the game over sound

        startGameSteps(view); // configure the routine
        // initiate the textfields
        ((TextView) view.findViewById(R.id.fragment_memorit_game_textview_score)).setText("" + mScore);
        showLives(view);

        if (mLives <= 3) {
            view.findViewById(R.id.fragment_memorit_game_imageview_heart4).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.fragment_memorit_game_imageview_heart5).setVisibility(View.INVISIBLE);
            if (mLives == 1) {
                view.findViewById(R.id.fragment_memorit_game_imageview_heart2).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.fragment_memorit_game_imageview_heart3).setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private void showLives(View view) {
        if (mCategory == EXTRA_LIVES_FEW)
        {
            if (mLives >= 1)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setVisibility(View.INVISIBLE);
        }
        else if (mCategory == EXTRA_LIVES_MEDIUM)
        {
            if (mLives >= 1)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart2)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart2)).setVisibility(View.INVISIBLE);
            if (mLives >= 2)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setVisibility(View.INVISIBLE);
            if (mLives >= 3)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart3)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart3)).setVisibility(View.INVISIBLE);
        }
        else if (mCategory == EXTRA_LIVES_MANY)
        {
            if (mLives >= 1)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart4)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart4)).setVisibility(View.INVISIBLE);
            if (mLives >= 2)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart2)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart2)).setVisibility(View.INVISIBLE);
            if (mLives >= 3)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart1)).setVisibility(View.INVISIBLE);
            if (mLives >= 4)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart3)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart3)).setVisibility(View.INVISIBLE);
            if (mLives >= 5)
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart5)).setImageResource(R.drawable.ic_hart_memorit);
            else
                ((ImageView) view.findViewById(R.id.fragment_memorit_game_imageview_heart5)).setVisibility(View.INVISIBLE);
        }
    }

    // End the game
    private void stopGame() {
        if (mListener != null)
            mListener.stopGame(EXTRA_GAME_MEMORIT, mScore, "" + mCategory); // Send the highscore
        gameoverPlayer.start();
        mTimer.cancel(); // Stop the actual game
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // Turn all leds off
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
        if (frame * STEP_TIME > COUNTDOWN_TIME * 1000) // after 3 seconds
        {
            String time;
            if ((frame * STEP_TIME / 1000 - COUNTDOWN_TIME) / 60 < 10)
                time = "0" + (frame * STEP_TIME / 1000 - COUNTDOWN_TIME) / 60;
            else
                time = "" + (frame * STEP_TIME / 1000 - COUNTDOWN_TIME) / 60;
            if ((frame * STEP_TIME / 1000 - COUNTDOWN_TIME) % 60 < 10)
                time += ":0" + (frame * STEP_TIME / 1000 - COUNTDOWN_TIME) % 60;
            else
                time += ":" + (frame * STEP_TIME / 1000 - COUNTDOWN_TIME) % 60;
            ((TextView) view.findViewById(R.id.fragment_memorit_game_textview_timer)).setText(time);

            if (mIterator >= mSequence.size() && mUserinput) {
                // If user has pressed all buttons or hasn't started yet
                // Get a new random button that is different from the previous one
                ArduinoButton arduinoButton;
                int i = 0;
                Random random = new Random();
                do {
                    arduinoButton = mArduinoButtons.get(random.nextInt(mArduinoButtons.size()));
                    if (i++ > 100) {
                        stopGame();
                        break; // BREAK OUT OF LOOP AFTER 100 TRIES
                    }
                }
                while (!arduinoButton.isEnabled() || !arduinoButton.isConnected() || arduinoButton == mLitButton);
                // Get a new random button that is different from the previous one

                mSequence.add(arduinoButton); // Add this to the sequence
                mIterator = 0; // reset iterator
                mLitButton = null; // Set the lit button to null (no button is now lit)
                mLastFrameLit = frame; // set the lastframelit to this frame
                mUserinput = false; // Start the automated "show"
            } else if (mLitButton == null && (frame - mLastFrameLit) * STEP_TIME > 1000 && mIterator == 0 && !mUserinput) {
                // if no button is lit, and a second has passed, the iterator is 0 and the user isn't pressing buttons
                mLastFrameLit = frame; // set the lastframelit to this frame
                mLitButton = mSequence.get(mIterator); // set the lit button to the first one
                //                 max wait time |    ^  | increase rate |      total size   | current | minus 1 (iterator starts at 0, while size is min 1)
                mWaitTime = (int)(WAIT_TIME_MAX * Math.pow(WAIT_TIME_RATE, mSequence.size() - mIterator) - 1); // Increase the wait time
                if (mWaitTime < WAIT_TIME_MIN)
                    mWaitTime = WAIT_TIME_MIN;
                mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), COMMAND_LED_ON); // Turn the led on this device on
            } else if (mLitButton == null && (frame - mLastFrameLit) * STEP_TIME > 200 && mIterator != 0 && mIterator < mSequence.size() && !mUserinput) {
                // if no button is lit, and a 200 ms has passed, the iterator is not the max and the user isn't pressing buttons
                mLastFrameLit = frame; // set the lastframelit to this frame
                mLitButton = mSequence.get(mIterator); // set the lit button to the next one
                mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), COMMAND_LED_ON); // Turn the led on this device on
            } else if (mLitButton == null && (frame - mLastFrameLit) * STEP_TIME > 200 && mIterator >= mSequence.size() && !mUserinput) {
                // if no button is lit, and a 200 ms has passed, the iterator is or is bigger than the max and the user isn't pressing buttons
                mLastFrameLit = frame; // set the lastframelit to this frame
                mUserinput = true; // Start the users play
                mIterator = 0; // reset iterator
                mLitButton = mSequence.get(mIterator); // Wait for the user to press the first button
            } else if (mLitButton != null && (frame - mLastFrameLit) * STEP_TIME > mWaitTime && mIterator < mSequence.size() && !mUserinput) {
                // if a button is lit, and mWaitTime [ms] has passed, the iterator is not the max and the user isn't pressing buttons
                mLastFrameLit = frame; // set the lastframelit to this frame
                mLitButton = null; // No button is lit > mLitButton = null
                mIterator++; // increment iterator
                //                 max wait time |    ^  | increase rate |      total size   | current | minus 1 (iterator starts at 0, while size is min 1)
                mWaitTime = (int)(WAIT_TIME_MAX * Math.pow(WAIT_TIME_RATE, mSequence.size() - mIterator) - 1); // Increase the wait time
                if (mWaitTime < WAIT_TIME_MIN)
                    mWaitTime = WAIT_TIME_MIN;
                mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // Turn all leds off
            } else if (mUserinput && mIterator < mSequence.size()) {
                // If it's the users turn and he hasn't pressed all buttons
                mLitButton = mSequence.get(mIterator); // set the to check button
                if (mLitButton.isPressed() && mLitButton.isConnected() && mLitButton.isEnabled() && !mIsPressed) {
                    // If this correct button is pressed and can be pressed
                    mScore++;
                    pointPlayer.start(); // Play the sound
                    ((TextView) view.findViewById(R.id.fragment_memorit_game_textview_score)).setText("" + mScore);
                    mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), COMMAND_LED_ON); // Turn this button on
                    mIsPressed = true; // Remember that it is pressed
                } else if (mIsPressed && !mLitButton.isPressed()) {
                    // If the button is released
                    mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off
                    mIterator++; // increment the iterator
                    mIsPressed = false; // the button isn't pressed anymore
                }

                for (int i = 0; i < mArduinoButtons.size(); i++) {
                    ArduinoButton arduinoButton = mArduinoButtons.get(i);
                    // loop through all buttons
                    if (arduinoButton.isPressed() && arduinoButton.isConnected() && arduinoButton.isEnabled() && !arduinoButton.getDeviceName().equals(mLitButton.getDeviceName())) {
                        // Check to see if the wrong button is pressed
                        loseLife(frame, view); // Lose a life and restart the sequance show
                        i = mArduinoButtons.size();
                    }
                }
            }
        } else
            if(frame ==0)showCountdownDialog();
            //((TextView) view.findViewById(R.id.fragment_memorit_game_textview_timer)).setText("00:0" + (3 - frame * STEP_TIME / 1000) % 60);
    }
    void showCountdownDialog() {
        DialogFragment newFragment = GameCountdownFragment.newInstance(EXTRA_GAME_MEMORIT);
        newFragment.show(getFragmentManager(), "dialog");
    }

    // Lose a life
    // @param frame: the current frame
    // @param view: the current view
    private void loseLife(int frame, View view) {
        mLives--; // decrement a life
        mIterator = 0; // reset iterator
        mLitButton = null; // no button is lit
        mLastFrameLit = frame; // set the lastframelit to this frame
        mUserinput = false; // Start the show sequence
        showLives(view);
        if (mLives <= 0)
            stopGame(); // Stop the game when run out of lives
        else {
            failPlayer.start();  // Play the sound
        }
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMemoritGameListener) {
            mListener = (OnMemoritGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMemoritGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMemoritGameListener {
        void stopGame(String gameMode, int score, String category);
    }
}
