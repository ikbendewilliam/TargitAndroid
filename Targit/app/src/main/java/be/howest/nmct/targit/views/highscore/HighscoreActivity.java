package be.howest.nmct.targit.views.highscore;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.ScreenSlidePagerAdapter;
import be.howest.nmct.targit.models.HighscoreEntry;
import be.howest.nmct.targit.views.MainActivity;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;

import static be.howest.nmct.targit.Constants.EXTRA_CATEGORY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_SCORE;

// The activity that shows the HighscoreListFragment
public class HighscoreActivity extends AppCompatActivity {

    //list of fragments showed in the tabs
    private List<Fragment> fragments;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        //set activity to full screen
        findViewById(R.id.activity_highscore_top_view).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        //intialisatie toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //back toets toevoegen aan toolbar
        myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white, getTheme()));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        //aanmaken fragments
        fragments = new ArrayList<>();
        fragments.add(new SmashitHighscoreFragment());
        fragments.add(new ZenitHighscoreFragment());
        fragments.add(new MemoritHighscoreFragment());

        //get tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.highscoreTabs);

        //setup tabs
        tabLayout.addTab(tabLayout.newTab().setText("Smash-it"));
        tabLayout.addTab(tabLayout.newTab().setText("Zen-it"));
        tabLayout.addTab(tabLayout.newTab().setText("Memor-it"));



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.highscorePager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        // linken  van tabs met viewpager
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //set on tab pressed
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        // Set onclicklistener to go back to mainactivity
//        findViewById(R.id.highscore_button_to_mainactivity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showActivity(MainActivity.class);
//            }
//        });

        // get the intent
        Intent intent = getIntent();
        // If this intent is sent from ingame
        if (intent.hasExtra(EXTRA_GAME) && intent.hasExtra(EXTRA_SCORE) && intent.hasExtra(EXTRA_CATEGORY)) {
            // get the mode, category and score from the intent
            String gameMode = intent.getStringExtra(EXTRA_GAME);
            int gameScore = intent.getIntExtra(EXTRA_SCORE, 0);
            String category = intent.getStringExtra(EXTRA_CATEGORY);
            // Show the score
            //((TextView) (findViewById(R.id.highscore_textview_score))).setText("score: " + gameScore + " in " + gameMode + " " + category);
            // TODO: Let the user define his/her own name
            // Show the highscorelist with a "new player"
            showFragment(be.howest.nmct.targit.views.highscore.HighscoreListFragment.newInstance(gameMode, category, new HighscoreEntry("new Player", gameScore)));
        } else {
            // Show the highscorelist without a new score
            showFragment(be.howest.nmct.targit.views.highscore.HighscoreListFragment.newInstance(EXTRA_GAME_MEMORIT, EXTRA_LIVES_FEW, null));
        }
    }

    @Override
    public void onBackPressed() {
        showActivity(MainActivity.class);
    }

    //handles full screen autohiding
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.activity_highscore_top_view).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}


    }

    // show a fragment
    // @param newFragment: the fragment to show
    private void showFragment(Fragment newFragment) {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.framelayout_in_highscoreactivity, newFragment);
//        transaction.commit();
    }

    // Change to an activity
    // @param activity: the class of the activity you want to show
    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
