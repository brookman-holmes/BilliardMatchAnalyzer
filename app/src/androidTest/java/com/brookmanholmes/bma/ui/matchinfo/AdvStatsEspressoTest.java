package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.turn.AdvStats.Angle.CROSSOVER;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.FIVE_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.FOUR_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.LONG_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.NATURAL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.ONE_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.SHORT_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.THREE_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.Angle.TWO_RAIL;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_LEFT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_RIGHT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICKED_IN;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.MISCUE;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.BANK;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.BREAK_SHOT;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.KICK;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.MASSE;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.SAFETY;
import static com.brookmanholmes.billiards.turn.AdvStats.ShotType.SAFETY_ERROR;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.NONE;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.ENGLISH;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.FORCING_II;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.TOO_LITTLE_DRAW;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.TOO_LITTLE_FOLLOW;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.TOO_MUCH_DRAW;
import static com.brookmanholmes.billiards.turn.AdvStats.WhyType.TOO_MUCH_FOLLOW;

/**
 * Created by helios on 9/9/2016.
 */

public abstract class AdvStatsEspressoTest extends BaseMatchTest {
    static ITurn wonGame = turn().madeBalls(9).win();
    // break miss
    static ITurn break0 = turn().setAdvStats(breakMiss(MISCUE, KICKED_IN, AdvStats.HowType.TOO_HARD, AIM_LEFT)).breakMiss();
    static ITurn break1 = turn().setAdvStats(breakMiss(MISCUE, KICKED_IN, AdvStats.HowType.TOO_SOFT, AIM_RIGHT)).breakMiss();
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

    @Override
    protected Match getMatch() {
        return new Match.Builder("Test 1", "Test 2")
                .setStatsDetail(Match.StatsDetail.ADVANCED)
                .build(GameType.BCA_NINE_BALL);
    }

    List<ITurn> list(ITurn... turns) {
        return Arrays.asList(turns);
    }
}
