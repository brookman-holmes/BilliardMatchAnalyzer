package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class BreakTest {
    Game game;

    @Test
    public void setBreakerWithWinnerBreakReturnsPlayer() {
        setupWinnerGame();

        assertThat(game.setBreaker(PlayerTurn.PLAYER), is(PlayerTurn.PLAYER));
    }

    @Test
    public void setBreakerWithWinnerBreakReturnsOpponent() {
        setupWinnerGame();

        assertThat(game.setBreaker(PlayerTurn.OPPONENT), is(PlayerTurn.OPPONENT));
    }

    @Test
    public void setBreakerWithAlternateBreakAlternatesPlayers() {
        setupAlternateGame();

        List<PlayerTurn> actualBreaks = new ArrayList<>();
        actualBreaks.addAll(
                Arrays.asList(
                        game.addTurn(createMockTurnWithLoss()).breaker,
                        game.addTurn(createMockTurnWithWin()).breaker,
                        game.addTurn(createMockTurnWithLoss()).breaker,
                        game.addTurn(createMockTurnWithWin()).breaker,
                        game.addTurn(createMockTurnWithLoss()).breaker,
                        game.addTurn(createMockTurnWithLoss()).breaker,
                        game.addTurn(createMockTurnWithWin()).breaker,
                        game.addTurn(createMockTurnWithWin()).breaker
                )
        );

        List<PlayerTurn> expectedBreaks = new ArrayList<>();
        expectedBreaks.addAll(
                Arrays.asList(
                        PlayerTurn.OPPONENT,
                        PlayerTurn.PLAYER,
                        PlayerTurn.OPPONENT,
                        PlayerTurn.PLAYER,
                        PlayerTurn.OPPONENT,
                        PlayerTurn.PLAYER,
                        PlayerTurn.OPPONENT,
                        PlayerTurn.PLAYER
                )
        );

        assertThat(actualBreaks, is(expectedBreaks));
    }

    @Test
    public void setBreakerWithLoserBreakReturnsPlayer() {
        setupLoserGame();

        assertThat(game.addTurn(createMockTurnWithLoss()).breaker, is(PlayerTurn.PLAYER));
    }

    @Test
    public void setBreakerWithLoserBreakReturnsOpponent() {
        setupLoserGame();

        assertThat(game.addTurn(createMockTurnWithWin()).breaker, is(PlayerTurn.OPPONENT));
    }

    @Test
    public void breakerDoesntGetFuckedWithWhileAddingTurns() {
        setupWinnerGame();

        game.addTurn(createMockTurnWithTheseBallsMade(true, 1, 2, 3, 9));
        assertThat(game.breaker, is(PlayerTurn.PLAYER));
        game.addTurn(createMockTurnWithTheseBallsMade(false, 4, 5, 6));
        assertThat(game.breaker, is(PlayerTurn.PLAYER));
        game.addTurn(createMockTurnWithTheseBallsMade(false, 7, 8, 9));
        assertThat(game.breaker, is(PlayerTurn.PLAYER));
    }

    private void setupWinnerGame() {
        game = new NineBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    private void setupLoserGame() {
        game = new NineBallGame(PlayerTurn.PLAYER, BreakType.LOSER);
    }

    private void setupAlternateGame() {
        game = new NineBallGame(PlayerTurn.PLAYER, BreakType.ALTERNATE);
    }

    private com.brookmanholmes.billiards.turn.Turn createMockTurnWithWin() {
        com.brookmanholmes.billiards.turn.Turn mockedTurn = mock(com.brookmanholmes.billiards.turn.Turn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.GAME_WON);
        return mockedTurn;
    }

    private com.brookmanholmes.billiards.turn.Turn createMockTurnWithLoss() {
        com.brookmanholmes.billiards.turn.Turn mockedTurn = mock(com.brookmanholmes.billiards.turn.Turn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.MISS);
        when(mockedTurn.isGameLost()).thenReturn(true);
        return mockedTurn;
    }

    private com.brookmanholmes.billiards.turn.Turn createMockTurnWithTheseBallsMade(boolean scratch, int... balls) {
        List<Integer> ballsMade = new ArrayList<>();
        for (int ball : balls) {
            ballsMade.add(ball);
        }

        com.brookmanholmes.billiards.turn.Turn mockedTurn = mock(com.brookmanholmes.billiards.turn.Turn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.MISS);
        when(mockedTurn.isFoul()).thenReturn(scratch);
        when(mockedTurn.getBallsToRemoveFromTable()).thenReturn(ballsMade);
        return mockedTurn;
    }
}
