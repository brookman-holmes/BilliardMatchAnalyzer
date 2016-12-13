package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiards.game.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.PlayerColor.STRIPES;


/**
 * Keeps track of the status of a game of pool
 * <p></p>Created by Brookman Holmes on 10/26/2015.
 */
public abstract class Game {
    final int GAME_BALL;
    final int MAX_BALLS;
    final BreakType breakType;
    final GameType gameType;
    private final PlayerTurn firstPlayerToShoot;
    PlayerColor playerColor = OPEN;
    PlayerTurn turn, breaker;

    boolean playerAllowedToBreakAgain = false;
    boolean newGame = true;
    boolean allowTurnSkip = false;
    boolean allowPush = false;
    boolean opponentPlayedSuccessfulSafe = false;

    int consecutivePlayerFouls = 0;
    int consecutiveOpponentFouls = 0;
    int innings = 0;

    List<Integer> ballsOnTable;

    /**
     * Sets up the initial game status based on the inputs
     *
     * @param gameType  the type of game being played
     * @param turn      the turn of the first player
     * @param breakType the type of break that the game will use
     * @param MAX_BALLS The maximum balls in the game
     * @param GAME_BALL The ball that ends the game (8,9,10 generally)
     */
    Game(GameType gameType, PlayerTurn turn, BreakType breakType, int MAX_BALLS, int GAME_BALL) {
        this.gameType = gameType;
        this.breakType = breakType;

        this.GAME_BALL = GAME_BALL;
        this.MAX_BALLS = MAX_BALLS;

        this.turn = turn;
        this.breaker = turn;
        firstPlayerToShoot = turn;

        ballsOnTable = newTable();
    }

    /**
     * Creates a new game of the correct subclass with the supplied parameters
     * @param gameType The type of game to create
     * @param turn Which player starts the game
     * @param breakType The type of break that will be used throughout the game
     * @return A new game with the supplied parameters
     * @throws InvalidGameTypeException
     */
    public static Game newGame(GameType gameType, PlayerTurn turn, BreakType breakType) throws InvalidGameTypeException {
        // TODO: 10/27/2015 implement american rotation games
        switch (gameType) {
            case BCA_NINE_BALL:
                return new NineBallGame(turn, breakType);
            case BCA_TEN_BALL:
                return new TenBallGame(turn, breakType);
            case APA_EIGHT_BALL:
                return new ApaEightBallGame(turn);
            case APA_NINE_BALL:
                return new ApaNineBallGame(turn);
            case BCA_EIGHT_BALL:
                return new EightBallGame(turn, breakType);
            case BCA_GHOST_EIGHT_BALL:
                return new EightBallGame(GameType.BCA_GHOST_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.PLAYER);
            case BCA_GHOST_NINE_BALL:
                return new NineBallGame(GameType.BCA_GHOST_NINE_BALL, PlayerTurn.PLAYER, BreakType.PLAYER);
            case BCA_GHOST_TEN_BALL:
                return new TenBallGame(GameType.BCA_GHOST_TEN_BALL, PlayerTurn.PLAYER, BreakType.PLAYER);
            case APA_GHOST_EIGHT_BALL:
                return new EightBallGame(GameType.APA_GHOST_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.PLAYER);
            case APA_GHOST_NINE_BALL:
                return new NineBallGame(GameType.APA_GHOST_NINE_BALL, PlayerTurn.PLAYER, BreakType.PLAYER);
            case STRAIGHT_POOL:
                return new StraightPoolGame(turn);
            default:
                throw new InvalidGameTypeException(gameType.name());
        }
    }

    /**
     * Adds a turn to the game and updates it's state based on the turn
     * @param turn The turn to add to the game
     * @return The new game status of the game
     */
    final public GameStatus addTurn(ITurn turn) {
        if (isGameOver(turn))
            startNewGame(turn);
        else if (turn.getTurnEnd() == TurnEnd.CONTINUE_WITH_GAME) {
            playerAllowedToBreakAgain = false;
        } else if (turn.getTurnEnd() == TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN) {
            startNewGame(this.turn);
        } else if (turn.getTurnEnd() == TurnEnd.OPPONENT_BREAKS_AGAIN) {
            startNewGame(changeTurn(this.turn));
        } else
            updateGameStatus(turn);

        return new GameStatus(this);
    }

