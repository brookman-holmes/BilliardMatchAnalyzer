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
@SuppressWarnings("unused")
public class ApaNineBallGameTest extends AbstractGameTest {
    @Override public void setUp() {
        game = new ApaNineBallGame();
    }

    @Override List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Override Game createNewGame() {
        return new ApaNineBallGame();
    }

    @Test
    public void setAllowPushReturnsFalse() {
        assertThat(game.setAllowPush(
                turn().madeBalls(1, 2, 3).miss()), is(false));
    }

    @Test
    public void setAllowTurnSkipReturnsFalse() {
        assertThat(game.setAllowTurnSkip(
                turn().madeBalls(1, 2, 3).miss()), is(false));
    }

    @Test
    public void getPointsFromTurnReturns0() {
        TableStatus table = createTableStatus();
        ITurn turn = createGameTurn(TurnEnd.BREAK_MISS, table);

        assertThat(ApaNineBallGame.getPointsFromTurn(turn), is(0));
    }

    @Test
    public void getPointsFromTurnWithBreakBallsReturns3() {
        ITurn turn = turn().breakBalls(1, 2, 3).miss();

        assertThat(ApaNineBallGame.getPointsFromTurn(turn), is(3));
    }

    @Test
    public void getPointsFromTurnWithGameBallMadeReturns7() {
        ITurn turn = turn().breakBalls(1, 2, 3).madeBalls(4, 5, 9).win();

        assertThat(ApaNineBallGame.getPointsFromTurn(turn), is(7));
    }

    @Test
    public void getPointsFromTurnWithGameBallMadeOnBreakReturns2() {
        ITurn turn = turn().madeBalls(9).win();

        assertThat(ApaNineBallGame.getPointsFromTurn(turn), is(2));
    }

    @Override GameType thisGamesGameType() {
        return GameType.APA_NINE_BALL;
    }

    private ITurn createGameTurn(TurnEnd turnEnd, TableStatus tableStatus) {
        return new Turn(turnEnd, tableStatus, false, false, null);
    }

    private TableStatus createTableStatus() {
        return TableStatus.newTable(GameType.APA_NINE_BALL);
    }

    @Override int[] getAllBallsOnTable() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override int[] getBallsOnTableAfterRemoval() {
        return new int[]{2, 3, 4, 5, 6, 7, 9};
    }

    @Override void removeBalls() {
        game.ballsOnTable.removeAll(Arrays.asList(1, 8));
    }
}
