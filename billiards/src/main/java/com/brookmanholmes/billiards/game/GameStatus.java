package com.brookmanholmes.billiards.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A data class to capture the state of a game
 * <p></p>Created by Brookman Holmes on 10/30/2015.
 */
public final class GameStatus implements Serializable {
    public final boolean playerAllowedToBreakAgain;
    public final boolean newGame;
    public final boolean opponentPlayedSuccessfulSafe;
    public final PlayerTurn turn;
    public final PlayerTurn breaker;
    public final GameType gameType;
    public final boolean allowPush;
    public final boolean allowTurnSkip;
    public final PlayerColor currentPlayerColor;
    public final PlayerColor playerColor;
    public final int currentPlayerConsecutiveFouls;
    public final int consecutivePlayerFouls;
    public final int consecutiveOpponentFouls;
    public final boolean winOnBreak;
    public final List<Integer> ballsOnTable;
    public final BreakType breakType;
    public final int turnsThisGame;
    public final int maxAttemptsPerGame;
    public final int innings; // not put into equals and hashcode because it's only used for APA and is meaningless in alternating break format

    GameStatus(Game game) {
        playerAllowedToBreakAgain = game.playerAllowedToBreakAgain;
        newGame = game.newGame;
        opponentPlayedSuccessfulSafe = game.opponentPlayedSuccessfulSafe;
        turn = game.turn;
        breaker = game.breaker;
        gameType = game.gameType;
        allowPush = game.allowPush;
        allowTurnSkip = game.allowTurnSkip;
        currentPlayerColor = game.getCurrentPlayerColor();
        currentPlayerConsecutiveFouls = game.getCurrentPlayersConsecutiveFouls();
        winOnBreak = game.winOnBreak();
        ballsOnTable = new ArrayList<>(game.ballsOnTable);
        breakType = game.breakType;
        innings = game.innings;
        turnsThisGame = game.turnsThisGame;
        maxAttemptsPerGame = game.maxAttemptsPerGame;
        playerColor = game.playerColor;
        consecutiveOpponentFouls = game.consecutiveOpponentFouls;
        consecutivePlayerFouls = game.consecutivePlayerFouls;
    }

    private GameStatus(Builder builder) {
        playerAllowedToBreakAgain = builder.playerAllowedToBreakAgain;
        newGame = builder.newGame;
        opponentPlayedSuccessfulSafe = builder.opponentPlayedSuccessfulSafe;
        turn = builder.turn;
        breaker = builder.breaker;
        gameType = builder.gameType;
        allowPush = builder.allowPush;
        allowTurnSkip = builder.allowTurnSkip;
        currentPlayerColor = builder.currentPlayerColor;
        currentPlayerConsecutiveFouls = builder.currentPlayerConsecutiveFouls;
        winOnBreak = builder.winOnBreak;
        ballsOnTable = builder.ballsOnTable;
        breakType = builder.breakType;
        innings = builder.innings;
        turnsThisGame = builder.turnsThisGame;
        maxAttemptsPerGame = builder.maxAttemptsPerGame;

        playerColor = builder.playerColor;
        consecutiveOpponentFouls = builder.consecutiveOpponentFouls;
        consecutivePlayerFouls = builder.consecutivePlayerFouls;
    }

    @Override
    public String toString() {
        return "GameStatus{" +
                "playerAllowedToBreakAgain=" + playerAllowedToBreakAgain +
                ", newGame=" + newGame +
                ", opponentPlayedSuccessfulSafe=" + opponentPlayedSuccessfulSafe +
                ", turn=" + turn +
                ", breaker=" + breaker +
                ", gameType=" + gameType +
                ", allowPush=" + allowPush +
                ", allowTurnSkip=" + allowTurnSkip +
                ", currentPlayerColor=" + currentPlayerColor +
                ", playerColor=" + playerColor +
                ", currentPlayerConsecutiveFouls=" + currentPlayerConsecutiveFouls +
                ", consecutivePlayerFouls=" + consecutivePlayerFouls +
                ", consecutiveOpponentFouls=" + consecutiveOpponentFouls +
                ", winOnBreak=" + winOnBreak +
                ", ballsOnTable=" + ballsOnTable +
                ", breakType=" + breakType +
                ", turnsThisGame=" + turnsThisGame +
                ", maxAttemptsPerGame=" + maxAttemptsPerGame +
                ", innings=" + innings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameStatus that = (GameStatus) o;

        if (playerAllowedToBreakAgain != that.playerAllowedToBreakAgain) return false;
        if (newGame != that.newGame) return false;
        if (opponentPlayedSuccessfulSafe != that.opponentPlayedSuccessfulSafe) return false;
        if (allowPush != that.allowPush) return false;
        if (allowTurnSkip != that.allowTurnSkip) return false;
        if (currentPlayerConsecutiveFouls != that.currentPlayerConsecutiveFouls) return false;
        if (consecutivePlayerFouls != that.consecutivePlayerFouls) return false;
        if (consecutiveOpponentFouls != that.consecutiveOpponentFouls) return false;
        if (winOnBreak != that.winOnBreak) return false;
        if (turn != that.turn) return false;
        if (breaker != that.breaker) return false;
        if (gameType != that.gameType) return false;
        if (currentPlayerColor != that.currentPlayerColor) return false;
        if (playerColor != that.playerColor) return false;
        if (!ballsOnTable.containsAll(((GameStatus) o).ballsOnTable)) return false;
        if (!((GameStatus) o).ballsOnTable.containsAll(ballsOnTable)) return false;
        return breakType == that.breakType;

    }

