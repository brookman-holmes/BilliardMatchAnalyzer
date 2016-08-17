package com.brookmanholmes.billiards.game.util;


/**
 * Created by helios on 1/9/2016.
 */
public class ApaRaceToHelper {
    private static final int[] apa9BallRaces = new int[]{14, 19, 25, 31, 38, 46, 55, 65, 75};
    private static final RaceTo[] SL2_8 = new RaceTo[]{new RaceTo(2, 2), new RaceTo(2, 3), new RaceTo(2, 4), new RaceTo(2, 5), new RaceTo(2, 6), new RaceTo(2, 7)};
    private static final RaceTo[] SL3_8 = new RaceTo[]{new RaceTo(3, 2), new RaceTo(2, 2), new RaceTo(2, 3), new RaceTo(2, 4), new RaceTo(2, 5), new RaceTo(2, 6)};
    private static final RaceTo[] SL4_8 = new RaceTo[]{new RaceTo(4, 2), new RaceTo(3, 2), new RaceTo(3, 3), new RaceTo(3, 4), new RaceTo(3, 5), new RaceTo(2, 5)};
    private static final RaceTo[] SL5_8 = new RaceTo[]{new RaceTo(5, 2), new RaceTo(4, 2), new RaceTo(4, 3), new RaceTo(4, 4), new RaceTo(4, 5), new RaceTo(3, 5)};
    private static final RaceTo[] SL6_8 = new RaceTo[]{new RaceTo(6, 2), new RaceTo(5, 2), new RaceTo(5, 3), new RaceTo(5, 4), new RaceTo(5, 5), new RaceTo(4, 5)};
    private static final RaceTo[] SL7_8 = new RaceTo[]{new RaceTo(7, 2), new RaceTo(6, 2), new RaceTo(5, 2), new RaceTo(5, 3), new RaceTo(5, 4), new RaceTo(5, 5)};
    private static final RaceTo[][] apa8BallRaceTable = new RaceTo[][]{SL2_8, SL3_8, SL4_8, SL5_8, SL6_8, SL7_8};
    private static final int[] SL1_9 = new int[]{0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 6, 7, 8, 8};
    private static final int[] SL2_9 = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8};
    private static final int[] SL3_9 = new int[]{0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 8};
    private static final int[] SL4_9 = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8};
    private static final int[] SL5_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8};
    private static final int[] SL6_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8};
    private static final int[] SL7_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8};
    private static final int[] SL8_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8};
    private static final int[] SL9_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8};
    private static final int[][] minMatchPoints = new int[][]{SL1_9, SL2_9, SL3_9, SL4_9, SL5_9, SL6_9, SL7_9, SL8_9, SL9_9};

    public static int apa9BallRaceTo(int rank) {
        if (rank > 9 || rank < 1)
            throw new IllegalArgumentException("Rank can only be between 1-9 inclusive");

        return apa9BallRaces[rank - 1];
    }

    public static RaceTo apa8BallRaceTo(int playerRank, int opponentRank) {
        if ((playerRank > 7 || playerRank < 2) || (opponentRank > 7 || opponentRank < 2))
            return new RaceTo(0,0);
        else {
            return apa8BallRaceTable[playerRank - 2][opponentRank - 2];
        }
    }

    public static int getMinimumMatchPointsEarned(int playerRank, int playerScore) {
        if (playerScore > apa9BallRaceTo(playerRank))
            return 8;
        else
            return minMatchPoints[playerRank - 1][playerScore];
    }
}
