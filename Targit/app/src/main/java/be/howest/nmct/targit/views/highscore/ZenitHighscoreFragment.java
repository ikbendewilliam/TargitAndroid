package be.howest.nmct.targit.views.highscore;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_DURATION_LONG;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;


public class ZenitHighscoreFragment extends Fragment {

    private Button btnHighscoreEasy;
    private Button btnHighscoreMedium;
    private Button btnHighscoreHard;
    
    public ZenitHighscoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zenit_highscore, container, false);

        showList(EXTRA_DURATION_LONG);

        btnHighscoreEasy = (Button) view.findViewById(R.id.fragment_zenit_highscore_easy);
        btnHighscoreMedium = (Button) view.findViewById(R.id.fragment_zenit_highscore_medium);
        btnHighscoreHard = (Button) view.findViewById(R.id.fragment_zenit_highscore_hard);

        btnHighscoreEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(1);
                showList(EXTRA_DURATION_LONG);
            }
        });
        btnHighscoreMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(2);
                showList(EXTRA_DURATION_MEDIUM);
            }
        });
        btnHighscoreHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(3);
                showList(EXTRA_DURATION_SHORT);
            }
        });

        return view;
    }

    private void selectButton(int id) {
        btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green);
        btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange);
        btnHighscoreHard.setBackgroundResource(R.drawable.postit_red);

        switch(id) {
            case 1:
                btnHighscoreEasy.setBackgroundResource(R.drawable.postit_green_selected);
                break;
            case 2:
                btnHighscoreMedium.setBackgroundResource(R.drawable.postit_orange_selected);
                break;
            case 3:
                btnHighscoreHard.setBackgroundResource(R.drawable.postit_red_selected);
                break;
        }
    }

    private void showList(int diff) {
        HighscoreListFragment listFragment = HighscoreListFragment.newInstance(EXTRA_GAME_ZENIT, diff, null);
        showFragment(listFragment);
    }

    private void showFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_in_fragment_zenit_highscore, newFragment);
        transaction.commit();
    }

}
