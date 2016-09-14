package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 5/9/2016.
 */
public class CompPlayer extends AbstractPlayer implements IWinsOnBreak {
    // TODO: 8/26/2016 create tests for this whole class
    private int earlyWins = 0;
    private int winsOnBreak = 0;

    public CompPlayer(String name, int rank) {
        super(name, rank);
    }

    public CompPlayer(String name) {
        super(name);
    }

    @Override public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IEarlyWins) {
            earlyWins += ((IEarlyWins) player).getEarlyWins();
        }

        if (player instanceof IWinsOnBreak) {
            winsOnBreak += ((IWinsOnBreak) player).getWinsOnBreak();
        }
    }

    @Override public void addWinOnBreak() {
        winsOnBreak++;
    }

    @Override public int getWinsOnBreak() {
        return winsOnBreak;
    }

    @Override public void addWinsOnBreak(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        winsOnBreak += wins;
    }

    @Override public void addEarlyWin() {
        earlyWins++;
    }

    @Override public int getEarlyWins() {
        return earlyWins;
    }

    @Override public void addEarlyWins(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        earlyWins += wins;
    }
}
