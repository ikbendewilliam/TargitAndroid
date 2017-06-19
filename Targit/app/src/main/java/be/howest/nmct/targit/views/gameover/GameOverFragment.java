package be.howest.nmct.targit.views.gameover;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.infogamemode.SmashitInfoFragment;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class GameOverFragment extends Fragment {

    private OnGameOverListener mListener; // A listener to start the game

    public GameOverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        //init gamemode variable
        String gamemode = "";
        String category = "";

        //check if score is present, if true -> set the score in ellipse
        //check if gamemode argument has been passed, if true -> set to variable
        if (!(getArguments().getString(EXTRA_SCORE).equals("")) &&
                !(getArguments().getString(EXTRA_GAME).equals("") &&
                        !(getArguments().getString(EXTRA_CATEGORY).equals("")))) {
            ((TextView) view.findViewById(R.id.fragment_game_over_textview_score)).setText("" + getArguments().get(EXTRA_SCORE));
            gamemode = getArguments().getString(EXTRA_GAME);
            category = getArguments().getString(EXTRA_CATEGORY);

        }

        //get the UI elements
        Button btnMenu = (Button) view.findViewById(R.id.fragment_game_over_button_menu);
        Button btnRestart = (Button) view.findViewById(R.id.fragment_game_over_button_restart);
        ImageView imgGameOver = (ImageView) view.findViewById(R.id.fragment_game_over_imageview_game_over);
        ImageView imgEllipse = (ImageView) view.findViewById(R.id.fragment_game_over_imageview_ellipse);

        //region set font
        //get font
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        //score text
        TextView txtScore = (TextView) view.findViewById(R.id.fragment_game_over_textview_score);
        txtScore.setTypeface(font);
        //button save
        btnMenu.setTypeface(font);
        //button cancel
        btnRestart.setTypeface(font);
        //endregion

        //region set design for correct gamemode
        if (gamemode.equals(EXTRA_GAME_SMASHIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnRestart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            btnMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            //change oval image
            imgEllipse.setImageResource(R.drawable.ellipse_1);
            imgGameOver.setImageResource(R.drawable.game_over_smashit);

        } else if (gamemode.equals(EXTRA_GAME_ZENIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnRestart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            btnMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            //change oval image
            imgEllipse.setImageResource(R.drawable.zenit_ellipse);
            imgGameOver.setImageResource(R.drawable.game_over_zenit);

        } else if (gamemode.equals(EXTRA_GAME_MEMORIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnRestart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            btnMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            //change oval image
            imgEllipse.setImageResource(R.drawable.memorit_ellipse);
            imgGameOver.setImageResource(R.drawable.game_over_memorit);

        }
        //endregion

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.showMainActivity();
            }
        });
        final String finalGamemode = gamemode;
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.showGameInfoActivity(finalGamemode);
            }
        });

        return view;


    }

    //make instance of fragment
    public static GameOverFragment newInstance(String score, String gamemode, String category) {
        Bundle args = new Bundle();
        //put the params
        args.putString(EXTRA_SCORE, score);
        args.putString(EXTRA_GAME, gamemode);
        args.putString(EXTRA_CATEGORY, category);
        GameOverFragment fragment = new GameOverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // A standard implementation when using a listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGameOverListener) {
            mListener = (OnGameOverListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGameOverListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // The listener defined
    public interface OnGameOverListener {
        void showMainActivity();

        void showGameInfoActivity(String gameMode);
    }

}
