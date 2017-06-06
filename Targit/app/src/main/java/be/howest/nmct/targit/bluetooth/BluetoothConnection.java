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
import me.aflak.bluetooth.Bluetooth;

/**
 * Created by ikben on 31/05/2017.
 * using Library me.aflak.bluetooth.Bluetooth: https://github.com/omaflak/Bluetooth-Library
 */

public class BluetoothConnection {
    private static BluetoothConnection thisBluetoothConnection;
    private static List<Bluetooth> mBluetooth;
    private static OnConnectionListener mConnectionListener;
    private static OnMessageListener mMessageListener;

    private BluetoothConnection(Activity activity, OnConnectionListener onConnectionListener) {
        registerConnectionListener(onConnectionListener);
        mBluetooth = new ArrayList<>();
        Bluetooth bluetooth = new Bluetooth(activity);
        bluetooth.enableBluetooth();
    }

    public static BluetoothConnection initiate(Activity activity, OnConnectionListener onConnectionListener) {
        if (thisBluetoothConnection == null)
            thisBluetoothConnection = new BluetoothConnection(activity, onConnectionListener);
        return thisBluetoothConnection;
    }

    public void registerConnectionListener(OnConnectionListener onConnectionListener) {
        if (mConnectionListener != null)
            unregisterConnectionListener();

        mConnectionListener = onConnectionListener;
        registerListener();
    }

    public void unregisterConnectionListener() {
        mConnectionListener = null;
        unregisterMessageListener();
    }

    public void registerMessageListener(OnMessageListener onMessageListener) {
        if (mMessageListener != null)
            unregisterMessageListener();

        mMessageListener = onMessageListener;
        registerListener();
    }

    public void unregisterMessageListener() {
        mMessageListener = null;
        unregisterMessageListener();
    }

    private void registerListener() {
        if (mBluetooth != null) {
            for (Bluetooth bluetooth : mBluetooth) {
                if (bluetooth.isConnected()) {
                    bluetooth.setCommunicationCallback(getCommunicationCallback(bluetooth.getDevice().getName(), mConnectionListener, mMessageListener));
                }
            }
        }
    }

    private void unregisterListener() {
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

    public void addConnection(final String deviceName, Activity activity) {
        Bluetooth bluetooth = new Bluetooth(activity);
        bluetooth.setCommunicationCallback(getCommunicationCallback(deviceName, mConnectionListener, null));

        Log.i(Constants.TAG, "attempting connection to " + deviceName);
        bluetooth.connectToName(deviceName);

        mBluetooth.add(bluetooth);
    }

    private Bluetooth.CommunicationCallback getCommunicationCallback(final String deviceName, final OnConnectionListener connectionListener, final OnMessageListener messageListener) {
        return new Bluetooth.CommunicationCallback() {
            @Override
            public void onConnect(BluetoothDevice device) {
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onConnect: " + device.getName());
                if (connectionListener != null)
                    connectionListener.finishConnecting(device);
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onDisconnect: " + device.getName() + "\nMessage: " + message);
            }

            @Override
            public void onMessage(String message) {
                Log.i(Constants.TAG_MESSAGE, "connection " + deviceName + " - onMessage: " + message);
                if (messageListener != null)
                    messageListener.incomingMessage(deviceName, message);
            }

            @Override
            public void onError(String message) {
                Log.e(Constants.TAG_MESSAGE, "connection " + deviceName + " - onError: " + message);
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                Log.e(Constants.TAG_MESSAGE, "connection " + deviceName + " - onConnectError: " + message);
            }
        };
    }

    public void removeConnection(String deviceName) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.getDevice().getName().equals(deviceName))
                mBluetooth.remove(bluetooth);
        }
    }

    public void sendMessageToDevice(String deviceName, String message) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                if (bluetooth.getDevice().getName().equals(deviceName)) {
                    bluetooth.send(message + Constants.COMMAND_END);
                    Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + deviceName + ": " + message);
                }
            }
        }
    }

    public void sendMessageToAll(String message) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                bluetooth.send(message + Constants.COMMAND_END);
                Log.i(Constants.TAG_MESSAGE, "sendMessageToDevice: " + bluetooth.getDevice().getName() + ": " + message);
            }
        }
    }

    public boolean isDeviceConnected(String deviceName) {
        for (Bluetooth bluetooth : mBluetooth) {
            if (bluetooth.isConnected()) {
                if (bluetooth.getDevice().getName().equals(deviceName))
                    return true;
            }
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
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
            if (!bluetooth.isConnected()) {
                bluetooth.connectToName(toConnect.get(i));
                i++;
            }
        }
    }

    public interface OnConnectionListener {
        void finishConnecting(BluetoothDevice device);
    }

    public interface OnMessageListener {
        void incomingMessage(String deviceName, String message);
    }
}

