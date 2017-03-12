package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolRunsBinder extends ShootingBinder {
    public boolean isStraightPoolPlayer = false;

    public String playerMax = "0", opponentMax = "0";

    public String playerMean = defaultAvg, opponentMean = defaultAvg;
    public String playerMedian = defaultAvg, opponentMedian = defaultAvg;

    StraightPoolRunsBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, gameType.isStraightPool() || gameType == GameType.ALL);
        isStraightPoolPlayer = gameType.isStraightPool();
    }

    @Override
    public void update(Player player, Player opponent, GameStatus gameStatus) {
        if (gameStatus != null && gameStatus.gameType.isStraightPool()) {
            playerMax = Integer.toString(player.getHighRun());
            opponentMax = Integer.toString(opponent.getHighRun());

            playerMean = avgf.format(player.getAverageRunLength());
            opponentMean = avgf.format(opponent.getAverageRunLength());

            playerMedian = avgf.format(player.getMedianRunLength());
            opponentMedian = avgf.format(opponent.getMedianRunLength());
        }

        super.update(player, opponent, gameStatus);
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
