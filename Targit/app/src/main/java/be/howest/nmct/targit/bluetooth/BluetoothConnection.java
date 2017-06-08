package be.howest.nmct.targit.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.howest.nmct.targit.bluetooth.Constants;
import be.howest.nmct.targit.models.ArduinoButton;
import me.aflak.bluetooth.Bluetooth;

/**
 * Created by ikben on 31/05/2017.
 * using Library me.aflak.bluetooth.Bluetooth: https://github.com/omaflak/Bluetooth-Library
 */

public class BluetoothConnection {
    private static BluetoothConnection thisBluetoothConnection;
    private static List<Bluetooth> mBluetooth;
    private static List<ArduinoButton> mArduinoButtons = new ArrayList<>();
    private static OnConnectionListener mListener;

    public BluetoothConnection(Activity activity, OnConnectionListener onConnectionListener) {
//        registerListener(onConnectionListener);
        mBluetooth = new ArrayList<>();
        Bluetooth bluetooth = new Bluetooth(activity);
        bluetooth.enableBluetooth();
    }

    public static BluetoothConnection initiate(Activity activity, OnConnectionListener onConnectionListener) {
        if (thisBluetoothConnection == null)
            thisBluetoothConnection = new BluetoothConnection(activity, onConnectionListener);
        return thisBluetoothConnection;
    }

//    public void registerListener(OnConnectionListener onConnectionListener) {
//        if (mListener != null)
//            unregisterListener();
//
//        mListener = onConnectionListener;
//        if (mBluetooth != null) {
//            for (Bluetooth bluetooth : mBluetooth) {
//                if (bluetooth.isConnected()) {
//                    bluetooth.setCommunicationCallback(getCommunicationCallback(bluetooth.getDevice().getName(), onConnectionListener));
//                }
//            }
//        }
//    }

    public void unregisterListener() {
        mListener = null;

        if (mBluetooth != null) {
            for (Bluetooth bluetooth : mBluetooth) {
                if (bluetooth.isConnected()) {
                    bluetooth.removeCommunicationCallback();
                }
            }
        }
    }

    public static BluetoothConnection getBluetoothConnection() {
        return thisBluetoothConnection;
    }

    public void addConnection(final ArduinoButton arduinoButton, Activity activity) {
        try {
            Bluetooth bluetooth = new Bluetooth(activity);
            bluetooth.setCommunicationCallback(getCommunicationCallback(arduinoButton));

            Log.i(Constants.TAG, "attempting connection to " + arduinoButton.getDeviceName());
            bluetooth.connectToName(arduinoButton.getDeviceName());

            mBluetooth.add(bluetooth);
            mArduinoButtons.add(arduinoButton);
        } catch (Exception e) {
            Log.e(Constants.TAG_MESSAGE, "addConnection: " + e);
        }
    }

    private Bluetooth.CommunicationCallback getCommunicationCallback(final ArduinoButton arduinoButton) {
        return new Bluetooth.CommunicationCallback() {
            @Override
            public void onConnect(BluetoothDevice device) {
                arduinoButton.setConnected(true);
                Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onConnect: " + device.getName());
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {
                arduinoButton.setConnected(false);
                Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onDisconnect: " + device.getName() + "\nMessage: " + message);
            }

            @Override
            public void onMessage(String message) {
                if (arduinoButton.isEnabled()) {
                    arduinoButton.setConnected(true);
                    arduinoButton.incomingMessage(message);
//                    Log.i(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onMessage: " + message);
                }
            }

            @Override
            public void onError(String message) {
                arduinoButton.setConnected(false);
                Log.e(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onError: " + message);
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                arduinoButton.setConnected(false);
                Log.e(Constants.TAG_MESSAGE, "connection " + arduinoButton.getDeviceName() + " - onConnectError: " + message);
            }
        };
    }

//    public void removeConnection(String deviceName) {
//        for (Bluetooth bluetooth : mBluetooth) {
//            if (bluetooth.getDevice().getName().equals(deviceName))
//                mBluetooth.remove(bluetooth);
//        }
//    }

    public void sendMessageToDevice(String deviceName, String message) {
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            if (arduinoButton.getDeviceName().equals(deviceName))
                if (!arduinoButton.isEnabled()) return;
        }
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                if (bluetooth.getDevice().getName().equals(deviceName)) {
                    bluetooth.send(message + Constants.COMMAND_END);
                    bluetooth.send(message + Constants.COMMAND_END); // Do it twice, just to be sure
                    //Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + deviceName + ": " + message);
                }
            }
        }
    }

    public void sendMessageToAll(String message) {
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            if (arduinoButton.isConnected() && arduinoButton.isEnabled()) {
                for (Bluetooth bluetooth : mBluetooth) {
                    if (bluetooth.isConnected()) {
                        if (bluetooth.getDevice().getName().equals(arduinoButton.getDeviceName())) {
                            bluetooth.send(message + Constants.COMMAND_END);
                            bluetooth.send(message + Constants.COMMAND_END); // Do it twice, just to be sure
                            //Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + bluetooth.getDevice().getName() + ": " + message);
                        }
                    }
                }
            }
        }
    }

    public boolean isDeviceConnected(String deviceName) {
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            if (arduinoButton.getDeviceName().equals(deviceName))
                return arduinoButton.isConnected() && arduinoButton.isEnabled();
        }
        return false;
    }

    public List<ArduinoButton> getArduinoButtons() {
        return mArduinoButtons;
    }

    public int getConnectedDevicesSize() {
        int i = 0;
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                i++;
            }
        }
        return i;
    }

    public void retryConnections() {
        int i = 0;
        for (ArduinoButton arduinoButton : mArduinoButtons) {
            if (!(arduinoButton.isConnected() || arduinoButton.isConnecting())) {
                mBluetooth.get(i).connectToName(arduinoButton.getDeviceName());
                Log.i(Constants.TAG, "attempting connection to " + arduinoButton.getDeviceName());
            }
            i++;
        }
    }

    public interface OnConnectionListener {
        void incomingMessage(String deviceName, String message);

        void finishConnecting(BluetoothDevice device);
    }
}

