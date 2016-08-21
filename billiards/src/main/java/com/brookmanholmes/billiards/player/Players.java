package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 8/21/2016.
 */
public class Players {
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

    private Players() {
    }


    public static boolean isMatchOver(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof ApaNineBallPlayer && opponent instanceof ApaNineBallPlayer)
            return isMatchOverApa9((ApaNineBallPlayer)player, (ApaNineBallPlayer)opponent);
        else if (player instanceof ApaEightBallPlayer && opponent instanceof ApaEightBallPlayer)
            return isMatchOverApa8((ApaEightBallPlayer)player, (ApaEightBallPlayer)opponent);
        /**
         * todo setup isMatchOver for other player types, which will most likely be something like storing
         * a player rank in Abstract player and checking if their win count is equal to their rank (or point count,
         * depending on the game type)
         */
        else return false;
    }

    private static boolean isMatchOverApa9(ApaNineBallPlayer player, ApaNineBallPlayer opponent) {
        return player.getPointsNeeded(0) <= player.getPoints() ||
                opponent.getPointsNeeded(0) <= opponent.getPoints();
    }

    private static boolean isMatchOverApa8(ApaEightBallPlayer player, ApaEightBallPlayer opponent) {
        RaceTo raceTo = apa8BallRaceTo(player.getRank(), opponent.getRank());

        return raceTo.getPlayerRaceTo() <= player.getPoints() ||
                raceTo.getOpponentRaceTo() <= opponent.getPoints();
    }

    public static int apa9BallRaceTo(int rank) {
        if (rank > 9 || rank < 1)
            throw new IllegalArgumentException("Player ranks must be between 1 and 9 inclusive");
        else
            return apa9BallRaces[rank - 1];
    }

    public static RaceTo apa8BallRaceTo(int playerRank, int opponentRank) {
        if (playerRank > 7 || playerRank < 2 || opponentRank > 7 || opponentRank < 2)
            throw new IllegalArgumentException("Player ranks must be between 2 and 7 inclusive");
        else
            return apa8BallRaceTable[playerRank - 2][opponentRank - 2];
    }

    public static int getMinimumMatchPointsEarned(int playerRank, int playerScore) {
        if (playerScore > apa9BallRaceTo(playerRank))
            return 8;
        else
            return minMatchPoints[playerRank - 1][playerScore];
    }
}
