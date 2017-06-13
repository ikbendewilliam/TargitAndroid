package be.howest.nmct.targit.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import me.aflak.bluetooth.Bluetooth;

/**
 * Created by ikben on 31/05/2017.
 * using Library me.aflak.bluetooth.Bluetooth: https://github.com/omaflak/Bluetooth-Library
 */

// Class that manages ALL bluetoothConnections and the ArduinoButtonList
public class BluetoothConnection {
    // The only instance of this class
    private static BluetoothConnection thisBluetoothConnection;
    // A list of Bluetooth objects
    // These are the actual connections
    // For more information on how this works goto https://github.com/omaflak/Bluetooth-Library
    private static List<Bluetooth> mBluetooth;
    // The list of ArduinoButtons
    // More information about these buttons in models>ArduinoButton
    private static List<ArduinoButton> mArduinoButtons = new ArrayList<>();

    // Constructor
    // This is private so no new instances can be created of this class
    // @param activity: the activity the connection is in created
    private BluetoothConnection(Activity activity) {
        // initiate the list
        mBluetooth = new ArrayList<>();
        // make a temp instance to enable bt
        Bluetooth bluetooth = new Bluetooth(activity);
        bluetooth.enableBluetooth();
    }

    // Initialize the connection
    // @param activity: the activity the connection is in created
    // @return BluetoothConnection: Either the new connection or the already existing one
    public static BluetoothConnection initiate(Activity activity) {
        // Check whether the instance is already created
        if (thisBluetoothConnection == null) {
            // Create the only instance of this class
            thisBluetoothConnection = new BluetoothConnection(activity);
        }
        // Return the instance of this class
        return thisBluetoothConnection;
    }

    // Return the initialized connection
    // @return BluetoothConnection: Either the existing connection
    public static BluetoothConnection getBluetoothConnection() {
        return thisBluetoothConnection;
    }

    // Add a connection to a new ArduinoButton
    // @param arduinoButton: The device to connect to
    // @param activity: The active activity
    public void addConnection(final ArduinoButton arduinoButton, Activity activity) {
        try {
            // Make a new connection
            Bluetooth bluetooth = new Bluetooth(activity);
            // Set the communication
            bluetooth.setCommunicationCallback(getCommunicationCallback(arduinoButton));

            // Log that we are attempting a connection
            Log.i(Constants.TAG, "attempting connection to " + arduinoButton.getDeviceName());
            // Start to connect to this device
            bluetooth.connectToName(arduinoButton.getDeviceName());

            // Add the connection to mBluetooth
            mBluetooth.add(bluetooth);
            // Add the device to mArduinoButtons
            mArduinoButtons.add(arduinoButton);
        } catch (Exception e) {
            // When an error in this process occurs, log it
            // This is done to test the ui on emulators which have no bt
            Log.e(Constants.TAG_MESSAGE, "addConnection: " + e);
        }
    }

