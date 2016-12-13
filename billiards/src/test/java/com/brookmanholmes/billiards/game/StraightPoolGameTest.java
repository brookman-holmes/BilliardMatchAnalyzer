package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brookman Holmes on 12/6/2016.
 * Test straight pool games, doesn't extend AbstractGameTest because it's too dissimilar
 */

public class StraightPoolGameTest {
    StraightPoolGame game;

    TurnBuilder turn() {
        return new TurnBuilder(game.getGameType());
    }

    @Before
    public void setup() {
        game = new StraightPoolGame(PlayerTurn.PLAYER);
    }

    @Test
    public void setAllowPlayerToBreakAgainReturnsTrue() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);
        when(mockedTurn.isFoul()).thenReturn(true);

        assertThat(game.setAllowPlayerToBreakAgain(mockedTurn), is(true));
    }

    @Test
    public void setAllowPlayerToBreakAgainReturnsFalseWithNoFoul() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);
        when(mockedTurn.isFoul()).thenReturn(false);

        assertThat(game.setAllowPlayerToBreakAgain(mockedTurn), is(false));
    }

    @Test
    public void setAllowPlayerToBreakAgainReturnsFalseWithBallsMade() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.getShootingBallsMade()).thenReturn(1);
        when(mockedTurn.isFoul()).thenReturn(true);

        assertThat(game.setAllowPlayerToBreakAgain(mockedTurn), is(false));
    }

    @Test
    public void setAllowPushIsFalse() {
        ITurn mockedTurn = mock(ITurn.class);
        assertThat(game.setAllowPush(mockedTurn), is(false));
    }

    @Test
    public void setAllowTurnSkipIsFalse() {
        ITurn mockedTurn = mock(ITurn.class);
        assertThat(game.setAllowTurnSkip(mockedTurn), is(false));
    }

    @Test
    public void setConsecutiveFoulsBreakingFoulAddsZero() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.isFoul()).thenReturn(true);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);

        game.setConsecutiveFouls(mockedTurn);

        assertThat(game.consecutiveOpponentFouls, is(0));
        assertThat(game.consecutivePlayerFouls, is(0));
    }

    @Test
    public void setConsecutiveFoulsAddsOne() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.isFoul()).thenReturn(true);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);

        game.newGame = false;
        game.setConsecutiveFouls(mockedTurn);

        assertThat(game.consecutiveOpponentFouls, is(0));
        assertThat(game.consecutivePlayerFouls, is(1));
    }

    @Test
    public void setConsecutiveFoulsSeriousFoulRevertsFoulsToZero() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.isFoul()).thenReturn(true);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);
        when(mockedTurn.isSeriousFoul()).thenReturn(true);

        game.newGame = false;
        game.consecutivePlayerFouls = 3;

        game.setConsecutiveFouls(mockedTurn);

        assertThat(game.consecutivePlayerFouls, is(0));
    }

    @Test
    public void setConsecutiveFoulsNoFoulRevertsFoulsToZero() {
        ITurn mockedTurn = mock(ITurn.class);
        when(mockedTurn.isFoul()).thenReturn(false);
        when(mockedTurn.getShootingBallsMade()).thenReturn(0);
        when(mockedTurn.isSeriousFoul()).thenReturn(false);

        game.newGame = false;
        game.consecutivePlayerFouls = 3;

        game.setConsecutiveFouls(mockedTurn);

        assertThat(game.consecutivePlayerFouls, is(0));
    }
}
