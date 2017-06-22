package be.howest.nmct.targit.models;

// Class to represent an entry in the highscoreList
public class HighscoreEntry {
    private String nickname; // Player name
    private int score; // player score

    // Constructor
    // @param nickname: the name of this player
    // @param score: the score of the player
    public HighscoreEntry(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    // Constructor
    // @param fromString: The string from the file
    public HighscoreEntry(String fromString) {
        // Check if this string is legit
        if (!fromString.contains(";")) {

            //remove this when done debugging
            // Otherwise throw an error
            //throw new RuntimeException("fromString must contain a ';'");
        }
        // get the last ; so the program doesn't break when a user enters "wil;999999999"
        int lastIndex = fromString.lastIndexOf(";");
        // set the name to the first part
        nickname = fromString.substring(0, lastIndex);
        // Set the score to the last part of the string
        score = Integer.parseInt(fromString.substring(lastIndex + 1, fromString.length()));
    }

    // @return int: the score of this player
    public int getScore() {
        return score;
    }

    // @return String: the name of this player
    public String getNickname() {
        return nickname;
    }

    // @return String: a string to be used with the constructor
    @Override
    public String toString() {
        return nickname + ";" + score;
    }
}
