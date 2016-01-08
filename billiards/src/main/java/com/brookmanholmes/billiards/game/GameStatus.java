package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
public final class GameStatus {
    public final int GAME_BALL;
    public final int MAX_BALLS;
    public final boolean playerAllowedToBreakAgain;
    public final boolean newGame;
    public final boolean opponentPlayedSuccessfulSafe;
    public final PlayerTurn turn;
    public final PlayerTurn breaker;
    public final GameType gameType;
    public final boolean allowPush;
    public final boolean allowTurnSkip;
    public final PlayerColor currentPlayerColor;
    public final int currentPlayerConsecutiveFouls;
    public final boolean winOnBreak;
    public final List<Integer> ballsOnTable;

    GameStatus(Game game) {
        playerAllowedToBreakAgain = game.playerAllowedToBreakAgain;
        GAME_BALL = game.GAME_BALL;
        MAX_BALLS = game.MAX_BALLS;
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
        MAX_BALLS = builder.MAX_BALLS;
        GAME_BALL = builder.GAME_BALL;
        ballsOnTable = builder.ballsOnTable;
    }


    @Override
    public String toString() {
        return "GameStatus{" +
                "GAME_BALL=" + GAME_BALL +
                "\n MAX_BALLS=" + MAX_BALLS +
                "\n playerAllowedToBreakAgain=" + playerAllowedToBreakAgain +
                "\n newGame=" + newGame +
                "\n opponentPlayedSuccessfulSafe=" + opponentPlayedSuccessfulSafe +
                "\n turn=" + turn +
                "\n breaker=" + breaker +
                "\n gameType=" + gameType +
                "\n allowPush=" + allowPush +
                "\n allowTurnSkip=" + allowTurnSkip +
                "\n currentPlayerColor=" + currentPlayerColor +
                "\n currentPlayerConsecutiveFouls=" + currentPlayerConsecutiveFouls +
                "\n ballsOnTable=" + ballsOnTable.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameStatus that = (GameStatus) o;

        if (GAME_BALL != that.GAME_BALL) return false;
        if (MAX_BALLS != that.MAX_BALLS) return false;
        if (playerAllowedToBreakAgain != that.playerAllowedToBreakAgain) return false;
        if (newGame != that.newGame) return false;
        if (opponentPlayedSuccessfulSafe != that.opponentPlayedSuccessfulSafe) return false;
        if (allowPush != that.allowPush) return false;
        if (allowTurnSkip != that.allowTurnSkip) return false;
        if (currentPlayerConsecutiveFouls != that.currentPlayerConsecutiveFouls) return false;
        if (turn != that.turn) return false;
        if (breaker != that.breaker) return false;
        if (gameType != that.gameType) return false;
        return currentPlayerColor == that.currentPlayerColor;

    }

    @Override
    public int hashCode() {
        int result = GAME_BALL;
        result = 31 * result + MAX_BALLS;
        result = 31 * result + (playerAllowedToBreakAgain ? 1 : 0);
        result = 31 * result + (newGame ? 1 : 0);
        result = 31 * result + (opponentPlayedSuccessfulSafe ? 1 : 0);
        result = 31 * result + turn.hashCode();
        result = 31 * result + breaker.hashCode();
        result = 31 * result + gameType.hashCode();
        result = 31 * result + (allowPush ? 1 : 0);
        result = 31 * result + (allowTurnSkip ? 1 : 0);
        result = 31 * result + currentPlayerColor.hashCode();
        result = 31 * result + currentPlayerConsecutiveFouls;
        return result;
    }


    public static final class Builder {
        private int MAX_BALLS;
        private int GAME_BALL;
        private boolean playerAllowedToBreakAgain = false;
        private boolean newGame = false;
        private boolean opponentPlayedSuccessfulSafe = false;
        private PlayerTurn turn;
        private PlayerTurn breaker;
        private GameType gameType;
        private boolean allowPush = false;
        private boolean allowTurnSkip = false;
        private PlayerColor currentPlayerColor = PlayerColor.OPEN;
        private int currentPlayerConsecutiveFouls = 0;
        private boolean winOnBreak;
        private List<Integer> ballsOnTable;

        public Builder(GameType gameType) throws InvalidGameTypeException {
            this.gameType = gameType;

            switch (gameType) {
                case BCA_NINE_BALL:
                    MAX_BALLS = 9;
                    GAME_BALL = 9;
                    winOnBreak = true;
                    break;
                case BCA_EIGHT_BALL:
                    MAX_BALLS = 15;
                    GAME_BALL = 8;
                    winOnBreak = false;
                    break;
                case BCA_TEN_BALL:
                    MAX_BALLS = 10;
                    GAME_BALL = 10;
                    winOnBreak = false;
                    break;
                case APA_EIGHT_BALL:
                    MAX_BALLS = 15;
                    GAME_BALL = 8;
                    winOnBreak = true;
                    break;
                case APA_NINE_BALL:
                    MAX_BALLS = 9;
                    GAME_BALL = 9;
                    winOnBreak = true;
                    break;
                default:
                    throw new InvalidGameTypeException(gameType.name());
            }

            ballsOnTable = new ArrayList<>();
            for (int i = 1; i <= MAX_BALLS; i++) {
                ballsOnTable.add(i);
            }
        }

        public Builder reBreak() {
            playerAllowedToBreakAgain = true;
            return this;
        }

        public Builder newGame() {
            newGame = true;
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

        public Builder removeBalls(int... balls) {
            List<Integer> temp = new ArrayList<>();
            for (int ball : balls) {
                temp.add(ball);
            }

            ballsOnTable.removeAll(temp);
            return this;
        }

        public GameStatus build() {
            return new GameStatus(this);
        }
    }
}
