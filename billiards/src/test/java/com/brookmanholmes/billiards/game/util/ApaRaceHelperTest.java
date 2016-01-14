package com.brookmanholmes.billiards.game.util;

import org.junit.Test;

import static com.brookmanholmes.billiards.game.util.ApaRaceToHelper.apa8BallRaceTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by helios on 1/9/2016.
 */
public class ApaRaceHelperTest {
    @Test
    public void rank22Returns22() {
        assertThat(apa8BallRaceTo(2, 2), is(new RaceTo(2, 2)));
    }

    @Test
    public void rank23Returns23() {
        assertThat(apa8BallRaceTo(2, 3), is(new RaceTo(2, 3)));
    }

    @Test
    public void rank24Returns24() {
        assertThat(apa8BallRaceTo(2, 4), is(new RaceTo(2, 4)));
    }

    @Test
    public void rank25Returns25() {
        assertThat(apa8BallRaceTo(2, 5), is(new RaceTo(2, 5)));
    }

    @Test
    public void rank26Returns26() {
        assertThat(apa8BallRaceTo(2, 6), is(new RaceTo(2, 6)));
    }

    @Test
    public void rank27Returns27() {
        assertThat(apa8BallRaceTo(2, 7), is(new RaceTo(2, 7)));
    }

    @Test
    public void rank32Returns32() {
        assertThat(apa8BallRaceTo(3, 2), is(new RaceTo(3, 2)));
    }

    @Test
    public void rank33Returns22() {
        assertThat(apa8BallRaceTo(3, 3), is(new RaceTo(2, 2)));
    }

    @Test
    public void rank34Returns23() {
        assertThat(apa8BallRaceTo(3, 4), is(new RaceTo(2, 3)));
    }

    @Test
    public void rank35Returns24() {
        assertThat(apa8BallRaceTo(3, 5), is(new RaceTo(2, 4)));
    }

    @Test
    public void rank36Returns25() {
        assertThat(apa8BallRaceTo(3, 6), is(new RaceTo(2, 5)));
    }

    @Test
    public void rank37Returns26() {
        assertThat(apa8BallRaceTo(3, 7), is(new RaceTo(2, 6)));
    }

    @Test
    public void rank42Returns42() {
        assertThat(apa8BallRaceTo(4, 2), is(new RaceTo(4, 2)));
    }

    @Test
    public void rank43Returns32() {
        assertThat(apa8BallRaceTo(4, 3), is(new RaceTo(3, 2)));
    }

    @Test
    public void rank44Returns33() {
        assertThat(apa8BallRaceTo(4, 4), is(new RaceTo(3, 3)));
    }

    @Test
    public void rank45Returns34() {
        assertThat(apa8BallRaceTo(4, 5), is(new RaceTo(3, 4)));
    }

    @Test
    public void rank46Returns35() {
        assertThat(apa8BallRaceTo(4, 6), is(new RaceTo(3, 5)));
    }

    @Test
    public void rank47Returns25() {
        assertThat(apa8BallRaceTo(4, 7), is(new RaceTo(2, 5)));
    }

    @Test
    public void rank52Returns52() {
        assertThat(apa8BallRaceTo(5, 2), is(new RaceTo(5, 2)));
    }

    @Test
    public void rank53Returns42() {
        assertThat(apa8BallRaceTo(5, 3), is(new RaceTo(4, 2)));
    }

    @Test
    public void rank54Returns43() {
        assertThat(apa8BallRaceTo(5, 4), is(new RaceTo(4, 3)));
    }

    @Test
    public void rank55Returns44() {
        assertThat(apa8BallRaceTo(5, 5), is(new RaceTo(4, 4)));
    }

    @Test
    public void rank56Returns45() {
        assertThat(apa8BallRaceTo(5, 6), is(new RaceTo(4, 5)));
    }

    @Test
    public void rank57Returns35() {
        assertThat(apa8BallRaceTo(5, 7), is(new RaceTo(3, 5)));
    }

    @Test
    public void rank62Returns62() {
        assertThat(apa8BallRaceTo(6, 2), is(new RaceTo(6, 2)));
    }

    @Test
    public void rank63Returns52() {
        assertThat(apa8BallRaceTo(6, 3), is(new RaceTo(5, 2)));
    }

    @Test
    public void rank64Returns53() {
        assertThat(apa8BallRaceTo(6, 4), is(new RaceTo(5, 3)));
    }

    @Test
    public void rank65Returns54() {
        assertThat(apa8BallRaceTo(6, 5), is(new RaceTo(5, 4)));
    }

    @Test
    public void rank66Returns55() {
        assertThat(apa8BallRaceTo(6, 6), is(new RaceTo(5, 5)));
    }

    @Test
    public void rank67Returns45() {
        assertThat(apa8BallRaceTo(6, 7), is(new RaceTo(4, 5)));
    }

    @Test
    public void rank72Returns72() {
        assertThat(apa8BallRaceTo(7, 2), is(new RaceTo(7, 2)));
    }

    @Test
    public void rank73Returns62() {
        assertThat(apa8BallRaceTo(7, 3), is(new RaceTo(6, 2)));
    }

    @Test
    public void rank74Returns52() {
        assertThat(apa8BallRaceTo(7, 4), is(new RaceTo(5, 2)));
    }

    @Test
    public void rank75Returns53() {
        assertThat(apa8BallRaceTo(7, 5), is(new RaceTo(5, 3)));
    }

    @Test
    public void rank76Returns54() {
        assertThat(apa8BallRaceTo(7, 6), is(new RaceTo(5, 4)));
    }

    @Test
    public void rank77Returns55() {
        assertThat(apa8BallRaceTo(7, 7), is(new RaceTo(5, 5)));
    }

    @Test
    public void rankOutOfRangeReturns00() {
        RaceTo expected = new RaceTo(0, 0);
        assertThat(apa8BallRaceTo(8,2), is(expected));
        assertThat(apa8BallRaceTo(7,1), is(expected));
        assertThat(apa8BallRaceTo(1,2), is(expected));
        assertThat(apa8BallRaceTo(7,8), is(expected));
    }
}
