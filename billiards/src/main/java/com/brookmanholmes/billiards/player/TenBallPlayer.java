package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.player.interfaces.EarlyWins;
import com.brookmanholmes.billiards.player.interfaces.EarlyWinsImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class TenBallPlayer extends AbstractPlayer implements EarlyWins {
    EarlyWins earlyWins;

    public TenBallPlayer(String name) {
        super(name);
        earlyWins = new EarlyWinsImp();
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof EarlyWins) {
            earlyWins.addEarlyWins(((EarlyWins) player).getEarlyWins());
        }
    }

    @Override
    public void addEarlyWin() {
        earlyWins.addEarlyWin();
    }

    @Override
    public int getEarlyWins() {
        return earlyWins.getEarlyWins();
    }

    @Override
    public void addEarlyWins(int wins) {
        earlyWins.addEarlyWins(wins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TenBallPlayer player = (TenBallPlayer) o;

        return earlyWins.equals(player.earlyWins);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + earlyWins.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TenBallPlayer{" +
                "earlyWins=" + earlyWins +
                "\n " + super.toString() +
                '}';
    }
}