    @Override
    public int hashCode() {
        int result = (playerAllowedToBreakAgain ? 1 : 0);
        result = 31 * result + (newGame ? 1 : 0);
        result = 31 * result + (opponentPlayedSuccessfulSafe ? 1 : 0);
        result = 31 * result + turn.hashCode();
        result = 31 * result + breaker.hashCode();
        result = 31 * result + gameType.hashCode();
        result = 31 * result + (allowPush ? 1 : 0);
        result = 31 * result + (allowTurnSkip ? 1 : 0);
        result = 31 * result + currentPlayerColor.hashCode();
        result = 31 * result + playerColor.hashCode();
        result = 31 * result + currentPlayerConsecutiveFouls;
        result = 31 * result + consecutivePlayerFouls;
        result = 31 * result + consecutiveOpponentFouls;
        result = 31 * result + (winOnBreak ? 1 : 0);
        result = 31 * result + ballsOnTable.hashCode();
        result = 31 * result + breakType.hashCode();
        return result;
    }

    // TODO: 9/4/2016 JavaDoc this class
    public static final class Builder {
        private final GameType gameType;
        private boolean playerAllowedToBreakAgain = false;
        private boolean newGame = false;
        private boolean opponentPlayedSuccessfulSafe = false;
        private PlayerTurn turn;
        private PlayerTurn breaker;
        private boolean allowPush = false;
        private boolean allowTurnSkip = false;
        private PlayerColor currentPlayerColor = PlayerColor.OPEN;
        private int currentPlayerConsecutiveFouls = 0;
        private boolean winOnBreak;
        private List<Integer> ballsOnTable;
        private BreakType breakType;
        private int innings;
        private int turnsThisGame = 0;
        private int maxAttemptsPerGame = 0;

        private int consecutivePlayerFouls;
        private int consecutiveOpponentFouls;
        private PlayerColor playerColor;

        public Builder(GameType gameType) throws InvalidGameTypeException {
            this.gameType = gameType;

            switch (gameType) {
                case BCA_NINE_BALL:
                    winOnBreak = true;
                    break;
                case BCA_EIGHT_BALL:
                    winOnBreak = false;
                    break;
                case BCA_TEN_BALL:
                    winOnBreak = false;
                    break;
                case APA_EIGHT_BALL:
                    winOnBreak = true;
                    break;
                case APA_NINE_BALL:
                    winOnBreak = true;
                    break;
                case BCA_GHOST_NINE_BALL:
                    winOnBreak = true;
                    break;
                case BCA_GHOST_EIGHT_BALL:
                    winOnBreak = false;
                    break;
                case BCA_GHOST_TEN_BALL:
                    winOnBreak = false;
                    break;
                case APA_GHOST_EIGHT_BALL:
                    winOnBreak = true;
                    break;
                case APA_GHOST_NINE_BALL:
                    winOnBreak = true;
                    break;
                case STRAIGHT_POOL:
                    winOnBreak = false;
                    break;
                case STRAIGHT_GHOST:
                    winOnBreak = false;
                    break;
                default:
                    throw new InvalidGameTypeException(gameType.name());
            }

            ballsOnTable = new ArrayList<>();
            for (int i = 1; i <= gameType.getMaxBalls(); i++) {
                ballsOnTable.add(i);
            }
        }

        public Builder turnsThisGame(int turns) {
            turnsThisGame = turns;
            return this;
        }

        public Builder maxAttemptsPerGame(int maxAttempts) {
            maxAttemptsPerGame = maxAttempts;
            return this;
        }

        public Builder reBreak() {
            playerAllowedToBreakAgain = true;
            return this;
        }

        public Builder newGame() {
            newGame = true;
            return this;
        }

        public Builder breakType(BreakType breakType) {
            this.breakType = breakType;
            return this;
        }

        public Builder safetyLastTurn() {
            opponentPlayedSuccessfulSafe = true;
            return this;
        }

        public Builder turn(PlayerTurn turn) {
            this.turn = turn;
            return this;
        }

        public Builder breaker(PlayerTurn turn) {
            breaker = turn;
            return this;
        }

        public Builder allowPush() {
            allowPush = true;
            return this;
        }

        public Builder allowSkip() {
            allowTurnSkip = true;
            return this;
        }

        public Builder currentPlayerColor(PlayerColor color) {
            currentPlayerColor = color;
            return this;
        }

        public Builder currentPlayerConsecutiveFouls(int fouls) {
            currentPlayerConsecutiveFouls = fouls;
            return this;
        }

        public Builder consecutivePlayerFouls(int fouls) {
            consecutivePlayerFouls = fouls;
            return this;
        }

        public Builder consecutiveOpponentFouls(int fouls) {
            consecutiveOpponentFouls = fouls;
            return this;
        }

        public Builder playerColor(PlayerColor color) {
            playerColor = color;
            return this;
        }

        public Builder removeBalls(int... balls) {
            List<Integer> temp = new ArrayList<>();
            for (int ball : balls) {
                temp.add(ball);
            }

            ballsOnTable.removeAll(temp);
            return this;
        }

        public Builder setBalls(List<Integer> balls) {
            ballsOnTable.clear();
            ballsOnTable.addAll(balls);

            return this;
        }

        public Builder setInnings(int innings) {
            this.innings = innings;
            return this;
        }

        public GameStatus build() {
            if (turn == PlayerTurn.PLAYER)
                currentPlayerConsecutiveFouls = consecutivePlayerFouls;
            else currentPlayerConsecutiveFouls = consecutiveOpponentFouls;
            return new GameStatus(this);
        }
    }
}
