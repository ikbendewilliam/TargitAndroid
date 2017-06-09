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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.MyArduinoButtonRecyclerViewAdapter;
import be.howest.nmct.targit.adapters.MyHighscoreRecyclerViewAdapter;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.HighscoreEntry;

public class HighscoreFragment extends Fragment {
    private String mGameMode;
    private String mCategory;
    List<HighscoreEntry> mHighscoreEntries;
    private HighscoreEntry mNewEntry;
    private MyHighscoreRecyclerViewAdapter myHighscoreRecyclerViewAdapter;

    public HighscoreFragment() {
        // Required empty public constructor
    }

    public static HighscoreFragment newInstance(String gameMode, int category, @Nullable HighscoreEntry newEntry) {
        return newInstance(gameMode, "" + category, newEntry);
    }

    public static HighscoreFragment newInstance(String gameMode, String category, @Nullable HighscoreEntry newEntry) {
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

        view.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHighscoreEntries.clear();
                mHighscoreEntries.add(new HighscoreEntry("IkbendeWiliam", 32));
                mHighscoreEntries.add(new HighscoreEntry("Don Gillius", 31));
                mHighscoreEntries.add(new HighscoreEntry("Atak", 16));
                mHighscoreEntries.add(new HighscoreEntry("Mathias", 8));
                mHighscoreEntries.add(new HighscoreEntry("Donkey", 5));
                saveHighscoreToFile(mHighscoreEntries, mGameMode + "_" + mCategory);
                myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        mHighscoreEntries = getHighscoreFromFile(mGameMode + "_" + mCategory);

        if (mHighscoreEntries.size() < 5) {
            mHighscoreEntries.clear();
            mHighscoreEntries.add(new HighscoreEntry("IkbendeWiliam", 32));
            mHighscoreEntries.add(new HighscoreEntry("Don Gillius", 31));
            mHighscoreEntries.add(new HighscoreEntry("Atak", 16));
            mHighscoreEntries.add(new HighscoreEntry("Mathias", 8));
            mHighscoreEntries.add(new HighscoreEntry("Donkey", 5));
        }

        if (mNewEntry != null) {
            checkList();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.highscore_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myHighscoreRecyclerViewAdapter = new MyHighscoreRecyclerViewAdapter(mHighscoreEntries);
        recyclerView.setAdapter(myHighscoreRecyclerViewAdapter);

        return view;
    }

    private boolean checkList() {
        if (mHighscoreEntries.size() >= 10 && mHighscoreEntries.get(mHighscoreEntries.size() - 1).getScore() >= mNewEntry.getScore())
            return false;

        if (mHighscoreEntries.size() < 10)
            mHighscoreEntries.add(mNewEntry);
        else
            mHighscoreEntries.set(10, mNewEntry);
        for (int i = mHighscoreEntries.size() - 2; i >= 0; i--) {
            if (mHighscoreEntries.get(i).getScore() < mNewEntry.getScore()) {
                HighscoreEntry temp = mHighscoreEntries.get(i - 1);
                mHighscoreEntries.set(i - 1, mHighscoreEntries.get(i));
                mHighscoreEntries.set(i, temp);
            }
        }
        saveHighscoreToFile(mHighscoreEntries, mGameMode + "_" + mCategory);
        return true;
    }

    public void addEntry(HighscoreEntry newEntry) {
        mNewEntry = newEntry;
        if (checkList())
            myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void saveHighscoreToFile(List<HighscoreEntry> mHighscoreEntries, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getContext().getFilesDir() + filename, false));
            for (HighscoreEntry highscoreEntry : mHighscoreEntries) {
                writer.write(highscoreEntry.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<HighscoreEntry> getHighscoreFromFile(String filename) {
        List<HighscoreEntry> mHighscoreEntries = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getContext().getFilesDir() + filename));
            String line;
            while ((line = reader.readLine()) != null) {
                mHighscoreEntries.add(new HighscoreEntry(line));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHighscoreEntries;
    }
}
