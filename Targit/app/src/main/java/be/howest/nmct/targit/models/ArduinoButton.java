package be.howest.nmct.targit.models;

import be.howest.nmct.targit.bluetooth.Constants;

/**
 * Created by ikben on 02/06/2017.
 */

public class ArduinoButton {
    private String deviceName;
    private int pressedCount = 0;
    private boolean isPressed = false;
    private boolean isConnected = false;
    private boolean isConnecting = true;
    private boolean isEnabled = true;

    public ArduinoButton(String deviceName) {
        this.deviceName = deviceName;
    }

    public void incomingMessage(String message) {
        if (isEnabled) {
            if (message.contains(Constants.COMMAND_INCOMING_BUTTON_PRESSED)) {
                if (!isPressed)
                    pressedCount++;
                isPressed = true;
            } else if (message.contains(Constants.COMMAND_INCOMING_BUTTON_RELEASED)) {
                isPressed = false;
            }
        }
    }

    public boolean isPressed() {
        return isPressed;
    }

    public int getPressedCount() {
        return pressedCount;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnecting = false;
        isConnected = connected;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && (deviceName.equals(o)) && (o.getClass() == String.class || o.getClass() == ArduinoButton.class);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isPressed = false;
        isEnabled = enabled;
    }
}
