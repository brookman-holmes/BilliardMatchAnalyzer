package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolRunsBinder extends ShootingBinder {
    public String playerMax = "0", opponentMax = "0";

    public String playerMean = "0.0", opponentMean = "0.0";
    public String playerMedian = "0.0", opponentMedian = "0.0";

    StraightPoolRunsBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        super(player, opponent, title, expanded);
        showCard = true;

        if (player instanceof StraightPoolPlayer && opponent instanceof StraightPoolPlayer) {
            playerMax = Integer.toString(((StraightPoolPlayer) player).getHighRun());
            opponentMax = Integer.toString(((StraightPoolPlayer) opponent).getHighRun());

            playerMean = ((StraightPoolPlayer) player).getAverageRunLength();
            opponentMean = ((StraightPoolPlayer) opponent).getAverageRunLength();

            playerMedian = ((StraightPoolPlayer) player).getMedianRunLength();
            opponentMedian = ((StraightPoolPlayer) opponent).getMedianRunLength();
        }
    }

    @Override
    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof StraightPoolPlayer && opponent instanceof StraightPoolPlayer) {
            playerMax = Integer.toString(((StraightPoolPlayer) player).getHighRun());
            opponentMax = Integer.toString(((StraightPoolPlayer) opponent).getHighRun());

            playerMean = ((StraightPoolPlayer) player).getAverageRunLength();
            opponentMean = ((StraightPoolPlayer) opponent).getAverageRunLength();

            playerMedian = ((StraightPoolPlayer) player).getMedianRunLength();
            opponentMedian = ((StraightPoolPlayer) opponent).getMedianRunLength();
        }

        super.update(player, opponent);
    }
}
