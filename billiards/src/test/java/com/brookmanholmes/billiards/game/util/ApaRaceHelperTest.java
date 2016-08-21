package com.brookmanholmes.billiards.game.util;

import org.junit.Test;

import static com.brookmanholmes.billiards.player.ApaRaceToHelper.apa8BallRaceTo;
import static com.brookmanholmes.billiards.player.ApaRaceToHelper.getMinimumMatchPointsEarned;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by helios on 1/9/2016.
 */
public class ApaRaceHelperTest {
    @Test
    public void rank22Returns22() {
        assertThat(apa8BallRaceTo(2, 2), is(new com.brookmanholmes.billiards.player.RaceTo(2, 2)));
    }

    @Test
    public void rank23Returns23() {
        assertThat(apa8BallRaceTo(2, 3), is(new com.brookmanholmes.billiards.player.RaceTo(2, 3)));
    }

    @Test
    public void rank24Returns24() {
        assertThat(apa8BallRaceTo(2, 4), is(new com.brookmanholmes.billiards.player.RaceTo(2, 4)));
    }

    @Test
    public void rank25Returns25() {
        assertThat(apa8BallRaceTo(2, 5), is(new com.brookmanholmes.billiards.player.RaceTo(2, 5)));
    }

    @Test
    public void rank26Returns26() {
        assertThat(apa8BallRaceTo(2, 6), is(new com.brookmanholmes.billiards.player.RaceTo(2, 6)));
    }

    @Test
    public void rank27Returns27() {
        assertThat(apa8BallRaceTo(2, 7), is(new com.brookmanholmes.billiards.player.RaceTo(2, 7)));
    }

    @Test
    public void rank32Returns32() {
        assertThat(apa8BallRaceTo(3, 2), is(new com.brookmanholmes.billiards.player.RaceTo(3, 2)));
    }

    @Test
    public void rank33Returns22() {
        assertThat(apa8BallRaceTo(3, 3), is(new com.brookmanholmes.billiards.player.RaceTo(2, 2)));
    }

    @Test
    public void rank34Returns23() {
        assertThat(apa8BallRaceTo(3, 4), is(new com.brookmanholmes.billiards.player.RaceTo(2, 3)));
    }

    @Test
    public void rank35Returns24() {
        assertThat(apa8BallRaceTo(3, 5), is(new com.brookmanholmes.billiards.player.RaceTo(2, 4)));
    }

    @Test
    public void rank36Returns25() {
        assertThat(apa8BallRaceTo(3, 6), is(new com.brookmanholmes.billiards.player.RaceTo(2, 5)));
    }

    @Test
    public void rank37Returns26() {
        assertThat(apa8BallRaceTo(3, 7), is(new com.brookmanholmes.billiards.player.RaceTo(2, 6)));
    }

    @Test
    public void rank42Returns42() {
        assertThat(apa8BallRaceTo(4, 2), is(new com.brookmanholmes.billiards.player.RaceTo(4, 2)));
    }

    @Test
    public void rank43Returns32() {
        assertThat(apa8BallRaceTo(4, 3), is(new com.brookmanholmes.billiards.player.RaceTo(3, 2)));
    }

    @Test
    public void rank44Returns33() {
        assertThat(apa8BallRaceTo(4, 4), is(new com.brookmanholmes.billiards.player.RaceTo(3, 3)));
    }

    @Test
    public void rank45Returns34() {
        assertThat(apa8BallRaceTo(4, 5), is(new com.brookmanholmes.billiards.player.RaceTo(3, 4)));
    }

    @Test
    public void rank46Returns35() {
        assertThat(apa8BallRaceTo(4, 6), is(new com.brookmanholmes.billiards.player.RaceTo(3, 5)));
    }

    @Test
    public void rank47Returns25() {
        assertThat(apa8BallRaceTo(4, 7), is(new com.brookmanholmes.billiards.player.RaceTo(2, 5)));
    }

    @Test
    public void rank52Returns52() {
        assertThat(apa8BallRaceTo(5, 2), is(new com.brookmanholmes.billiards.player.RaceTo(5, 2)));
    }

    @Test
    public void rank53Returns42() {
        assertThat(apa8BallRaceTo(5, 3), is(new com.brookmanholmes.billiards.player.RaceTo(4, 2)));
    }

