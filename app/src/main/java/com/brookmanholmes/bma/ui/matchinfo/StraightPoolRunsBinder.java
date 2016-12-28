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

        if (player instanceof IStraightPool && opponent instanceof IStraightPool) {
            playerMax = Integer.toString(((IStraightPool) player).getHighRun());
            opponentMax = Integer.toString(((IStraightPool) opponent).getHighRun());

            playerMean = ((IStraightPool) player).getAverageRunLength();
            opponentMean = ((IStraightPool) opponent).getAverageRunLength();

            playerMedian = ((IStraightPool) player).getMedianRunLength();
            opponentMedian = ((IStraightPool) opponent).getMedianRunLength();
        }
    }

    @Override
    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof IStraightPool && opponent instanceof IStraightPool) {
            playerMax = Integer.toString(((IStraightPool) player).getHighRun());
            opponentMax = Integer.toString(((IStraightPool) opponent).getHighRun());

            playerMean = ((IStraightPool) player).getAverageRunLength();
            opponentMean = ((IStraightPool) opponent).getAverageRunLength();

            playerMedian = ((IStraightPool) player).getMedianRunLength();
            opponentMedian = ((IStraightPool) opponent).getMedianRunLength();
        }

        super.update(player, opponent);
    }

    public int highlightMax() {
        int playerVal = Integer.valueOf(this.playerMax);
        int opponentVal = Integer.valueOf(this.opponentMax);

        return Integer.compare(playerVal, opponentVal);
    }

    public int highlightMedian() {
        double playerVal = Double.valueOf(this.playerMedian);
        double opponentVal = Double.valueOf(this.opponentMedian);

        return Double.compare(playerVal, opponentVal);
    }

    public int highlightAvg() {
        double playerVal = Double.valueOf(this.playerMean);
        double opponentVal = Double.valueOf(this.opponentMean);

        return Double.compare(playerVal, opponentVal);
    }

    public boolean showFoulTotal() {
        return expanded && isStraightPoolPlayer;
    }
}
