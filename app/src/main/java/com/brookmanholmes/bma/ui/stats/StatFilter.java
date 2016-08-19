package com.brookmanholmes.bma.ui.stats;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.AmericanRotationPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
import com.brookmanholmes.billiards.player.TenBallPlayer;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public class StatFilter {
    private final String[] dates;
    private final String[] gameTypes;
    private String opponent;
    private int selectedDate = 0;
    private int selectedGameType = 0;
    private Date date;

    public StatFilter(String opponent, String[] gameTypes, String[] dates) {
        this.opponent = opponent;
        this.gameTypes = gameTypes;
        this.dates = dates;

        setDate(dates[0]);
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getGameType() {
        return gameTypes[selectedGameType];
    }

    public void setGameType(String gameType) {
        for (int i = 0; i < gameTypes.length; i++) {
            if (gameTypes[i].equals(gameType))
                selectedGameType = i;
        }
    }

    private void setTimeToStartOfDate(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
    }

    private boolean isDateInRange(Date date) {
        return date.after(this.date);
    }

    private boolean isPlayerOfGameType(AbstractPlayer player) {
        if (selectedGameType == 0)
            return true;
        else if (selectedGameType == 1)
            return player instanceof ApaEightBallPlayer;
        else if (selectedGameType == 2)
            return player instanceof ApaNineBallPlayer;
        else if (selectedGameType == 3)
            return player instanceof TenBallPlayer;
        else if (selectedGameType == 4)
            return player instanceof EightBallPlayer;
        else if (selectedGameType == 5)
            return player instanceof NineBallPlayer;
        else if (selectedGameType == 6)
            return player instanceof StraightPoolPlayer;
        else if (selectedGameType == 7)
            return player instanceof AmericanRotationPlayer;
        else return false;
    }

    public String getDate() {
        return dates[selectedDate];
    }

    public void setDate(String date) {
        Calendar cal = Calendar.getInstance();
        if (date.equals(dates[1])) {
            selectedDate = 1;
        } else if (date.equals(dates[2])) {
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 7);
            selectedDate = 2;
        } else if (date.equals(dates[3])) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            selectedDate = 3;
        } else if (date.equals(dates[4])) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 3);
            selectedDate = 4;
        } else if (date.equals(dates[5])) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 6);
            selectedDate = 5;
        } else {
            cal.set(Calendar.YEAR, 1900);
            cal.set(Calendar.DAY_OF_YEAR, 1);
            selectedDate = 0;
        }

        setTimeToStartOfDate(cal);
        this.date = cal.getTime();
    }

    public boolean isPlayerQualified(AbstractPlayer player) {
        return isPlayerNameCorrect(player.getName()) &&
                isDateInRange(player.getMatchDate()) &&
                isPlayerOfGameType(player);
    }

    private boolean isPlayerNameCorrect(String name) {
        return opponent.equals("All opponents") || opponent.equals(name);
    }
}
