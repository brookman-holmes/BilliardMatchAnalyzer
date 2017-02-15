package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolRunsBinder extends ShootingBinder {
    public boolean isStraightPoolPlayer = false;

    public String playerMax = "0", opponentMax = "0";

    public String playerMean = "0.0", opponentMean = "0.0";
    public String playerMedian = "0.0", opponentMedian = "0.0";

    StraightPoolRunsBinder(Player player, Player opponent, String title, boolean expanded) {
        super(player, opponent, title, expanded);
        showCard = player.getGameType().isStraightPool();
        isStraightPoolPlayer = player.getGameType().isStraightPool();

        update(player, opponent);
    }

    @Override
    public void update(Player player, Player opponent) {
        if (player.getGameType().isStraightPool()) {
            playerMax = Integer.toString(player.getHighRun());
            opponentMax = Integer.toString(opponent.getHighRun());

            playerMean = avgf.format(player.getAverageRunLength());
            opponentMean = avgf.format(opponent.getAverageRunLength());

            playerMedian = avgf.format(player.getMedianRunLength());
            opponentMedian = avgf.format(opponent.getMedianRunLength());
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
