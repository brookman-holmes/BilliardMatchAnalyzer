package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;

import org.junit.Before;
import org.junit.Test;

import static com.brookmanholmes.billiards.game.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE_ON_BREAK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TableUtilsTest {
    private TableStatus tableStatus;
    @Before
    public void setUp() {
        tableStatus = TableStatus.newTable(GameType.BCA_EIGHT_BALL);
    }

    @Test
    public void getColorMadeShouldReturn4Solids() {
        setBallsTo(MADE, 1, 2, 3, 7, 8, 9);

        assertThat(TableUtils.getSolidsMade(tableStatus.getBallStatuses()), is(4));
    }

    @Test
    public void getColorMadeShouldReturn2Stripes() {
        setBallsTo(MADE, 7, 8, 9, 15);

        assertThat(TableUtils.getStripesMade(tableStatus.getBallStatuses()), is(2));
    }

    @Test
    public void getColorRemainingShouldReturn3Solids() {
        setBallsTo(MADE, 1, 2, 3, 7, 8, 9);

        assertThat(TableUtils.getSolidsRemaining(tableStatus.getBallStatuses()), is(3));
    }

    @Test
    public void getColorRemainingShouldReturn5Stripes() {
        setBallsTo(MADE, 7, 8, 9, 15);

        assertThat(TableUtils.getStripesRemaining(tableStatus.getBallStatuses()), is(5));
    }

    @Test
    public void getColorMadeOnBreakShouldReturn4Solids() {
        setBallsTo(MADE_ON_BREAK, 1, 2, 3, 7, 8, 9);

        assertThat(TableUtils.getSolidsMadeOnBreak(tableStatus.getBallStatuses()), is(4));
    }

    @Test
    public void getColorMadeOnBreakShouldReturn2Stripes() {
        setBallsTo(MADE_ON_BREAK, 7, 8, 9, 15);

        assertThat(TableUtils.getStripesMadeOnBreak(tableStatus.getBallStatuses()), is(2));
    }

    private void setBallsTo(BallStatus status, int... balls) {
        tableStatus.setBallTo(status, balls);
    }
}
