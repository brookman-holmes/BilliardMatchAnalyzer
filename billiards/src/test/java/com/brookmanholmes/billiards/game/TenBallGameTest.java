package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class TenBallGameTest extends AbstractRotationGameTest {
    @Test
    public void playerMakingDeadBallAllowsForTurnSkip() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(3, 4).deadBalls(1).miss();

        assertThat(game.setAllowTurnSkip(turn), is(true));

        game.addTurn(turn);
        assertThat(game.allowTurnSkip, is(true));
    }

    @Test
    public void playerMakingDeadBallAndScratchingDoesntAllowTurnSkip() {
        com.brookmanholmes.billiards.turn.Turn turn = turn().breakBalls(3, 4).deadBalls(1).scratch().miss();

        assertThat(game.setAllowTurnSkip(turn), is(false));

        game.addTurn(turn);
        assertThat(game.allowTurnSkip, is(false));
    }

    @Override public void setUp() {
        game = new TenBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Override Game createNewGame() {
        return new TenBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }


    @Override GameType thisGamesGameType() {
        return GameType.BCA_TEN_BALL;
    }
}
