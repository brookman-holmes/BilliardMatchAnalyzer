package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.GameTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public abstract class AbstractTurnEndHelperTest {
    TurnEndHelper helper;
    TableStatus tableStatus;
    Turn turn;
    GameType gameType;
    GameStatus.Builder gameBuilder;
    int GAME_BALL;
    boolean showScratchOnDeadBall = false;


    @Before
    public abstract void setUp();

    void setupTurn() {
        turn = new GameTurn(0, 0, false, TurnEnd.MISS, tableStatus, false, null);
    }

    @Test
    public void showWinIfWinOnBreakIsTrue() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, GAME_BALL);

        helper.game = gameBuilder.newGame().build();
        setupTurn();
        helper.nextInning = turn;

        assertThat(helper.showWin(), is(helper.game.winOnBreak));
    }

    @Test
    public void showScratchOnDeadBall() {
        tableStatus.setBallTo(BallStatus.DEAD, 5);
        setupTurn();
        helper.nextInning = turn;
        helper.game = gameBuilder.newGame().build();

        assertThat(helper.checkFoul(), is(showScratchOnDeadBall));
    }

    @Test
    public void showScratchOnDeadBallOnBreak() {
        tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, 5);
        setupTurn();
        helper.nextInning = turn;

        // TODO: 11/14/2015 when i do straight pool this will have to be modified
        assertThat(helper.checkFoul(), is(true));
    }

    @Test
    public void showLoss() {
        setupLossStuff();

        setupTurn();
        helper.nextInning = turn;
        helper.game = gameBuilder.build();

        assertThat(helper.lostGame(), is(true));
    }

    @Test
    public void showDefaultSelection() {
        helper.game = gameBuilder.build();
        setupTurn();
        helper.nextInning = turn;

        assertThat(helper.showMiss(), is(true));
        assertThat(helper.showSafety(), is(true));
        assertThat(helper.showSafetyMiss(), is(true));
    }

    @Test
    public void showBreakMiss() {
        helper.game = gameBuilder.newGame().build();
        setupTurn();
        helper.nextInning = turn;

        assertThat(helper.showBreakMiss(), is(true));
    }


    abstract void setupLossStuff();
}
