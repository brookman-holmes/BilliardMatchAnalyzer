package com.brookmanholmes.billiards.game.util;


/**
 * Created by helios on 1/9/2016.
 */
public class ApaRaceToHelper {
    public static int apa9BallRaceTo(int rank) {
        switch (rank) {
            case 1:
                return 14;
            case 2:
                return 19;
            case 3:
                return 25;
            case 4:
                return 31;
            case 5:
                return 38;
            case 6:
                return 46;
            case 7:
                return 55;
            case 8:
                return 65;
            case 9:
                return 75;
            default:
                return 0;
        }
    }

    public static RaceTo apa8BallRaceTo(int playerRank, int opponentRank) {
        if ((playerRank > 7 || playerRank < 2) || (opponentRank > 7 || opponentRank < 2))
            return new RaceTo(0,0);
        else {
            switch (playerRank) {
                case 2:
                    return playerRankIs2(opponentRank);
                case 3:
                    return playerRankIs3(opponentRank);
                case 4:
                    return playerRankIs4(opponentRank);
                case 5:
                    return playerRankIs5(opponentRank);
                case 6:
                    return playerRankIs6(opponentRank);
                default:
                    return playerRankIs7(opponentRank);
            }
        }
    }


    private static RaceTo playerRankIs2(int opponentRank) {
        return new RaceTo(2,opponentRank);
    }

