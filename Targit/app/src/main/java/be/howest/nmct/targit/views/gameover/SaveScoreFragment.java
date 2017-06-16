package be.howest.nmct.targit.views.gameover;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
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
import be.howest.nmct.targit.models.HighscoreEntry;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

public class SaveScoreFragment extends Fragment {
    private SaveScoreTransitionListener mListener;

    public SaveScoreFragment() {
        // Required empty public constructor
    }

    public static SaveScoreFragment newInstance() {
        SaveScoreFragment fragment = new SaveScoreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
        TextView txtScoreTitle = (TextView) view.findViewById(R.id.fragment_save_score_textview_score);
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

        final View tempView = view;
        final int tempScore = Integer.parseInt(score);
        view.findViewById(R.id.fragment_save_score_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) tempView.findViewById(R.id.fragment_save_score_edittext_name)).getText().toString();
                if (name.length() < 3) {
                    // Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Je moet minstens 3 characters ingeven.")
                            .setTitle("Oeps, foutje");
                    AlertDialog dialog = builder.create();
                } else {
                    mListener.saveScore(new HighscoreEntry(name, tempScore));
                }
            }
        });
        view.findViewById(R.id.fragment_save_score_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.saveScore(null);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaveScoreTransitionListener) {
            mListener = (SaveScoreTransitionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaveScoreTransitionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface SaveScoreTransitionListener {
        void saveScore(HighscoreEntry newEntry);
    }
}
