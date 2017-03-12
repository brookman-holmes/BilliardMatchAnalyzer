package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/31/2016.
 */
@SuppressWarnings("unused")
public class ApaNineBallPlayerControllerTest extends AbstractNineBallPlayerControllerTest {
    private final int playerRank = 4;

    @Override public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER, 100);
        int opponentRank = 4;
        playerController = new ApaNineBallController("", "", "", "", playerRank, opponentRank);
        actualPlayer = new Player("", "", GameType.BCA_NINE_BALL, playerRank, playerRank);
        expectedPlayer = new Player("", "", GameType.BCA_NINE_BALL, playerRank, playerRank);
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override
    Player getBlankPlayer() {
        return new Player("", "", GameType.BCA_NINE_BALL, playerRank, playerRank);
    }

    @Test public void foulOnBreakAdds2DeadBalls() {
        expectedPlayer.addDeadBalls(2);

        actualPlayer = playerController.addTurn(game.getGameStatus(), turnBuilder.deadOnBreak(2, 3).fouled().breakMiss()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(2));
    }

    @Test public void foulOnShotAdds1DeadBall() {
        expectedPlayer.addDeadBalls(1);

        actualPlayer = playerController.addTurn(game.getGameStatus(), turnBuilder.breakBalls(1).madeBalls(2, 3).deadBalls(4).fouled().miss()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(1));
    }

    @Test public void nineBallOnBreakGives7DeadBalls() {
        expectedPlayer.addDeadBalls(7);

        actualPlayer = playerController.addTurn(game.getGameStatus(), turnBuilder.breakBalls(7, 9).win()).getPlayer();

        assertThat(actualPlayer.getDeadBalls(), is(7));
    }
}
