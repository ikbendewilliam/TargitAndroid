package be.howest.nmct.targit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.models.HighscoreEntry;

// Class that fills recycleview in highscore (HighscoreFragment)
public class MyHighscoreRecyclerViewAdapter extends RecyclerView.Adapter<MyHighscoreRecyclerViewAdapter.ViewHolder> {

    private final List<HighscoreEntry> mValues; // Values in the list

    // Constructor
    // @param items: the items to show in the list
    public MyHighscoreRecyclerViewAdapter(List<HighscoreEntry> items) {
        mValues = items;
    }

    // Create method of adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_highscore_list_item, parent, false);
        return new ViewHolder(view);
    }

    // When items are placed in their UI (fragment_highscore_list_item.xml)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set the place (position starts at 0 while the ranking starts at 1)
        holder.mPlace.setText("" + (position + 1));
        // Set the name of the player with this score
        holder.mName.setText(mValues.get(position).getNickname());
        // Set the score of this player
        holder.mScore.setText("" + mValues.get(position).getScore());
    }

    // Returns the number of items in this list
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // ViewHolder represents a row in the list (fragment_highscore_list_item.xml)
    public class ViewHolder extends RecyclerView.ViewHolder {
        // The ranking of this player
        public final TextView mPlace;
        // The name of this player
        public final TextView mName;
        // The score of this player
        public final TextView mScore;

        // Constructor to declare the fields
        public ViewHolder(View view) {
            super(view);
            mPlace = (TextView) view.findViewById(R.id.highscore_list_item_place);
            mName = (TextView) view.findViewById(R.id.highscore_list_item_name);
            mScore = (TextView) view.findViewById(R.id.highscore_list_item_score);
        }
    }
}

