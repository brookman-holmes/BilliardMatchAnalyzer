package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;

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
    GameType gameType;
    GameStatus.Builder gameBuilder;
    int GAME_BALL;
    boolean showScratchOnDeadBall = false;


    @Before
    public abstract void setUp();

    @Test
    public void showWinIfWinOnBreakIsTrue() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, GAME_BALL);

        helper.game = gameBuilder.newGame().build();
        helper.nextInning = tableStatus;

        assertThat(helper.showWin(), is(helper.game.winOnBreak));
    }

    @Test
    public void showScratchOnDeadBall() {
        tableStatus.setBallTo(BallStatus.DEAD, 5);
        helper.nextInning = tableStatus;

        assertThat(helper.checkScratch(), is(showScratchOnDeadBall));
    }

    @Test
    public void showScratchOnDeadBallOnBreak() {
        tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, 5);
        helper.nextInning = tableStatus;

        // TODO: 11/14/2015 when i do straight pool this will have to be modified
        assertThat(helper.checkScratch(), is(true));
    }

    @Test
    public void showLoss() {
        setupLossStuff();

        helper.nextInning = tableStatus;
        helper.game = gameBuilder.build();

        assertThat(helper.showLoss(), is(true));
    }

    @Test
    public void showDefaultSelection() {
        helper.game = gameBuilder.build();
        helper.nextInning = tableStatus;

        assertThat(helper.showMiss(), is(true));
        assertThat(helper.showSafety(), is(true));
        assertThat(helper.showSafetyMiss(), is(true));
    }

    @Test
    public void showBreakMiss() {
        helper.game = gameBuilder.newGame().build();
        helper.nextInning = tableStatus;

        assertThat(helper.showBreakMiss(), is(true));
    }


    abstract void setupLossStuff();
}