    @Test
    public void rank54Returns43() {
        assertThat(apa8BallRaceTo(5, 4), is(new com.brookmanholmes.billiards.player.RaceTo(4, 3)));
    }

    @Test
    public void rank55Returns44() {
        assertThat(apa8BallRaceTo(5, 5), is(new com.brookmanholmes.billiards.player.RaceTo(4, 4)));
    }

    @Test
    public void rank56Returns45() {
        assertThat(apa8BallRaceTo(5, 6), is(new com.brookmanholmes.billiards.player.RaceTo(4, 5)));
    }

    @Test
    public void rank57Returns35() {
        assertThat(apa8BallRaceTo(5, 7), is(new com.brookmanholmes.billiards.player.RaceTo(3, 5)));
    }

    @Test
    public void rank62Returns62() {
        assertThat(apa8BallRaceTo(6, 2), is(new com.brookmanholmes.billiards.player.RaceTo(6, 2)));
    }

    @Test
    public void rank63Returns52() {
        assertThat(apa8BallRaceTo(6, 3), is(new com.brookmanholmes.billiards.player.RaceTo(5, 2)));
    }

    @Test
    public void rank64Returns53() {
        assertThat(apa8BallRaceTo(6, 4), is(new com.brookmanholmes.billiards.player.RaceTo(5, 3)));
    }

    @Test
    public void rank65Returns54() {
        assertThat(apa8BallRaceTo(6, 5), is(new com.brookmanholmes.billiards.player.RaceTo(5, 4)));
    }

    @Test
    public void rank66Returns55() {
        assertThat(apa8BallRaceTo(6, 6), is(new com.brookmanholmes.billiards.player.RaceTo(5, 5)));
    }

    @Test
    public void rank67Returns45() {
        assertThat(apa8BallRaceTo(6, 7), is(new com.brookmanholmes.billiards.player.RaceTo(4, 5)));
    }

    @Test
    public void rank72Returns72() {
        assertThat(apa8BallRaceTo(7, 2), is(new com.brookmanholmes.billiards.player.RaceTo(7, 2)));
    }

    @Test
    public void rank73Returns62() {
        assertThat(apa8BallRaceTo(7, 3), is(new com.brookmanholmes.billiards.player.RaceTo(6, 2)));
    }

    @Test
    public void rank74Returns52() {
        assertThat(apa8BallRaceTo(7, 4), is(new com.brookmanholmes.billiards.player.RaceTo(5, 2)));
    }

    @Test
    public void rank75Returns53() {
        assertThat(apa8BallRaceTo(7, 5), is(new com.brookmanholmes.billiards.player.RaceTo(5, 3)));
    }

    @Test
    public void rank76Returns54() {
        assertThat(apa8BallRaceTo(7, 6), is(new com.brookmanholmes.billiards.player.RaceTo(5, 4)));
    }

