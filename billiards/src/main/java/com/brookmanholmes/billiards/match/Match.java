package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.GameTurn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.controller.PlayerController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 10/27/2015.
 */
public class Match<T extends AbstractPlayer> {
    long matchId;
    PlayerController<T> playerController;
    TurnEndHelper turnEndHelper;
    String location;
    Game game;
    List<GameStatus> gameStatuses = new ArrayList<>();
    List<Turn> turns = new ArrayList<>();
    List<Turn> undoneTurns = new ArrayList<>();

    Match(Builder builder, PlayerController<T> playerController) {
        location = builder.location;
        matchId = builder.id;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        this.playerController = playerController;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getLocation() {
        return location;
    }

    public GameStatus getGameStatus() {
        return game.getGameStatus();
    }

    public T getPlayer() {
        return playerController.getPlayer1();
    }

    public T getOpponent() {
        return playerController.getPlayer2();
    }

    public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch) {
        Turn turn = new GameTurn(turns.size(), matchId, scratch, turnEnd, tableStatus);

        updatePlayerStats(turn);
        updateGameState(turn);

        turns.add(turn);

        return turn;
    }

    public void addTurn(Turn turn) {
        updatePlayerStats(turn);
        updateGameState(turn);
    }

    void updatePlayerStats(Turn turn) {

    }

    public int getTurnCount() {
        return turns.size();
    }

    void updateGameState(Turn turn) {
        gameStatuses.add(game.addTurn(turn));
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                "\n location='" + location + '\'' +
                "\n game=" + game.toString() +
                '}';
    }

    public static class Builder {
        private String playerName, opponentName;
        private int playerRank = 100, opponentRank = 100;
        private BreakType breakType = BreakType.ALTERNATE;
        private PlayerTurn playerTurn = PlayerTurn.PLAYER;
        private GameType gameType = GameType.BCA_EIGHT_BALL;
        private String location = "Sam's Billiards";
        private long id;

        public Builder(String playerName, String opponentName) {
            this.playerName = playerName;
            this.opponentName = opponentName;
        }

        public Builder setPlayerRanks(int playerRank, int opponentRank) {
            this.playerRank = playerRank;
            this.opponentRank = opponentRank;
            return this;
        }

        public Match<?> build(GameType gameType) {
            this.gameType = gameType;
            return new Match<>(this, PlayerController.createController(Game.newGame(gameType, playerTurn, breakType), playerName, opponentName, playerRank, opponentRank));
        }

        public Builder setBreakType(BreakType breakType) {
            this.breakType = breakType;
            return this;
        }

        public Builder setPlayerTurn(PlayerTurn playerTurn) {
            this.playerTurn = playerTurn;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setMatchId(long id) {
            this.id = id;
            return this;
        }
    }
}
