package com.brookmanholmes.bma.ui.stats;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public class StatFilter {
    String opponent;

    public StatFilter(String opponent) {
        this.opponent = opponent;
    }


    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
