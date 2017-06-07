package be.howest.nmct.targit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.howest.nmct.targit.R;

public class HighscoreActivity extends AppCompatActivity {
    public static String EXTRA_SCORE = "score";
    public static String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showActivity(MainActivity.class);
    }

    void showActivity(Class activity)
    {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
