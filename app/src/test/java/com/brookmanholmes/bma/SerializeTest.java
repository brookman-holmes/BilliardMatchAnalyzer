package com.brookmanholmes.bma;


import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/25/2017.
 */

public class SerializeTest {

    @Test
    public void advStatsSerializeTest() {
        AdvStats actual = getAdvStat();

        AdvStats result = SerializationUtils.deserialize(SerializationUtils.serialize(actual));
        assertThat(actual, is(result));
    }

    @Test
    public void TurnSerializeTest() {
        ITurn actual = getTurn();

        ITurn result = SerializationUtils.deserialize(SerializationUtils.serialize(actual));

        assertThat(actual, is(result));
    }

    @Test
    public void MatchSerializeTest() {
        Match actual = new Match.Builder("Name", "Name 2")
                .build(GameType.BCA_EIGHT_BALL);

        actual.addTurn(getTurn());

        Match result = SerializationUtils.deserialize(SerializationUtils.serialize(actual));


        assertThat(getTurn(), is(result.getTurns().get(0)));
        assertThat(actual, is(result));
    }


    private ITurn getTurn() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL).madeBalls(1, 3).setAdvStats(getAdvStat()).miss();
    }

    private AdvStats getAdvStat() {
        return new AdvStats.Builder("Name")
                .angle(AdvStats.Angle.EIGHTY)
                .cbDistance(4.5f)
                .obDistance(3.5f)
                .speed(3)
                .shotType(AdvStats.ShotType.BREAK_SHOT)
                .subType(AdvStats.SubType.NO_DIRECT_SHOT)
                .cueing(10, 40)
                .howTypes(AdvStats.HowType.AIM_LEFT, AdvStats.HowType.AIM_RIGHT)
                .startingPosition("Open")
                .use(true)
                .build();
    }
}
