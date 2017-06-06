package be.howest.nmct.targit.models;

import be.howest.nmct.targit.bluetooth.Constants;

/**
 * Created by ikben on 02/06/2017.
 */

public class ArduinoButton {
    private String deviceName;
    private boolean isPressed = false;
    private int pressedCount = 0;

    public ArduinoButton(String deviceName) {
        this.deviceName = deviceName;
    }

    public void incomingMessage(String message) {
        if (message.contains(Constants.COMMAND_INCOMING_BUTTON_PRESSED)) {
            if (!isPressed)
                pressedCount++;
            isPressed = true;
        } else if (message.contains(Constants.COMMAND_INCOMING_BUTTON_RELEASED)) {
            isPressed = false;
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
}
