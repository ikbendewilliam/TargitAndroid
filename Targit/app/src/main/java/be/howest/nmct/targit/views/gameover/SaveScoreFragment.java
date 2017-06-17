package be.howest.nmct.targit.views.gameover;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get arguments
        String gamemode = getArguments().getString(EXTRA_GAME);
        final int score = getArguments().getInt(EXTRA_SCORE);
        String category = getArguments().getString(EXTRA_CATEGORY);
        final View view = inflater.inflate(R.layout.fragment_save_score, container, false);

        //region setfont
        //getfont
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        //name inputfield
        EditText name = (EditText) view.findViewById(R.id.fragment_save_score_edittext_name);
        name.setTypeface(font);
        //save button
        Button btnSave = (Button) view.findViewById(R.id.fragment_save_score_button_save);
        btnSave.setTypeface(font);
        //cancel button
        Button btnCancel = (Button) view.findViewById(R.id.fragment_save_score_button_cancel);
        btnCancel.setTypeface(font);
        //score title
        TextView txtScoreTitle = (TextView) view.findViewById(R.id.fragment_save_score_textview_scoretitle);
        txtScoreTitle.setTypeface(font);
        //score text
        TextView txtScore = (TextView) view.findViewById(R.id.fragment_save_score_textview_score);
        txtScore.setTypeface(font);
        //endregion

        //set score value
        txtScore.setText("" + score);

        RelativeLayout relativeLayoutBox = (RelativeLayout) view.findViewById(R.id.fragment_save_score_relativelayout_box);
        ImageView ellipse = (ImageView) view.findViewById(R.id.fragment_game_over_imageview_ellipse);

        //region set design for correct gamemode
        if (gamemode.equals(EXTRA_GAME_SMASHIT)) {
            //set the right colors according to the selected gamemode
            //change the name box background
            relativeLayoutBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSmashit));
            //change oval image
            ellipse.setImageResource(R.drawable.ellipse_1);

        } else if (gamemode.equals(EXTRA_GAME_ZENIT)) {
            //set the right colors according to the selected gamemode
            //change the name box background
            relativeLayoutBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorZenit));
            //change oval image
            ellipse.setImageResource(R.drawable.zenit_ellipse);

        } else if (gamemode.equals(EXTRA_GAME_MEMORIT)) {
            //set the right colors according to the selected gamemode
            //change the name box background
            relativeLayoutBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            //change buttons color
            btnCancel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMemorit));
            //change oval image
            ellipse.setImageResource(R.drawable.memorit_ellipse);

        }
        //endregion

        //region OnClickListeners
        view.findViewById(R.id.fragment_save_score_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) view.findViewById(R.id.fragment_save_score_edittext_name)).getText().toString();
                if (name.length() < 3) {
                    // Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Je moet minstens 3 characters ingeven.")
                            .setTitle("Oeps, foutje");
                    AlertDialog dialog = builder.create();
                } else {
                    mListener.saveScore(new HighscoreEntry(name, score));
                }
            }
        });
        view.findViewById(R.id.fragment_save_score_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.dismissScore(new HighscoreEntry("", score));
            }
        });
        //endregion
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

        void dismissScore(HighscoreEntry newEntry);
    }
}
