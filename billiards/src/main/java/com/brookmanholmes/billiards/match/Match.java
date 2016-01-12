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
import com.brookmanholmes.billiards.player.PlayerPair;
import com.brookmanholmes.billiards.player.controller.PlayerController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 10/27/2015.
 */
public class Match {
    final long matchId;
    PlayerController<?> playerController;
    TurnEndHelper turnEndHelper;
    Game game;
    List<PlayerPair<?>> players = new ArrayList<>();
    List<GameStatus> gameStatuses = new ArrayList<>();
    List<GameTurn> turns = new ArrayList<>();
    List<GameTurn> undoneTurns = new ArrayList<>();

    Match(Builder builder) {
        matchId = 0;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        /* These need to be set
        builder.playerRank;
        builder.opponentRank;
        builder.playerName;
        builder.opponentName;
        */
    }

    Match(GameType gameType, PlayerTurn turn, BreakType breakType) {
        game = Game.newGame(gameType, turn, breakType);
        turnEndHelper = TurnEndHelper.newTurnEndHelper(game.getGameType());
        matchId = 0;
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

    public static class Builder {
        private String playerName, opponentName;
        private int playerRank = 0, opponentRank = 0;
        private BreakType breakType = BreakType.ALTERNATE;
        private PlayerTurn playerTurn = PlayerTurn.PLAYER;
        private GameType gameType = GameType.BCA_EIGHT_BALL;

        public Builder(String playerName, String opponentName) {
            this.playerName = playerName;
            this.opponentName = opponentName;
        }

        public Builder setPlayerRanks(int playerRank, int opponentRank) {
            this.playerRank = playerRank;
            this.opponentRank = opponentRank;
            return this;
        }

        public Match build(GameType gameType) {
            this.gameType = gameType;
            return new Match(this);
        }

        public Builder setBreakType(BreakType breakType) {
            this.breakType = breakType;
            return this;
        }

        public Builder setPlayerTurn(PlayerTurn playerTurn) {
            this.playerTurn = playerTurn;
            return this;
        }
    }
}
