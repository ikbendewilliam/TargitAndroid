package be.howest.nmct.targit.models;

/**
 * Created by ikben on 09/06/2017.
 */

public class HighscoreEntry {
    private String nickname;
    private int score;

    public HighscoreEntry(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public HighscoreEntry(String fromString) {
        if (!fromString.contains(";"))
            throw new RuntimeException("fromString must contain a ';'");
        int lastIndex = fromString.lastIndexOf(";");
        nickname = fromString.substring(0, lastIndex);
        score = Integer.parseInt(fromString.substring(lastIndex + 1, fromString.length()));
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return nickname + ";" + score;
    }
}
