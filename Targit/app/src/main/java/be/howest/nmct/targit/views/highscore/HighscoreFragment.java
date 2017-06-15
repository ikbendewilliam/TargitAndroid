package be.howest.nmct.targit.views.highscore;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import be.howest.nmct.targit.adapters.MyHighscoreRecyclerViewAdapter;
import be.howest.nmct.targit.models.HighscoreEntry;

// The fragment that shows the highscore
public class HighscoreFragment extends Fragment {
    private String mGameMode; // The game mode (as defined in Constants)
    private String mCategory; // The game mode (as defined in Constants, ints are transformed into Strings)
    List<HighscoreEntry> mHighscoreEntries; // A list of all entries in this category
    private HighscoreEntry mNewEntry; // The new entry provided (can be null)
    private MyHighscoreRecyclerViewAdapter myHighscoreRecyclerViewAdapter; // The adapter that will fill the list

    // Required empty public constructor
    public HighscoreFragment() {
        // Required empty public constructor
    }

    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    public static HighscoreFragment newInstance(String gameMode, int category, @Nullable HighscoreEntry newEntry) {
        // Transform category to String and call the other constructor
        return newInstance(gameMode, "" + category, newEntry);
    }


    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    public static HighscoreFragment newInstance(String gameMode, String category, @Nullable HighscoreEntry newEntry) {
        // Create a new fragment
        HighscoreFragment fragment = new HighscoreFragment();
        // Set the variables to the provided parameters
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

        // TODO: Remove this
        // add a clicklistener to reset the highscore
        view.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHighscoreEntries.clear();
                mHighscoreEntries.add(new HighscoreEntry("William", 50));
                mHighscoreEntries.add(new HighscoreEntry("Gilles", 40));
                mHighscoreEntries.add(new HighscoreEntry("Downey", 30));
                mHighscoreEntries.add(new HighscoreEntry("Matthijs", 20));
                mHighscoreEntries.add(new HighscoreEntry("Tarik", 10));
                mHighscoreEntries.add(new HighscoreEntry("Eefje", 1));
                saveHighscoreToFile(mHighscoreEntries, mGameMode + "_" + mCategory);
                myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        // retrieve the highscores
        loadList();

        // if a new entry is provided, check if it is in the highscore
        if (mNewEntry != null) {
            checkList();
        }

        // get the list
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.highscore_recycleview);
        // set the layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // create a new adapter
        myHighscoreRecyclerViewAdapter = new MyHighscoreRecyclerViewAdapter(mHighscoreEntries);
        // set the adapter
        recyclerView.setAdapter(myHighscoreRecyclerViewAdapter);

        return view;
    }

    // Check if the new value is in the highscore
    // @return boolean: true if the entry is added
    private boolean checkList() {
        // Check if it deserve to be added
        // if the list is full and it's smaller than the last entry in the list
        if (mHighscoreEntries.size() >= 10 && mHighscoreEntries.get(mHighscoreEntries.size() - 1).getScore() >= mNewEntry.getScore())
            return false;

        // If the list is not full
        if (mHighscoreEntries.size() < 10) {
            // add it to the list
            mHighscoreEntries.add(mNewEntry);
        } else {
            // if the list is full, replace the last entry
            mHighscoreEntries.set(9, mNewEntry);
        }
        // Go through the list, starting at the almost last place
        for (int i = mHighscoreEntries.size() - 2; i >= 0; i--) {
            // Check if this score is less than the new
            if (mHighscoreEntries.get(i).getScore() < mNewEntry.getScore()) {
                // Swap the scores
                HighscoreEntry temp = mHighscoreEntries.get(i + 1);
                mHighscoreEntries.set(i + 1, mHighscoreEntries.get(i));
                mHighscoreEntries.set(i, temp);
            }
        }

        // save the scores
        saveHighscoreToFile(mHighscoreEntries, mGameMode + "_" + mCategory);
        return true;
    }

    // new entry to the highscores
    // @param newEntry: the new entry to add to the list
    public void addEntry(HighscoreEntry newEntry) {
        mNewEntry = newEntry;
        // Check if the entry is added to the list
        if (checkList()) {
            // notify the adapter that the list has changed
            myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void changeList(String gameMode, int category)
    {
        mGameMode= gameMode;
        mCategory = "" + category;

        loadList();
        myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void loadList() {
        // retrieve the highscores
        // returns a new list if nothing found
        mHighscoreEntries = getHighscoreFromFile(mGameMode + "_" + mCategory);

        // Check the size of the list
        if (mHighscoreEntries.size() < 6) {
            // If to few in the list, create new entries
            mHighscoreEntries.clear();
            mHighscoreEntries.add(new HighscoreEntry("William", 50));
            mHighscoreEntries.add(new HighscoreEntry("Gilles", 40));
            mHighscoreEntries.add(new HighscoreEntry("Downey", 30));
            mHighscoreEntries.add(new HighscoreEntry("Matthijs", 20));
            mHighscoreEntries.add(new HighscoreEntry("Tarik", 10));
            mHighscoreEntries.add(new HighscoreEntry("Eefje", 1));
        }
    }

    // Save the scores
    // @param highscoreEntries: The list to save
    // @param filename: The name of the file to save to
    private void saveHighscoreToFile(List<HighscoreEntry> highscoreEntries, String filename) {
        try {
            // Create a new writer to the specified file that will replace that file
            BufferedWriter writer = new BufferedWriter(new FileWriter(getContext().getFilesDir() + filename, false));
            // Go through the list
            for (HighscoreEntry highscoreEntry : highscoreEntries) {
                // Write the entry in a new line
                writer.write(highscoreEntry.toString());
                writer.newLine();
            }
            // Close the file
            writer.close();
        } catch (IOException e) {
            // If an error occurs, print it on the stackTrace
            e.printStackTrace();
        }
    }

    // Load the scores
    // @param filename: The name of the file to load from
    // @return List<HighscoreEntry>: The list from the given file
    private List<HighscoreEntry> getHighscoreFromFile(String filename) {
        // A new list
        List<HighscoreEntry> highscoreEntries = new ArrayList<>();
        try {
            // create a reader for the given file
            BufferedReader reader = new BufferedReader(new FileReader(getContext().getFilesDir() + filename));
            // define string
            String line;
            // read a line and save it to "line" as many times as possible
            while ((line = reader.readLine()) != null) {
                // Add this entry to the list
                highscoreEntries.add(new HighscoreEntry(line));
            }
            // Close the file
            reader.close();
        } catch (IOException e) {
            // If an error occurs, print it on the stackTrace
            e.printStackTrace();
        }
        // Return the list
        return highscoreEntries;
    }
}
