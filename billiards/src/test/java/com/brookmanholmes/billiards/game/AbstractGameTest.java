package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brookman Holmes on 11/5/2015.
 */
@SuppressWarnings("unused")
public abstract class AbstractGameTest {
    Game game;

    TurnBuilder turn() {
        return new TurnBuilder(game.getGameType());
    }

    @Before
    public abstract void setUp();

    @Test
    public void getGhostBallsToWinGameReturnsAllBalls() {
        // TODO: 8/26/2016 create this test
    }

    @Test
    public void getGhostBallsToWinGameReturnsSomeBalls() {
        // TODO: 8/26/2016 create this test
    }

    @Test
    public void removeBallsFromTableDoesntRemoveGameBall() {
        game.removeBallsFromTable(Arrays.asList(game.GAME_BALL, 3, 5));

        assertThat(game.ballsOnTable.contains(game.GAME_BALL), is(true));
    }

    @Test
    public void removeBallsFromTableRemovesThe5and3() {
        game.removeBallsFromTable(Arrays.asList(game.GAME_BALL, 3, 5));

        assertThat(game.ballsOnTable.contains(3), is(false));
        assertThat(game.ballsOnTable.contains(5), is(false));
        assertThat(game.ballsOnTable.contains(game.GAME_BALL), is(true));
    }

    @Test
    public void tableContainsAllTheBallsItShould() {
        List<Integer> ballsAtStartOfGameList = populateList();
        game.ballsOnTable.removeAll(ballsAtStartOfGameList);
        assertThat(game.ballsOnTable.isEmpty(), is(true));
    }

    @Test
    public void getGameWinnerReturnsPlayer() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.GAME_WON);

        game.turn = PlayerTurn.PLAYER;

        assertThat(game.getGameWinner(mockedTurn), is(PlayerTurn.PLAYER));
    }

    @Test
    public void getGameWinnerReturnsOpponent() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.MISS);

        game.turn = PlayerTurn.PLAYER;

        assertThat(game.getGameWinner(mockedTurn), is(PlayerTurn.OPPONENT));
    }

    @Test
    public void isGameOverReturnsTrue() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.GAME_WON);
        assertThat(game.isGameOver(mockedTurn), is(true));

        when(mockedTurn.getTurnEnd()).thenReturn(TurnEnd.MISS);
        when(mockedTurn.isGameLost()).thenReturn(true);
        assertThat(game.isGameOver(mockedTurn), is(true));
    }

    @Test
    public void startNewGameReturnsCorrectValues() {
        Game newGame = createNewGame();

        game.startNewGame(turn().win());

        assertThat(game.getGameStatus(), is(newGame.getGameStatus()));
    }

    @Test
    public void getPlayerTurnReturnsPlayer() {
        assertThat(game.getTurn(), is(PlayerTurn.PLAYER));

        game.addTurn(turn().madeBalls(1, 2, 3, 4, 5, 6, 7, game.GAME_BALL).win());

        assertThat(game.getTurn(), is(PlayerTurn.PLAYER));
    }

    @Test
    public void getGameTypeReturnsCorrectGame() {
        assertThat(game.getGameType(), is(thisGamesGameType()));
    }

    @Test
    public void setAllowPlayerToBreakAgainReturnsFalse() {
        ITurn turn = turn().deadOnBreak(1, 7).breakMiss();

        assertThat(game.setAllowPlayerToBreakAgain(turn), is(false));

        game.addTurn(turn);

        assertThat(game.playerAllowedToBreakAgain, is(false));
    }

    @Test
    public void setOpponentPlayedSuccessfulSafeReturnsTrue() {
        ITurn turn = turn().safety();

        assertThat(game.setOpponentPlayedSuccessfulSafe(turn), is(true));

        game.addTurn(turn);
        assertThat(game.opponentPlayedSuccessfulSafe, is(true));
    }

    @Test
    public void setCurrentPlayerConsecutiveFouls() {
        ITurn turn = turn().scratch().miss();

        game.addTurn(turn().scratch().breakMiss());
        assertThat(game.consecutivePlayerFouls, is(1));
        assertThat(game.consecutiveOpponentFouls, is(0));

        game.addTurn(turn);
        assertThat(game.consecutiveOpponentFouls, is(1));
        assertThat(game.consecutivePlayerFouls, is(1));

        game.addTurn(turn().miss());
        assertThat(game.consecutivePlayerFouls, is(0));
        assertThat(game.consecutiveOpponentFouls, is(1));

        game.addTurn(turn);
        assertThat(game.consecutiveOpponentFouls, is(2));
        assertThat(game.consecutivePlayerFouls, is(0));
    }

    @Test
    public void testStaticNewGameMethod() {
        List<GameType> supportGameTypes = new ArrayList<>(
                Arrays.asList(
                        GameType.APA_EIGHT_BALL,
                        GameType.APA_NINE_BALL,
                        GameType.BCA_EIGHT_BALL,
                        GameType.BCA_NINE_BALL,
                        GameType.BCA_TEN_BALL
                )
        );

        for (GameType type : supportGameTypes) {
            Game game = newGame(type);
            assertThat(game.getGameType(), is(type));
            assertThat(game.breaker, is(PlayerTurn.PLAYER));
            assertThat(game.breakType, is(BreakType.WINNER));
        }
    }

    @Test(expected = InvalidGameTypeException.class)
    public void testStaticNewGameMethodThrowsException() {
        Game.newGame(GameType.AMERICAN_ROTATION, PlayerTurn.PLAYER, BreakType.ALTERNATE);
    }

    @Test
    public void getCurrentTableStatusReturnsCorrectBalls() {
        game.ballsOnTable.removeAll(Arrays.asList(1, 2, 3, 7));

        ITableStatus table = game.getCurrentTableStatus();

        assertThat(table.getBallsToRemoveFromTable(), is(Arrays.asList(1, 2, 3, 7)));
    }

    abstract GameType thisGamesGameType();

    abstract Game createNewGame();

    abstract List<Integer> populateList();

    private Game newGame(GameType type) {
        return Game.newGame(type, PlayerTurn.PLAYER, BreakType.WINNER);
    }
}
