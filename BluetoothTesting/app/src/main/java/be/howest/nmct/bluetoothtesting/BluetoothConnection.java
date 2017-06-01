package be.howest.nmct.bluetoothtesting;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

/**
 * Created by ikben on 31/05/2017.
 * using Library me.aflak.bluetooth.Bluetooth: https://github.com/omaflak/Bluetooth-Library
 */

class BluetoothConnection {
    private List<Bluetooth> mBluetooth;
    private Activity mActivity;
    private OnConnectionListener mListener;

    BluetoothConnection(Activity activity, OnConnectionListener onConnectionListener) {
        mBluetooth = new ArrayList<>();

        Bluetooth bluetooth = new Bluetooth(activity);
        bluetooth.enableBluetooth();
        mActivity = activity;

        mListener = onConnectionListener;
    }

    void addConnection(final String deviceName) {
        Bluetooth bluetooth = new Bluetooth(mActivity);
        bluetooth.setCommunicationCallback(new Bluetooth.CommunicationCallback() {
            @Override
            public void onConnect(BluetoothDevice device) {
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onConnect: " + device.getName());
                mListener.finishConnecting(device);
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onDisconnect: " + device.getName() + "\nMessage: " + message);
            }

            @Override
            public void onMessage(String message) {
                mListener.incomingMessage(deviceName, message);
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onMessage: " + message);
            }

            @Override
            public void onError(String message) {
                Log.e(Constants.TAG_MESSAGE, "connection " + deviceName + " - onError: " + message);
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                Log.e(Constants.TAG_MESSAGE, "connection " + deviceName + " - onConnectError: " + message);
            }
        });

        Log.i(Constants.TAG, "attempting connection to " + deviceName);
        bluetooth.connectToName(deviceName);

        mBluetooth.add(bluetooth);
    }

    void removeConnection(String deviceName) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.getDevice().getName().equals(deviceName))
                mBluetooth.remove(bluetooth);
        }
    }

    void sendMessageToDevice(String deviceName, String message) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                if (bluetooth.getDevice().getName().equals(deviceName)) {
                    bluetooth.send(message + Constants.COMMAND_END);
                    Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + deviceName + ": " + message);
                }
            }
        }
    }

    void sendMessageToAll(String message) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                bluetooth.send(message + Constants.COMMAND_END);
                Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + bluetooth.getDevice().getName() + ": " + message);
            }
        }
    }

    boolean isDeviceConnected(String deviceName) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                if (bluetooth.getDevice().getName().equals(deviceName))
                    return true;
            }
        }
        return false;
    }

    List<BluetoothDevice> getConnectedDevices() {
        List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                bluetoothDeviceList.add(bluetooth.getDevice());
            }
        }
        return bluetoothDeviceList;
    }

    public void retryConnections(String[] deviceNames) {
        List<String> toConnect = Arrays.asList(deviceNames);

        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected())
                toConnect.remove(bluetooth.getDevice().getName());
        }

        int i = 0;
        for (Bluetooth bluetooth : mBluetooth) {
            if (!bluetooth.isConnected())
            {
                bluetooth.connectToName(toConnect.get(i));
                i++;
            }
        }
    }

    interface OnConnectionListener {
        void incomingMessage(String deviceName, String message);
        void finishConnecting(BluetoothDevice device);
    }
}
