package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_HARD;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.STEP_TIME;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MAX_EASY;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MAX_MEDIUM;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MAX_HARD;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MIN_EASY;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MIN_MEDIUM;
import static be.howest.nmct.targit.Constants.TIME_TO_PRESS_MIN_HARD;

public class SmashitGameFragment extends Fragment {
    String mDifficulty; // The difficulty
    private int mWaitFrames; // How many frames the game should wait
    private int mFrameCounter = 0; // A counter to keep count the frames
    private int mPressedOnFrame = 0; // When the button was pressed
    private int mScore = 0; // The current score
    private int mLives = 3; // The lives the player has left
    private ArduinoButton mLitButton = null; // Current lit button
    private ArduinoButton mPreviousLitButton = null; // The last lit button
    private Timer mTimer = new Timer(); // The game timer
    private List<ArduinoButton> mArduinoButtons; // All devices
    private BluetoothConnection mBluetoothConnection; // The bt connection

    private OnSmashitGameListener mListener; // Listener to stop the game

    public SmashitGameFragment() {
        // Required empty public constructor
    }

    // Create an instance of this fragment
    public static SmashitGameFragment newInstance(String difficulty) {
        SmashitGameFragment fragment = new SmashitGameFragment();
        fragment.mDifficulty = difficulty; // Set the difficulty
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smashit_game, container, false);

        // Set the stop button to stop the game
        view.findViewById(R.id.fragment_smashit_game_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });

        // Set the time to press depending on the difficulty
        if (mDifficulty.equals(EXTRA_DIFFICULTY_EASY))
            mWaitFrames = TIME_TO_PRESS_MAX_EASY / STEP_TIME;
        else if (mDifficulty.equals(EXTRA_DIFFICULTY_MEDIUM))
            mWaitFrames = TIME_TO_PRESS_MAX_MEDIUM / STEP_TIME;
        else if (mDifficulty.equals(EXTRA_DIFFICULTY_HARD))
            mWaitFrames = TIME_TO_PRESS_MAX_HARD / STEP_TIME;

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection(); // Get the connection
        mArduinoButtons = mBluetoothConnection.getArduinoButtons(); // get the devices
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off

        startGameSteps(view); // configure the routine
        // initiate the textfields
        ((TextView) view.findViewById(R.id.fragment_smashit_game_textview_score)).setText("" + mScore);
        showLives(view);

        return view;
    }


    private void showLives(View view) {
        if (mLives >= 1)
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart1)).setImageResource(R.drawable.ic_hart);
        else
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart1)).setImageResource(R.drawable.ic_hart_leeg);
        if (mLives >= 2)
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart2)).setImageResource(R.drawable.ic_hart);
        else
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart2)).setImageResource(R.drawable.ic_hart_leeg);
        if (mLives >= 3)
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart3)).setImageResource(R.drawable.ic_hart);
        else
            ((ImageView) view.findViewById(R.id.fragment_smashit_game_imageview_heart3)).setImageResource(R.drawable.ic_hart_leeg);
    }

    // End the game
    private void stopGame() {
        if (mListener != null)
            mListener.stopGame(EXTRA_GAME_SMASHIT, mScore, mDifficulty); // Send the highscore
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
            String time;
            if ((frame * STEP_TIME / 1000 - 3) / 60 < 10)
                time = "0" + (frame * STEP_TIME / 1000 - 3) / 60;
            else
                time = "" + (frame * STEP_TIME / 1000 - 3) / 60;
            if ((frame * STEP_TIME / 1000 - 3) % 60 < 10)
                time += ":0" + (frame * STEP_TIME / 1000 - 3) % 60;
            else
                time += ":" + (frame * STEP_TIME / 1000 - 3) % 60;
            ((TextView) view.findViewById(R.id.fragment_smashit_game_textview_timer)).setText(time);

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
                    if ((mDifficulty.equals(EXTRA_DIFFICULTY_EASY) && mWaitFrames > TIME_TO_PRESS_MIN_EASY / STEP_TIME)
                            || (mDifficulty.equals(EXTRA_DIFFICULTY_MEDIUM) && mWaitFrames > TIME_TO_PRESS_MIN_MEDIUM / STEP_TIME)
                            || (mDifficulty.equals(EXTRA_DIFFICULTY_HARD) && mWaitFrames > TIME_TO_PRESS_MIN_HARD / STEP_TIME))
                        mWaitFrames--; // decrement waitFrames

                    ((TextView) view.findViewById(R.id.fragment_smashit_game_textview_score)).setText("" + mScore);
                    mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF); // Turn all leds off
                }
                for (ArduinoButton arduinoButton : mArduinoButtons) {
                    // Loop all buttons
                    if (mLitButton != null) {
                        // If a button is lit
                        if (arduinoButton.isPressed() && arduinoButton.isConnected() && arduinoButton.isEnabled() && !arduinoButton.getDeviceName().equals(mLitButton.getDeviceName())) {
                            // if a wrong button is pressed
                            loseLive(frame, view);
                        }
                    }
                }
            }

            if (mWaitFrames + mPressedOnFrame < frame) {
                // If you are to late to press the next button
                loseLive(frame, view);
            }

        } else
            ((TextView) view.findViewById(R.id.fragment_smashit_game_textview_timer)).setText("00:0" + (3 - frame * STEP_TIME / 1000));
    }

    // Lose a life
    // @param frame: the current frame
    // @param view: the current view
    private void loseLive(int frame, View view) {
        mLives--;
        mLitButton = null;
        mPressedOnFrame = frame;
        showLives(view);
        if (mLives <= 0)
            stopGame(); // Stop the game when run out of lives
        mBluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all leds off
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSmashitGameListener) {
            mListener = (OnSmashitGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSmashitGameListener");
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

    public interface OnSmashitGameListener {
        void stopGame(String gameMode, int score, String category);
    }
}
