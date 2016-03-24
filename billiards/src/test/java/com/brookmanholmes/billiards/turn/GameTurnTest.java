package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.GameType;

import org.junit.Before;
import org.junit.Test;

import static com.brookmanholmes.billiards.game.util.BallStatus.DEAD;
import static com.brookmanholmes.billiards.game.util.BallStatus.DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.turn.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/5/2015.
 */
public class GameTurnTest {
    com.brookmanholmes.billiards.game.Turn turn;
    TableStatus table;

    @Before
    public void setUp() {
        table = TableStatus.newTable(GameType.BCA_NINE_BALL);
    }

    @Test
    public void getShootingBallsMadeReturns5() {
        table.setBallTo(MADE, 1, 2, 3, 4);
        table.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_MADE, 9);

        turn = createGameTurn(table, false, GAME_WON, false);

        assertThat(turn.getShootingBallsMade(), is(5));
    }

    @Test
    public void getDeadBallsReturns2() {
        table.setBallTo(DEAD, 1, 9);

        turn = createGameTurn(table, true, MISS, false);

        assertThat(turn.getDeadBalls(), is(2));
    }

    @Test
    public void getDeadBallsOnBreakReturns2() {
        table.setBallTo(DEAD_ON_BREAK, 1, 9);

        turn = createGameTurn(table, true, BREAK_MISS, false);

        assertThat(turn.getDeadBallsOnBreak(), is(2));
    }

    @Test
    public void getBreakBallsMadeReturns2() {
        table.setBallTo(MADE_ON_BREAK, 1, 9);

        turn = createGameTurn(table, false, GAME_WON, false);

        assertThat(turn.getBreakBallsMade(), is(2));
    }

    @Test
    public void isScratchIsTrue() {
        table.setBallTo(MADE_ON_BREAK, 1, 9);

        turn = createGameTurn(table, true, MISS, true);

        assertThat(turn.isScratch(), is(true));
    }

    @Test
    public void isScratchIsFalse() {
        table.setBallTo(MADE_ON_BREAK, 1, 9);

        turn = createGameTurn(table, false, MISS, true);

        assertThat(turn.isScratch(), is(false));
    }

    @Test
    public void getTurnEndReturnsTurnEnd() {
        turn = createGameTurn(table, false, MISS, false);

        assertThat(turn.getTurnEnd(), is(MISS));
    }


    private Turn createGameTurn(TableStatus table, boolean scratch, com.brookmanholmes.billiards.turn.TurnEnd turnEnd, boolean isGameLost) {
        return new com.brookmanholmes.billiards.turn.GameTurn(0, 0l, scratch, turnEnd, table, isGameLost);
    }
}