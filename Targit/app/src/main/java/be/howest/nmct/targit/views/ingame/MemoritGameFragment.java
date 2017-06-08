package be.howest.nmct.targit.views.ingame;

import android.content.Context;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;

import static be.howest.nmct.targit.views.ingame.GameActivity.STEP_TIME;

public class MemoritGameFragment extends Fragment {
    private int mLastFrameLit = 0;
    private int mFrameCounter = 0;
    private int mScore = 0;
    private int mCategory;
    private int mLives;
    private int mIterator = 0;
    private boolean mUserinput = true;
    private boolean mIsPressed = false;
    private ArduinoButton mLitButton = null;
    private Timer mTimer = new Timer();
    private List<ArduinoButton> mArduinoButtons;
    private BluetoothConnection mBluetoothConnection;
    private List<ArduinoButton> mSequence = new ArrayList<>();

    private OnMemoritGameListener mListener;

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

        view.findViewById(R.id.ingame_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.stopGame(GameActivity.EXTRA_GAME_MEMORIT, 0, "" + mCategory);
            }
        });

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
            mListener.stopGame(GameActivity.EXTRA_GAME_MEMORIT, mScore, "" + mCategory);
        mTimer.cancel();
        mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
    }

    public void startGameSteps(final View view) {
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
        mTimer.schedule(gamestep, 0, (long) STEP_TIME);
    }

    private void gameStep(int frame, View view) {
        if (frame * STEP_TIME > 3000) // after 3 seconds
        {
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("tijd bezig: " + (frame * STEP_TIME / 1000));

            if (mIterator >= mSequence.size() && mUserinput) {
                if (mSequence.size() > 0) {
                    mScore++;
                    ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);
                }

                Random random = new Random();
                do {
                    mLitButton = mArduinoButtons.get(random.nextInt(mArduinoButtons.size()));
                }
                while (!mLitButton.isEnabled() || !mLitButton.isConnected() || mLitButton.isPressed());

                mSequence.add(mLitButton);
                mIterator = 0;
                mLitButton = null;
                mLastFrameLit = frame;
                mUserinput = false;
            } else if (mLitButton == null && (frame - mLastFrameLit) * STEP_TIME > 200 && mIterator < mSequence.size() && !mUserinput) {
                mLastFrameLit = frame;
                mLitButton = mSequence.get(mIterator);
                mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), Constants.COMMAND_LED_ON);
            } else if (mLitButton == null && (frame - mLastFrameLit) * STEP_TIME > 200 && mIterator >= mSequence.size() && !mUserinput) {
                mLastFrameLit = frame;
                mUserinput = true;
                mIterator = 0;
                mLitButton = mSequence.get(mIterator);
            } else if (mLitButton != null && (frame - mLastFrameLit) * STEP_TIME > 1000 && mIterator < mSequence.size() && !mUserinput) {
                mLastFrameLit = frame;
                mLitButton = null;
                mIterator++;
                mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
            } else if (mUserinput && mIterator < mSequence.size()) {
                mLitButton = mSequence.get(mIterator);

                if (mLitButton.isPressed() && mLitButton.isConnected() && mLitButton.isEnabled()) {
                    mBluetoothConnection.sendMessageToDevice(mLitButton.getDeviceName(), Constants.COMMAND_LED_ON);
                    mIsPressed = true;
                } else if (mIsPressed && !mLitButton.isPressed()) {
                    mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
                    mIterator++;
                    mIsPressed = false;
                }

                for (ArduinoButton arduinoButton : mArduinoButtons) {
                    if (arduinoButton.isPressed() && arduinoButton.isConnected() && arduinoButton.isEnabled() && !arduinoButton.getDeviceName().equals(mLitButton.getDeviceName()))
                        loseLive(frame, view);
                }
            }
        } else
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("het spel start in: " + (3 - frame * STEP_TIME / 1000));
    }

    private void loseLive(int frame, View view) {
        mLives--;
        mIterator = 0;
        mLitButton = null;
        mLastFrameLit = frame;
        mUserinput = false;
        ((TextView) view.findViewById(R.id.ingame_textview_lives)).setText("levens: " + mLives);
        if (mLives <= 0)
            stopGame();
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

    public interface OnMemoritGameListener {
        void stopGame(String gameMode, int score, String category);
    }
}
