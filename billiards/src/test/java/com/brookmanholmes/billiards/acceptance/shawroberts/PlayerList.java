package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.player.AbstractPlayer;
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

    private static Pair<AbstractPlayer> turn1() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(7, false);
        roberts.addBreakShot(3, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn2() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn3() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn4() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn5() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(4, false);
        roberts.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn6() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(1, true);
        shaw.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn7() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(2, false);
        roberts.addGameWon();
        roberts.addFourBallRun();

        shaw.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn8() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(0, false, true);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn9() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn10() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addBreakAndRun();
        shaw.addBreakShot(2, true, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn11() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(8, false);
        roberts.addBreakShot(2, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn12() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(2, false);
        shaw.addSafety(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn13() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addSafetyAttempt(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn14() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(7, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn15() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(3, false, false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn16() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();


        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn17() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, true);
        roberts.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn18() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafety(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn19() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addSafetyAttempt(true);
        roberts.addSafetyForcedError();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn20() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addGameWon();
        shaw.addShootingBallsMade(6, false);

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn21() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(1, false);
        shaw.addSafety(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn22() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn23() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn24() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingMiss();
        roberts.addShootingBallsMade(0, false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn25() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn26() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(2, false, false);
        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn27() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn28() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn29() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(1, false);
        roberts.addEarlyWin();
        roberts.addFourBallRun();
        roberts.addGameWon();

        shaw.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn30() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addBreakShot(1, false, false);

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn31() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        // skip turn

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn32() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    private static Pair<AbstractPlayer> turn33() {
        TenBallPlayer shaw = shaw();
        TenBallPlayer roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<AbstractPlayer>(shaw, roberts);
    }

    public static List<Pair<AbstractPlayer>> getPlayerPairs() {
        return Arrays.asList(
                turn1(), turn2(), turn3(), turn4(), turn5(), turn6(), turn7(), turn8(), turn9(), turn10(),
                turn11(), turn12(), turn13(), turn14(), turn15(), turn16(), turn17(), turn18(), turn19(), turn20(),
                turn21(), turn22(), turn23(), turn24(), turn25(), turn26(), turn27(), turn28(), turn29(), turn30(),
                turn31(), turn32(), turn33()
        );
    }
}
