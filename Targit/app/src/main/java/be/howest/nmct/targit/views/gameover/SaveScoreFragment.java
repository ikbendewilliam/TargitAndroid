package be.howest.nmct.targit.views.gameover;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class SaveScoreFragment extends Fragment {


    public SaveScoreFragment() {
        // Required empty public constructor
    }

    public static SaveScoreFragment newInstance() {
        SaveScoreFragment fragment = new SaveScoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get arguments
        String gamemode = getArguments().getString(EXTRA_GAME);
        String score = getArguments().getString(EXTRA_SCORE);
        String category = getArguments().getString(EXTRA_CATEGORY);

        View view = inflater.inflate(R.layout.fragment_save_score, container, false);

        //set font
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        EditText name = (EditText) view.findViewById(R.id.fragment_save_score_edittext_name);
        name.setTypeface(font);
        TextView txtTitle = (TextView) view.findViewById(R.id.fragment_save_score_textview_gametitle);
        txtTitle.setTypeface(font);
        Button btnSave = (Button) view.findViewById(R.id.fragment_save_score_button_save);
        btnSave.setTypeface(font);
        Button btnCancel = (Button) view.findViewById(R.id.fragment_save_score_button_cancel);
        btnCancel.setTypeface(font);
        TextView txtScoreTitle = (TextView) view.findViewById(R.id.fragment_save_score_textview_scoretext);
        txtScoreTitle.setTypeface(font);
        TextView txtScore = (TextView) view.findViewById(R.id.fragment_save_score_textview_score);
        txtScore.setTypeface(font);

        //set score
        txtScore.setText(score);

        //get the elements that need color change



        //check which game mode it was
        //TODO: insert category
        if(gamemode.equals(EXTRA_GAME_SMASHIT)){
            //set title name
            txtTitle.setText("SMASH - iT");


        }else if(gamemode.equals(EXTRA_GAME_ZENIT)){
            //set title name
            txtTitle.setText("ZEN - iT");


        }else if(gamemode.equals(EXTRA_GAME_MEMORIT)){
            //set title name
            txtTitle.setText("MEMOR - iT");

        }



        return view;

    }

}
