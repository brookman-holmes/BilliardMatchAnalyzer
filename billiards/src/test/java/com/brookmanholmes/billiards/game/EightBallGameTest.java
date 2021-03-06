package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
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
    @Override
    public void setUp() {
        game = new EightBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override
    List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Override
    Game createNewGame() {
        return new EightBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }
    
    @Test
    public void setPlayerAllowedToBreakAgainReturnsTrue() {
        TableStatus tableStatus = TableStatus.newTable(game.gameType);
        tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, 1, 8);

        ITurn turn = new Turn(TurnEnd.BREAK_MISS, tableStatus, true, false, null);
        assertThat(game.setAllowPlayerToBreakAgain(turn), is(true));

        game.addTurn(turn);

        assertThat(game.playerAllowedToBreakAgain, is(true));
    }

    @Test
    public void tableStatusShouldContain15Balls() {
        ITurn turn = turn().win();

        assertThat(turn.getBallsRemaining(), is(15));
    }

    @Test
    public void dryBreakShouldReturnOpenTable() {
        ITurn turn = turn().breakMiss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));
    }

    @Test
    public void noBallMadeAfterBreakShouldReturnOpenTable() {
        ITurn turn = turn().breakBalls(1, 9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));
    }

    @Test
    public void solidMadeAfterBreakShouldReturnSolids() {
        ITurn turn = turn().breakBalls(1, 9).madeBalls(2).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));

        game.addTurn(turn);

        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.STRIPES));
    }

    @Test
    public void stripeMadeAfterBreakShouldReturnStripes() {
        ITurn turn = turn().breakBalls(1, 9).madeBalls(10).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));
    }

    @Test
    public void afterColorIsChosenSetPlayerColorShouldReturnCurrentPlayerColor() {
        ITurn turn = turn().breakBalls(1, 9).madeBalls(10).miss();

        game.addTurn(turn);

        ITurn turn2 = turn().miss();
        assertThat(game.setPlayerColor(turn2), is(PlayerColor.STRIPES));
    }

    @Test
    public void convertPlayerColorShouldReturnOppositeForOpponentTurn() {
        game.turn = PlayerTurn.OPPONENT;
        assertThat(((EightBallGame) game).convertCurrentPlayerColorToPlayerColor(PlayerColor.SOLIDS), is(PlayerColor.STRIPES));
    }

    @Test
    public void ifReBreakAndPlayerChoosesToBreakAgain() {
        ITurn turn = turn().deadOnBreak(8).fouled().breakMiss();

        game.addTurn(turn);

        assertThat(game.playerAllowedToBreakAgain, is(true));
        assertThat(game.turn, is(PlayerTurn.OPPONENT));
        assertThat(game.breaker, is(PlayerTurn.PLAYER));

        ITurn turn2 = turn().currentPlayerBreaks();

        game.addTurn(turn2);

        assertThat(game.playerAllowedToBreakAgain, is(false));
        assertThat(game.newGame, is(true));
        assertThat(game.turn, is(PlayerTurn.OPPONENT));
    }

    @Override GameType thisGamesGameType() {
        return GameType.BCA_EIGHT_BALL;
    }
}
