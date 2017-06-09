package be.howest.nmct.targit.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.models.HighscoreEntry;
import be.howest.nmct.targit.views.settings.StatusFragment;

public class MyHighscoreRecyclerViewAdapter extends RecyclerView.Adapter<MyHighscoreRecyclerViewAdapter.ViewHolder> {

    private final List<HighscoreEntry> mValues;

    public MyHighscoreRecyclerViewAdapter(List<HighscoreEntry> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_highscore_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPlace.setText("" + (position + 1));
        holder.mName.setText(mValues.get(position).getNickname());
        holder.mScore.setText("" + mValues.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mPlace;
        public final TextView mName;
        public final TextView mScore;

        public ViewHolder(View view) {
            super(view);
            mPlace = (TextView) view.findViewById(R.id.highscore_list_item_place);
            mName = (TextView) view.findViewById(R.id.highscore_list_item_name);
            mScore = (TextView) view.findViewById(R.id.highscore_list_item_score);
        }
    }
}

