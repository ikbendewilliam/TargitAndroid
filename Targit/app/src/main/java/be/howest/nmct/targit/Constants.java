package be.howest.nmct.targit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// The constants that we use
public abstract class Constants {
    // Tag used in logging
    public static final String TAG = "Android - Arduino";
    // Tag used to debug the connection
    public static final String TAG_MESSAGE = "Arduino Connection";

    // All devices we need to search
    // NOTE: If you want to connect to an extra button, just add its name in this array. That's all
    public static final String[] DEVICE_NAMES = {
            "TARGIT-01",
            "TARGIT-02",
            "TARGIT-03",
            "TARGIT-04",
            "TARGIT-05"
    };

    // Settings status
    public static final int ITEM_VIEW_TYPE_HEADER = 1;
    public static final int ITEM_VIEW_TYPE_ITEM = 2; // to differentiate header and items

    // commands for communitcation with arduino
    public static final String COMMAND_END = ";"; // the end of every command send
    public static final String COMMAND_LED_ON = "LedOn"; // turn the led on the device on
    public static final String COMMAND_LED_OFF = "LedOff"; // turn the led on the device off
    public static final String COMMAND_LED_FLASH_SLOW = "LedFlash(500)"; // Make the led turn on every 500 ms and off every 500 ms
    public static final String COMMAND_LED_FLASH_MEDIUM = "LedFlash(250)"; // you can enter any value between the brackets
    public static final String COMMAND_LED_FLASH_FAST = "LedFlash(100)"; // these don't need to be 3 digits

    // commands incoming from arduino
    public static final String COMMAND_INCOMING_BUTTON_PRESSED = "BtnPressed"; // The command that the arduino sends when the button is pressed
    public static final String COMMAND_INCOMING_BUTTON_RELEASED = "BtnReleased"; // The command that the arduino sends when the button is released

    // Highscore
    public static String EXTRA_SCORE = "score";
    public static String EXTRA_CATEGORY = "category";

    // Ingame
    public static String EXTRA_GAME = "gameMode";
    public static String EXTRA_GAME_SMASHIT = "smashit";
    public static String EXTRA_GAME_ZENIT = "zenit";
    public static String EXTRA_GAME_MEMORIT = "memorit";
    public static int STEP_TIME = 10; // The time between frames (in ms)
    // Smashit
    public static String EXTRA_DIFFICULTY = "difficulty";
    public static String EXTRA_DIFFICULTY_EASY = "easy";
    public static String EXTRA_DIFFICULTY_MEDIUM = "medium";
    public static String EXTRA_DIFFICULTY_HARD = "hard";
    public static int TIME_TO_PRESS_MAX_EASY = 8000;
    public static int TIME_TO_PRESS_MAX_MEDIUM = 6000;
    public static int TIME_TO_PRESS_MAX_HARD = 3000;
    public static int TIME_TO_PRESS_MIN_EASY = 3000;
    public static int TIME_TO_PRESS_MIN_MEDIUM = 2000;
    public static int TIME_TO_PRESS_MIN_HARD = 1000;
    // Zenit
    public static String EXTRA_DURATION = "duration";
    public static int EXTRA_DURATION_SHORT = 30; // WARNING: changing these times discards the previous highscore
    public static int EXTRA_DURATION_MEDIUM = 60; // WARNING: changing these times discards the previous highscore
    public static int EXTRA_DURATION_LONG = 90; // WARNING: changing these times discards the previous highscore
    // Memorit
    public static String EXTRA_LIVES = "lives";
    public static int EXTRA_LIVES_MANY = 5; // WARNING: changing these lives discards the previous highscore
    public static int EXTRA_LIVES_MEDIUM = 3; // WARNING: changing these lives discards the previous highscore
    public static int EXTRA_LIVES_FEW = 1; // WARNING: changing these lives discards the previous highscore
    public static int WAIT_TIME_MAX = 2000; // The time in ms the last button lits up
    public static int WAIT_TIME_MIN = 200; // The min time in ms each button has to lit up
    public static float WAIT_TIME_RATE = 0.8f; // The time increased with every lit up button

    // Info game
    public static String EXTRA_GAMEMODE = "gameMode";
    public static String EXTRA_GAMEMODE_SMASHIT = "smashit";
    public static String EXTRA_GAMEMODE_ZENIT = "zenit";
    public static String EXTRA_GAMEMODE_MEMORIT = "memorit";
    public static int MIN_DEVICES_CONNETED = 2;

    // Helpstepfragment
    public static final String EXTRA_HELP_TEXT = "helptext";
    public static final String EXTRA_HELP_IMAGE = "helpimage";

    // Textsize
    public static final double TEXT_SIZE = 8000;

    // HighscoreFragment
    public static final int BUTTON_EASY = 1;
    public static final int BUTTON_MEDIUM = 2;
    public static final int BUTTON_HARD = 3;
}