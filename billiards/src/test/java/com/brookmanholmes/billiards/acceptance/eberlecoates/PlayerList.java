package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/6/2016.
 */
public class PlayerList {
    private static NineBallPlayer jeff() {
        return new NineBallPlayer("Jeff");
    }

    private static NineBallPlayer max() {
        return new NineBallPlayer("Max");
    }

    private static Pair<NineBallPlayer> turn1() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(2, false, false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn2() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn3() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(7, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn4() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(1, false, false);
        jeff.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn5() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();


        max.addShootingBallsMade(2, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn6() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(6, false);
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn7() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(3, true, false);
        max.addBreakAndRun();
        max.addGameWon();
        max.addShootingBallsMade(6, false);

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn8() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn9() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addGameLost();

        max.addShootingBallsMade(7, false);
        max.addGameWon();


        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn10() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn11() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn12() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(2, false);
        max.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn13() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn14() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn15() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(6, false);
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn16() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(1, false, false);
        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn17() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn18() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn19() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        max.addSafetyEscape();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn20() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(3, true, false);
        max.addShootingBallsMade(5, true);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn21() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(1, false);
        jeff.addGameWon();
        jeff.addFourBallRun();

        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn22() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn23() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn24() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn25() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(0, true);
        max.addShootingMiss();


        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn26() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn27() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingMiss();
        max.addShootingBallsMade(0, true);
        max.addSafetyForcedError();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn28() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(5, false);
        jeff.addGameWon();
        jeff.addFourBallRun();

        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn29() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(2, false, false);
        max.addShootingMiss();
        max.addShootingBallsMade(0, false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn30() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(4, false);
        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn31() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn32() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(3, false);
        jeff.addGameWon();
        jeff.addFourBallRun();
        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn33() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn34() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn35() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn36() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingMiss();
        max.addShootingBallsMade(0, false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn37() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(5, false);
        jeff.addFourBallRun();
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn38() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn39() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafety(true);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn40() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addSafety(true);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn41() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn42() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn43() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(1, true, false);
        jeff.addShootingBallsMade(5, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn44() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(3, false);
        max.addGameWon();
        max.addFourBallRun();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn45() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn46() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn47() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(6, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn48() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addBreakShot(3, true, false);
        jeff.addShootingBallsMade(1, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn49() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addSafety(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn50() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn51() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn52() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(1, false);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn53() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(4, false);
        max.addGameWon();
        max.addFourBallRun();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn54() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addSafetyAttempt(false);

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn55() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        jeff.addShootingBallsMade(0, true);
        jeff.addShootingMiss();

        return new Pair<>(max, jeff);
    }

    private static Pair<NineBallPlayer> turn56() {
        NineBallPlayer max = max();
        NineBallPlayer jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<>(max, jeff);
    }

    public static List<Pair<NineBallPlayer>> getPlayerPairs() {
        return Arrays.asList(
                turn1(), turn2(), turn3(), turn4(), turn5(), turn6(), turn7(), turn8(), turn9(), turn10(),
                turn11(), turn12(), turn13(), turn14(), turn15(), turn16(), turn17(), turn18(), turn19(), turn20(),
                turn21(), turn22(), turn23(), turn24(), turn25(), turn26(), turn27(), turn28(), turn29(), turn30(),
                turn31(), turn32(), turn33(), turn34(), turn35(), turn36(), turn37(), turn38(), turn39(), turn40(),
                turn41(), turn42(), turn43(), turn44(), turn45(), turn46(), turn47(), turn48(), turn49(), turn50(),
                turn51(), turn52(), turn53(), turn54(), turn55(), turn56()
        );
    }
}
