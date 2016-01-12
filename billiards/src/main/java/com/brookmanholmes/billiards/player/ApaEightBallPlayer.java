package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaEightBallPlayer extends AbstractPlayer implements Apa {
    WinsOnBreak winsOnBreak;
    int rank;

    public ApaEightBallPlayer(String name, int rank) {
        super(name);
        winsOnBreak = new WinsOnBreakImp();
        this.rank = rank;
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
    public int getMatchPoints(int opponentScore) {
        return 0;
    }

    @Override
    public int getRank() {
        return rank;
    }
}
