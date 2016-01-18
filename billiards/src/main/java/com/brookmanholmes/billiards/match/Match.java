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
import com.brookmanholmes.billiards.inning.TurnEndOptions;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.PlayerPair;
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
    List<PlayerPair<?>> players = new ArrayList<>();
    List<GameStatus> gameStatuses = new ArrayList<>();
    List<GameTurn> turns = new ArrayList<>();
    List<GameTurn> undoneTurns = new ArrayList<>();

    Match(Builder builder, PlayerController<T> playerController) {
        location = builder.location;
        matchId = 0;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        this.playerController = playerController;
        turnEndHelper = TurnEndHelper.newTurnEndHelper(game.getGameType());
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

    public GameTurn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch) {
        GameTurn turn = new GameTurn(turns.size(), matchId, scratch, turnEnd, tableStatus);

        updatePlayerStats(turn);
        updateGameState(turn);

        return turn;
    }

    void updatePlayerStats(Turn turn) {
        players.add(playerController.updatePlayerStats(game.getGameStatus(), turn));
    }

    void updateGameState(Turn turn) {
        gameStatuses.add(game.addTurn(turn));
    }

    public TurnEndOptions getTurnEndOptions(TableStatus nextTurn) {
        return turnEndHelper.create(game.getGameStatus(), nextTurn);
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
    }
}
