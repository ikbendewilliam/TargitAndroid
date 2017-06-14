package be.howest.nmct.targit.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.bluetooth.BluetoothConnection;
import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;

import java.util.List;

import static be.howest.nmct.targit.Constants.COMMAND_LED_FLASH_SLOW;
import static be.howest.nmct.targit.Constants.COMMAND_LED_OFF;

// Class that fills recycleview in settings -> status (StatusFragment)
public class MyArduinoButtonRecyclerViewAdapter extends RecyclerView.Adapter<MyArduinoButtonRecyclerViewAdapter.ViewHolder> {

    //if return 1 show header, if 2 show list item
    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    private final List<ArduinoButton> mValues; // Values in the list

    // Constructor
    // @param items: the items to show in the list
    public MyArduinoButtonRecyclerViewAdapter(List<ArduinoButton> items) {
        mValues = items;
    }

    // Create method of adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // inflate your first item layout & return that viewHolder
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_status_bluetooth_item_header, parent, false);
        } else {
            // inflate your second item layout & return that viewHolder
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_status_bluetooth_item, parent, false);
        }
        return new ViewHolder(view);
    }

    // When items are placed in their UI (fragment_status_bluetooth_item.xml)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //voor header geen functionaliteit
        if (getItemViewType(position) == 1)
            return;


        if (mValues.get(position - 1).isConnected()) {
            // if the device is connected, show connected image
            holder.mConnected.setImageResource(R.drawable.ic_status_connected);
        } else if (mValues.get(position - 1).isConnecting()) {
            // if the device is still connecting, show connecting image
            holder.mConnected.setImageResource(R.drawable.ic_status_pending);
        } else {
            // if the device is not connected or connecting, show not connected image
            holder.mConnected.setImageResource(R.drawable.ic_status_disconnected);
        }

        // Show deviceName
        holder.mName.setText(mValues.get(position - 1).getDeviceName());

        // Show how many times the button is pressed
        // This is for debugging purposes only
        //holder.mPressed.setText("" + mValues.get(position - 1).getPressedCount());

        // Set ClickListener on the switch
        holder.mEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mEnabled.isChecked()) {
                    // The switch is set on true
                    // Enable this device
                    mValues.get(holder.getAdapterPosition() - 1).setEnabled(true);
                    // Get the BluetoothConnection manager
                    BluetoothConnection bluetoothConnection = BluetoothConnection.getBluetoothConnection();
                    bluetoothConnection.sendMessageToAll(COMMAND_LED_OFF); // turn all devices off so they flash in sync
                    bluetoothConnection.sendMessageToAll(COMMAND_LED_FLASH_SLOW); // flash all leds
                } else {
                    // Get the BluetoothConnection manager
                    BluetoothConnection bluetoothConnection = BluetoothConnection.getBluetoothConnection();
                    // Get the deviceName
                    String deviceName = mValues.get(holder.getAdapterPosition() - 1).getDeviceName();
                    // Let this device  flash slowly
                    bluetoothConnection.sendMessageToDevice(deviceName, Constants.COMMAND_LED_OFF);
                    // Disable the device
                    // WARNING: This is immediatly, you can't send any commands until you enable it again!
                    mValues.get(holder.getAdapterPosition() - 1).setEnabled(false);
                }
            }
        });

        // Set the switch to true if the device is enabled
        // Set the switch to false if the device is NOT enabled
        // Enable does not have anything to do with connected (see models>ArduinoButton for more info)
        holder.mEnabled.setChecked(mValues.get(position - 1).isEnabled());
        if (mValues.get(position - 1).isPressed()) {
            // If the button on the device is pressed, change the background of this row
            holder.mLayout.setBackgroundColor(Color.rgb(128, 200, 200));
        } else {
            // If the button on the device is NOT pressed, revert the background of this row
            holder.mLayout.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

    // Returns the number of items in this list
    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    // ViewHolder represents a row in the list (fragment_status_bluetooth_item.xml)
    public class ViewHolder extends RecyclerView.ViewHolder {
        // The complete row
        public final LinearLayout mLayout;
        // The name of the device
        public final TextView mName;

        // Counter of the pressed amount
        //public final TextView mPressed;
        // Whether or not the device is connected/connecting
        public final ImageView mConnected;
        // Switch to enable/disable the device
        public final Switch mEnabled;

        // Constructor to declare the fields
        public ViewHolder(View view) {
            super(view);
            mLayout = (LinearLayout) view.findViewById(R.id.arduinobutton_layout);
            mName = (TextView) view.findViewById(R.id.arduinobutton_name);

            //mPressed = (TextView) view.findViewById(R.id.arduinobutton_pressed);
            mConnected = (ImageView) view.findViewById(R.id.arduinobutton_connected);
            mEnabled = (Switch) view.findViewById(R.id.arduinobutton_enabled);
        }
    }
}