    /**
     * Updates the status of the game based on the incoming turn
     * @param turn The turn that is being added to the game
     */
    private void updateGameStatus(ITurn turn) {
        playerColor = setPlayerColor(turn); // this must be called before new game otherwise stuff on the break doesn't work!
        playerAllowedToBreakAgain = setAllowPlayerToBreakAgain(turn); // ditto to above comment
        setConsecutiveFouls(turn);

        removeBallsFromTable(turn.getBallsToRemoveFromTable());

        newGame = false;

        allowPush = setAllowPush(turn);
        allowTurnSkip = setAllowTurnSkip(turn);
        opponentPlayedSuccessfulSafe = setOpponentPlayedSuccessfulSafe(turn);
        // this happens at the end
        this.turn = changeTurn(this.turn);
    }

    /**
     * Determines whether or not the game should allow a push shot
     * @param turn the incoming turn
     * @return true for if a push shot is allowed, false if a push shot is not
     */
    abstract boolean setAllowPush(ITurn turn);

    /**
     * Determines whether or not the game should allow a player to skip their turn
     * @param turn the incoming turn
     * @return true for if turn skipping is allowed, false if turn skipping is not
     */
    abstract boolean setAllowTurnSkip(ITurn turn);

    /**
     * Determines whether the player is solids, stripes or undetermined
     * @param turn The incoming turn
     * @return 0 for open, 1 for Player is solids, 2 for Player is solids
     */
    abstract PlayerColor setPlayerColor(ITurn turn);

    /**
     * Determine whether a player is allowed to break again
     * @param turn the incoming turn
     * @return true for a re-break is allowed, false for it is not
     */
    abstract boolean setAllowPlayerToBreakAgain(ITurn turn);

    /**
     * Determines whether the incoming turn was a SAFETY
     *
     * @param turn The incoming turn
     * @return true: the player played a safety, false: the player did not play a safety
     */
    boolean setOpponentPlayedSuccessfulSafe(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.SAFETY;
    }

    /**
     * Changes the turn of the game to the next player
     *
     * @param turn Which player's turn it currently is
     * @return Which player shoots next
     */
    private PlayerTurn changeTurn(PlayerTurn turn) {
        if (turn.nextPlayer() == firstPlayerToShoot)
            innings++;

        return turn.nextPlayer();
    }

    /**
     * Determines whether a new game should be started
     * @param turn The incoming turn
     * @return true for the game is over, false for the game is not over
     */
    boolean isGameOver(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.GAME_WON || turn.isSeriousFoul();
    }

    /**
     * Sets the game status to a new game
     * @param turn The incoming turn
     */
    void startNewGame(ITurn turn) {
        this.breaker = setBreaker(getGameWinner(turn));
        startNewGame(breaker);
    }

    /**
     * Determines the player which should break based on who won the last game
     *
     * @param previousGameWinner The winner of the previous game
     * @return The player that breaks in the new game (PLAYER or OPPONENT)
     */
    PlayerTurn setBreaker(PlayerTurn previousGameWinner) {
        switch (breakType) {
            case WINNER:
                return previousGameWinner;
            case LOSER:
                return changeTurn(previousGameWinner);
            case PLAYER:
                return PlayerTurn.PLAYER;
            case OPPONENT:
                return PlayerTurn.OPPONENT;
            case ALTERNATE:
                return changeTurn(breaker);
            default:
                throw new IllegalStateException("breakType: " + breakType + " is not supported");
        }
    }

