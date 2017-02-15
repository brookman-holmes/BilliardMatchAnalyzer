package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.ITurn;

import java.io.Serializable;

import static com.brookmanholmes.billiards.turn.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY_ERROR;

/**
 * Created by Brookman Holmes on 10/28/2015.
 * A controller for adding up player stats based on the game they are playing
 */
public abstract class PlayerController implements Serializable {
    GameStatus gameStatus;
    ITurn turn;
    String playerName, opponentName;
    int playerRank, opponentRank;

    /**
     * Creates a new player controller
     * @param playerName The name of the player
     * @param opponentName The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     */
    PlayerController(String playerName, String opponentName, int playerRank, int opponentRank) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
    }

    /**
     * Creates a new player controller
     * @param playerName The name of the player
     * @param opponentName The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 9 ball matches
     */
    public static PlayerController createNineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new NineBallController(playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param playerName The name of the player
     * @param opponentName The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 10 ball matches
     */
    public static PlayerController createTenBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new TenBallController(playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param playerName The name of the player
     * @param opponentName The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 8 ball matches
     */
    public static PlayerController createEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new EightBallController(playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param game The game that this player controller will be based on
     * @param playerName The name of the player
     * @param opponentName The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return  returns a new player controller based on the type of game passed in
     */
    public static PlayerController createController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        switch (game.getGameType()) {
            case BCA_NINE_BALL:
            case BCA_GHOST_NINE_BALL:
                return new NineBallController(playerName, opponentName, playerRank, opponentRank);
            case BCA_TEN_BALL:
            case BCA_GHOST_TEN_BALL:
                return new TenBallController(playerName, opponentName, playerRank, opponentRank);
            case APA_EIGHT_BALL:
            case APA_GHOST_EIGHT_BALL:
                return new ApaEightBallController(playerName, opponentName, playerRank, opponentRank);
            case APA_NINE_BALL:
            case APA_GHOST_NINE_BALL:
                return new ApaNineBallController(playerName, opponentName, playerRank, opponentRank);
            case BCA_EIGHT_BALL:
            case BCA_GHOST_EIGHT_BALL:
                return new EightBallController(playerName, opponentName, playerRank, opponentRank);
            case STRAIGHT_POOL:
            case STRAIGHT_GHOST:
                return new StraightPoolController(playerName, opponentName, playerRank, opponentRank);
            case EQUAL_OFFENSE:
                return null; // TODO: 1/5/2017 implement equal offense here
            case EQUAL_DEFENSE:
                return null; // // TODO: 1/5/2017 implement equal defense here
            default:
                throw new InvalidGameTypeException(game.getGameType().name());
        }
    }

    /**
     * Getter for the player name
     * @return The name of the 'player'
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Setter for the player name
     * @param newName The new name for 'player'
     */
    public void setPlayerName(String newName) {
        playerName = newName;
    }

    /**
     * Getter for the opponent name
     * @return The name of the 'opponent'
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * Setter for the opponent name
     * @param newName The new name for 'opponent'
     */
    public void setOpponentName(String newName) {
        opponentName = newName;
    }

    /**
     * Adds in a turn to the match and accumulates player scores based on the turn and game status provided
     * @param gameStatus The current status of the game
     * @param turn The turn that is being added to the match
     * @return A pair of players with scores based on this turn
     */
    public Pair<Player> addTurn(GameStatus gameStatus, ITurn turn) {
        assert gameStatus != null;
        assert turn != null;

        this.gameStatus = gameStatus;
        this.turn = turn;

        Player player1 = new Player(playerName, gameStatus.gameType, playerRank, opponentRank);
        Player player2 = new Player(opponentName, gameStatus.gameType, opponentRank, playerRank);

        switch (gameStatus.turn) {
            case PLAYER:
                addStatsToPlayer(player1);
                break;
            case OPPONENT:
                addStatsToPlayer(player2);
                break;
            default:
                throw new IllegalStateException("I'm not sure if this is possible to reach");
        }

        if (isGameOver()) {
            addGamesToPlayers(player1, player2);
        }

        return new Pair<>(player1, player2);
    }

    /**
     * Adds games won/lost to both players
     * @param player1 The player
     * @param player2 The opponent
     */
    void addGamesToPlayers(Player player1, Player player2) {
        if (getGameWinner() == PlayerTurn.PLAYER) {
            player1.addGameWon();
            player2.addGameLost();
        } else {
            player1.addGameLost();
            player2.addGameWon();
        }
    }

    /**
     * Returns whether the game is over
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver() {
        return turn.getTurnEnd() == GAME_WON || turn.isSeriousFoul();
    }

    /**
     * Retrieve the turn of the player who won the game
     * @return {@link com.brookmanholmes.billiards.game.PlayerTurn#PLAYER} for the player winning,
     * {@link com.brookmanholmes.billiards.game.PlayerTurn#OPPONENT} if the opponent won
     */
    PlayerTurn getGameWinner() {
        if (turn.getTurnEnd() == GAME_WON)
            return gameStatus.turn;
        else if (turn.isSeriousFoul())
            return gameStatus.turn.nextPlayer();
        else throw new IllegalStateException("Should not be called if the game is not over");
    }

    /**
     * Add all stats to the current player
     * @param player The player who's current turn it is
     */
    private void addStatsToPlayer(Player player) {
        addSafetyStats(player);
        addShootingStats(player);

        if (turn.getTurnEnd() == GAME_WON)
            addRunOutStats(player);

        if (gameStatus.newGame)
            addBreakingStats(player);
    }

    /**
     * Add in safety stats for a player
     * @param player The player to add safety stats to
     */
    void addSafetyStats(Player player) {
        if (turn.getTurnEnd() == SAFETY)
            player.addSafety(gameStatus.opponentPlayedSuccessfulSafe, turn.getShootingBallsMade());
        else if (turn.getTurnEnd() == SAFETY_ERROR)
            player.addSafetyAttempt(turn.isFoul());

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.getShootingBallsMade() > 0)
            player.addSafetyEscape();

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.isFoul()) {
            player.addSafetyForcedError();
        }
    }

    /**
     * Add in shooting stats for a player
     * @param player The player to add shooting stats to
     */
    void addShootingStats(Player player) {
        if (turn.getTurnEnd() == MISS)
            player.addShootingMiss();

        if (addTurnToPlayer())
            player.addShootingBallsMade(turn.getShootingBallsMade(), turn.isFoul());
    }

    /**
     * Determine if the player should earn a turn to be used for their average balls per turn stats
     * @return True a turn should be added for the player, false otherwise
     */
    boolean addTurnToPlayer() {
        // if the player made some balls or missed
        return (turn.getShootingBallsMade() > 0 || turn.getTurnEnd() == MISS);
    }

    /**
     * Add breaking stats for this player
     * @param player The player to add breaking stats to
     */
    void addBreakingStats(Player player) {
        if (gameStatus.gameType.isGhostGame()) {
            player.addBreakShot(turn.getBreakBallsMade(),
                    turn.getShootingBallsMade() > 0 && turn.getBreakBallsMade() > 0,
                    turn.getDeadBallsOnBreak() > 0);
        } else
            player.addBreakShot(
                    turn.getBreakBallsMade(), // how many balls the player made on the break
                    turn.getShootingBallsMade() > 0, // determine if there was continuation or not
                    turn.getTurnEnd() == BREAK_MISS && turn.isFoul()  // determine if the player scratched on the break
            );
    }

    /**
     * Add run out stats to this player
     * @param player The player to add run out stats to
     */
    void addRunOutStats(Player player) {
        if (gameStatus.newGame && getTotalBallsMade() >= getMaximumBallsMakeable()) // break and run
            player.addBreakAndRun();
        else if (turn.getShootingBallsMade() == getMaximumBallsMakeable()) // table run
            player.addTableRun();
        else if (turn.getShootingBallsMade() <= 5) // four ball run
            player.addFiveBallRun();
    }

    /**
     * Returns the number of balls that the player made during their turn, shooting and breaking balls
     * included
     * @return The number of balls made this turn
     */
    private int getTotalBallsMade() {
        return turn.getShootingBallsMade() + turn.getBreakBallsMade();
    }

    /**
     * The maximum number of balls that a player can make in this game,
     * (8 in 8 ball, 9 in 9 ball, 10 in 10 ball)
     * @return An integer of the maximum number of balls makeable
     */
    int getMaximumBallsMakeable() {
        return gameStatus.MAX_BALLS;
    }

    @Override
    public String toString() {
        return "PlayerController{" +
                "gameStatus=" + gameStatus +
                ", turn=" + turn +
                ", playerName='" + playerName + '\'' +
                ", opponentName='" + opponentName + '\'' +
                ", playerRank=" + playerRank +
                ", opponentRank=" + opponentRank +
                '}';
    }
}
