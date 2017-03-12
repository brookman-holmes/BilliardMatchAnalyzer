package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/6/2016.
 */
public class PlayerList {
    private static Player roberts() {
        return new Player("Roberts", "Roberts", GameType.BCA_TEN_BALL, 7, 7);
    }

    private static Player shaw() {
        return new Player("Shaw", "Shaw", GameType.BCA_TEN_BALL, 7, 7);
    }

    private static Pair<Player> turn1() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(7, false);
        roberts.addBreakShot(3, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn2() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn3() {
        Player shaw = shaw();
        Player roberts = roberts();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn4() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn5() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(4, false);
        roberts.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn6() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(1, true);
        shaw.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn7() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(2, false);
        roberts.addGameWon();
        roberts.addFiveBallRun();

        shaw.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn8() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addBreakShot(0, false, true);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn9() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn10() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addBreakAndRun();
        shaw.addBreakShot(2, true, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn11() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addGameWon();
        roberts.addShootingBallsMade(8, false);
        roberts.addBreakShot(2, true, false);
        roberts.addBreakAndRun();

        shaw.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn12() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(2, false);
        shaw.addSafety(false, 0);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn13() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addSafetyAttempt(false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn14() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(7, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn15() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addBreakShot(3, false, false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn16() {
        Player shaw = shaw();
        Player roberts = roberts();


        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn17() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(0, true);
        roberts.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn18() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addSafety(false, 0);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn19() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addSafetyAttempt(true);
        roberts.addSafetyForcedError();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn20() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addGameWon();
        shaw.addShootingBallsMade(6, false);

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn21() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addBreakShot(1, true, false);
        shaw.addShootingBallsMade(1, false);
        shaw.addSafety(false, 0);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn22() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn23() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addSafetyAttempt(false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn24() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingMiss();
        roberts.addShootingBallsMade(0, false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn25() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn26() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addBreakShot(2, false, false);
        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn27() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(8, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn28() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addBreakShot(0, false, false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn29() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(1, false);
        roberts.addEarlyWin();
        roberts.addFiveBallRun();
        roberts.addGameWon();

        shaw.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn30() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addBreakShot(1, false, false);

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn31() {
        Player shaw = shaw();
        Player roberts = roberts();

        // skip turn

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn32() {
        Player shaw = shaw();
        Player roberts = roberts();

        roberts.addShootingBallsMade(0, false);
        roberts.addShootingMiss();

        return new Pair<Player>(shaw, roberts);
    }

    private static Pair<Player> turn33() {
        Player shaw = shaw();
        Player roberts = roberts();

        shaw.addShootingBallsMade(9, false);
        shaw.addGameWon();

        roberts.addGameLost();

        return new Pair<Player>(shaw, roberts);
    }

    public static List<Pair<Player>> getPlayerPairs() {
        return Arrays.asList(
                turn1(), turn2(), turn3(), turn4(), turn5(), turn6(), turn7(), turn8(), turn9(), turn10(),
                turn11(), turn12(), turn13(), turn14(), turn15(), turn16(), turn17(), turn18(), turn19(), turn20(),
                turn21(), turn22(), turn23(), turn24(), turn25(), turn26(), turn27(), turn28(), turn29(), turn30(),
                turn31(), turn32(), turn33()
        );
    }
}
