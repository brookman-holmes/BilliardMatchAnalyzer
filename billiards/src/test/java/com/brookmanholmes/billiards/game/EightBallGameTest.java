package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.GameTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class EightBallGameTest extends AbstractEightBallGameTest {
    @Override public void setUp() {
        game = new EightBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Override Game createNewGame() {
        return new EightBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Test
    public void setPlayerAllowedToBreakAgainReturnsTrue() {
        TableStatus tableStatus = TableStatus.newTable(game.gameType);
        tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, 1, 8);

        com.brookmanholmes.billiards.turn.Turn turn = new GameTurn(0, 0L, true, TurnEnd.BREAK_MISS, tableStatus, false, null);
        assertThat(game.setAllowPlayerToBreakAgain(turn), is(true));

        game.addTurn(turn);

        assertThat(game.playerAllowedToBreakAgain, is(true));
    }

    @Test
    public void tableStatusShouldContain15Balls() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().win();

        assertThat(turn.getBallsRemaining(), is(15));
    }

    @Test
    public void dryBreakShouldReturnOpenTable() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakMiss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));
    }

    @Test
    public void noBallMadeAfterBreakShouldReturnOpenTable() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(1, 9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));
    }

    @Test
    public void solidMadeAfterBreakShouldReturnSolids() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(1, 9).madeBalls(2).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));

        game.addTurn(turn);

        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.STRIPES));
    }

    @Test
    public void stripeMadeAfterBreakShouldReturnStripes() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(1, 9).madeBalls(10).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));
    }

    @Test
    public void afterColorIsChosenSetPlayerColorShouldReturnCurrentPlayerColor() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(1, 9).madeBalls(10).miss();

        game.addTurn(turn);

        com.brookmanholmes.billiards.turn.Turn turn2 = turn().miss();
        assertThat(game.setPlayerColor(turn2), is(PlayerColor.STRIPES));
    }

    @Test
    public void convertPlayerColorShouldReturnOppositeForOpponentTurn() {
        game.turn = PlayerTurn.OPPONENT;
        assertThat(((EightBallGame) game).convertCurrentPlayerColorToPlayerColor(PlayerColor.SOLIDS), is(PlayerColor.STRIPES));
    }

    @Test
    public void ifReBreakAndPlayerChoosesToBreakAgain() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().deadOnBreak(8).scratch().breakMiss();

        game.addTurn(turn);

        assertThat(game.playerAllowedToBreakAgain, is(true));
        assertThat(game.turn, is(PlayerTurn.OPPONENT));
        assertThat(game.breaker, is(PlayerTurn.PLAYER));

        com.brookmanholmes.billiards.turn.Turn turn2 = turn().currentPlayerBreaks();

        game.addTurn(turn2);

        assertThat(game.playerAllowedToBreakAgain, is(false));
        assertThat(game.newGame, is(true));
        assertThat(game.turn, is(PlayerTurn.OPPONENT));
    }

    @Override GameType thisGamesGameType() {
        return GameType.BCA_EIGHT_BALL;
    }
}
