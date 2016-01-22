package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;
import com.brookmanholmes.billiards.player.interfaces.Apa;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreakImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaNineBallPlayer extends AbstractPlayer implements Apa {
    WinsOnBreak winsOnBreak;
    int rank;
    int points = 0;

    public ApaNineBallPlayer(String name, int rank) {
        super(name);
        this.rank = rank;
        winsOnBreak = new WinsOnBreakImp();
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void addEarlyWin() {
        winsOnBreak.addEarlyWin();
    }

    @Override
    public int getEarlyWins() {
        return winsOnBreak.getEarlyWins();
    }

    @Override
    public void addWinOnBreak() {
        winsOnBreak.addWinOnBreak();
    }

    @Override
    public int getWinsOnBreak() {
        return winsOnBreak.getWinsOnBreak();
    }

    @Override
    public int getMatchPoints(int opponentScore, int opponentRank) {
        if (points == ApaRaceToHelper.apa9BallRaceTo(rank))
            return 20 - ApaRaceToHelper.getMinimumMatchPointsEarned(opponentRank, opponentScore);
        else return ApaRaceToHelper.getMinimumMatchPointsEarned(rank, points);
    }

    @Override
    public int getRank() {
        return rank;
    }
}
