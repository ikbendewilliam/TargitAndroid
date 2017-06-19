package be.howest.nmct.targit.views.highscore;


import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_EASY;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_HARD;
import static be.howest.nmct.targit.Constants.EXTRA_DIFFICULTY_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_LONG;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_MEDIUM;
import static be.howest.nmct.targit.Constants.EXTRA_DURATION_SHORT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAME_ZENIT;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_FEW;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MANY;
import static be.howest.nmct.targit.Constants.EXTRA_LIVES_MEDIUM;
import static be.howest.nmct.targit.Constants.SAVESTATE_LIST_CATEGORY;
import static be.howest.nmct.targit.Constants.SAVESTATE_LIST_GAMEMODE;
import static be.howest.nmct.targit.Constants.TAG;

// The fragment that shows the highscore
public class HighscoreListFragment extends Fragment {
    private String mGameMode; // The game mode (as defined in Constants)
    private String mCategory; // The game mode (as defined in Constants, ints are transformed into Strings)
    List<HighscoreEntry> mHighscoreEntries; // A list of all entries in this category
    private HighscoreEntry mNewEntry; // The new entry provided (can be null)
    private MyHighscoreRecyclerViewAdapter myHighscoreRecyclerViewAdapter; // The adapter that will fill the list
    private TextView txtTitle; // The text field for the title
    private boolean mRemoveBottom = false; // If true the bottom of the background will be removed

    // Required empty public constructor
    public HighscoreListFragment() {
        // Required empty public constructor
    }

    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    public static HighscoreListFragment newInstance(String gameMode, int category, @Nullable HighscoreEntry newEntry) {
        // Transform category to String and call the other constructor
        return newInstance(gameMode, "" + category, newEntry);
    }


    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    public static HighscoreListFragment newInstance(String gameMode, String category, @Nullable HighscoreEntry newEntry) {
        // Create a new fragment
        HighscoreListFragment fragment = new HighscoreListFragment();
        // Set the variables to the provided parameters
        fragment.mGameMode = gameMode;
        fragment.mCategory = category;
        fragment.mNewEntry = newEntry;
        return fragment;
    }

    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    // @param removeBoolean: if true bottom of the image will be removed
    public static HighscoreListFragment newInstance(String gameMode, int category, @Nullable HighscoreEntry newEntry, boolean removeBottom) {
        HighscoreListFragment fragment = newInstance(gameMode, category, newEntry);
        // Set the variable removeBoolean
        fragment.mRemoveBottom = removeBottom;
        return fragment;
    }


    // New instance of this fragment, with parameters
    // @param gameMode: the gamemode where you want the highscores from
    // @param category: the category where you want the highscores from
    // @param Nullable HighscoreEntry: if there is a new entry, define it here, otherwise null
    // @param removeBoolean: if true bottom of the image will be removed
    public static HighscoreListFragment newInstance(String gameMode, String category, @Nullable HighscoreEntry newEntry, boolean removeBottom) {
        HighscoreListFragment fragment = newInstance(gameMode, category, newEntry);
        // Set the variable removeBoolean
        fragment.mRemoveBottom = removeBottom;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(SAVESTATE_LIST_GAMEMODE) && savedInstanceState.keySet().contains(SAVESTATE_LIST_CATEGORY)) {
                mGameMode = savedInstanceState.getString(SAVESTATE_LIST_GAMEMODE);
                mCategory = savedInstanceState.getString(SAVESTATE_LIST_CATEGORY);
                Log.i(TAG, "onCreateView: " + mGameMode);
            }
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highscore_list, container, false);

        //Set title based on gamemode and diff
        txtTitle = (TextView) view.findViewById(R.id.fragment_highscore_list_title);

        //set the fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        txtTitle.setTypeface(font);

        //remove the bottom part of the background
        if (mRemoveBottom)
            removeBottomPart(view);

        // retrieve the highscores
        loadList();

        // if a new entry is provided, check if it is in the highscore
        if (mNewEntry != null) {
            checkList();
        }

        // get the list
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.highscore_recycleview);
        // set the layoutmanager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // create a new adapter
        myHighscoreRecyclerViewAdapter = new MyHighscoreRecyclerViewAdapter(mHighscoreEntries);
        // set the adapter
        recyclerView.setAdapter(myHighscoreRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()); //create a divider between rows
        recyclerView.addItemDecoration(dividerItemDecoration); //set the divider

        return view;
    }

    //Remove the bottom part of the background by editing the layout
    private void removeBottomPart(View view) {
        //change the background
        View layout = view.findViewById(R.id.highscore_list_layout);
        layout.setBackgroundResource(R.drawable.highscoreboard_bottom);

        //Remove bottom border
        View borderBottom = view.findViewById(R.id.highscore_list_border_bottom);
        ((ViewGroup) borderBottom.getParent()).removeView(borderBottom);

        //expand the list to the bottom
        RecyclerView list = (RecyclerView) view.findViewById(R.id.highscore_recycleview);
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) list.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.heightPercent = 1f;
        list.setLayoutParams(params);
    }

    // Set title based on gamemode and diff
    private void setTitle(TextView txtTitle) {
        int id = 0;

        Log.i(TAG, "setTitle: " + mGameMode);
        if (mGameMode.equals(EXTRA_GAME_SMASHIT)) {
            if (mCategory.equals(EXTRA_DIFFICULTY_EASY))
                id = R.string.smashit_easy;
            else if (mCategory.equals(EXTRA_DIFFICULTY_MEDIUM))
                id = R.string.smashit_medium;
            else if (mCategory.equals(EXTRA_DIFFICULTY_HARD))
                id = R.string.smashit_hard;
        } else if (mGameMode.equals(EXTRA_GAME_ZENIT)) {
            if (mCategory.equals("" + EXTRA_DURATION_SHORT))
                id = R.string.zenit_easy;
            else if (mCategory.equals("" + EXTRA_DURATION_MEDIUM))
                id = R.string.zenit_medium;
            else if (mCategory.equals("" + EXTRA_DURATION_LONG))
                id = R.string.zenit_hard;
        } else if (mGameMode.equals(EXTRA_GAME_MEMORIT)) {
            if (mCategory.equals("" + EXTRA_LIVES_MANY))
                id = R.string.memorit_easy;
            else if (mCategory.equals("" + EXTRA_LIVES_MEDIUM))
                id = R.string.memorit_medium;
            else if (mCategory.equals("" + EXTRA_LIVES_FEW))
                id = R.string.memorit_hard;
        }

        if (id != 0)
            txtTitle.setText(getString(id));

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

    public void changeList(String gameMode, String category) {
        mGameMode = gameMode;
        mCategory = category;

        loadList();
        myHighscoreRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void changeList(String gameMode, int category) {
        changeList(gameMode, "" + category);
    }

    private void loadList() {
        // retrieve the highscores
        if (mHighscoreEntries == null)
            mHighscoreEntries = new ArrayList<>();
        // returns a new list if nothing found
        List<HighscoreEntry> tempList = getHighscoreFromFile(mGameMode + "_" + mCategory);
        mHighscoreEntries.clear();
        mHighscoreEntries.addAll(tempList);

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
        Log.i(TAG, "loadList: " + mHighscoreEntries.toString());
        //change the title
        setTitle(txtTitle);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVESTATE_LIST_GAMEMODE, mGameMode);
        outState.putString(SAVESTATE_LIST_CATEGORY, mCategory);
        super.onSaveInstanceState(outState);
    }
}
