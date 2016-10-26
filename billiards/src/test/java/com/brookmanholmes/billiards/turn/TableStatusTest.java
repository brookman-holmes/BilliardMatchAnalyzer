package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.game.BallStatus.DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.OFF_TABLE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/5/2015.
 */
public class TableStatusTest {
    private TableStatus tableStatus;

    @Before
    public void setUp() {
        tableStatus = TableStatus.newTable(GameType.BCA_EIGHT_BALL);
    }

    @Test
    public void getBallsThatAreOffTableRemovesCorrectBalls() {
        tableStatus.setBallTo(MADE, 1, 2, 3, 4, 5, 15);

        List<Integer> expectedBallsRemoved = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 15, 5)
        );

        List<Integer> ballsRemoved = tableStatus.getBallsToRemoveFromTable();

        assertThat(ballsRemoved.containsAll(expectedBallsRemoved), is(true));
    }

    @Test(expected = InvalidBallException.class)
    public void getBallStatusThrowsException() {
        tableStatus.getBallStatus(16);
    }

    @Test
    public void sizeReturns15() {
        assertThat(tableStatus.size(), is(15));
    }

    @Test
    public void testRemoveBallsFromTable() {
        tableStatus.removeBallsFromTable(1, 2, 3, 4, 15, 5);

        assertThat(tableStatus.getBallStatus(1), is(OFF_TABLE));
        assertThat(tableStatus.getBallStatus(2), is(OFF_TABLE));
        assertThat(tableStatus.getBallStatus(3), is(OFF_TABLE));
        assertThat(tableStatus.getBallStatus(4), is(OFF_TABLE));
        assertThat(tableStatus.getBallStatus(15), is(OFF_TABLE));
        assertThat(tableStatus.getBallStatus(5), is(OFF_TABLE));
    }

    @Test
    public void testSetBallStatus() {
        tableStatus.setBallTo(MADE, 1);
        tableStatus.setBallTo(OFF_TABLE, 1);

        assertThat(tableStatus.getBallStatus(1), is(OFF_TABLE));
    }

    @Test
    public void setBallStatusWithNoNumbers() {
        tableStatus.setBallTo(MADE);

        assertThat(tableStatus.getBallsRemaining(), is(15));
    }

    @Test
    public void testGetDeadBalls() {
        tableStatus.setBallTo(DEAD, 1, 15);
        assertThat(tableStatus.getDeadBalls(), is(2));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_DEAD, 8);
        assertThat(tableStatus.getDeadBalls(), is(3));
    }

    @Test
    public void testGetDeadBallsOnBreak() {
        tableStatus.setBallTo(DEAD_ON_BREAK, 1, 15);
        assertThat(tableStatus.getDeadBallsOnBreak(), is(2));
    }

    @Test
    public void testGetBallsOnBreak() {
        tableStatus.setBallTo(MADE_ON_BREAK, 1, 15);
        assertThat(tableStatus.getBreakBallsMade(), is(2));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK, 8);
        assertThat(tableStatus.getBreakBallsMade(), is(3));
    }

    @Test
    public void testGetShootingBallsMade() {
        tableStatus.setBallTo(MADE_ON_BREAK, 1, 15);
        tableStatus.setBallTo(MADE, 2, 3, 4, 5);
        assertThat(tableStatus.getShootingBallsMade(), is(4));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_MADE, 8);
        assertThat(tableStatus.getShootingBallsMade(), is(5));
    }

    @Test
    public void testGetBallsRemaining() {
        assertThat(tableStatus.getBallsRemaining(), is(15));

        tableStatus.setBallTo(MADE_ON_BREAK, 1, 15);
        tableStatus.setBallTo(MADE, 2, 3, 4, 5);

        assertThat(tableStatus.getBallsRemaining(), is(9));
    }

    @Test
    public void testGetGameBallMade() {
        tableStatus.setBallTo(MADE, 8);
        assertThat(tableStatus.isGameBallMade(), is(true));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_MADE, 8);
        assertThat(tableStatus.isGameBallMade(), is(true));
    }

    @Test
    public void testGetGameBallMadeOnBreak() {
        tableStatus.setBallTo(MADE_ON_BREAK, 8);
        assertThat(tableStatus.isGameBallMadeOnBreak(), is(true));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_MADE, 8);
        assertThat(tableStatus.isGameBallMadeOnBreak(), is(true));

        tableStatus.setBallTo(GAME_BALL_MADE_ON_BREAK_THEN_DEAD, 8);
        assertThat(tableStatus.isGameBallMadeOnBreak(), is(true));
    }

    @Test
    public void testEquality() {
        TableStatus table = TableStatus.newTable(GameType.BCA_EIGHT_BALL);

        assertThat(table, is(tableStatus));
        assertThat(table, is(table));

        table.setBallTo(MADE, 1, 15);
        tableStatus.setBallTo(MADE, 1, 15);

        assertThat(table, is(tableStatus));

        assertThat(table.hashCode(), is(tableStatus.hashCode()));
    }

    @Test(expected = InvalidBallException.class)
    public void testConstructorThrowsException() {
        tableStatus = new TableStatus(8, 9, GameType.BCA_NINE_BALL);
    }

    @Test(expected = InvalidGameTypeException.class)
    public void testStaticMethodThrowsException() {
        tableStatus = TableStatus.newTable(GameType.AMERICAN_ROTATION);
    }

    @Test
    public void creatingTableWithBallsPreRemoved() {
        List<Integer> ballsToKeepOnTable = new ArrayList<>(
                Arrays.asList(1, 2, 3, 6, 7, 8, 9)
        );

        TableStatus table = TableStatus.newTable(GameType.BCA_NINE_BALL, ballsToKeepOnTable);

        assertThat(table.getBallsRemaining(), is(7));
        assertThat(table.getBallsToRemoveFromTable().containsAll(Arrays.asList(4, 5)), is(true));
    }

    @Test(expected = InvalidBallException.class)
    public void createTableWithGameBallRemovedThrowsExcpetion() {
        List<Integer> ballsToKeepOnTable = new ArrayList<>(
                Arrays.asList(1, 2, 3, 6, 7, 8)
        );

        TableStatus.newTable(GameType.BCA_NINE_BALL, ballsToKeepOnTable);
    }
}
