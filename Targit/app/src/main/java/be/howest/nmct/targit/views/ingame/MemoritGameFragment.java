package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Timer;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;

public class MemoritGameFragment extends Fragment {
    private int mLastFrameLit = 0;
    private int mScore = 0;
    private int mCategory;
    private int mLives;
    private ArduinoButton mLidButton = null;
    Timer mTimer = new Timer();
    List<ArduinoButton> mArduinoButtons;
    BluetoothConnection mBluetoothConnection;

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

        return view;
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