    /**
     * Sets the game status to a new game with the correct player breaking
     * @param nextBreaker The player that should break for the new game
     */
    private void startNewGame(PlayerTurn nextBreaker) {
        // don't set this.breaker because it'll ruin the order of switching breaks
        this.turn = nextBreaker;
        newGame = true;
        opponentPlayedSuccessfulSafe = false;

        playerAllowedToBreakAgain = false;
        playerColor = OPEN;
        consecutiveOpponentFouls = 0;
        consecutivePlayerFouls = 0;
        allowPush = false;
        allowTurnSkip = false;

        ballsOnTable = newTable();
    }

    /**
     * Determines the winner of the game
     * @param turn The incoming turn
     * @return The player that won the game (PLAYER or OPPONENT)
     */
    PlayerTurn getGameWinner(ITurn turn) {
        if (turn.getTurnEnd() == TurnEnd.GAME_WON)
            return this.turn;
        else return changeTurn(this.turn);
    }

    /**
     * Creates a list of balls that are on the table based on the maximum number of balls allowed
     * @return a list of integers between 1 and MAX_BALLS
     */
    private List<Integer> newTable() {
        List<Integer> table = new ArrayList<>(MAX_BALLS);
        for (int i = 1; i <= MAX_BALLS; i++)
            table.add(i);

        return table;
    }

    /**
     * Sets the consecutive fouls of PLAYER and OPPONENT
     * @param turn The incoming turn
     */
    void setConsecutiveFouls(ITurn turn) {
        if (turn.isFoul()) {
            if (this.turn == PlayerTurn.PLAYER)
                consecutivePlayerFouls++;
            else
                consecutiveOpponentFouls++;
        } else {
            if (this.turn == PlayerTurn.PLAYER)
                consecutivePlayerFouls = 0;
            else
                consecutiveOpponentFouls = 0;
        }
    }

    /**
     * Removes balls that are not on the table anymore
     * @param ballsToRemove The list of balls that are to be removed
     */
    void removeBallsFromTable(List<Integer> ballsToRemove) {
        ballsOnTable.removeAll(ballsToRemove);

        if (!ballsOnTable.contains(GAME_BALL))
            ballsOnTable.add(GAME_BALL);
    }

    /**
     * Retrieve the consecutive fouls based on who the current player is
     * @return an integer that is between 0 and (hopefully) 2
     */
    int getCurrentPlayersConsecutiveFouls() {
        if (turn == PlayerTurn.PLAYER)
            return consecutivePlayerFouls;
        else return consecutiveOpponentFouls;
    }

    /**
     * Retrieves who's turn it is
     * @return PLAYER or OPPONENT
     */
    public PlayerTurn getTurn() {
        return turn;
    }

    /**
     * Retrieves the type of game that is being played
     * @return a value from GameType
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * Retrieves the current game status of the game
     * @return a new GameStatus
     */
    public GameStatus getGameStatus() {
        return new GameStatus(this);
    }

    /**
     * Sets the game status of this game to the same as another game status
     *
     * @param gameStatus The game status that you would like this game to have
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.turn = gameStatus.turn;
        this.breaker = gameStatus.breaker;
        this.playerAllowedToBreakAgain = gameStatus.playerAllowedToBreakAgain;
        this.newGame = gameStatus.newGame;
        this.allowPush = gameStatus.allowPush;
        this.allowTurnSkip = gameStatus.allowTurnSkip;
        this.opponentPlayedSuccessfulSafe = gameStatus.opponentPlayedSuccessfulSafe;
        this.consecutiveOpponentFouls = gameStatus.consecutiveOpponentFouls;
        this.consecutivePlayerFouls = gameStatus.consecutivePlayerFouls;
        this.playerColor = gameStatus.playerColor;
        this.innings = gameStatus.innings;

        ballsOnTable = new ArrayList<>(gameStatus.ballsOnTable);
    }

    /**
     * Retrieves the current table status of this game
     * @return A new TableStatus that contains the current balls on the table
     */
    public ITableStatus getCurrentTableStatus() {
        return TableStatus.newTable(gameType, ballsOnTable);
    }

