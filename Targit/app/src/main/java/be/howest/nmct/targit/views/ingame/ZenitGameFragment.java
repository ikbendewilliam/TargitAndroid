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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;

import static android.content.Context.BLUETOOTH_SERVICE;
import static be.howest.nmct.targit.views.ingame.GameActivity.STEP_TIME;

public class ZenitGameFragment extends Fragment {
    private String mDuration;
    private int mMaxFrame;
    private int frameCounter = 0;
    private int mPressedOnFrame = 0;
    private int mScore = 0;
    private ArduinoButton mLidButton = null;
    Timer mTimer = new Timer();
    List<ArduinoButton> mArduinoButtons;
    BluetoothConnection mBluetoothConnection;

    private OnZenitGameListener mListener;

    public ZenitGameFragment() {
        // Required empty public constructor
    }

    public static ZenitGameFragment newInstance(String duration, int maxTime) {
        ZenitGameFragment fragment = new ZenitGameFragment();
        fragment.mDuration = duration;
        fragment.mMaxFrame = (maxTime + 3) * 1000 / STEP_TIME;
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

        mBluetoothConnection = BluetoothConnection.getBluetoothConnection();
        mArduinoButtons = mBluetoothConnection.getArduinoButtons();
        mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);

        startGameSteps(view);

        return view;
    }

    private void stopGame() {
        if (mListener != null)
            mListener.stopGame(GameActivity.EXTRA_GAME_ZENIT, mScore, mDuration);
        Log.i(Constants.TAG, "stopGame: " + GameActivity.EXTRA_GAME_ZENIT + " dur: " + mDuration);
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
        mTimer.schedule(gamestep, 0, (long)STEP_TIME);
    }

    private void gameStep(int frame, View view) {
        if (frame * STEP_TIME > 3000) // after 3 seconds
        {
            ((TextView) view.findViewById(R.id.ingame_textview_timer)).setText("tijd over: " + ((mMaxFrame - frame) * STEP_TIME / 1000));

            if (mPressedOnFrame + 2 < frame && mLidButton == null) {
                Random random = new Random();
                do {
                    mLidButton = mArduinoButtons.get(random.nextInt(mArduinoButtons.size()));
                }
                while (!mLidButton.isEnabled() || !mLidButton.isConnected() || mLidButton.isPressed());
                mBluetoothConnection.sendMessageToDevice(mLidButton.getDeviceName(), Constants.COMMAND_LED_FLASH_FAST);
                Log.i(Constants.TAG_MESSAGE, "gameStep: turn on " + mLidButton.getDeviceName());
            } else if (mLidButton != null) {
                if (mLidButton.isPressed() && mLidButton.isConnected() && mLidButton.isEnabled()) {
                    mLidButton = null;
                    mScore++;
                    mPressedOnFrame = frame;
                    ((TextView) view.findViewById(R.id.ingame_textview_score)).setText("punten: " + mScore);
                    mBluetoothConnection.sendMessageToAll(Constants.COMMAND_LED_OFF);
                }
            }

            if (frame >= mMaxFrame)
                stopGame();
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
