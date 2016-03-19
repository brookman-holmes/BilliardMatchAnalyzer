package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class TenBallPlayerControllerTest extends AbstractPlayerControllerTest<TenBallPlayer> {
    @Override
    public void setUp() {
        game = Game.newGame(GameType.BCA_TEN_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new TenBallController("", "");
        actualPlayer = new TenBallPlayer("");
        expectedPlayer = new TenBallPlayer("");
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override
    Turn breakAndRunTurn() {
        return turnBuilder.breakBalls(1, 3).madeBalls(2, 4, 5, 6, 7, 8, 9, 10).win();
    }

    @Override
    Turn tableRunTurn() {
        return turnBuilder.madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    }

    @Override
    Turn fourBallRunTurn() {
        return turnBuilder.offTable(1, 2, 3).madeBalls(4, 5, 6, 7, 8, 9, 10).win();
    }

    @Override
    TenBallPlayer getBlankPlayer() {
        return new TenBallPlayer("");
    }

    @Override
    Turn failedRunOutTurn() {
        return turnBuilder.breakBalls(1, 2).madeBalls(3).safety();
    }

    @Override
    TenBallPlayer failedRunOutPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(1, false);
        player.addSafety(false);

        return player;
    }

    @Override
    TenBallPlayer fourBallRunOutPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addShootingBallsMade(7, false);
        player.addGameWon();
        player.addFourBallRun();

        return player;
    }

    @Override
    TenBallPlayer getBreakAndRunPlayer() {
        TenBallPlayer player = new TenBallPlayer("");
        player.addBreakShot(2, true, false);
        player.addShootingBallsMade(8, false);

        return player;
    }
}
