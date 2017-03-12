package com.brookmanholmes.bma.ui.stats;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public class StatFilter {
    private final String[] dates;
    private final String[] gameTypeStrings;
    private String opponent;
    private int selectedDate = 0;
    private int selectedGameType = 0;
    private Date date;

    public StatFilter(String opponent, String[] gameTypeStrings, String[] dates) {
        this.opponent = opponent;
        this.gameTypeStrings = gameTypeStrings;
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
        return gameTypeStrings[selectedGameType];
    }

    public void setGameType(String gameType) {
        for (int i = 0; i < gameTypeStrings.length; i++) {
            if (gameTypeStrings[i].equals(gameType))
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

    public String getDate() {
        return dates[selectedDate];
    }

    public void setDate(String date) {
        Calendar cal = Calendar.getInstance();
        if (date.equals(dates[1])) {
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
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

    private boolean isGameType(GameType gameType) {
        switch (selectedGameType) {
            case 0:
                return true;
            case 1:
                return gameType.isApa8Ball();
            case 2:
                return gameType.isApa9Ball();
            case 3:
                return gameType.isBca8Ball();
            case 4:
                return gameType.isBca9Ball();
            case 5:
                return gameType.is10Ball();
            case 6:
                return gameType.isStraightPool();
            default:
                throw new IllegalArgumentException("No such mapping between " + gameType + " and gameTypeStrings[selectedGameType]");
        }
    }

    private boolean isOpponentName(Match match) {
        return opponent.equals("All opponents") || opponent.equals(match.getPlayer().getName())
                || opponent.equals(match.getOpponent().getName());
    }

    public boolean isMatchQualified(Match match) {
        return isDateInRange(match.getCreatedOn()) && isGameType(match.getGameStatus().gameType)
                && isOpponentName(match);
    }
}
