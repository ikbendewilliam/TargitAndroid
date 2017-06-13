package be.howest.nmct.targit.models;

import be.howest.nmct.targit.Constants;

// Class that represents a device
public class ArduinoButton {
    private String deviceName; // The name of the device
    private int pressedCount = 0; // How many times this button is pressed
    private boolean isPressed = false; // Whether this button is currently pressed
    private boolean isConnected = false; // Whether this button is currently connected
    private boolean isConnecting = true; // Whether this button is currently connecting
    private boolean isEnabled = true; // Whether this button is currently enabled

    // Constructor
    // @param deviceName: the name of this device
    public ArduinoButton(String deviceName) {
        this.deviceName = deviceName;
    }

    // Precess an incoming message
    // @param message: The received message from the arduino
    public void incomingMessage(String message) {
        // Check if this device is enabled
        if (isEnabled) {
            // if the message says that the button is being pressed
            if (message.contains(Constants.COMMAND_INCOMING_BUTTON_PRESSED)) {
                // if this button isn't already pressed
                if (!isPressed) {
                    // increment pressed counter
                    pressedCount++;
                }
                // set this button to pressed
                isPressed = true;
            // if the message says that the button is being released
            } else if (message.contains(Constants.COMMAND_INCOMING_BUTTON_RELEASED)) {
                // set this button to not pressed
                isPressed = false;
            }
        }
    }

    // @return boolean: true if this device is currently being pressed
    public boolean isPressed() {
        return isPressed;
    }

    // @return int: how many times this button is pressed
    public int getPressedCount() {
        return pressedCount;
    }

    // @return String: the name of this device
    public String getDeviceName() {
        return deviceName;
    }

    // @return boolean: true if this device is currently connected
    public boolean isConnected() {
        return isConnected;
    }

    // @param connected: true if this device is connected
    public void setConnected(boolean connected) {
        // This means that the connecting-phase is done
        isConnecting = false;
        isConnected = connected;
    }

    // @return boolean: true if this device is currently connecting
    public boolean isConnecting() {
        return isConnecting;
    }

    // @param connected: true if this device is connected
    public void setConnecting(boolean connecting) {
        // This means that the connecting-phase is done
        isConnecting = connecting;
    }

    // @return boolean: true if this device is currently enabled
    public boolean isEnabled() {
        return isEnabled;
    }

    // @param enabled: enable or disable this device
    public void setEnabled(boolean enabled) {
        // the device is not pressed when disabled
        isPressed = false;
        isEnabled = enabled;
    }
}
