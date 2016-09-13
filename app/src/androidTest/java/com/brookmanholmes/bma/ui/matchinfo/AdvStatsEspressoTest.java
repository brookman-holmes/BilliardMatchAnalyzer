package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.*;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.NONE;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.*;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.*;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.*;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.*;

/**
 * Created by helios on 9/9/2016.
 */

public abstract class AdvStatsEspressoTest extends BaseMatchTest {
    @Override
    protected Match getMatch() {
        return new Match.Builder("Test 1", "Test 2")
                .setStatsDetail(Match.StatsDetail.ADVANCED)
                .build(GameType.BCA_NINE_BALL);
    }

    static ITurn wonGame = turn().madeBalls(9).win();

    // break miss
    static ITurn break0 = turn().setAdvStats(breakMiss(MISCUE,KICKED_IN, AdvStats.HowType.TOO_HARD, AIM_LEFT)).breakMiss();
    static ITurn break1 = turn().setAdvStats(breakMiss(MISCUE,KICKED_IN, AdvStats.HowType.TOO_SOFT, AIM_RIGHT)).breakMiss();
    static ITurn break2 = turn().setAdvStats(breakMiss(FORCING_II, TOO_LITTLE_DRAW, ENGLISH)).breakMiss();
    static ITurn break3 = turn().setAdvStats(breakMiss(FORCING_II, TOO_MUCH_DRAW, ENGLISH)).breakMiss();
    static ITurn break4 = turn().setAdvStats(breakMiss(FORCING_II, TOO_LITTLE_FOLLOW, ENGLISH)).breakMiss();
    static ITurn break5 = turn().setAdvStats(breakMiss(FORCING_II, TOO_MUCH_FOLLOW, ENGLISH)).breakMiss();



    static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_NINE_BALL);
    }

    static AdvStats.Builder advStats() {
        return new AdvStats.Builder();
    }

    static AdvStats safety(AdvStats.SubType subType) {
        return advStats().shotType(SAFETY).subType(subType).build();
    }

    static AdvStats breakMiss(AdvStats.HowType... howTypes) {
        return advStats().shotType(BREAK_SHOT).howTypes(howTypes).build();
    }

    static AdvStats breakMiss(AdvStats.WhyType... whyTypes) {
        return advStats().shotType(BREAK_SHOT).whyTypes(whyTypes).build();
    }

    static AdvStats safetyMiss(AdvStats.HowType... howTypes) {
        return advStats().shotType(SAFETY_ERROR).howTypes(howTypes).build();
    }

    static AdvStats miss(AdvStats.ShotType shotType, AdvStats.HowType... howTypes) {
        return miss(shotType, NONE, howTypes);
    }

    static AdvStats miss(AdvStats.ShotType shotType, AdvStats.SubType subType, AdvStats.HowType... howTypes) {
        return advStats().shotType(shotType).subType(subType).howTypes(howTypes).build();
    }

    static AdvStats bank(AdvStats.HowType... howTypes) {
        return advStats()
                .shotType(BANK)
                .subType(NONE)
                .howTypes(howTypes)
                .angle(ONE_RAIL, TWO_RAIL, THREE_RAIL, FOUR_RAIL,
                        FIVE_RAIL, NATURAL, CROSSOVER, LONG_RAIL, SHORT_RAIL)
                .build();
    }

    static AdvStats kick(AdvStats.HowType... howTypes) {
        return advStats()
                .shotType(KICK)
                .subType(NONE)
                .howTypes(howTypes)
                .angle(ONE_RAIL, TWO_RAIL, THREE_RAIL, FOUR_RAIL,
                        FIVE_RAIL)
                .build();
    }

    static AdvStats masse(AdvStats.HowType... howTypes) {
        return advStats()
                .shotType(MASSE)
                .subType(NONE)
                .howTypes(howTypes)
                .build();
    }

    List<ITurn> list(ITurn... turns) {
        return Arrays.asList(turns);
    }
}
