package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFouls;
import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFoulsImp;
import com.brookmanholmes.billiards.player.interfaces.EarlyWins;
import com.brookmanholmes.billiards.player.interfaces.EarlyWinsImp;

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
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof EarlyWins) {
            earlyWins.addEarlyWins(((EarlyWins) player).getEarlyWins());
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
    public void addFouls(int fouls) {
        consecutiveFouls.addFouls(fouls);
    }
}
