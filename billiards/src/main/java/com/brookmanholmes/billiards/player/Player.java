package com.brookmanholmes.billiards.player;

/**
 * Class that controls a player's stats
 * Created by Brookman on 11/29/2014.
 */

public class Player extends AbstractPlayer {
    int breakWins = 0;
    int runEarlyWins = 0;
    int consecutiveFouls = 0;

    public Player(String name) {
        super(name);
    }

    public void addPlayerData(Player player) {
        this.shootingBallsMade += player.shootingBallsMade;
        this.shootingScratches += player.shootingScratches;
        this.shootingTurns += player.shootingTurns;
        this.shootingMisses += player.shootingMisses;

        this.breakAttempts += player.breakAttempts;
        this.breakScratches += player.breakScratches;
        this.breakBallsMade += player.breakBallsMade;
        this.breakContinuations += player.breakContinuations;
        this.breakSuccesses += player.breakSuccesses;
        this.breakWins += player.breakWins;

        this.safetyAttempts += player.safetyAttempts;
        this.safetySuccesses += player.safetySuccesses;
        this.safetyScratches += player.safetyScratches;
        this.safetyEscapes += player.safetyEscapes;
        this.safetyReturns += player.safetyReturns;
        this.safetyForcedErrors += player.safetyForcedErrors;

        this.runEarlyWins += player.runEarlyWins;
        this.runTierTwo += player.runTierTwo;
        this.runTierOne += player.runTierOne;
        this.runOuts += player.runOuts;

        this.gameTotal += player.gameTotal;
        this.gameWins += player.gameWins;
    }

}
