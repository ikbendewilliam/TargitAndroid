package be.howest.nmct.targit.views.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.models.ArduinoButton;

public class SettingsActivity extends AppCompatActivity implements StatusFragment.OnStatusFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        showFragment(new StatusFragment());
    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_settingsactivity, newFragment);
        transaction.commit();
    }

    void showActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction() {

    }
}
