package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class StraightPoolPlayer extends AbstractPlayer implements ConsecutiveFouls {
    ConsecutiveFouls consecutiveFouls;

    public StraightPoolPlayer(String name) {
        super(name);
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
}
