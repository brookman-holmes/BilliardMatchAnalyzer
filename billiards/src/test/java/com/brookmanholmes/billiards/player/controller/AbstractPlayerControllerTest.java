package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.TurnBuilder;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Pair;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public abstract class AbstractPlayerControllerTest<T extends AbstractPlayer> {
    PlayerController<T> playerController;
    Game game;
    TurnBuilder turnBuilder;
    T actualPlayer, expectedPlayer;

    @Before
    public abstract void setUp();


    /**
     * Break tests
     */
    @Test
    public void noBallsMadeOnBreak() {
        playerController.turn = turnBuilder.breakMiss();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(0, false, false);


        testPlayerEquality();
    }

    @Test
    public void ballsMadeOnBreakWithNoContinuation() {
        playerController.turn = turnBuilder.breakBalls(1, 2, 3).miss();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(3, false, false);

        testPlayerEquality();
    }

    @Test
    public void ballsMadeOnBreakWithContinuation() {
        playerController.turn = turnBuilder.breakBalls(1, 2, 3).madeBalls(4, 5).miss();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(3, true, false);

        testPlayerEquality();
    }

    @Test
    public void scratchOnBreak() {
        playerController.turn = turnBuilder.scratch().breakMiss();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(0, false, true);

        testPlayerEquality();
    }

    /**
     * Shooting tests
     */
    @Test
    public void addTurnToPlayerReturnsTrue() {
        // made a few balls and then play a safe
        playerController.turn = turnBuilder.madeBalls(1).safety();

        assertThat(playerController.setAddTurnToPlayer(), is(true));

        // miss your shot and make no balls
        playerController.turn = turnBuilder.miss();

        assertThat(playerController.setAddTurnToPlayer(), is(true));
    }

    @Test
    public void addTurnToPlayerReturnsFalse() {
        // not sure what to test here? there are lots of reasons this could be false,
        // but very few it could be true
    }

    @Test
    public void missTurnEndGivesShootingMiss() {
        playerController.turn = turnBuilder.miss();
        playerController.addShootingStats(actualPlayer);

        expectedPlayer.addShootingMiss();
        expectedPlayer.addShootingBallsMade(0, false);

        testPlayerEquality();
    }

    /**
     * Safety tests
     */
    @Test
    public void safetyTurnEndGivesASafety() {
        playerController.turn = turnBuilder.safety();
        playerController.gameStatus = game.getGameStatus();
        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafety(false);

        testPlayerEquality();
    }

    @Test
    public void safetyTurnEndGivesASafetyWithOppSafe() {
        playerController.turn = turnBuilder.safety();
        playerController.gameStatus = new GameStatus.Builder(GameType.BCA_NINE_BALL).safetyLastTurn().build();
        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafety(true);

        testPlayerEquality();
    }

    @Test
    public void safetyMissTurnEndGivesASafetyAttempt() {
        playerController.turn = turnBuilder.safetyMiss();
        playerController.gameStatus = game.getGameStatus();

        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafetyAttempt(false);

        testPlayerEquality();
    }

    @Test
    public void safetyMissTurnEndWithScratchGivesASafetyAttempt() {
        playerController.turn = turnBuilder.scratch().safetyMiss();
        playerController.gameStatus = game.getGameStatus();

        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafetyAttempt(true);

        testPlayerEquality();
    }

    /**
     * Run out tests
     */

    @Test
    public void maximumMakeableBallsReturnsMaxBalls() {
        playerController.gameStatus = game.getGameStatus();
        assertThat(playerController.getMaximumBallsMakeable(), is(game.getGameStatus().MAX_BALLS));
    }

    @Test
    public void breakAndRunGivesABreakAndRun() {
        playerController.turn = breakAndRunTurn();
        playerController.gameStatus = game.getGameStatus();

        playerController.addRunOutStats(actualPlayer);

        expectedPlayer.addBreakAndRun();

        testPlayerEquality();
    }

    @Test
    public void tableRunGivesATableRun() {
        playerController.gameStatus = game.addTurn(turnBuilder.breakMiss());
        playerController.turn = tableRunTurn();
        playerController.addRunOutStats(actualPlayer);

        expectedPlayer.addTableRun();

        testPlayerEquality();
    }

    @Test
    public void fourBallRunOutGivesAFourBallRun() {
        // // TODO: 1/30/2016 add in a turn here to simulate the game being underway
        playerController.turn = fourBallRunTurn();
        playerController.gameStatus = game.getGameStatus();

        playerController.addRunOutStats(actualPlayer);

        expectedPlayer.addFourBallRun();

        testPlayerEquality();
    }

    /**
     * helper addWinOnBreak tests
     */

    @Test
    public void getGameWinnerReturnsPlayerAfterWin() {
        playerController.turn = turnBuilder.win();
        playerController.gameStatus = game.getGameStatus();

        assertThat(playerController.getGameWinner(), is(PlayerTurn.PLAYER));
    }

    @Test
    public void getGameWinnerReturnsOpponentAfterLoss() {
        playerController.turn = turnBuilder.lose();
        playerController.gameStatus = game.getGameStatus();

        assertThat(playerController.getGameWinner(), is(PlayerTurn.OPPONENT));
    }

    @Test
    public void isGameOverReturnsTrue() {
        playerController.turn = turnBuilder.win();
        assertThat(playerController.isGameOver(), is(true));

        playerController.turn = turnBuilder.lose();
        assertThat(playerController.isGameOver(), is(true));
    }

    @Test
    public void isGameOverReturnsFalse() {
        Turn mockedTurn = Mockito.mock(Turn.class);
        playerController.turn = mockedTurn;
        for (TurnEnd turnEnd : TurnEnd.values()) {
            if (turnEnd != TurnEnd.GAME_WON) {
                when(mockedTurn.getTurnEnd()).thenReturn(turnEnd);
                assertThat(playerController.isGameOver(), is(false));
            }
        }
    }

    @Test
    public void test1() {
        Pair<T> players = playerController.updatePlayerStats(game.getGameStatus(), breakAndRunTurn());
        game.addTurn(breakAndRunTurn());


        T player1 = getBreakAndRunPlayer();
        player1.addGameWon();
        player1.addBreakAndRun();

        assertThat(players.getPlayer(), is(player1));

        T player2 = getBlankPlayer();
        player2.addGameLost();

        assertThat(players.getOpponent(), is(player2));
    }


    @Test(expected = IllegalStateException.class)
    public void updatePlayerStatsThrowsIllegalStateExceptionOnNullInput() {
        playerController.updatePlayerStats(game.getGameStatus(), null);
    }

    @Test(expected = IllegalStateException.class)
    public void updatePlayerStatsThrowsIllegalStateExceptionOnNullInput2() {
        playerController.updatePlayerStats(null, turnBuilder.miss());
    }

    abstract Turn breakAndRunTurn();

    abstract Turn tableRunTurn();

    abstract Turn fourBallRunTurn();

    abstract Turn failedRunOutTurn();

    abstract T getBlankPlayer();

    abstract T getBreakAndRunPlayer();

    abstract T failedRunOutPlayer();

    abstract T fourBallRunOutPlayer();

    private void testPlayerEquality() {
        assertThat(actualPlayer, is(expectedPlayer));
    }
}