    // The CommunicationCallback
    // What happens when we have something happening with the connection
    // @param arduinoButton: The button for the connection
    // @return Bluetooth.CommunicationCallback: a new callback
    private Bluetooth.CommunicationCallback getCommunicationCallback(final ArduinoButton arduinoButton) {
        // Return a new callback
        return new Bluetooth.CommunicationCallback() {
            @Override
            public void onConnect(BluetoothDevice device) {
                // When the connection succeeded
                // Set connected true for this device
                arduinoButton.setConnected(true);
                // Log that the connection succeeded
                Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onConnect: " + device.getName());
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {
                // When the connection breaks
                // Set connected false for this device
                arduinoButton.setConnected(false);
                // Log that the connection disconnected
                Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onDisconnect: " + device.getName() + "\nMessage: " + message);
            }

            @Override
            public void onMessage(String message) {
                // When a message comes in
                // Check if the button should receive messages
                if (arduinoButton.isEnabled()) {
                    // Set connected true for this device (just a fail-safe)
                    arduinoButton.setConnected(true);
                    // Let the arduinobutton process this message
                    arduinoButton.incomingMessage(message);
                    // DON'T log it. This creates a lot of output
                    // Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onMessage: " + message);
                }
            }

            @Override
            public void onError(String message) {
                // When an error occurs
                // Set connected false for this device
                arduinoButton.setConnected(false);
                // Log that there was an error and what the error was
                Log.e(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onError: " + message);
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                // When connecting doesn't succeed
                // Set connected false for this device
                arduinoButton.setConnected(false);
                // Log that there was an error connecting
                Log.e(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onConnectError: " + message);
            }
        };
    }

    // Send a message to a specific device
    // @param deviceName: Specifies the device to send to
    // @param message: The message to send
    public void sendMessageToDevice(String deviceName, String message) {
        // Find the arduinoButton that represents this device
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            if (arduinoButton.getDeviceName().equals(deviceName)) {
                // See if this device is enabled
                if (!arduinoButton.isEnabled()) return;
            }
        }
        // Loop all connections to get the correct one
        for (Bluetooth bluetooth : mBluetooth) {
            // Only check connections that are actually connected
            if (bluetooth.isConnected()) {
                // Compare the names
                if (bluetooth.getDevice().getName().equals(deviceName)) {
                    // Send the message
                    bluetooth.send(message + Constants.COMMAND_END);
                    // Do it twice, just to be sure
                    // Some buttons didn't respond, now they do
                    bluetooth.send(message + Constants.COMMAND_END);
                    // DON'T log it. This creates a lot of output
                    //Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + deviceName + ": " + message);
                }
            }
        }
    }

    // Send a message to all devices that are connected and enabled
    // @param message: The message to send
    public void sendMessageToAll(String message) {
        // Loop all devices
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            // Check if they are connected and enabled
            if (arduinoButton.isConnected() && arduinoButton.isEnabled()) {
                // Loop all connections
                for (Bluetooth bluetooth : mBluetooth) {
                    // Check if the connection is connected
                    if (bluetooth.isConnected()) {
                        // See if this is the connection that corresponds with the device
                        if (bluetooth.getDevice().getName().equals(arduinoButton.getDeviceName())) {
                            // Send the message
                            bluetooth.send(message + Constants.COMMAND_END);
                            // Do it twice, just to be sure
                            // Some buttons didn't respond, now they do
                            bluetooth.send(message + Constants.COMMAND_END);
                            // DON'T log it. This creates a lot of output
                            //Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + bluetooth.getDevice().getName() + ": " + message);
                        }
                    }
                }
            }
        }
    }

    // Check if this device is connected and enabled
    // @param deviceName: The device to see if it is connected and enabled
    // @return boolean: true if this device is connected and enabled, otherwise false
    public boolean isDeviceConnected(String deviceName) {
        // Loop all connections
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            // Check if this is the device we are looking for
            if (arduinoButton.getDeviceName().equals(deviceName))
                // return whether the device is both connected and enabled
                return arduinoButton.isConnected() && arduinoButton.isEnabled();
        }
        // The device was not found -> cannot be connected
        return false;
    }

    // Get the list of devices
    // @return List<ArduinoButton>: The list of arduinoButtons that we use
    public List<ArduinoButton> getArduinoButtons() {
        return mArduinoButtons;
    }

    // Get the number of connected devices
    // @return int: the amount of connected devices
    public int getConnectedDevicesSize() {
        // initiate connections to 0
        int connections = 0;
        // Loop all connections
        for (Bluetooth bluetooth : mBluetooth) {
            // If this connection is connected
            if (bluetooth.isConnected()) {
                // increment connections
                connections++;
            }
        }
        // return connections, the amount of connected devices
        return connections;
    }

    // Retry all failed connections
    public void retryConnections() {
        // TODO: replace this variable with another loop perhaps
        // initiate connection to 0
        int connection = 0;
        // Loop all devices
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            // if this device is not connected and not connecting
            if (!(arduinoButton.isConnected() || arduinoButton.isConnecting())) {
                // Start to connect to this device
                arduinoButton.setConnecting(true);
                // get the connection to connect to this device
                mBluetooth.get(connection).connectToName(arduinoButton.getDeviceName());
                // Log that we are attempting a new connection
                Log.i(Constants.TAG, "attempting connection to " + arduinoButton.getDeviceName());
            }
            // Increment connection
            connection++;
        }
    }
}

