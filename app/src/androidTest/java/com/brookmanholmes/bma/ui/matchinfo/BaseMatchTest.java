package com.brookmanholmes.bma.ui.matchinfo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.data.SampleMatchProvider;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Brookman Holmes on 9/7/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class BaseMatchTest {
    Match match;
    DatabaseAdapter database;

    @Rule
    public ActivityTestRule<MatchInfoActivity> activityRule =
            new ActivityTestRule<>(MatchInfoActivity.class, true, false);

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = new DatabaseAdapter(context);
        match = getMatch();
        long id = database.insertMatch(match);
        Intent intent = new Intent(context, MatchInfoActivity.class);
        intent.putExtra(MatchInfoActivity.ARG_MATCH_ID, id);
        activityRule.launchActivity(intent);
    }

    @After
    public void tearDown() {
        database.deleteMatch(match.getMatchId());
    }

    @Test
    public void testNames() {
        onView(ViewMatchers.withId(R.id.playerName)).check(matches(withText(match.getPlayer().getName())));
        onView(withId(R.id.opponentName)).check(matches(withText(match.getOpponent().getName())));
    }

    @Test
    public void testInputTurn() {
        for (ITurn turn : getTurns()) {
            insertTurn(turn);
            match.addTurn(turn);
        }
    }

    private void insertTurn(ITurn turn) {
        onView(withId(R.id.buttonAddTurn)).perform(click());

        if (match.getGameStatus().newGame) {
            selectBreakBalls(turn);

            nextPage();
            if (turn.getBreakBallsMade() > 0) {
                selectBalls(turn);
                nextPage();
            }
        } else {
            selectBalls(turn);
            nextPage();
        }

        onView(withText(getTurnEnd(turn))).perform(click());

        selectFoul(turn);

        nextPage();
    }

    private String getTurnEnd(ITurn turn) {
        return MatchDialogHelperUtils.convertTurnEndToString(
                activityRule.getActivity(),
                turn.getTurnEnd(),
                match.getCurrentPlayersName(),
                match.getNonCurrentPlayersName());
    }

    private void selectFoul(ITurn turn) {
        if (turn.isGameLost())
            onView(withId(R.id.lostGame)).perform(click());
        else if (turn.isFoul())
            onView(withId(R.id.yes)).perform(click());
        else if (turn.getTurnEnd() == TurnEnd.MISS ||
                turn.getTurnEnd() == TurnEnd.SAFETY_ERROR ||
                turn.getTurnEnd() == TurnEnd.BREAK_MISS ||
                turn.getTurnEnd() == TurnEnd.ILLEGAL_BREAK)
            onView(withId(R.id.no)).perform(click());
    }

    private void nextPage() {
        onView(withId(R.id.next_button)).perform(click());
    }

    private void clickBall(int ball) {
        onView(allOf(withId(MatchDialogHelperUtils.convertBallToId(ball)), isDisplayed())).perform(click());
    }

    private boolean isMadeOnBreak(BallStatus status) {
        return status == BallStatus.MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK;
    }

    private boolean isDeadOnBreak(BallStatus status) {
        return status == BallStatus.DEAD_ON_BREAK || status == BallStatus.GAME_BALL_DEAD_ON_BREAK;
    }

    private boolean isMade(BallStatus status) {
        return status == BallStatus.MADE ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
    }

    private boolean isDead(BallStatus status) {
        return status == BallStatus.DEAD ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }

    private void selectBreakBalls(ITurn turn) {
        for (int ball = 1; ball <= turn.size(); ball++) {
            if (isMadeOnBreak(turn.getBallStatus(ball)))
                clickBall(ball);
            else if (isDeadOnBreak(turn.getBallStatus(ball))) {
                clickBall(ball);
                clickBall(ball);
            }
        }
    }

    private void selectBalls(ITurn turn) {
        for (int ball = 1; ball <= turn.size(); ball++) {
            if (isMade(turn.getBallStatus(ball)))
                clickBall(ball);
            else if (isDead(turn.getBallStatus(ball))) {
                clickBall(ball);
                clickBall(ball);
            }
        }
    }

    protected abstract Match getMatch();
    protected abstract List<ITurn> getTurns();
}
