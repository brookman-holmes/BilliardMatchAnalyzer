package com.brookmanholmes.billiards.inning;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

import org.junit.Before;
import org.junit.Test;

import static com.brookmanholmes.billiards.game.util.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.inning.TableUtils.getSolidsMade;
import static com.brookmanholmes.billiards.inning.TableUtils.getSolidsMadeOnBreak;
import static com.brookmanholmes.billiards.inning.TableUtils.getSolidsRemaining;
import static com.brookmanholmes.billiards.inning.TableUtils.getStripesMade;
import static com.brookmanholmes.billiards.inning.TableUtils.getStripesMadeOnBreak;
import static com.brookmanholmes.billiards.inning.TableUtils.getStripesRemaining;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TableUtilsTest {
    TableStatus table;

    @Before
    public void setUp() {
        table = TableStatus.newTable(GameType.BCA_EIGHT_BALL);
    }

    @Test
    public void getColorMadeShouldReturn4Solids() {
        setBallsTo(MADE, 1, 2, 3, 7, 8, 9);

        assertThat(getSolidsMade(table), is(4));
    }

    @Test
    public void getColorMadeShouldReturn2Stripes() {
        setBallsTo(MADE, 7, 8, 9, 15);

        assertThat(getStripesMade(table), is(2));
    }

    @Test
    public void getColorRemainingShouldReturn3Solids() {
        setBallsTo(MADE, 1, 2, 3, 7, 8, 9);

        assertThat(getSolidsRemaining(table), is(3));
    }

    @Test
    public void getColorRemainingShouldReturn5Stripes() {
        setBallsTo(MADE, 7, 8, 9, 15);

        assertThat(getStripesRemaining(table), is(5));
    }

    @Test
    public void getColorMadeOnBreakShouldReturn4Solids() {
        setBallsTo(MADE_ON_BREAK, 1, 2, 3, 7, 8, 9);

        assertThat(getSolidsMadeOnBreak(table), is(4));
    }

    @Test
    public void getColorMadeOnBreakShouldReturn2Stripes() {
        setBallsTo(MADE_ON_BREAK, 7, 8, 9, 15);

        assertThat(getStripesMadeOnBreak(table), is(2));
    }

    private void setBallsTo(BallStatus status, int... balls) {
        table.setBallTo(status, balls);
    }
}
