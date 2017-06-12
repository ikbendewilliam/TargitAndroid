package be.howest.nmct.targit.views.highscore;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.models.HighscoreEntry;
import be.howest.nmct.targit.views.MainActivity;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

// The activity that shows the HighscoreFragment
public class HighscoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set onclicklistener to go back to mainactivity
        findViewById(R.id.highscore_button_to_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(MainActivity.class);
            }
        });

        // get the intent
        Intent intent = getIntent();
        // If this intent is sent from ingame
        if (intent.hasExtra(EXTRA_GAME) && intent.hasExtra(EXTRA_SCORE) && intent.hasExtra(EXTRA_CATEGORY)) {
            // get the mode, category and score from the intent
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            int gameScore = intent.getIntExtra(EXTRA_SCORE, 0);
            String category = intent.getStringExtra(EXTRA_CATEGORY);
            // TODO: Remove this
            // Show the score
            ((TextView) (findViewById(R.id.highscore_textview_score))).setText("score: " + gameScore + " in " + gameMode + " " + category);
            // TODO: Let the user define his/her own name
            // Show the highscorelist with a "new player"
            showFragment(HighscoreFragment.newInstance(gameMode, category, new HighscoreEntry("new Player", gameScore)));
        } else {
            // Show the highscorelist without a new score
            showFragment(HighscoreFragment.newInstance(EXTRA_GAME_MEMORIT, EXTRA_LIVES_FEW, null));
        }
    }

    // show a fragment
    // @param newFragment: the fragment to show
    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_highscoreactivity, newFragment);
        transaction.commit();
    }

    // Change to an activity
    // @param activity: the class of the activity you want to show
    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
