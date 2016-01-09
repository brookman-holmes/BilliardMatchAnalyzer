package com.brookmanholmes.billiards.game.util;

import java.awt.Point;

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

    public static Point apa8BallRaceTo(int playerRank, int opponentRank) {
        Point point = new Point();
        if ((playerRank > 7 || playerRank < 2) || (opponentRank > 7 || opponentRank < 2))
            return new Point(0,0);
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


    private static Point playerRankIs2(int opponentRank) {
        return new Point(2,opponentRank);
    }

    private static Point playerRankIs3(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new Point(3,2);
            case 3:
                return new Point(2,2);
            default:
                return new Point(2,opponentRank - 1);
        }
    }

    private static Point playerRankIs4(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new Point(4,2);
            case 7:
                return new Point(2,5);
            default:
                return new Point(3, 3 + (opponentRank - 4));
        }
    }

    private static Point playerRankIs5(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new Point(5,2);
            case 3:
                return new Point(4,2);
            case 4:
                return new Point(4,3);
            case 5:
                return new Point(4,4);
            case 6:
                return new Point(4,5);
            default:
                return new Point(3,5);
        }
    }

    private static Point playerRankIs6(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new Point(6,2);
            case 7:
                return new Point(4,5);
            default:
                return new Point(5, 5 + (opponentRank - 6));
        }
    }

    private static Point playerRankIs7(int opponentRank) {
        switch (opponentRank) {
            case 2:
                return new Point(7,2);
            case 3:
                return new Point(6,2);
            default:
                return new Point(5, 5 + (opponentRank - 7));
        }
    }
}
