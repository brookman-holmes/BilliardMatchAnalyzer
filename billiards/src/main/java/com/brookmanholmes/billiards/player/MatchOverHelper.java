package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 8/20/2016.
 */
public class MatchOverHelper {
    private MatchOverHelper() {
    }


    public static boolean isMatchOver(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof ApaNineBallPlayer && opponent instanceof ApaNineBallPlayer)
            return isMatchOverApa9((ApaNineBallPlayer)player, (ApaNineBallPlayer)opponent);
        else if (player instanceof ApaEightBallPlayer && opponent instanceof ApaEightBallPlayer)
            return isMatchOverApa8((ApaEightBallPlayer)player, (ApaEightBallPlayer)opponent);
        else return false;
    }

    private static boolean isMatchOverApa9(ApaNineBallPlayer player, ApaNineBallPlayer opponent) {
        return player.getPointsNeeded(0) <= player.getPoints() ||
                opponent.getPointsNeeded(0) <= opponent.getPoints();
    }

    private static boolean isMatchOverApa8(ApaEightBallPlayer player, ApaEightBallPlayer opponent) {
        RaceTo raceTo = ApaRaceToHelper.apa8BallRaceTo(player.getRank(), opponent.getRank());

        return raceTo.getPlayerRaceTo() <= player.getPoints() ||
                raceTo.getOpponentRaceTo() <= opponent.getPoints();
    }
}
