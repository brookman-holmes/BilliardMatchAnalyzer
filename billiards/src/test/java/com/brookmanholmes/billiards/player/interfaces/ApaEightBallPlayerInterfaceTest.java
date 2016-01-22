package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;
import com.brookmanholmes.billiards.game.util.RaceTo;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class ApaEightBallPlayerInterfaceTest extends ApaInterfaceTest {
    RaceTo raceTo = ApaRaceToHelper.apa8BallRaceTo(rank, 4);

    public void setUp() {
        actual = new ApaEightBallPlayer("", rank);
    }

    @Override
    public void getMatchPointsReturns0() {
        assertThat(actual.getMatchPoints(0, 4), is(0));
    }

    @Override
    public void getMatchPointsReturns1() {
        for (int i = 1; i < raceTo.getPlayerRaceTo(); i++) {
            getPlayer().addGameWon();
        }

        assertThat(getPlayer().getWins(), is(raceTo.getPlayerRaceTo() - 1));

        assertThat(actual.getMatchPoints(0, 4), is(1));
    }

    @Override
    public void getMatchPointsReturns2() {
        for (int i = 0; i < raceTo.getPlayerRaceTo(); i++) {
            getPlayer().addGameWon();
        }

        assertThat(getPlayer().getWins(), is(raceTo.getPlayerRaceTo()));

        assertThat(actual.getMatchPoints(1, 4), is(2));
    }

    @Override
    public void getMatchPointsReturns3() {
        for (int i = 0; i < raceTo.getPlayerRaceTo(); i++) {
            getPlayer().addGameWon();
        }

        assertThat(getPlayer().getWins(), is(raceTo.getPlayerRaceTo()));

        assertThat(actual.getMatchPoints(0, 4), is(3));
    }


    private ApaEightBallPlayer getPlayer() {
        return (ApaEightBallPlayer) actual;
    }
}
