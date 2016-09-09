package com.brookmanholmes.bma.ui.matchinfo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
        onView(withId(R.id.action_match_view)).perform(click());
        for (ITurn turn : getTurns()) {
            insertTurn(turn);
            match.addTurn(turn);
        }
    }

    private void insertTurn(ITurn turn) {
        onView(withId(R.id.buttonAddTurn)).perform(click());

        if (match.getGameStatus().newGame) {
            selectBreakBalls(turn);
            checkImageLevelsOfBreakBalls(turn);

            nextPage();
            if (turn.getBreakBallsMade() > 0) {
                selectBalls(turn);
                nextPage();
            }
        } else if (!match.getGameStatus().playerAllowedToBreakAgain) {
            selectBalls(turn);
            checkImageLevelsOfBalls(turn);
            nextPage();
        }

        checkDisplayedOptions(turn);

        nextPage();

        if (turn.getAdvStats() != null)
            doAdvancedStats(turn);
    }

    private void checkDisplayedOptions(ITurn turn) {
        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(match.getGameStatus(), turn);
        for (TurnEnd end : options.possibleEndings) {
            onView(allOf(isDisplayed(), withText(getTurnEndString(end))));
        }

        onView(withText(getTurnEndString(turn.getTurnEnd()))).perform(click());
        selectFoul(turn);
    }

    private String getTurnEndString(TurnEnd end) {
        return MatchDialogHelperUtils.convertTurnEndToString(
                activityRule.getActivity(),
                end,
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
                status == BallStatus.GAME_BALL_MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
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

    private void checkImageLevelsOfBreakBalls(ITurn turn) {
        for (int ball = 1; ball <= turn.size(); ball++) {
            int level = 1;
            if (isMadeOnBreak(turn.getBallStatus(ball)))
                level = 2;
            else if (isDeadOnBreak(turn.getBallStatus(ball)))
                level = 3;

            onView(allOf(isDisplayed(),
                    withId(MatchDialogHelperUtils.convertBallToId(ball)),
                    withImageLevel(level)));

        }
    }

    private void checkImageLevelsOfBalls(ITurn turn) {
        for (int ball = 1; ball <= turn.size(); ball++) {
            int level = 1;
            if (isMade(turn.getBallStatus(ball)))
                level = 2;
            else if (isDead(turn.getBallStatus(ball)))
                level = 3;
            else if (isMadeOnBreak(turn.getBallStatus(ball)))
                level = 0;
            else if (turn.getBallStatus(ball) == BallStatus.OFF_TABLE)
                level = 0;

            onView(allOf(isDisplayed(),
                    withId(MatchDialogHelperUtils.convertBallToId(ball)),
                    withImageLevel(level)));

        }
    }

    private static Matcher<View> withImageLevel(final int level) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            protected boolean matchesSafely(ImageView item) {
                return item.getDrawable().getLevel() == level;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected image level " + level + " is not correct");
            }
        };
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

    private void doAdvancedStats(ITurn turn) {

    }

    protected abstract Match getMatch();
    protected abstract List<ITurn> getTurns();
}
