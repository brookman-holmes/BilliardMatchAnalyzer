package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.TurnBuilder;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.player.AbstractPlayer;

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
        expectedPlayer.addShootingTurn();

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
        playerController.addSafetyStats(actualPlayer);

        expectedPlayer.addSafetyAttempt(false);

        testPlayerEquality();
    }

    @Test
    public void safetyMissTurnEndWithScratchGivesASafetyAttempt() {
        playerController.turn = turnBuilder.scratch().safetyMiss();
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
            if (turnEnd != TurnEnd.GAME_WON && turnEnd != TurnEnd.GAME_LOST) {
                when(mockedTurn.getTurnEnd()).thenReturn(turnEnd);
                assertThat(playerController.isGameOver(), is(false));
            }
        }
    }

    @Test
    public void addGamesToPlayersReturnsWinForPlayer1() {
        playerController.turn = turnBuilder.win();
        playerController.gameStatus = game.getGameStatus();
        playerController.addGamesToPlayers();

        T expectedPlayer1 = getBlankPlayer();
        expectedPlayer1.addGameWon();
        T expectedPlayer2 = getBlankPlayer();
        expectedPlayer2.addGameLost();

        assertThat(playerController.player1, is(expectedPlayer1));
        assertThat(playerController.player2, is(expectedPlayer2));
    }

    @Test
    public void addGamesToPlayersReturnsWinForPlayer2() {
        playerController.turn = turnBuilder.lose();
        playerController.gameStatus = game.getGameStatus();
        playerController.addGamesToPlayers();


        T expectedPlayer1 = getBlankPlayer();
        expectedPlayer1.addGameLost();
        T expectedPlayer2 = getBlankPlayer();
        expectedPlayer2.addGameWon();

        assertThat(playerController.player1, is(expectedPlayer1));
        assertThat(playerController.player2, is(expectedPlayer2));
    }

    @Test(expected = IllegalStateException.class)
    public void getGameWinnerThrowsIllegalStateExceptionIfCalledWithoutAGameOver() {
        playerController.turn = turnBuilder.miss();

        playerController.getGameWinner();
    }

    @Test
    public void addStatsToPlayerCorrectlyAddsStats() {
        playerController.turn = breakAndRunTurn();
        playerController.gameStatus = game.getGameStatus();
        playerController.addStatsToPlayer(playerController.player1);

        expectedPlayer = getBreakAndRunPlayer();
        expectedPlayer.addGameWon();
        expectedPlayer.addBreakAndRun();
        expectedPlayer.addShootingTurn();

        assertThat(playerController.player1, is(expectedPlayer));
    }

    @Test
    public void updatePlayerStatsCorrectlySetsVariables() {
        playerController.updatePlayerStats(game.getGameStatus(), breakAndRunTurn());


        expectedPlayer = getBreakAndRunPlayer();
        expectedPlayer.addGameWon();
        expectedPlayer.addBreakAndRun();
        expectedPlayer.addShootingTurn();

        assertThat(playerController.turn, is(breakAndRunTurn()));
        assertThat(playerController.gameStatus, is(game.getGameStatus()));
        assertThat(playerController.player1, is(expectedPlayer));
    }

    @Test
    public void updatePlayerStatsCorrectlySetsVariablesForMultipleTurns() {
        playerController.updatePlayerStats(game.getGameStatus(), failedRunOutTurn());

        expectedPlayer = failedRunOutPlayer();

        assertThat(playerController.turn, is(failedRunOutTurn()));
        assertThat(playerController.gameStatus, is(game.getGameStatus()));
        assertThat(playerController.player1, is(expectedPlayer));

        game.addTurn(failedRunOutTurn());

        playerController.updatePlayerStats(game.getGameStatus(), fourBallRunTurn());

        expectedPlayer.addGameLost();
        assertThat(playerController.turn, is(fourBallRunTurn()));
        assertThat(playerController.gameStatus, is(game.getGameStatus()));
        assertThat(playerController.player1, is(expectedPlayer));
        assertThat(playerController.player2, is(fourBallRunOutPlayer()));
    }

    @Test
    public void getPlayer1ReturnsPlayer1() {
        playerController.updatePlayerStats(game.getGameStatus(), breakAndRunTurn());

        assertThat(playerController.player1, is(playerController.getPlayer1()));
    }

    @Test
    public void getPlayer2ReturnsPlayer2() {
        playerController.updatePlayerStats(game.getGameStatus(), breakAndRunTurn());

        assertThat(playerController.player2, is(playerController.getPlayer2()));
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
