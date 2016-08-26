package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class EightBallPlayerControllerTest extends AbstractPlayerControllerTest<EightBallPlayer> {
    @Override public void setUp() {
        game = Game.newGame(GameType.BCA_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new EightBallController("", "", 0, 0);
        actualPlayer = new EightBallPlayer("");
        expectedPlayer = new EightBallPlayer("");
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override public void maximumMakeableBallsReturnsMaxBalls() {
        assertThat(playerController.getMaximumBallsMakeable(), is(8));
    }

    @Override ITurn breakAndRunTurn() {
        return turnBuilder.breakBalls(1, 3, 9).madeBalls(10, 11, 12, 13, 14, 15, 8).win();
    }

    @Override ITurn tableRunTurn() {
        return turnBuilder.madeBalls(1, 2, 3, 4, 5, 6, 7, 8).win();
    }

    @Override ITurn fourBallRunTurn() {
        return turnBuilder.offTable(1, 2, 3).madeBalls(4, 5, 6, 7, 8).win();
    }

    @Override EightBallPlayer getBreakAndRunPlayer() {
        EightBallPlayer player = new EightBallPlayer("");
        player.addBreakShot(3, true, false);
        player.addShootingBallsMade(7, false);

        return player;
    }

    @Override ITurn failedRunOutTurn() {
        return turnBuilder.breakBalls(1, 2, 3).miss();
    }

    @Override EightBallPlayer fourBallRunOutPlayer() {
        EightBallPlayer player = new EightBallPlayer("");
        player.addShootingBallsMade(5, false);
        player.addGameWon();
        player.addFourBallRun();

        return player;
    }

    @Override EightBallPlayer failedRunOutPlayer() {
        EightBallPlayer player = new EightBallPlayer("");
        player.addBreakShot(3, false, false);
        player.addShootingBallsMade(0, false);
        player.addShootingMiss();

        return player;
    }

    @Override EightBallPlayer getBlankPlayer() {
        return new EightBallPlayer("");
    }
}
