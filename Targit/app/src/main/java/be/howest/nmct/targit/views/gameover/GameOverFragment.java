package be.howest.nmct.targit.views.gameover;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class GameOverFragment extends Fragment {


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

        //check if score is present, if true -> set the score in ellipse
        //check if gamemode argument has been passed, if true -> set to variable
        if(!(getArguments().getString(EXTRA_SCORE).equals("")) && !(getArguments().getString(EXTRA_GAME).equals(""))){
            ((TextView)view.findViewById(R.id.fragment_game_over_textview_score)).setText(""+getArguments().get(EXTRA_SCORE));
            gamemode = getArguments().getString(EXTRA_GAME);
        }


        //get the UI elements
        Button btnSave = (Button)view.findViewById(R.id.fragment_game_over_button_save);
        Button btnCancel = (Button) view.findViewById(R.id.fragment_game_over_button_cancel);
        ImageView imgGameOver = (ImageView) view.findViewById(R.id.fragment_game_over_imageview_game_over);


        if (gamemode.equals(EXTRA_GAME_SMASHIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            //change oval image
            imgGameOver.setImageResource(R.drawable.game_over_smashit);

        } else if (gamemode.equals(EXTRA_GAME_ZENIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            //change oval image
            imgGameOver.setImageResource(R.drawable.game_over_zenit);

        } else if (gamemode.equals(EXTRA_GAME_MEMORIT)) {
            //set the right colors & assets according to the selected gamemode
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            //change oval image
            imgGameOver.setImageResource(R.drawable.memorit_ellipse);

        }
        return view;



    }

    //make instance of fragment
    public static GameOverFragment newInstance(String score) {
        Bundle args = new Bundle();
        //Give score as a param
        args.putString(EXTRA_SCORE,score);
        GameOverFragment fragment = new GameOverFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
