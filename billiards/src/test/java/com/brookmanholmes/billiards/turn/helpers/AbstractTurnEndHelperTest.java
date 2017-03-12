package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
@SuppressWarnings("unused")
public abstract class AbstractTurnEndHelperTest {
    TurnEndHelper helper;
    TableStatus tableStatus;
    ITurn turn;
    GameStatus.Builder gameBuilder;
    boolean showFoulOnDeadBall = false;


    @Before
    public abstract void setUp();

    @Test
    public void showWinIfWinOnBreakIsTrue() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, gameBuilder.build().gameType.getGameBall());

        helper = TurnEndHelper.create(gameBuilder.newGame().build(), tableStatus);

        assertThat(helper.showWin(), is(helper.game.winOnBreak));
    }

    @Test
    public void showFoulOnDeadBallOnBreak() {
        tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, 5);
        helper = TurnEndHelper.create(gameBuilder.newGame().build(), tableStatus);

        // TODO: 11/14/2015 when i do straight pool this will have to be modified
        assertThat(helper.checkFoul(), is(true));
    }

    @Test
    public void showLoss() {
        setupLossStuff();

        helper = TurnEndHelper.create(gameBuilder.build(), tableStatus);

        assertThat(helper.seriousFoul(), is(true));
    }

    @Test
    public void showDefaultSelection() {
        helper = TurnEndHelper.create(gameBuilder.build(), tableStatus);

        assertThat(helper.showMiss(), is(true));
        assertThat(helper.showSafety(), is(true));
        assertThat(helper.showSafetyMiss(), is(true));
    }

    @Test
    public void showBreakMiss() {
        helper = TurnEndHelper.create(gameBuilder.newGame().build(), tableStatus);

        assertThat(helper.showBreakMiss(), is(true));
    }


    abstract void setupLossStuff();
}
