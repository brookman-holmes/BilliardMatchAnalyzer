package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.TenBallPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/6/2016.
 */
public class PlayerList {
    private static TenBallPlayer roberts() {
        return new TenBallPlayer("Roberts");
    }

    private static TenBallPlayer shaw() {
        return new TenBallPlayer("Shaw");
    }

    private static Pair<TenBallPlayer> turn1() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(7, false);
        roberts.addBreakShot(3, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn2() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn3() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn4() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn5() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(4, false);
        roberts.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn6() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(1, true);
        shaw.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn7() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(2, false);
        roberts.addGameWon();

        shaw.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn8() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(0, false, true);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn9() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();
        shaw.addFourBallRun();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn10() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addBreakAndRun();
        shaw.addBreakShot(2, true, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn11() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(8, false);
        roberts.addBreakShot(2, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn12() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(2, false);
        shaw.addSafety(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn13() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addSafetyAttempt(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn14() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(7, false);
        shaw.addGameWon();
        shaw.addFourBallRun();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn15() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(3, false, false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn16() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();


        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn17() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, true);
        roberts.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn18() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafety(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn19() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addSafetyAttempt(true);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn20() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addFourBallRun();
        shaw.addGameWon();
        shaw.addShootingBallsMade(6, false);

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn21() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(1, false);
        shaw.addSafety(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn22() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn23() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn24() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingMiss();
        roberts.addShootingBallsMade(0, false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn25() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addFourBallRun();
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn26() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(2, false, false);
        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn27() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addFourBallRun();
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn28() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn29() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(1, false);
        roberts.addEarlyWin();
        roberts.addGameWon();

        shaw.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn30() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(1, false, false);

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn31() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        // skip turn

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn32() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<>(shaw, roberts);
    }

    private static Pair<TenBallPlayer> turn33() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();
        shaw.addFourBallRun();

        roberts.addGameLost();

        return new Pair<>(shaw, roberts);
    }

    public static List<Pair<TenBallPlayer>> getPlayerPairs() {
        return Arrays.asList(
                turn1(), turn2(), turn3(), turn4(), turn5(), turn6(), turn7(), turn8(), turn9(), turn10(),
                turn11(), turn12(), turn13(), turn14(), turn15(), turn16(), turn17(), turn18(), turn19(), turn20(),
                turn21(), turn22(), turn23(), turn24(), turn25(), turn26(), turn27(), turn28(), turn29(), turn30(),
                turn31(), turn32(), turn33()
        );
    }
}
