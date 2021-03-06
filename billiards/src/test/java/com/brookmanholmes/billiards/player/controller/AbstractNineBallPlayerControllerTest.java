package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IWinsOnBreak;
import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public abstract class AbstractNineBallPlayerControllerTest<T extends AbstractPlayer & IWinsOnBreak> extends AbstractPlayerControllerTest<T> {
    @Test
    public void gameBallOnBreakGivesWinOnBreakAndEarlyWin() {
        playerController.turn = turnBuilder.breakBalls(game.getGameStatus().GAME_BALL).win();
        playerController.gameStatus = game.getGameStatus();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addWinOnBreak();
        expectedPlayer.addBreakShot(1, false, false);

        assertThat(playerController.isGameOver(), is(true));
        assertThat(actualPlayer, is(expectedPlayer));
    }

    @Override ITurn breakAndRunTurn() {
        return turnBuilder.breakBalls(1, 3).madeBalls(2, 4, 5, 6, 7, 8, 9).win();
    }

    @Override ITurn tableRunTurn() {
        return turnBuilder.madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9).win();
    }

    @Override ITurn fourBallRunTurn() {
        return turnBuilder.offTable(1, 2, 3, 4, 5).madeBalls(6, 7, 8, 9).win();
    }

    @Override T getBreakAndRunPlayer() {
        T player = getBlankPlayer();
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(7, false);

        return player;
    }

    @Override ITurn failedRunOutTurn() {
        return turnBuilder.breakBalls(1, 2).madeBalls(3).safetyMiss();
    }

    @Override T fourBallRunOutPlayer() {
        T player = getBlankPlayer();
        player.addShootingBallsMade(6, false);
        player.addGameWon();
        player.addFiveBallRun();

        return player;
    }

    @Override T failedRunOutPlayer() {
        T player = getBlankPlayer();

        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(1, false);
        player.addSafetyAttempt(false);

        return player;
    }
}
