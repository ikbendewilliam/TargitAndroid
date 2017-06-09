package be.howest.nmct.targit.views.highscore;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.MyArduinoButtonRecyclerViewAdapter;
import be.howest.nmct.targit.adapters.MyHighscoreRecyclerViewAdapter;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models. HighscoreEntry ;

public class HighscoreFragment extends Fragment {
    private String mGameMode;
    private String mCategory;
    List<HighscoreEntry> highscoreEntries;
    private HighscoreEntry  mNewEntry;
    private MyHighscoreRecyclerViewAdapter myHighscoreRecyclerViewAdapter;

    public HighscoreFragment() {
        // Required empty public constructor
    }

    public static HighscoreFragment newInstance(String gameMode, int category,@Nullable HighscoreEntry newEntry) {
        return newInstance(gameMode, "" + category, newEntry);
    }

    public static HighscoreFragment newInstance(String gameMode, String category,@Nullable HighscoreEntry newEntry) {
        HighscoreFragment fragment = new HighscoreFragment();
        fragment.mGameMode = gameMode;
        fragment.mCategory = category;
        fragment.mNewEntry = newEntry;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        highscoreEntries = new ArrayList<>();
        highscoreEntries.add(new HighscoreEntry("IkbendeWiliam", 9001));
        highscoreEntries.add(new HighscoreEntry("Atak", 10));
        highscoreEntries.add(new HighscoreEntry("Mathias", 10));
        highscoreEntries.add(new HighscoreEntry("Donkey", -2));
        // TODO: get list

        if (mNewEntry != null) {
            checkList();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.highscore_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myHighscoreRecyclerViewAdapter = new MyHighscoreRecyclerViewAdapter(highscoreEntries);
        recyclerView.setAdapter(myHighscoreRecyclerViewAdapter);


        // TODO: show list

        return view;
    }

    private boolean checkList() {
        // TODO: put in list
        // TODO: save list
        return false; // TODO: return if added
    }

    public void addEntry(HighscoreEntry newEntry)
    {
        mNewEntry = newEntry;
        if (checkList())
            myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
    }
}
