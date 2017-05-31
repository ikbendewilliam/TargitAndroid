package be.howest.nmct.bluetoothtesting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ikben on 30/05/2017.
 */

public abstract class Constants {
    static final String TAG = "Android - Arduino";
    static final String TAG_MESSAGE = "Arduino Connection";

    static final String[] DEVICE_NAMES = {
            "TARGIT-01",
            "TARGIT-02",
            "TARGIT-03",
            "TARGIT-04",
            "TARGIT-05"
    };

    //    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-03805f9b34fb");

    // commands for communitcation with arduino
    static final String COMMAND_END = ";";
    static final String COMMAND_LED_ON = "LedOn";
    static final String COMMAND_LED_FLASH_SLOW = "LedFlash(500)";
    static final String COMMAND_LED_FLASH_MEDIUM = "LedFlash(250)";
    static final String COMMAND_LED_FLASH_FAST = "LedFlash(100)";
    static final String COMMAND_LED_OFF = "LedOff";
}