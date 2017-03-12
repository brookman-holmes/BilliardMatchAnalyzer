package com.brookmanholmes.bma.ui.matchinfo;


import android.support.annotation.Nullable;
import android.util.Log;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class MatchOverviewBinder extends BindingAdapter {
    public String playerWinPct = "0", opponentWinPct = "0";

    public int playerGamesWon = 0, opponentGamesWon = 0;
    public int playerGamesPlayed = 0, opponentGamesPlayed = 0;

    public String playerTsp = defaultPct, opponentTsp = defaultPct;

    public int playerShotsSuccess = 0, opponentShotsSuccess = 0;
    public int playerTotalShots = 0, opponentTotalShots = 0;

    public String playerTotalFouls = "0", opponentTotalFouls = "0";

    public String playerAggRating = defaultPct, opponentAggRating = defaultPct;

    public boolean apaTitle;

    public boolean showWinPctLayout;

    MatchOverviewBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, !gameType.isStraightPool());

        apaTitle = useGameTotal(gameType);
        helpLayout = R.layout.dialog_help_match_overview;
        showWinPctLayout = !gameType.isApa();
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerWinPct = pctf.format(player.getWinPct());
        opponentWinPct = pctf.format(opponent.getWinPct());

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();

        if (useGameTotal(player.getGameType())) {
            playerGamesPlayed = player.getGameTotal();
            opponentGamesPlayed = opponent.getGameTotal();
            Log.i(TAG, "update: " + (player.getGameTotal() == player.getPointsNeeded()));
        } else {
            playerGamesPlayed = player.getRank();
            opponentGamesPlayed = opponent.getRank();
            Log.i(TAG, "update: " + (player.getRank() == player.getPointsNeeded()));
        }

        playerTsp = pctf.format(player.getTrueShootingPct());
        opponentTsp = pctf.format(opponent.getTrueShootingPct());

        playerShotsSuccess = player.getShotsSucceededOfAllTypes();
        opponentShotsSuccess = opponent.getShotsSucceededOfAllTypes();
        playerTotalShots = player.getShotAttemptsOfAllTypes();
        opponentTotalShots = opponent.getShotAttemptsOfAllTypes();

        playerTotalFouls = Integer.toString(player.getTotalFouls());
        opponentTotalFouls = Integer.toString(opponent.getTotalFouls());

        playerAggRating = pctf.format(player.getAggressivenessRating());
        opponentAggRating = pctf.format(opponent.getAggressivenessRating());

        notifyChange();
    }


    public int highlightShooting() {
        return compare(playerTsp, opponentTsp);
    }

    public int highlightFouls() {
        return compare(playerTotalFouls, opponentTotalFouls) * -1;
    }

    private boolean useGameTotal(GameType gameType) {
        return (gameType.isApa()) || (gameType == GameType.ALL);
    }
}
