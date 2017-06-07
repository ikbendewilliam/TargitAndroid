package be.howest.nmct.targit.adapters;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import be.howest.nmct.targit.views.settings.StatusFragment;

import java.util.List;

public class MyArduinoButtonRecyclerViewAdapter extends RecyclerView.Adapter<MyArduinoButtonRecyclerViewAdapter.ViewHolder> {

    private final List<ArduinoButton> mValues;
    private final StatusFragment.OnStatusFragmentInteractionListener mListener;

    public MyArduinoButtonRecyclerViewAdapter(List<ArduinoButton> items, StatusFragment.OnStatusFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_status_bluetooth_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues.get(position).isConnected()) {
            holder.mConnected.setImageResource(android.R.drawable.ic_input_add);
        }
        else {
            holder.mConnected.setImageResource(android.R.drawable.ic_delete);
        }
        holder.mName.setText(mValues.get(position).getDeviceName());
        holder.mPressed.setText("" + mValues.get(position).getPressedCount());

        holder.mEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mEnabled.isChecked()) {
                    mValues.get(holder.getAdapterPosition()).setEnabled(true);
                    BluetoothConnection.getBluetoothConnection().sendMessageToDevice(mValues.get(holder.getAdapterPosition()).getDeviceName(), Constants.COMMAND_LED_FLASH_SLOW);
                }
                else {
                    BluetoothConnection.getBluetoothConnection().sendMessageToDevice(mValues.get(holder.getAdapterPosition()).getDeviceName(), Constants.COMMAND_LED_OFF);
                    mValues.get(holder.getAdapterPosition()).setEnabled(false);
                }
            }
        });

        holder.mEnabled.setChecked(mValues.get(position).isEnabled());
        if (mValues.get(position).isPressed())
            holder.mLayout.setBackgroundColor(Color.rgb(128, 200, 200));
        else
            holder.mLayout.setBackgroundColor(Color.rgb(255, 255, 255));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mLayout;
        public final TextView mName;
        public final TextView mPressed;
        public final ImageView mConnected;
        public final Switch mEnabled;

        public ViewHolder(View view) {
            super(view);
            mLayout = (LinearLayout) view.findViewById(R.id.arduinobutton_layout);
            mName = (TextView) view.findViewById(R.id.arduinobutton_name);
            mPressed = (TextView) view.findViewById(R.id.arduinobutton_pressed);
            mConnected = (ImageView) view.findViewById(R.id.arduinobutton_connected);
            mEnabled = (Switch) view.findViewById(R.id.arduinobutton_enabled);
        }
    }
}

