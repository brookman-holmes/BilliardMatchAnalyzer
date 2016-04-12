package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreakImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class NineBallPlayer extends AbstractPlayer implements WinsOnBreak {
    WinsOnBreak winsOnBreak;

    public NineBallPlayer(String name) {
        super(name);
        winsOnBreak = new WinsOnBreakImp();
    }

    @Override public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof WinsOnBreak) {
            winsOnBreak.addWinsOnBreak(((WinsOnBreak) player).getWinsOnBreak());
            winsOnBreak.addEarlyWins(((WinsOnBreak) player).getEarlyWins());
        }

    }

    @Override public void addEarlyWin() {
        winsOnBreak.addEarlyWin();
    }

    @Override public int getEarlyWins() {
        return winsOnBreak.getEarlyWins();
    }

    @Override public void addWinOnBreak() {
        winsOnBreak.addWinOnBreak();
    }

    @Override public int getWinsOnBreak() {
        return winsOnBreak.getWinsOnBreak();
    }

    @Override public void addWinsOnBreak(int wins) {
        winsOnBreak.addWinsOnBreak(wins);
    }

    @Override public void addEarlyWins(int wins) {
        winsOnBreak.addEarlyWins(wins);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NineBallPlayer player = (NineBallPlayer) o;

        return winsOnBreak.equals(player.winsOnBreak);

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + winsOnBreak.hashCode();
        return result;
    }

    @Override public String toString() {
        return "NineBallPlayer{" +
                "winsOnBreak=" + winsOnBreak +
                "} " + super.toString();
    }
}
