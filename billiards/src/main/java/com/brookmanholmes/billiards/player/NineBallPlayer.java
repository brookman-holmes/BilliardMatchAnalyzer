package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFouls;
import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFoulsImp;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreakImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class NineBallPlayer extends AbstractPlayer implements ConsecutiveFouls, WinsOnBreak {
    WinsOnBreak winsOnBreak;
    ConsecutiveFouls consecutiveFouls;

    public NineBallPlayer(String name) {
        super(name);
        winsOnBreak = new WinsOnBreakImp();
        consecutiveFouls = new ConsecutiveFoulsImp();
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof WinsOnBreak) {
            winsOnBreak.addWinsOnBreak(((WinsOnBreak) player).getWinsOnBreak());
            winsOnBreak.addEarlyWins(((WinsOnBreak) player).getEarlyWins());
        }

        if (player instanceof ConsecutiveFouls) {
            consecutiveFouls.addFouls(((ConsecutiveFouls) player).getFouls());
        }
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
    public void addWinsOnBreak(int wins) {
        winsOnBreak.addWinsOnBreak(wins);
    }

    @Override
    public void addEarlyWins(int wins) {
        winsOnBreak.addEarlyWins(wins);
    }

    @Override
    public void addFouls(int fouls) {
        consecutiveFouls.addFouls(fouls);
    }
}
