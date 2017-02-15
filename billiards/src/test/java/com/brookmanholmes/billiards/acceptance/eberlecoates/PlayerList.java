package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/6/2016.
 */
public class PlayerList {
    private static Player jeff() {
        return new Player("Jeff", GameType.BCA_NINE_BALL);
    }

    private static Player max() {
        return new Player("Max", GameType.BCA_NINE_BALL);
    }

    private static Pair<Player> turn1() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(2, false, false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn2() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn3() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(7, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn4() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(1, false, false);
        jeff.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn5() {
        Player max = max();
        Player jeff = jeff();


        max.addShootingBallsMade(2, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn6() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(6, false);
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn7() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(3, true, false);
        max.addBreakAndRun();
        max.addGameWon();
        max.addShootingBallsMade(6, false);

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn8() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn9() {
        Player max = max();
        Player jeff = jeff();

        jeff.addGameLost();

        max.addShootingBallsMade(7, false);
        max.addGameWon();


        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn10() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn11() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn12() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(2, false);
        max.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn13() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn14() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn15() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(6, false);
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn16() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(1, false, false);
        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn17() {
        Player max = max();
        Player jeff = jeff();

        max.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn18() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn19() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        max.addSafetyEscape();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn20() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(3, true, false);
        max.addShootingBallsMade(5, true);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn21() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(1, false);
        jeff.addGameWon();
        jeff.addFiveBallRun();

        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn22() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn23() {
        Player max = max();
        Player jeff = jeff();

        max.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn24() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(0, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn25() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(0, true);
        max.addShootingMiss();


        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn26() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn27() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingMiss();
        max.addShootingBallsMade(0, true);
        max.addSafetyForcedError();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn28() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(5, false);
        jeff.addGameWon();
        jeff.addFiveBallRun();

        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn29() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(2, false, false);
        max.addShootingMiss();
        max.addShootingBallsMade(0, false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn30() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(4, false);
        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn31() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn32() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(3, false);
        jeff.addGameWon();
        jeff.addFiveBallRun();
        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn33() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(2, false, false);
        jeff.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn34() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn35() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn36() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingMiss();
        max.addShootingBallsMade(0, false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn37() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(5, false);
        jeff.addFiveBallRun();
        jeff.addGameWon();

        max.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn38() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn39() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafety(true, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn40() {
        Player max = max();
        Player jeff = jeff();

        max.addSafety(true, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn41() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn42() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn43() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(1, true, false);
        jeff.addShootingBallsMade(5, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn44() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(3, false);
        max.addGameWon();
        max.addFiveBallRun();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn45() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn46() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(2, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn47() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(6, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn48() {
        Player max = max();
        Player jeff = jeff();

        jeff.addBreakShot(3, true, false);
        jeff.addShootingBallsMade(1, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn49() {
        Player max = max();
        Player jeff = jeff();

        max.addSafety(false, 0);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn50() {
        Player max = max();
        Player jeff = jeff();

        jeff.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn51() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(0, false);
        max.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn52() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(1, false);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn53() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(4, false);
        max.addGameWon();
        max.addFiveBallRun();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn54() {
        Player max = max();
        Player jeff = jeff();

        max.addBreakShot(1, false, false);
        max.addSafetyAttempt(false);

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn55() {
        Player max = max();
        Player jeff = jeff();

        jeff.addShootingBallsMade(0, true);
        jeff.addShootingMiss();

        return new Pair<Player>(max, jeff);
    }

    private static Pair<Player> turn56() {
        Player max = max();
        Player jeff = jeff();

        max.addShootingBallsMade(8, false);
        max.addGameWon();

        jeff.addGameLost();

        return new Pair<Player>(max, jeff);
    }

    public static List<Pair<Player>> getPlayerPairs() {
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
