package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;

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
        Turn turn = turn().breakBalls(9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));
    }

    @Test
    public void solidsMadeOnBreakShouldReturnSolids() {
        Turn turn = turn().breakBalls(7).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));
    }

    @Test
    public void bothMadeOnBreakShouldReturnOpen() {
        Turn turn = turn().breakBalls(7, 9).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.OPEN));
    }

    @Test
    public void bothMadeOnBreakAndSolidsMadeShouldReturnSolids() {
        Turn turn = turn().breakBalls(7, 9).madeBalls(6).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.SOLIDS));
    }

    @Test
    public void bothMadeOnBreakAndStripesMadeShouldReturnStripes() {
        Turn turn = turn().breakBalls(7, 9).madeBalls(15).miss();

        assertThat(game.setPlayerColor(turn), is(PlayerColor.STRIPES));
    }

    @Override
    public void setUp() {
        game = new ApaEightBallGame();
    }

    @Override
    List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Override
    Game createNewGame() {
        return new ApaEightBallGame();
    }

    @Override
    GameType thisGamesGameType() {
        return GameType.APA_EIGHT_BALL;
    }
}
