package be.howest.nmct.targit.views.ingame;


import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.COUNTDOWN_TIME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameCountdownFragment extends DialogFragment {

    private Integer mCounter = COUNTDOWN_TIME;
    private View mView = null;
    private Timer mTimer = new Timer();

    public GameCountdownFragment() {
        // Required empty public constructor
    }

    public static GameCountdownFragment newInstance(String gamemode) {

        Bundle args = new Bundle();
        //pass gamemode for styling
        args.putString(EXTRA_GAME, gamemode);
        GameCountdownFragment fragment = new GameCountdownFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_game_countdown, container, false);
        //get topview
        RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.fragment_game_countdown_relativelayout_circleview);
        //region styling
        //set style
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        //set anti cancel
        this.setCancelable(false);
        //get font
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        //setfont on timer
        TextView txtTimer = (TextView) mView.findViewById(R.id.fragment_game_countdown_timer);
        txtTimer.setTypeface(font);
        //make dialog transparent
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //check if variables are available
        if (getArguments().getString(EXTRA_GAME) != null) {
            String gamemode = getArguments().getString(EXTRA_GAME);

            //check what gamemode player is in and change background color
            if (gamemode.equals(EXTRA_GAME_SMASHIT)) {

                relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
                relativeLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_smashit, null));

            } else if (gamemode.equals(EXTRA_GAME_ZENIT)) {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
                relativeLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_zenit, null));

            } else if (gamemode.equals(EXTRA_GAME_MEMORIT)) {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
                relativeLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_memorit, null));

            }
        }
        //endregion

        //set activity to full screen
        mView.findViewById(R.id.fragment_game_countdown_topview).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //init timer
        mTimer = new Timer();
        final Handler handler = new Handler();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tick();
                    }
                });
            }
        };
        mTimer.schedule(timerTask, 0, 1000);
        return mView;

    }

    //method handles the UI change during the countdown
    private void tick() {

        if (mCounter < 0) {
            mTimer.cancel();
            getDialog().cancel();
        } else if (mCounter == 0) {
            ((TextView) mView.findViewById(R.id.fragment_game_countdown_timer)).setText("START!");
            mCounter--;
        } else {
            ((TextView) mView.findViewById(R.id.fragment_game_countdown_timer)).setText("" + mCounter);
            mCounter--;
        }


    }

}