    private static RaceTo playerRankIs3(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new RaceTo(3,2);
            case 3:
                return new RaceTo(2,2);
            default:
                return new RaceTo(2,opponentRank - 1);
        }
    }

    private static RaceTo playerRankIs4(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new RaceTo(4,2);
            case 7:
                return new RaceTo(2,5);
            default:
                return new RaceTo(3, 3 + (opponentRank - 4));
        }
    }

    private static RaceTo playerRankIs5(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new RaceTo(5,2);
            case 3:
                return new RaceTo(4,2);
            case 4:
                return new RaceTo(4,3);
            case 5:
                return new RaceTo(4,4);
            case 6:
                return new RaceTo(4,5);
            default:
                return new RaceTo(3,5);
        }
    }

    private static RaceTo playerRankIs6(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new RaceTo(6,2);
            case 7:
                return new RaceTo(4,5);
            default:
                return new RaceTo(5, 5 + (opponentRank - 6));
        }
    }

    private static RaceTo playerRankIs7(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new RaceTo(7,2);
            case 3:
                return new RaceTo(6,2);
            default:
                return new RaceTo(5, 5 + (opponentRank - 7));
        }
    }

    public static int getMinimumMatchPointsEarned(int playerRank, int playerScore) {
        setPlayerScore(playerScore);
        switch (playerRank) {
            case 1:
                return minimumMatchPointsForSL1();
            case 2:
                return minimumMatchPointsForSL2();
            case 3:
                return minimumMatchPointsForSL3();
            case 4:
                return minimumMatchPointsForSL4();
            case 5:
                return minimumMatchPointsForSL5();
            case 6:
                return minimumMatchPointsForSL6();
            case 7:
                return minimumMatchPointsForSL7();
            case 8:
                return minimumMatchPointsForSL8();
            case 9:
                return minimumMatchPointsForSL9();
            default:
                throw new IllegalArgumentException("Player rank must be between 1 and 9, inclusive");
        }
    }


    private static int minimumMatchPointsForSL1() {
        if (playerScoreLessThan(2))
            return 0;
        else if (playerScoreLessThan(3))
            return 1;
        else if (playerScoreLessThan(4))
            return 2;
        else if (playerScoreLessThan(6))
            return 3;
        else if (playerScoreLessThan(7))
            return 4;
        else if (playerScoreLessThan(8))
            return 5;
        else if (playerScoreLessThan(10))
            return 6;
        else if (playerScoreLessThan(11))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL2() {
        if (playerScoreLessThan(3))
            return 0;
        else if (playerScoreLessThan(5))
            return 1;
        else if (playerScoreLessThan(7))
            return 2;
        else if (playerScoreLessThan(8))
            return 3;
        else if (playerScoreLessThan(10))
            return 4;
        else if (playerScoreLessThan(12))
            return 5;
        else if (playerScoreLessThan(14))
            return 6;
        else if (playerScoreLessThan(16))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL3() {
        if (playerScoreLessThan(4))
            return 0;
        else if (playerScoreLessThan(6))
            return 1;
        else if (playerScoreLessThan(9))
            return 2;
        else if (playerScoreLessThan(11))
            return 3;
        else if (playerScoreLessThan(14))
            return 4;
        else if (playerScoreLessThan(16))
            return 5;
        else if (playerScoreLessThan(19))
            return 6;
        else if (playerScoreLessThan(21))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL4() {
        if (playerScoreLessThan(5))
            return 0;
        else if (playerScoreLessThan(8))
            return 1;
        else if (playerScoreLessThan(11))
            return 2;
        else if (playerScoreLessThan(14))
            return 3;
        else if (playerScoreLessThan(18))
            return 4;
        else if (playerScoreLessThan(21))
            return 5;
        else if (playerScoreLessThan(24))
            return 6;
        else if (playerScoreLessThan(27))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL5() {
        if (playerScoreLessThan(6))
            return 0;
        else if (playerScoreLessThan(10))
            return 1;
        else if (playerScoreLessThan(14))
            return 2;
        else if (playerScoreLessThan(18))
            return 3;
        else if (playerScoreLessThan(22))
            return 4;
        else if (playerScoreLessThan(26))
            return 5;
        else if (playerScoreLessThan(29))
            return 6;
        else if (playerScoreLessThan(33))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL6() {
        if (playerScoreLessThan(8))
            return 0;
        else if (playerScoreLessThan(12))
            return 1;
        else if (playerScoreLessThan(17))
            return 2;
        else if (playerScoreLessThan(22))
            return 3;
        else if (playerScoreLessThan(27))
            return 4;
        else if (playerScoreLessThan(31))
            return 5;
        else if (playerScoreLessThan(36))
            return 6;
        else if (playerScoreLessThan(40))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL7() {
        if (playerScoreLessThan(10))
            return 0;
        else if (playerScoreLessThan(15))
            return 1;
        else if (playerScoreLessThan(21))
            return 2;
        else if (playerScoreLessThan(26))
            return 3;
        else if (playerScoreLessThan(32))
            return 4;
        else if (playerScoreLessThan(37))
            return 5;
        else if (playerScoreLessThan(43))
            return 6;
        else if (playerScoreLessThan(49))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL8() {
        if (playerScoreLessThan(13))
            return 0;
        else if (playerScoreLessThan(19))
            return 1;
        else if (playerScoreLessThan(26))
            return 2;
        else if (playerScoreLessThan(32))
            return 3;
        else if (playerScoreLessThan(39))
            return 4;
        else if (playerScoreLessThan(45))
            return 5;
        else if (playerScoreLessThan(52))
            return 6;
        else if (playerScoreLessThan(58))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL8() {
        if (playerScoreLessThan(13))
            return 0;
        else if (playerScoreLessThan(19))
            return 1;
        else if (playerScoreLessThan(26))
            return 2;
        else if (playerScoreLessThan(32))
            return 3;
        else if (playerScoreLessThan(39))
            return 4;
        else if (playerScoreLessThan(45))
            return 5;
        else if (playerScoreLessThan(52))
            return 6;
        else if (playerScoreLessThan(58))
            return 7;
        else
            return 8;
    }

    private static int minimumMatchPointsForSL9() {
        if (playerScoreLessThan(17))
            return 0;
        else if (playerScoreLessThan(24))
            return 1;
        else if (playerScoreLessThan(31))
            return 2;
        else if (playerScoreLessThan(38))
            return 3;
        else if (playerScoreLessThan(46))
            return 4;
        else if (playerScoreLessThan(53))
            return 5;
        else if (playerScoreLessThan(60))
            return 6;
        else if (playerScoreLessThan(67))
            return 7;
        else
            return 8;
    }

    private static int playerScore;

    private static void setPlayerScore(int score) {
        playerScore = score;
    }

    private static boolean playerScoreLessThan(int score) {
        if (score < 0)
            throw new IllegalArgumentException("Player score is negative");

        return playerScore <= score;
    }
}
