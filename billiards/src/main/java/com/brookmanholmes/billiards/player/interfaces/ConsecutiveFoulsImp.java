package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ConsecutiveFoulsImp implements ConsecutiveFouls {
    int consecutiveFouls = 0;

    public ConsecutiveFoulsImp() {
    }

    @Override
    public void addFoul() {
        consecutiveFouls++;
    }

    @Override
    public void removeFouls() {
        consecutiveFouls = 0;
    }

    @Override
    public int getFouls() {
        return consecutiveFouls;
    }

    @Override
    public void addFouls(int fouls) {
        if (fouls == 0)
            removeFouls();
        else
            consecutiveFouls += fouls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsecutiveFoulsImp that = (ConsecutiveFoulsImp) o;

        return consecutiveFouls == that.consecutiveFouls;

    }

    @Override
    public int hashCode() {
        return consecutiveFouls;
    }

    @Override
    public String toString() {
        return "ConsecutiveFoulsImp{" +
                "consecutiveFouls=" + consecutiveFouls +
                '}';
    }
}
