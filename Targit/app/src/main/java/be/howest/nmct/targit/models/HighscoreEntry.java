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
}
