package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFouls;
import com.brookmanholmes.billiards.player.interfaces.ConsecutiveFoulsImp;
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
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

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
    public void addFouls(int fouls) {
        consecutiveFouls.addFouls(fouls);
    }
}
