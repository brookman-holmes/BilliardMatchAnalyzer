package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/31/2016.
 */
@SuppressWarnings("unused")
public class ApaNineBallPlayerControllerTest extends AbstractNineBallPlayerControllerTest<ApaNineBallPlayer> {
    private final int playerRank = 4;

    @Override public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        int opponentRank = 4;
        playerController = new ApaNineBallController("", "", playerRank, opponentRank);
        actualPlayer = new ApaNineBallPlayer("", playerRank);
        expectedPlayer = new ApaNineBallPlayer("", playerRank);
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override ApaNineBallPlayer getBlankPlayer() {
        return new ApaNineBallPlayer("", playerRank);
    }

    @Test public void foulOnBreakAdds2DeadBalls() {
        expectedPlayer.addDeadBalls(2);

        actualPlayer = playerController.updatePlayerStats(game.getGameStatus(), turnBuilder.deadOnBreak(2, 3).fouled().breakMiss()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(2));
    }

    @Test public void foulOnShotAdds1DeadBall() {
        expectedPlayer.addDeadBalls(1);

        actualPlayer = playerController.updatePlayerStats(game.getGameStatus(), turnBuilder.breakBalls(1).madeBalls(2, 3).deadBalls(4).fouled().miss()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(1));
    }

    @Test public void nineBallOnBreakGives7DeadBalls() {
        expectedPlayer.addDeadBalls(7);

        actualPlayer = playerController.updatePlayerStats(game.getGameStatus(), turnBuilder.breakBalls(7, 9).win()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(7));
    }
}
