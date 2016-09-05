package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class RotationTurnEndHelperTest extends AbstractTurnEndHelperTest {
    @Override public void setUp() {
        tableStatus = TableStatus.newTable(GameType.BCA_NINE_BALL);
        showScratchOnDeadBall = true;
        gameBuilder = new GameStatus.Builder(GameType.BCA_NINE_BALL);
    }

    @Override void setupLossStuff() {
        gameBuilder.currentPlayerConsecutiveFouls(2);
    }


    @Test
    public void showPushOnBallsMadeOnBreak() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, 1);
        helper = TurnEndHelper.create(gameBuilder.allowPush().newGame().build(), tableStatus);

        assertThat(helper.showPush(), is(true));
    }

    @Test
    public void dontShowPushOnBallsMadeOnBreakWithMoreBallsMade() {
        helper = TurnEndHelper.create(gameBuilder.allowPush().build(), tableStatus);

        assertThat(helper.showPush(), is(true));
    }

    @Test
    public void showTurnSkipAfterPush() {
        helper = TurnEndHelper.create(gameBuilder.allowSkip().build(), tableStatus);
        assertThat(helper.showTurnSkip(), is(true));

        TurnEndOptions options = helper.getTurnEndOptions().build();

        assertThat(options.possibleEndings.contains(TurnEnd.SKIP_TURN), is(true));
    }

    @Test
    public void dontShowTurnSkipAfterPushWithMoreBallsMade() {
        tableStatus.setBallTo(BallStatus.MADE, 3);
        helper = TurnEndHelper.create(gameBuilder.allowSkip().build(), tableStatus);

        assertThat(helper.showTurnSkip(), is(false));
    }
}
