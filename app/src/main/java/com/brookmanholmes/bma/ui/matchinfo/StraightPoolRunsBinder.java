package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IStraightPool;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolRunsBinder extends ShootingBinder {
    public boolean isStraightPoolPlayer = false;

    public String playerMax = "0", opponentMax = "0";

    public String playerMean = "0.0", opponentMean = "0.0";
    public String playerMedian = "0.0", opponentMedian = "0.0";

    StraightPoolRunsBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        super(player, opponent, title, expanded);
        showCard = player instanceof IStraightPool;
        isStraightPoolPlayer = player instanceof StraightPoolPlayer;

        update(player, opponent);
    }

    @Override
    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof IStraightPool && opponent instanceof IStraightPool) {
            playerMax = Integer.toString(((IStraightPool) player).getHighRun());
            opponentMax = Integer.toString(((IStraightPool) opponent).getHighRun());

            playerMean = avgf.format(((IStraightPool) player).getAverageRunLength());
            opponentMean = avgf.format(((IStraightPool) opponent).getAverageRunLength());

            playerMedian = avgf.format(((IStraightPool) player).getMedianRunLength());
            opponentMedian = avgf.format(((IStraightPool) opponent).getMedianRunLength());
        }

        super.update(player, opponent);
    }

    public int highlightMax() {
        return compare(playerMax, opponentMax);
    }

    public int highlightMedian() {
        return compare(playerMedian, opponentMedian);
    }

    public int highlightAvg() {
        return compare(playerMean, opponentMean);
    }

    public boolean showFoulTotal() {
        return expanded && isStraightPoolPlayer;
    }
}
