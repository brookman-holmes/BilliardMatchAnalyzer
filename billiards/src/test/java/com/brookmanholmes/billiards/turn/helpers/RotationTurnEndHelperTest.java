package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
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
    @Override
    public void setUp() {
        helper = TurnEndHelper.newTurnEndHelper(GameType.BCA_NINE_BALL);
        tableStatus = TableStatus.newTable(GameType.BCA_NINE_BALL);
        gameType = GameType.BCA_NINE_BALL;
        GAME_BALL = 9;
        showScratchOnDeadBall = true;
        gameBuilder = new GameStatus.Builder(gameType);
    }

    @Override
    void setupLossStuff() {
        gameBuilder.currentPlayerConsecutiveFouls(2);
    }


    @Test
    public void showPushOnBallsMadeOnBreak() {
        helper.game = gameBuilder.allowPush().newGame().build();
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, 1);
        setupTurn();
        helper.nextInning = turn;

        assertThat(helper.showPush(), is(true));
    }

    @Test
    public void dontShowPushOnBallsMadeOnBreakWithMoreBallsMade() {
        helper.game = gameBuilder.allowPush().build();
        setupTurn();
        helper.nextInning = turn;

        assertThat(helper.showPush(), is(true));
    }

    @Test
    public void showTurnSkipAfterPush() {
        setupTurn();
        helper.nextInning = turn;
        helper.game = gameBuilder.allowSkip().build();

        assertThat(helper.showTurnSkip(), is(true));

        TurnEndOptions options = helper.createTurnEndOptionsBuilder().build();

        assertThat(options.possibleEndings.contains(TurnEnd.SKIP_TURN), is(true));
    }

    @Test
    public void dontShowTurnSkipAfterPushWithMoreBallsMade() {
        tableStatus.setBallTo(BallStatus.MADE, 3);
        setupTurn();
        helper.nextInning = turn;
        helper.game = gameBuilder.allowSkip().build();

        assertThat(helper.showTurnSkip(), is(false));
    }
}
