package com.brookmanholmes.bma.ui.matchinfo;


import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class MatchOverviewBinder extends BindingAdapter {
    public String playerWinPct, opponentWinPct;

    public int playerGamesWon, opponentGamesWon;
    public int playerGamesPlayed, opponentGamesPlayed;

    public String playerTsp, opponentTsp;

    public int playerShotsSuccess, opponentShotsSuccess;
    public int playerTotalShots, opponentTotalShots;

    public String playerTotalFouls, opponentTotalFouls;

    public String playerAggRating, opponentAggRating;

    public boolean apaTitle = false;

    public boolean showWinPctLayout;

    MatchOverviewBinder(Player player, Player opponent, String title, boolean expanded) {
        super(expanded, !(player.getGameType().isStraightPool()));

        if (useGameTotal(player)) {
            apaTitle = true;
        }

        update(player, opponent);

        this.title = title;
        helpLayout = R.layout.dialog_help_match_overview;
        showWinPctLayout = !(player.getGameType().isApa());
    }

    public void update(Player player, Player opponent) {
        playerWinPct = pctf.format(player.getWinPct());
        opponentWinPct = pctf.format(opponent.getWinPct());

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();

        if (useGameTotal(player)) {
            playerGamesPlayed = player.getGameTotal();
            opponentGamesPlayed = opponent.getGameTotal();
        } else {
            playerGamesPlayed = player.getRank();
            opponentGamesPlayed = opponent.getRank();
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

    private boolean useGameTotal(Player player) {
        return (player.getGameType().isApa()) ||
                (player.getGameType() == GameType.ALL);
    }
}
