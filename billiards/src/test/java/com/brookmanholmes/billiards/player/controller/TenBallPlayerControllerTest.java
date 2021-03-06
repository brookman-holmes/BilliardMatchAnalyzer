package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class TenBallPlayerControllerTest extends AbstractPlayerControllerTest<TenBallPlayer> {
    @Override public void setUp() {
        game = Game.newGame(GameType.BCA_TEN_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new TenBallController("", "", 0, 0);
        actualPlayer = new TenBallPlayer("");
        expectedPlayer = new TenBallPlayer("");
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override ITurn breakAndRunTurn() {
        return turnBuilder.breakBalls(1, 3).madeBalls(2, 4, 5, 6, 7, 8, 9, 10).win();
    }

    @Override ITurn tableRunTurn() {
        return turnBuilder.madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    }

    @Override ITurn fourBallRunTurn() {
        return turnBuilder.offTable(1, 2, 3, 5, 6, 7).madeBalls(4, 8, 9, 10).win();
    }

    @Override TenBallPlayer getBlankPlayer() {
        return new TenBallPlayer("");
    }

    @Override ITurn failedRunOutTurn() {
        return turnBuilder.breakBalls(1, 2).madeBalls(3).safety();
    }

    @Override TenBallPlayer failedRunOutPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(1, false);
        player.addSafety(false, 0);

        return player;
    }

    @Override TenBallPlayer fourBallRunOutPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addShootingBallsMade(4, false);
        player.addGameWon();
        player.addFiveBallRun();

        return player;
    }

    @Override TenBallPlayer getBreakAndRunPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(8, false);

        return player;
    }
}
