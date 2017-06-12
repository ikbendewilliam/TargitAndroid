package be.howest.nmct.bluetoothtesting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ikben on 30/05/2017.
 */

public abstract class Constants {
    public static final String TAG = "Android - Arduino";
    public static final String TAG_MESSAGE = "Arduino Connection";

    public static final String[] DEVICE_NAMES = {
            "TARGIT-01",
            "TARGIT-02",
            "TARGIT-03",
            "TARGIT-04",
            "TARGIT-05"
    };

    //    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-03805f9b34fb");

    // commands for communitcation with arduino
    public static final String COMMAND_END = ";";
    public static final String COMMAND_LED_ON = "LedOn";
    public static final String COMMAND_LED_FLASH_SLOW = "LedFlash(500)";
    public static final String COMMAND_LED_FLASH_MEDIUM = "LedFlash(250)";
    public static final String COMMAND_LED_FLASH_FAST = "LedFlash(100)";
    public static final String COMMAND_LED_OFF = "LedOff";

    // commands incoming from arduino
    public static final String COMMAND_INCOMING_BUTTON_PRESSED = "BtnPressed";
    public static final String COMMAND_INCOMING_BUTTON_RELEASED = "BtnReleased";
}