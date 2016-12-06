package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.StraightPoolPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for straight pool
 */
class StraightPoolController extends PlayerController<StraightPoolPlayer> {
    StraightPoolController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    public StraightPoolPlayer newPlayer() {
        return new StraightPoolPlayer(playerName, playerRank);
    }

    @Override
    public StraightPoolPlayer newOpponent() {
        return new StraightPoolPlayer(opponentName, opponentRank);
    }

    @Override
    void addSafetyStats(StraightPoolPlayer player) {
        super.addSafetyStats(player);
    }

    @Override
    void addShootingStats(StraightPoolPlayer player) {
        super.addShootingStats(player);

        if (gameStatus.currentPlayerConsecutiveFouls >= 2 && turn.isSeriousFoul()) {
            player.addSeriousFoul();
        }
    }

    @Override
    void addBreakingStats(StraightPoolPlayer player) {
        if (turn.getShootingBallsMade() == 0)
            player.addBreakShot(0, false, turn.isFoul());
    }

    @Override
    void addRunOutStats(StraightPoolPlayer player) {

    }

    @Override
    boolean isGameOver() {
        return false;
    }


}
