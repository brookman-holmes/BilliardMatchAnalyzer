package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.controller.PlayerController;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.GameTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by Brookman Holmes on 10/27/2015.
 */
public class Match<T extends AbstractPlayer> implements IMatch {
    long matchId;
    PlayerController<T> playerController;
    String location;
    String notes;
    Date createdOn;
    Game game;
    ArrayDeque<T> player1 = new ArrayDeque<>();
    ArrayDeque<T> player2 = new ArrayDeque<>();
    ArrayDeque<Turn> turns = new ArrayDeque<>();
    ArrayDeque<Turn> undoneTurns = new ArrayDeque<>();
    ArrayDeque<GameStatus> games = new ArrayDeque<>();
    private StatsDetail detail;

    Match(Builder builder, PlayerController<T> playerController) {
        location = builder.location;
        notes = builder.notes;
        matchId = builder.id;
        detail = builder.statsDetail;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        this.playerController = playerController;
        createdOn = builder.date;
    }

    public static <T> List<T> convertArrayDequeToList(ArrayDeque<T> arrayDeque) {
        List<T> list = new ArrayList<>();

        for (T item : arrayDeque) {
            list.add(item);
        }

        Collections.reverse(list);

        return list;
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

    @Override public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    @Override public void setNotes(String notes) {
        this.notes = notes;
    }

    public GameStatus getGameStatus() {
        return game.getGameStatus();
    }

    public GameStatus getGameStatus(int turn) {
        return convertArrayDequeToList(games).get(turn);
    }

    public T getPlayer() {
        return PlayerController.getPlayerFromList(player1, playerController.newPlayer());
    }

    public T getOpponent() {
        return PlayerController.getPlayerFromList(player2, playerController.newOpponent());
    }

    @Override public T getPlayer(int from, int to) {
        return PlayerController.getPlayerFromList(convertArrayDequeToList(player1).subList(from, to), playerController.newPlayer());
    }

    @Override public T getOpponent(int from, int to) {
        return PlayerController.getPlayerFromList(convertArrayDequeToList(player2).subList(from, to), playerController.newOpponent());
    }

    @Override public String getCurrentPlayersName() {
        if (game.getTurn() == PlayerTurn.PLAYER)
            return playerController.getPlayerName();
        else return playerController.getOpponentName();
    }

    @Override public String getNonCurrentPlayersName() {
        if (game.getTurn() == PlayerTurn.OPPONENT)
            return playerController.getPlayerName();
        else return playerController.getOpponentName();
    }

    @Override public void setPlayerName(String newName) {
        playerController.setPlayerName(newName);
        for (T player : player1)
            player.setName(newName);
    }

    @Override public void setOpponentName(String newName) {
        playerController.setOpponentName(newName);
        for (T player : player2)
            player.setName(newName);
    }

    public Turn createAndAddTurn(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats) {
        Turn turn = new GameTurn(turns.size(), matchId, scratch, turnEnd, tableStatus, isGameLost, advStats);
        undoneTurns.clear();
        addTurn(turn);

        return turn;
    }

    public void addTurn(Turn turn) {
        updatePlayerStats(turn);
        updateGameState(turn);
        turns.push(turn);

        if (game.getGameStatus().breakType == BreakType.GHOST && turn.getTurnEnd() != TurnEnd.GAME_WON) {
            insertGameWonForGhost();
        }
    }

    private void insertGameWonForGhost() {
        TableStatus tableStatus = game.getCurrentTableStatus();
        tableStatus.setBallTo(BallStatus.MADE, game.getGhostBallsToWinGame());

        Turn turn = new GameTurn(turns.size(), matchId, false, TurnEnd.GAME_WON, tableStatus, false, null);


        addTurn(turn);
    }

    void updatePlayerStats(Turn turn) {
        Pair<T> pair = playerController.updatePlayerStats(getGameStatus(), turn);

        player1.push(pair.getPlayer());
        player2.push(pair.getOpponent());
    }

    public int getTurnCount() {
        return turns.size();
    }

    @Override public List<Turn> getTurns() {
        return convertArrayDequeToList(turns);
    }

    @Override public List<Turn> getTurns(int from, int to) {
        return convertArrayDequeToList(turns).subList(from, to);
    }

    void updateGameState(Turn turn) {
        games.push(game.getGameStatus());
        game.addTurn(turn);
    }

    public boolean isRedoTurn() {
        return undoneTurns.size() > 0;
    }

    public boolean isUndoTurn() {
        return turns.size() > 0;
    }

    @Override public Turn redoTurn() {
        if (isRedoTurn()) {
            addTurn(undoneTurns.pop());

            return turns.peek();
        } else return null;
    }

    @Override public void undoTurn() {
        if (isUndoTurn()) {
            player1.pop();
            player2.pop();

            game.setGameStatus(games.pop());

            undoneTurns.push(turns.pop());
        }
    }

    public StatsDetail getAdvStats() {
        return detail;
    }

    public Date getCreatedOn() {
        return new Date(createdOn.getTime());
    }

    @Override public String toString() {
        return "Match{" +
                "game=" + game +
                '}';
    }

    public enum StatsDetail {
        SIMPLE,
        NORMAL,
        ADVANCED,
        ADVANCED_PLAYER,
        ADVANCED_OPPONENT
    }

    public static class Builder {
        private String playerName, opponentName;
        private int playerRank = 100, opponentRank = 100;
        private BreakType breakType = BreakType.ALTERNATE;
        private PlayerTurn playerTurn = PlayerTurn.PLAYER;
        private GameType gameType = GameType.BCA_EIGHT_BALL;
        private String location = "";
        private String notes = "";
        private long id;
        private StatsDetail statsDetail = StatsDetail.NORMAL;
        private Date date;

        public Builder(String playerName, String opponentName) {
            this.playerName = playerName;
            this.opponentName = opponentName;
        }

        public Builder() {

        }

        public Builder setPlayerName(String name) {
            playerName = name;
            return this;
        }

        public Builder setOpponentName(String name) {
            opponentName = name;
            return this;
        }

        public Builder setPlayerRanks(int playerRank, int opponentRank) {
            this.playerRank = playerRank;
            this.opponentRank = opponentRank;
            return this;
        }

        public Builder setPlayerRank(int playerRank) {
            this.playerRank = playerRank;
            return this;
        }

        public Builder setOpponentRank(int opponentRank) {
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

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setMatchId(long id) {
            this.id = id;
            return this;
        }

        public Builder setStatsDetail(StatsDetail detail) {
            statsDetail = detail;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        @Override public String toString() {
            return "Builder{" +
                    "playerName='" + playerName + '\'' +
                    ", opponentName='" + opponentName + '\'' +
                    ", playerRank=" + playerRank +
                    ", opponentRank=" + opponentRank +
                    ", breakType=" + breakType +
                    ", playerTurn=" + playerTurn +
                    ", gameType=" + gameType +
                    ", location='" + location + '\'' +
                    ", notes='" + notes + '\'' +
                    ", id=" + id +
                    ", statsDetail=" + statsDetail +
                    '}';
        }
    }
}
