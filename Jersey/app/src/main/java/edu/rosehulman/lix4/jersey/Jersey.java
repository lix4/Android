package edu.rosehulman.lix4.jersey;

/**
 * Created by phillee on 6/10/2017.
 */

public class Jersey {
    private String playerName;
    private int PlayerNumber;
    private boolean isRed;

    public Jersey() {
        this.playerName = "Android";
        this.PlayerNumber = 17;
        this.isRed = true;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerNumber() {
        return PlayerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        PlayerNumber = playerNumber;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }
}
