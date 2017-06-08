package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;

import static be.howest.nmct.targit.views.ingame.GameActivity.STEP_TIME;

public class SmashitGameFragment extends Fragment {
    String mDifficulty;
    private int mWaitFrames;
    private int frameCounter = 0;
    private int mPressedOnFrame = 0;
    private int mScore = 0;
    private int mLives = 3;
    private ArduinoButton mLidButton = null;
    Timer mTimer = new Timer();
    List<ArduinoButton> mArduinoButtons;
    BluetoothConnection mBluetoothConnection;

    public static int TIME_TO_PRESS_MAX_EASY = 8000;
    public static int TIME_TO_PRESS_MAX_MEDIUM = 6000;
    public static int TIME_TO_PRESS_MAX_HARD = 3000;
    public static int TIME_TO_PRESS_MIN_EASY = 3000;
    public static int TIME_TO_PRESS_MIN_MEDIUM = 2000;
    public static int TIME_TO_PRESS_MIN_HARD = 1000;

    private OnSmashitGameListener mListener;

    public SmashitGameFragment() {
        // Required empty public constructor
    }

    public static SmashitGameFragment newInstance(String difficulty) {
        SmashitGameFragment fragment = new SmashitGameFragment();
        fragment.mDifficulty = difficulty;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smashit_game, container, false);

        view.findViewById(R.id.ingame_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.stopGame(GameActivity.EXTRA_GAME_SMASHIT, 0, mDifficulty.toString());
            }
        });
        if (mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_EASY))
            mWaitFrames = TIME_TO_PRESS_MAX_EASY / STEP_TIME;
        else if (mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_MEDIUM))
            mWaitFrames = TIME_TO_PRESS_MAX_MEDIUM / STEP_TIME;
        else if (mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_HARD))
            mWaitFrames = TIME_TO_PRESS_MAX_HARD / STEP_TIME;

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();
        mArduinoButtons = mBluetoothConnection.getArduinoButtons();
        mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);

        startGameSteps(view);
        ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);
        ((TextView) view.findViewById(R.id.ingame_textview_lives)).setText("levens: " + mLives);

        return view;
    }

    private void stopGame() {
        if (mListener != null)
            mListener.stopGame(GameActivity.EXTRA_GAME_SMASHIT, mScore, mDifficulty);
        mTimer.cancel();
    }

    public void startGameSteps(final View view) {
        final Handler handler = new Handler();
        TimerTask gamestep = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        gameStep(frameCounter++, view);
                    }
                });
            }
        };
        mTimer.schedule(gamestep, 0, (long) STEP_TIME);
    }

    private void gameStep(int frame, View view) {
        if (frame * STEP_TIME > 3000) // after 3 seconds
        {
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("tijd bezig: " + (frame * STEP_TIME / 1000));

            if ((frame - mPressedOnFrame) * STEP_TIME > 200 && mLidButton == null) {
                Random random = new Random();
                do {
                    mLidButton = mArduinoButtons.get(random.nextInt(mArduinoButtons.size()));
                }
                while (!mLidButton.isEnabled() || !mLidButton.isConnected() || mLidButton.isPressed());

                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
                mBluetoothConnection.sendMessageToDevice(mLidButton.getDeviceName(), Constants.COMMAND_LED_FLASH_FAST);
                //Log.i(Constants.TAG_MESSAGE, "gameStep: turn on " + mLidButton.getDeviceName());
            } else if (mLidButton != null) {
                for (ArduinoButton arduinoButton : mArduinoButtons) {
                    if (arduinoButton.isPressed() && arduinoButton.isConnected() && arduinoButton.isEnabled() && arduinoButton.equals(mLidButton))
                        loseLive(frame, view);
                }

                if (mLidButton.isPressed() && mLidButton.isConnected() && mLidButton.isEnabled()) {
                    mLidButton = null;
                    mScore++;
                    mPressedOnFrame = frame;
                    if ((mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_EASY) && mWaitFrames > TIME_TO_PRESS_MIN_EASY / STEP_TIME)
                            || (mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_MEDIUM) && mWaitFrames > TIME_TO_PRESS_MIN_MEDIUM / STEP_TIME)
                            || (mDifficulty.equals(GameActivity.EXTRA_DIFFICULTY_HARD) && mWaitFrames > TIME_TO_PRESS_MIN_HARD / STEP_TIME))
                        mWaitFrames--;

                    ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);
                    mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
                }
            }

            if (mWaitFrames + mPressedOnFrame < frame) {
                loseLive(frame, view);
            }

        } else
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("het spel start in: " + (3 - frame * STEP_TIME / 1000));
    }

    private void loseLive(int frame, View view) {
        mLives--;
        mLidButton = null;
        mPressedOnFrame = frame;
        ((TextView) view.findViewById(R.id.ingame_textview_lives)).setText("levens: " + mLives);
        if (mLives <= 0)
            stopGame();
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
