package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
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

    Match() {
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

    public void test() {

    }
}
