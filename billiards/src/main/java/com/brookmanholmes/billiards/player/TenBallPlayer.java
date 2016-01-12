package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class TenBallPlayer extends AbstractPlayer implements ConsecutiveFouls, EarlyWins {
    EarlyWins earlyWins;
    ConsecutiveFouls consecutiveFouls;

    public TenBallPlayer(String name) {
        super(name);
        earlyWins = new EarlyWinsImp();
        consecutiveFouls = new ConsecutiveFoulsImp();
    }

    @Override
    public void addFoul() {
        consecutiveFouls.addFoul();
    }

    @Override
    public void removeFouls() {
        consecutiveFouls.removeFouls();
    }

    @Override
    public int getFouls() {
        return consecutiveFouls.getFouls();
    }

    @Override
    public void addEarlyWin() {
        earlyWins.addEarlyWin();
    }

    @Override
    public int getEarlyWins() {
        return earlyWins.getEarlyWins();
    }
}
