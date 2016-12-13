package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
@SuppressWarnings("unused")
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
        playerController.gameStatus = game.getGameStatus();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(0, false, false);


        testPlayerEquality();
    }

    @Test
    public void ballsMadeOnBreakWithNoContinuation() {
        playerController.turn = turnBuilder.breakBalls(1, 2, 3).miss();
        playerController.gameStatus = game.getGameStatus();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(3, false, false);

        testPlayerEquality();
    }

    @Test
    public void ballsMadeOnBreakWithContinuation() {
        playerController.turn = turnBuilder.breakBalls(1, 2, 3).madeBalls(4, 5).miss();
        playerController.gameStatus = game.getGameStatus();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(3, true, false);

        testPlayerEquality();
    }

    @Test
    public void foulOnBreak() {
        playerController.turn = turnBuilder.fouled().breakMiss();
        playerController.gameStatus = game.getGameStatus();
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

        assertThat(playerController.addTurnToPlayer(), is(true));

        // miss your shot and make no balls
        playerController.turn = turnBuilder.miss();

        assertThat(playerController.addTurnToPlayer(), is(true));
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

        expectedPlayer.addSafety(false, 0);

        testPlayerEquality();
    }

    @Test
    public void safetyTurnEndGivesASafetyWithOppSafe() {
        playerController.turn = turnBuilder.safety();
        playerController.gameStatus = new GameStatus.Builder(GameType.BCA_NINE_BALL).safetyLastTurn().build();
        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafety(true, 0);

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
    public void safetyMissTurnEndWithFoulGivesASafetyAttempt() {
        playerController.turn = turnBuilder.fouled().safetyMiss();
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
        playerController.turn = fourBallRunTurn();
        playerController.gameStatus = game.getGameStatus();

        playerController.addRunOutStats(actualPlayer);

        expectedPlayer.addFiveBallRun();

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
        playerController.turn = turnBuilder.seriousFoul().miss();
        playerController.gameStatus = game.getGameStatus();

        assertThat(playerController.getGameWinner(), is(PlayerTurn.OPPONENT));
    }

    @Test
    public void isGameOverReturnsTrue() {
        playerController.turn = turnBuilder.win();
        assertThat(playerController.isGameOver(), is(true));

        playerController.turn = turnBuilder.seriousFoul().miss();
        assertThat(playerController.isGameOver(), is(true));
    }

    @Test
    public void isGameOverReturnsFalse() {
        ITurn mockedTurn = Mockito.mock(ITurn.class);
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
        Pair<T> players = playerController.addTurn(game.getGameStatus(), breakAndRunTurn());
        game.addTurn(breakAndRunTurn());


        T player1 = getBreakAndRunPlayer();
        player1.addGameWon();
        player1.addBreakAndRun();

        assertThat(players.getPlayer(), is(player1));

        T player2 = getBlankPlayer();
        player2.addGameLost();

        assertThat(players.getOpponent(), is(player2));
    }


    @Test(expected = AssertionError.class)
    public void updatePlayerStatsThrowsIllegalStateExceptionOnNullInput() {
        playerController.addTurn(game.getGameStatus(), null);
    }

    @Test(expected = AssertionError.class)
    public void updatePlayerStatsThrowsIllegalStateExceptionOnNullInput2() {
        playerController.addTurn(null, turnBuilder.miss());
    }

    abstract ITurn breakAndRunTurn();

    abstract ITurn tableRunTurn();

    abstract ITurn fourBallRunTurn();

    abstract ITurn failedRunOutTurn();

    abstract T getBlankPlayer();

    abstract T getBreakAndRunPlayer();

    abstract T failedRunOutPlayer();

    abstract T fourBallRunOutPlayer();

    private void testPlayerEquality() {
        assertThat(actualPlayer, is(expectedPlayer));
    }
}