    /**
     * Retrieves whether or not the game is possible to win on the break
     * @return True if you can win by making a specified ball on the break, false otherwise
     */
    boolean winOnBreak() {
        return false;
    }

    /**
     * Returns the type of balls that the current player is on
     * @return OPEN means that no color has been selected, SOLIDS if the current player is shooting
     * solids or STRIPES if the current player is shooting stripes
     */
    PlayerColor getCurrentPlayerColor() {
        if (playerColor == OPEN) {
            return OPEN;
        } else {
            if (turn == PlayerTurn.PLAYER)
                return playerColor;
            else {
                if (playerColor == SOLIDS)
                    return STRIPES;
                else return SOLIDS;
            }

        }
    }

    /**
     * Retrieves an array of balls that the ghost 'made' when it was their turn
     * @return a new integer array where each element is corresponds to a ball that is currently
     * on the table and will be 'made' by the ghost
     */
    public abstract int[] getGhostBallsToWinGame();

    @Override public String toString() {
        return "Game{" +
                "GAME_BALL=" + GAME_BALL +
                "\n MAX_BALLS=" + MAX_BALLS +
                "\n breakType=" + breakType +
                "\n gameType=" + gameType +
                "\n firstPlayerToShoot=" + firstPlayerToShoot +
                "\n playerColor=" + playerColor +
                "\n turn=" + turn +
                "\n breaker=" + breaker +
                "\n playerAllowedToBreakAgain=" + playerAllowedToBreakAgain +
                "\n newGame=" + newGame +
                "\n allowTurnSkip=" + allowTurnSkip +
                "\n allowPush=" + allowPush +
                "\n opponentPlayedSuccessfulSafe=" + opponentPlayedSuccessfulSafe +
                "\n consecutivePlayerFouls=" + consecutivePlayerFouls +
                "\n consecutiveOpponentFouls=" + consecutiveOpponentFouls +
                "\n innings=" + innings +
                "\n ballsOnTable=" + ballsOnTable +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (GAME_BALL != game.GAME_BALL) return false;
        if (MAX_BALLS != game.MAX_BALLS) return false;
        if (playerAllowedToBreakAgain != game.playerAllowedToBreakAgain) return false;
        if (newGame != game.newGame) return false;
        if (allowTurnSkip != game.allowTurnSkip) return false;
        if (allowPush != game.allowPush) return false;
        if (opponentPlayedSuccessfulSafe != game.opponentPlayedSuccessfulSafe) return false;
        if (consecutivePlayerFouls != game.consecutivePlayerFouls) return false;
        if (consecutiveOpponentFouls != game.consecutiveOpponentFouls) return false;
        if (innings != game.innings) return false;
        if (breakType != game.breakType) return false;
        if (gameType != game.gameType) return false;
        if (firstPlayerToShoot != game.firstPlayerToShoot) return false;
        if (playerColor != game.playerColor) return false;
        if (turn != game.turn) return false;
        if (breaker != game.breaker) return false;
        return ballsOnTable.equals(game.ballsOnTable);

    }

    @Override public int hashCode() {
        int result = GAME_BALL;
        result = 31 * result + MAX_BALLS;
        result = 31 * result + breakType.hashCode();
        result = 31 * result + gameType.hashCode();
        result = 31 * result + firstPlayerToShoot.hashCode();
        result = 31 * result + playerColor.hashCode();
        result = 31 * result + turn.hashCode();
        result = 31 * result + breaker.hashCode();
        result = 31 * result + (playerAllowedToBreakAgain ? 1 : 0);
        result = 31 * result + (newGame ? 1 : 0);
        result = 31 * result + (allowTurnSkip ? 1 : 0);
        result = 31 * result + (allowPush ? 1 : 0);
        result = 31 * result + (opponentPlayedSuccessfulSafe ? 1 : 0);
        result = 31 * result + consecutivePlayerFouls;
        result = 31 * result + consecutiveOpponentFouls;
        result = 31 * result + innings;
        result = 31 * result + ballsOnTable.hashCode();
        return result;
    }
}
