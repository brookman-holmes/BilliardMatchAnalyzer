package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiards.game.util.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.util.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.util.PlayerColor.STRIPES;


/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public abstract class Game {
    final int GAME_BALL;
    final int MAX_BALLS;
    final BreakType breakType;
    final GameType gameType;
    final PlayerTurn firstPlayerToShoot;
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

    public static Game newGame(GameType gameType, PlayerTurn turn, BreakType breakType) throws InvalidGameTypeException {
        // TODO: 10/27/2015 implement straight pool and american rotation games
        switch (gameType) {
            case BCA_NINE_BALL:
                return new NineBallGame(turn, breakType);
            case BCA_TEN_BALL:
                return new TenBallGame(turn, breakType);
            case APA_EIGHT_BALL:
                return new ApaEightBallGame();
            case APA_NINE_BALL:
                return new ApaNineBallGame();
            case BCA_EIGHT_BALL:
                return new EightBallGame(turn, breakType);
            default:
                throw new InvalidGameTypeException(gameType.name());
        }
    }

    public PlayerTurn changeTurn(PlayerTurn turn) {
        if (turn.nextPlayer() == firstPlayerToShoot)
            innings++;

        return turn.nextPlayer();
    }

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

    abstract boolean setAllowPush(com.brookmanholmes.billiards.turn.Turn turn);

    abstract boolean setAllowTurnSkip(com.brookmanholmes.billiards.turn.Turn turn);

    abstract PlayerColor setPlayerColor(com.brookmanholmes.billiards.turn.Turn turn);

    abstract boolean setAllowPlayerToBreakAgain(com.brookmanholmes.billiards.turn.Turn turn);

    final public GameStatus addTurn(com.brookmanholmes.billiards.turn.Turn turn) {
        if (isGameOver(turn))
            startNewGame(turn);
        else if (turn.getTurnEnd() == TurnEnd.CONTINUE_WITH_GAME) {
            playerAllowedToBreakAgain = false;
        } else if (turn.getTurnEnd() == TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN) {
            startNewGame(this.turn);
        } else if (turn.getTurnEnd() == TurnEnd.OPPONENT_BREAKS_AGAIN) {
            startNewGame(changeTurn(this.turn));
        } else
            setGameStatus(turn);

        return new GameStatus(this);
    }

    boolean isGameOver(com.brookmanholmes.billiards.turn.Turn turn) {
        return turn.getTurnEnd() == TurnEnd.GAME_WON || turn.isGameLost();
    }

    void startNewGame(com.brookmanholmes.billiards.turn.Turn turn) {
        this.breaker = setBreaker(getGameWinner(turn));
        startNewGame(breaker);
    }

    void startNewGame(PlayerTurn nextBreaker) {
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
            case GHOST:
                return PlayerTurn.PLAYER;
            default:
                throw new IllegalStateException("breakType: " + breakType + " is not supported");
        }
    }

    PlayerTurn getGameWinner(com.brookmanholmes.billiards.turn.Turn turn) {
        if (turn.getTurnEnd() == TurnEnd.GAME_WON)
            return this.turn;
        else return changeTurn(this.turn);
    }

    List<Integer> newTable() {
        List<Integer> table = new ArrayList<>(MAX_BALLS);
        for (int i = 1; i <= MAX_BALLS; i++)
            table.add(i);

        return table;
    }

    void setConsecutiveFouls(com.brookmanholmes.billiards.turn.Turn turn) {
        if (turn.isScratch()) {
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

    boolean setOpponentPlayedSuccessfulSafe(com.brookmanholmes.billiards.turn.Turn turn) {
        return turn.getTurnEnd() == TurnEnd.SAFETY;
    }

    void removeBallsFromTable(List<Integer> ballsToRemove) {
        ballsOnTable.removeAll(ballsToRemove);

        if (!ballsOnTable.contains(GAME_BALL))
            ballsOnTable.add(GAME_BALL);
    }

    int getCurrentPlayersConsecutiveFouls() {
        if (turn == PlayerTurn.PLAYER)
            return consecutivePlayerFouls;
        else return consecutiveOpponentFouls;
    }

    public PlayerTurn getTurn() {
        return turn;
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameStatus getGameStatus() {
        return new GameStatus(this);
    }

    void setGameStatus(com.brookmanholmes.billiards.turn.Turn turn) {
        setConsecutiveFouls(turn);

        removeBallsFromTable(turn.getBallsToRemoveFromTable());

        newGame = false;

        allowPush = setAllowPush(turn);
        allowTurnSkip = setAllowTurnSkip(turn);
        playerColor = setPlayerColor(turn);
        playerAllowedToBreakAgain = setAllowPlayerToBreakAgain(turn);
        opponentPlayedSuccessfulSafe = setOpponentPlayedSuccessfulSafe(turn);
        // this happens at the end
        this.turn = changeTurn(this.turn);
    }

    public TableStatus getCurrentTableStatus() {
        return TableStatus.newTable(gameType, ballsOnTable);
    }

    boolean winOnBreak() {
        return false;
    }

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
}
