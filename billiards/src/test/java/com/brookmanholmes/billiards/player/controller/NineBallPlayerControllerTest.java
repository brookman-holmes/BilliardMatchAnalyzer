package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.TurnBuilder;
import com.brookmanholmes.billiards.player.NineBallPlayer;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class NineBallPlayerControllerTest extends AbstractPlayerControllerTest<NineBallPlayer> {
    @Override
    public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new NineBallController("", "");
        actualPlayer = new NineBallPlayer("");
        expectedPlayer = new NineBallPlayer("");
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Test
    public void gameBallOnBreakGivesWinOnBreakAndEarlyWin() {
        playerController.turn = turnBuilder.breakBalls(game.getGameStatus().GAME_BALL).win();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addEarlyWin();
        expectedPlayer.addWinOnBreak();
        expectedPlayer.addBreakShot(1, false, false);

        assertThat(actualPlayer, is(expectedPlayer));
    }

    @Override
    Turn breakAndRunTurn() {
        return turnBuilder.breakBalls(1, 3).madeBalls(2, 4, 5, 6, 7, 8, 9).win();
    }

    @Override
    Turn tableRunTurn() {
        return turnBuilder.madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9).win();
    }

    @Override
    Turn fourBallRunTurn() {
        return turnBuilder.offTable(1, 2, 3).madeBalls(4, 5, 6, 7, 8, 9).win();
    }

    @Override
    NineBallPlayer getBreakAndRunPlayer() {
        NineBallPlayer player = new NineBallPlayer("");
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(7, false);

        return player;
    }

    @Override
    NineBallPlayer getBlankPlayer() {
        return new NineBallPlayer("");
    }

    @Override
    Turn failedRunOutTurn() {
        return turnBuilder.breakBalls(1, 2).madeBalls(3).safetyMiss();
    }

    @Override
    NineBallPlayer fourBallRunOutPlayer() {
        NineBallPlayer player = new NineBallPlayer("");
        player.addShootingBallsMade(6, false);
        player.addGameWon();
        player.addFourBallRun();
        player.addShootingTurn();

        return player;
    }

    @Override
    NineBallPlayer failedRunOutPlayer() {
        NineBallPlayer player = new NineBallPlayer("");

        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(1, false);
        player.addSafetyAttempt(false);
        player.addShootingTurn();

        return player;
    }
}
