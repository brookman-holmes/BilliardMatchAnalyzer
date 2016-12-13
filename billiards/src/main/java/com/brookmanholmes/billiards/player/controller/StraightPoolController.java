package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.StraightPoolPlayer;

import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY_ERROR;

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
        if (turn.getTurnEnd() == SAFETY)
            player.addSafety(gameStatus.opponentPlayedSuccessfulSafe, turn.getShootingBallsMade());
        else if (turn.getTurnEnd() == SAFETY_ERROR)
            player.addSafetyAttempt(turn.isFoul());

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.getShootingBallsMade() > 0)
            player.addSafetyEscape();

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.isFoul()) {
            player.addSafetyForcedError();
        }
    }

    @Override
    void addShootingStats(StraightPoolPlayer player) {
        if (turn.getTurnEnd() == MISS)
            player.addShootingMiss();

        if (addTurnToPlayer())
            player.addShootingBallsMade(turn.getShootingBallsMade(), turn.isFoul());

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