    @Test
    public void rank77Returns55() {
        assertThat(apa8BallRaceTo(7, 7), is(new com.brookmanholmes.billiards.player.RaceTo(5, 5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rankOutOfRangeReturnsThrowsException0() {
        apa8BallRaceTo(8, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rankOutOfRangeReturnsThrowsException1() {
        apa8BallRaceTo(7, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rankOutOfRangeReturnsThrowsException2() {
        apa8BallRaceTo(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rankOutOfRangeReturnsThrowsException3() {
        apa8BallRaceTo(7, 8);
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns0() {
        int expected = 0;
        int rank = 1;
        int startValue = 0;
        int endValue = 2;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns1() {
        int expected = 1;
        int rank = 1;
        int startValue = 3;
        int endValue = 3;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns2() {
        int expected = 2;
        int rank = 1;
        int startValue = 4;
        int endValue = 4;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns3() {
        int expected = 3;
        int rank = 1;
        int startValue = 5;
        int endValue = 6;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns4() {
        int expected = 4;
        int rank = 1;
        int startValue = 7;
        int endValue = 7;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns5() {
        int expected = 5;
        int rank = 1;
        int startValue = 8;
        int endValue = 8;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns6() {
        int expected = 6;
        int rank = 1;
        int startValue = 9;
        int endValue = 10;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns7() {
        int expected = 7;
        int rank = 1;
        int startValue = 11;
        int endValue = 11;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank1Returns8() {
        int expected = 8;
        int rank = 1;
        int startValue = 12;
        int endValue = 13;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns0() {
        int expected = 0;
        int rank = 2;
        int startValue = 0;
        int endValue = 3;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns1() {
        int expected = 1;
        int rank = 2;
        int startValue = 4;
        int endValue = 5;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns2() {
        int expected = 2;
        int rank = 2;
        int startValue = 6;
        int endValue = 7;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns3() {
        int expected = 3;
        int rank = 2;
        int startValue = 8;
        int endValue = 8;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns4() {
        int expected = 4;
        int rank = 2;
        int startValue = 9;
        int endValue = 10;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns5() {
        int expected = 5;
        int rank = 2;
        int startValue = 11;
        int endValue = 12;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns6() {
        int expected = 6;
        int rank = 2;
        int startValue = 13;
        int endValue = 14;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns7() {
        int expected = 7;
        int rank = 2;
        int startValue = 15;
        int endValue = 16;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank2Returns8() {
        int expected = 8;
        int rank = 2;
        int startValue = 17;
        int endValue = 18;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns0() {
        int expected = 0;
        int rank = 3;
        int startValue = 0;
        int endValue = 4;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns1() {
        int expected = 1;
        int rank = 3;
        int startValue = 5;
        int endValue = 6;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns2() {
        int expected = 2;
        int rank = 3;
        int startValue = 7;
        int endValue = 9;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns3() {
        int expected = 3;
        int rank = 3;
        int startValue = 10;
        int endValue = 11;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns4() {
        int expected = 4;
        int rank = 3;
        int startValue = 12;
        int endValue = 14;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns5() {
        int expected = 5;
        int rank = 3;
        int startValue = 15;
        int endValue = 16;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns6() {
        int expected = 6;
        int rank = 3;
        int startValue = 17;
        int endValue = 19;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns7() {
        int expected = 7;
        int rank = 3;
        int startValue = 20;
        int endValue = 21;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank3Returns8() {
        int expected = 8;
        int rank = 3;
        int startValue = 22;
        int endValue = 24;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns0() {
        int expected = 0;
        int rank = 4;
        int startValue = 0;
        int endValue = 5;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns1() {
        int expected = 1;
        int rank = 4;
        int startValue = 6;
        int endValue = 8;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns2() {
        int expected = 2;
        int rank = 4;
        int startValue = 9;
        int endValue = 11;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns3() {
        int expected = 3;
        int rank = 4;
        int startValue = 12;
        int endValue = 14;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns4() {
        int expected = 4;
        int rank = 4;
        int startValue = 15;
        int endValue = 18;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns5() {
        int expected = 5;
        int rank = 4;
        int startValue = 19;
        int endValue = 21;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns6() {
        int expected = 6;
        int rank = 4;
        int startValue = 22;
        int endValue = 24;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns7() {
        int expected = 7;
        int rank = 4;
        int startValue = 25;
        int endValue = 27;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank4Returns8() {
        int expected = 8;
        int rank = 4;
        int startValue = 28;
        int endValue = 30;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns0() {
        int expected = 0;
        int rank = 5;
        int startValue = 0;
        int endValue = 6;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns1() {
        int expected = 1;
        int rank = 5;
        int startValue = 7;
        int endValue = 10;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns2() {
        int expected = 2;
        int rank = 5;
        int startValue = 11;
        int endValue = 14;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns3() {
        int expected = 3;
        int rank = 5;
        int startValue = 15;
        int endValue = 18;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns4() {
        int expected = 4;
        int rank = 5;
        int startValue = 19;
        int endValue = 22;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns5() {
        int expected = 5;
        int rank = 5;
        int startValue = 23;
        int endValue = 26;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns6() {
        int expected = 6;
        int rank = 5;
        int startValue = 27;
        int endValue = 29;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns7() {
        int expected = 7;
        int rank = 5;
        int startValue = 30;
        int endValue = 33;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank5Returns8() {
        int expected = 8;
        int rank = 5;
        int startValue = 34;
        int endValue = 37;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns0() {
        int expected = 0;
        int rank = 6;
        int startValue = 0;
        int endValue = 8;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns1() {
        int expected = 1;
        int rank = 6;
        int startValue = 9;
        int endValue = 12;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns2() {
        int expected = 2;
        int rank = 6;
        int startValue = 13;
        int endValue = 17;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns3() {
        int expected = 3;
        int rank = 6;
        int startValue = 18;
        int endValue = 22;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns4() {
        int expected = 4;
        int rank = 6;
        int startValue = 23;
        int endValue = 27;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns5() {
        int expected = 5;
        int rank = 6;
        int startValue = 28;
        int endValue = 31;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns6() {
        int expected = 6;
        int rank = 6;
        int startValue = 32;
        int endValue = 36;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns7() {
        int expected = 7;
        int rank = 6;
        int startValue = 37;
        int endValue = 40;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank6Returns8() {
        int expected = 8;
        int rank = 6;
        int startValue = 41;
        int endValue = 45;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns0() {
        int expected = 0;
        int rank = 7;
        int startValue = 0;
        int endValue = 10;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns1() {
        int expected = 1;
        int rank = 7;
        int startValue = 11;
        int endValue = 15;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns2() {
        int expected = 2;
        int rank = 7;
        int startValue = 16;
        int endValue = 21;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns3() {
        int expected = 3;
        int rank = 7;
        int startValue = 22;
        int endValue = 26;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns4() {
        int expected = 4;
        int rank = 7;
        int startValue = 27;
        int endValue = 32;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns5() {
        int expected = 5;
        int rank = 7;
        int startValue = 33;
        int endValue = 37;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns6() {
        int expected = 6;
        int rank = 7;
        int startValue = 38;
        int endValue = 43;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns7() {
        int expected = 7;
        int rank = 7;
        int startValue = 44;
        int endValue = 49;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank7Returns8() {
        int expected = 8;
        int rank = 7;
        int startValue = 50;
        int endValue = 54;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns0() {
        int expected = 0;
        int rank = 8;
        int startValue = 0;
        int endValue = 13;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns1() {
        int expected = 1;
        int rank = 8;
        int startValue = 14;
        int endValue = 19;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns2() {
        int expected = 2;
        int rank = 8;
        int startValue = 20;
        int endValue = 26;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns3() {
        int expected = 3;
        int rank = 8;
        int startValue = 27;
        int endValue = 32;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns4() {
        int expected = 4;
        int rank = 8;
        int startValue = 33;
        int endValue = 39;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns5() {
        int expected = 5;
        int rank = 8;
        int startValue = 40;
        int endValue = 45;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns6() {
        int expected = 6;
        int rank = 8;
        int startValue = 46;
        int endValue = 52;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns7() {
        int expected = 7;
        int rank = 8;
        int startValue = 53;
        int endValue = 58;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank8Returns8() {
        int expected = 8;
        int rank = 8;
        int startValue = 59;
        int endValue = 64;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns0() {
        int expected = 0;
        int rank = 9;
        int startValue = 0;
        int endValue = 17;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns1() {
        int expected = 1;
        int rank = 9;
        int startValue = 18;
        int endValue = 24;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns2() {
        int expected = 2;
        int rank = 9;
        int startValue = 25;
        int endValue = 31;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns3() {
        int expected = 3;
        int rank = 9;
        int startValue = 32;
        int endValue = 38;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns4() {
        int expected = 4;
        int rank = 9;
        int startValue = 39;
        int endValue = 46;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns5() {
        int expected = 5;
        int rank = 9;
        int startValue = 47;
        int endValue = 53;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns6() {
        int expected = 6;
        int rank = 9;
        int startValue = 54;
        int endValue = 60;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns7() {
        int expected = 7;
        int rank = 9;
        int startValue = 61;
        int endValue = 67;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }

    @Test
    public void getMinimumMatchScoreForRank9Returns8() {
        int expected = 8;
        int rank = 9;
        int startValue = 68;
        int endValue = 74;
        for (int i = startValue; i <= endValue; i++) {
            assertThat(getMinimumMatchPointsEarned(rank, i), is(expected));
        }
    }
}
