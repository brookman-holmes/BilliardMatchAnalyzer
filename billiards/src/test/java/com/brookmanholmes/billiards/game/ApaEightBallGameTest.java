package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class ApaEightBallGameTest extends AbstractEightBallGameTest {
    @Test
    public void stripesMadeOnBreakShouldReturnStripes() {
        ITurn turn = turn().breakBalls(9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));
        game.addTurn(turn);
        assertThat(game.playerColor, is(PlayerColor.STRIPES));
    }

    @Test
    public void solidsMadeOnBreakShouldReturnSolids() {
        ITurn turn = turn().breakBalls(7).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));

        game.addTurn(turn);
        assertThat(game.playerColor, is(PlayerColor.SOLIDS));
    }

    @Test
    public void bothMadeOnBreakShouldReturnOpen() {
        ITurn turn = turn().breakBalls(7, 9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));

        game.addTurn(turn);
        assertThat(game.playerColor, is(PlayerColor.OPEN));
    }

    @Test
    public void bothMadeOnBreakAndSolidsMadeShouldReturnSolids() {
        ITurn turn = turn().breakBalls(7, 9).madeBalls(6).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));

        game.addTurn(turn);
        assertThat(game.playerColor, is(PlayerColor.SOLIDS));
    }

    @Test
    public void bothMadeOnBreakAndStripesMadeShouldReturnStripes() {
        ITurn turn = turn().breakBalls(7, 9).madeBalls(15).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));

        game.addTurn(turn);
        assertThat(game.playerColor, is(PlayerColor.STRIPES));
    }

    @Test
    public void solidMadeReturnsSolids() {
        ITurn turn = turn().madeBalls(1, 7).miss();
        game.addTurn(turn().breakMiss());

        assertThat(((ApaEightBallGame) game).getColorMade(turn.getBallStatuses()),
                is(((ApaEightBallGame) game).convertCurrentPlayerColorToPlayerColor(PlayerColor.SOLIDS)));
    }

    @Test
    public void stripesMadeReturnsStripes() {
        ITurn turn = turn().madeBalls(9, 15).miss();
        game.addTurn(turn().breakMiss());

        assertThat(((ApaEightBallGame) game).getColorMade(turn.getBallStatuses()),
                is(((ApaEightBallGame) game).convertCurrentPlayerColorToPlayerColor(PlayerColor.STRIPES)));
    }

    @Test
    public void nothingMadeReturnsOpen() {
        ITurn turn = turn().miss();
        game.addTurn(turn().breakMiss());

        assertThat(((ApaEightBallGame) game).getColorMade(turn.getBallStatuses()), is(PlayerColor.OPEN));
    }

    @Override public void setUp() {
        game = new ApaEightBallGame();
    }

    @Override List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Override Game createNewGame() {
        return new ApaEightBallGame();
    }

    @Override GameType thisGamesGameType() {
        return GameType.APA_EIGHT_BALL;
    }
}
