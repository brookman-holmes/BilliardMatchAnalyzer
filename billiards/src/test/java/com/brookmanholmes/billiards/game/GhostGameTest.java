package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 3/10/2017.
 */

public class GhostGameTest {
    Game game;

    TurnBuilder turn() {
        return new TurnBuilder(game.getGameType());
    }

    @Before
    public void setup() {
        game = Game.newGame(GameType.BCA_GHOST_NINE_BALL, PlayerTurn.PLAYER, BreakType.PLAYER, 3);
    }

    @Test
    public void startsNewGameAfter3Turns() {
        assertThat(game.maxAttemptsPerGame, is(3));
        assertThat(game.newGame, is(true));

        game.addTurn(turn().breakMiss());
        assertThat(game.newGame, is(false));
        assertThat(game.turnsThisGame, is(1));

        game.addTurn(turn().miss());
        assertThat(game.newGame, is(false));
        assertThat(game.turnsThisGame, is(2));

        game.addTurn(turn().miss());
        assertThat(game.newGame, is(true));
        assertThat(game.turnsThisGame, is(0));
    }
}
