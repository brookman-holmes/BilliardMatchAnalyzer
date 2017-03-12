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
    String playerId, opponentId;
    int playerRank, opponentRank;

    /**
     * Creates a new player controller
     * @param playerId The name of the player
     * @param opponentId The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     */
    PlayerController(String playerId, String opponentId,
                     String playerName, String opponentName,
                     int playerRank, int opponentRank) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerId = playerId;
        this.opponentId = opponentId;
        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
    }

    /**
     * Creates a new player controller
     * @param playerId The name of the player
     * @param opponentId The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 9 ball matches
     */
    public static PlayerController createNineBallController(String playerId, String opponentId,
                                                            String playerName, String opponentName,
                                                            int playerRank, int opponentRank) {
        return new NineBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param playerId The name of the player
     * @param opponentId The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 10 ball matches
     */
    public static PlayerController createTenBallController(String playerId, String opponentId,
                                                           String playerName, String opponentName,
                                                           int playerRank, int opponentRank) {
        return new TenBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param playerId The name of the player
     * @param opponentId The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return returns a new player controller for BCA 8 ball matches
     */
    public static PlayerController createEightBallController(String playerId, String opponentId,
                                                             String playerName, String opponentName,
                                                             int playerRank, int opponentRank) {
        return new EightBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
    }

    /**
     * Creates a new player controller
     * @param game The game that this player controller will be based on
     * @param playerId The name of the player
     * @param opponentId The name of the opponent
     * @param playerRank The rank of the player
     * @param opponentRank The rank of the opponent
     * @return  returns a new player controller based on the type of game passed in
     */
    public static PlayerController createController(Game game, String playerId, String opponentId,
                                                    String playerName, String opponentName,
                                                    int playerRank, int opponentRank) {
        switch (game.getGameType()) {
            case BCA_NINE_BALL:
            case BCA_GHOST_NINE_BALL:
                return new NineBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case BCA_TEN_BALL:
            case BCA_GHOST_TEN_BALL:
                return new TenBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case APA_EIGHT_BALL:
            case APA_GHOST_EIGHT_BALL:
                return new ApaEightBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case APA_NINE_BALL:
            case APA_GHOST_NINE_BALL:
                return new ApaNineBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case BCA_EIGHT_BALL:
            case BCA_GHOST_EIGHT_BALL:
                return new EightBallController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case STRAIGHT_POOL:
            case STRAIGHT_GHOST:
                return new StraightPoolController(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
            case EQUAL_OFFENSE:
                return null; // TODO: 1/5/2017 implement equal offense here
            case EQUAL_DEFENSE:
                return null; // // TODO: 1/5/2017 implement equal defense here
            default:
                throw new InvalidGameTypeException(game.getGameType().name());
        }
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter for the player name
     * @return The name of the 'player'
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Setter for the player name
     * @param newId The new name for 'player'
     */
    public void setPlayerId(String newId) {
        playerId = newId;
    }

    /**
     * Getter for the opponent name
     * @return The name of the 'opponent'
     */
    public String getOpponentId() {
        return opponentId;
    }

    /**
     * Setter for the opponent name
     * @param newId The new name for 'opponent'
     */
    public void setOpponentId(String newId) {
        opponentId = newId;
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

        Player player1 = new Player(playerId, playerName, gameStatus.gameType, playerRank, opponentRank);
        Player player2 = new Player(opponentId, opponentName, gameStatus.gameType, opponentRank, playerRank);

        switch (gameStatus.turn) {
            case PLAYER:
                player1.addTurn(turn);
                addStatsToPlayer(player1);
                break;
            case OPPONENT:
                player2.addTurn(turn);
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
        return turn.getTurnEnd() == GAME_WON || turn.isSeriousFoul()
                || (gameStatus.gameType.isSinglePlayer() && gameStatus.maxAttemptsPerGame <= gameStatus.turnsThisGame + 1);
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
        else if (gameStatus.gameType.isSinglePlayer() && gameStatus.maxAttemptsPerGame <= gameStatus.turnsThisGame + 1) // the ghost wins if you exceed your max attempts per game
            return PlayerTurn.OPPONENT;
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
        if (gameStatus.gameType.isSinglePlayer()) {
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
        return gameStatus.gameType.getMaxBalls();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerController that = (PlayerController) o;

        if (playerRank != that.playerRank) return false;
        if (opponentRank != that.opponentRank) return false;
        if (gameStatus != null ? !gameStatus.equals(that.gameStatus) : that.gameStatus != null)
            return false;
        if (turn != null ? !turn.equals(that.turn) : that.turn != null) return false;
        if (!playerName.equals(that.playerName)) return false;
        if (!opponentName.equals(that.opponentName)) return false;
        if (!playerId.equals(that.playerId)) return false;
        return opponentId.equals(that.opponentId);

    }

    @Override
    public int hashCode() {
        int result = gameStatus != null ? gameStatus.hashCode() : 0;
        result = 31 * result + (turn != null ? turn.hashCode() : 0);
        result = 31 * result + playerName.hashCode();
        result = 31 * result + opponentName.hashCode();
        result = 31 * result + playerId.hashCode();
        result = 31 * result + opponentId.hashCode();
        result = 31 * result + playerRank;
        result = 31 * result + opponentRank;
        return result;
    }

    @Override
    public String toString() {
        return "PlayerController{" +
                "gameStatus=" + gameStatus +
                ", turn=" + turn +
                ", playerId='" + playerId + '\'' +
                ", opponentId='" + opponentId + '\'' +
                ", playerRank=" + playerRank +
                ", opponentRank=" + opponentRank +
                '}';
    }

    public int getOpponentRank() {
        return opponentRank;
    }

    public int getPlayerRank() {
        return playerRank;
    }
}
