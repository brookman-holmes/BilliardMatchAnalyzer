package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.player.Players;
import com.brookmanholmes.billiards.player.controller.PlayerController;
import com.brookmanholmes.billiards.turn.ITurn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Brookman Holmes on 10/27/2015.
 */
public class Match implements Serializable {
    // TODO: 8/26/2016 test this class more thoroughly
    private final PlayerController playerController;
    private final Date createdOn;
    private final Game game;
    private final Game initialGameState;
    private final LinkedList<Player> player1 = new LinkedList<>();
    private final LinkedList<Player> player2 = new LinkedList<>();
    private final LinkedList<ITurn> turns = new LinkedList<>();
    private final LinkedList<ITurn> undoneTurns = new LinkedList<>();
    private final LinkedList<GameStatus> games = new LinkedList<>();
    private final EnumSet<StatsDetail> details;
    private String matchId;
    private String location;
    private String notes;
    private boolean matchOver;

    private Match(Builder builder, PlayerController playerController) {
        location = builder.location;
        notes = builder.notes;
        matchId = builder.id;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType, builder.maxAttemptsPerGame);
        initialGameState = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType, builder.maxAttemptsPerGame);
        this.playerController = playerController;
        createdOn = (builder.date == null ? new Date() : builder.date);
        details = EnumSet.copyOf(builder.details);
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public GameStatus getGameStatus() {
        return game.getGameStatus();
    }

    public Player getPlayer(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER)
            return getPlayer();
        else return getOpponent();
    }

    public GameStatus getGameStatus(int turn) {
        if (games.size() == 0)
            return game.getGameStatus();
        return games.get(turn);
    }

    public Player getPlayer() {
        Player player = new Player(playerController.getPlayerId(), playerController.getPlayerName(), getGameStatus().gameType, playerController.getPlayerRank(), player1);
        player.setMatchDate(getCreatedOn());
        return player;
    }

    public Player getOpponent() {
        Player player = new Player(playerController.getOpponentId(), playerController.getOpponentName(), getGameStatus().gameType, playerController.getOpponentRank(), player2);
        player.setMatchDate(getCreatedOn());
        return player;
    }

    public Player getPlayer(int from, int to) {
        Player player = new Player(playerController.getPlayerId(), playerController.getPlayerName(), getGameStatus().gameType, playerController.getPlayerRank(), player1.subList(from, to));
        player.setMatchDate(getCreatedOn());
        return player;
    }

    public Player getOpponent(int from, int to) {
        Player player = new Player(playerController.getOpponentId(), playerController.getOpponentName(), getGameStatus().gameType, playerController.getOpponentRank(), player2.subList(from, to));
        player.setMatchDate(getCreatedOn());
        return player;
    }

    public String getCurrentPlayersId() {
        if (game.getTurn() == PlayerTurn.PLAYER)
            return playerController.getPlayerId();
        else return playerController.getOpponentId();
    }

    public String getNonCurrentPlayersId() {
        if (game.getTurn() == PlayerTurn.OPPONENT)
            return playerController.getPlayerId();
        else return playerController.getOpponentId();
    }

    public void setPlayerId(String id) {
        playerController.setPlayerId(id);
        for (Player player : player1)
            player.setId(id);
    }

    public void setOpponentId(String id) {
        playerController.setOpponentId(id);
        for (Player player : player2)
            player.setId(id);
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public void addTurn(ITurn turn) {
        if (undoneTurns.size() > 0) {
            if (!undoneTurns.peekLast().equals(turn))
                undoneTurns.clear();
            else undoneTurns.removeLast();
        }

        updatePlayerStats(turn);
        updateGameState(turn);
        turns.addLast(turn);

        matchOver = isPlayersRaceFinished();
    }

    /**
     * Determines whether the match is finished (because either of the players have gotten to their
     * specified win/point total
     * @return True if the match is over, false otherwise
     */
    private boolean isPlayersRaceFinished() {
        return Players.isMatchOver(getPlayer(), getOpponent());
    }

    /**
     * Adds stats for each player to the list of players
     * @param turn The turn being added to the match
     */
    private void updatePlayerStats(ITurn turn) {
        Pair<Player> pair = playerController.addTurn(getGameStatus(), turn);

        player1.addLast(pair.getPlayer());
        player2.addLast(pair.getOpponent());
    }

    public int getTurnCount() {
        return turns.size();
    }

    public List<ITurn> getTurns() {
        return turns;
    }

    public List<ITurn> getTurns(int from, int to) {
        return turns.subList(from, to);
    }

    /**
     * Updates the stat of the game and adds it to a list of game statuses
     * @param turn The turn being added to the game
     */
    void updateGameState(ITurn turn) {
        games.addLast(game.getGameStatus());
        game.addTurn(turn);
    }

    public boolean isRedoTurn() {
        return undoneTurns.size() > 0;
    }

    public ArrayList<ITurn> getUndoneTurns() {
        ArrayList<ITurn> turns = new ArrayList<>();
        turns.addAll(undoneTurns);
        return turns;
    }

    public void setUndoneTurns(List undoneTurns) {
        for (Object item : undoneTurns) {
            if (item instanceof ITurn) {
                this.undoneTurns.addLast((ITurn) item);
            }
        }
    }

    public boolean isUndoTurn() {
        return turns.size() > 0;
    }

    public ITurn getRedoTurn() {
        if (isRedoTurn()) {
            return turns.peekLast();
        } else return null;
    }

    public void undoTurn() {
        if (isUndoTurn()) {
            player1.removeLast();
            player2.removeLast();

            game.setGameStatus(games.removeLast());

            undoneTurns.addLast(turns.removeLast());
            matchOver = isPlayersRaceFinished();
        }
    }

    public EnumSet<StatsDetail> getDetails() {
        return details;
    }

    public Date getCreatedOn() {
        return new Date(createdOn.getTime());
    }

    public GameStatus getInitialGameStatus() {
        return initialGameState.getGameStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (matchOver != match.matchOver) return false;
        if (!playerController.equals(match.playerController)) return false;
        if (!createdOn.equals(match.createdOn)) return false;
        if (!game.equals(match.game)) return false;
        if (!initialGameState.equals(match.initialGameState)) return false;
        if (!player1.equals(match.player1)) return false;
        if (!player2.equals(match.player2)) return false;
        if (!turns.equals(match.turns)) return false;
        if (!undoneTurns.equals(match.undoneTurns)) return false;
        if (!games.equals(match.games)) return false;
        if (!details.equals(match.details)) return false;
        if (!matchId.equals(match.matchId)) return false;
        if (!location.equals(match.location)) return false;
        return notes.equals(match.notes);

    }

    @Override
    public int hashCode() {
        int result = playerController.hashCode();
        result = 31 * result + createdOn.hashCode();
        result = 31 * result + game.hashCode();
        result = 31 * result + initialGameState.hashCode();
        result = 31 * result + player1.hashCode();
        result = 31 * result + player2.hashCode();
        result = 31 * result + turns.hashCode();
        result = 31 * result + undoneTurns.hashCode();
        result = 31 * result + games.hashCode();
        result = 31 * result + details.hashCode();
        result = 31 * result + matchId.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + notes.hashCode();
        result = 31 * result + (matchOver ? 1 : 0);
        return result;
    }

    public List<GameStatus> getGameStatuses() {
        return new ArrayList<>(games);
    }

    public enum StatsDetail {
        @Deprecated
        NORMAL,
        @Deprecated
        ADVANCED,
        @Deprecated
        ADVANCED_PLAYER,
        @Deprecated
        ADVANCED_OPPONENT,
        SHOT_TYPE_PLAYER,
        SHOT_TYPE_OPPONENT,
        CUEING_PLAYER,
        CUEING_OPPONENT,
        HOW_MISS_PLAYER,
        HOW_MISS_OPPONENT,
        SAFETIES_PLAYER,
        SAFETIES_OPPONENT,
        SPEED_PLAYER,
        SPEED_OPPONENT,
        BALL_DISTANCES_PLAYER,
        BALL_DISTANCES_OPPONENT,
        ANGLE_SIMPLE_PLAYER,
        ANGLE_SIMPLE_OPPONENT,
        ANGLE_PLAYER,
        ANGLE_OPPONENT;

        public static Collection<StatsDetail> getPlayerStatsTracked() {
            return Arrays.asList(SHOT_TYPE_PLAYER,
                    CUEING_PLAYER,
                    HOW_MISS_PLAYER,
                    SAFETIES_PLAYER,
                    SPEED_PLAYER,
                    BALL_DISTANCES_PLAYER,
                    ANGLE_SIMPLE_PLAYER,
                    ANGLE_PLAYER);
        }

        public static Collection<StatsDetail> getOpponentStatsTracked() {
            return Arrays.asList(SHOT_TYPE_OPPONENT,
                    CUEING_OPPONENT,
                    HOW_MISS_OPPONENT,
                    SAFETIES_OPPONENT,
                    SPEED_OPPONENT,
                    BALL_DISTANCES_OPPONENT,
                    ANGLE_SIMPLE_OPPONENT,
                    ANGLE_OPPONENT);
        }
    }

    public static class Builder {
        private int playerRank = 100, opponentRank = 100;
        private BreakType breakType = BreakType.ALTERNATE;
        private PlayerTurn playerTurn = PlayerTurn.PLAYER;
        private GameType gameType = GameType.BCA_EIGHT_BALL;
        private String location = "";
        private String notes = "";
        private String id;
        private Date date;
        private EnumSet<StatsDetail> details = EnumSet.noneOf(StatsDetail.class);
        private String playerId;
        private String opponentId;
        private String playerName, opponentName;
        private int maxAttemptsPerGame;

        public Builder(String playerId, String opponentId) {
            this.opponentId = opponentId;
            this.playerId = playerId;
        }

        public Builder() {

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

        public Builder setMaxAttemptsPerGhostGame(int maxAttemptsPerGame) {
            this.maxAttemptsPerGame = maxAttemptsPerGame;
            return this;
        }

        public Match build(GameType gameType) {
            this.gameType = gameType;
            return new Match(this, PlayerController.createController(Game.newGame(gameType, playerTurn, breakType, maxAttemptsPerGame),
                    playerId, opponentId,
                    playerName, opponentName,
                    playerRank, opponentRank));
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

        public Builder setMatchId(String id) {
            this.id = id;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setDetails(StatsDetail... details) {
            this.details.clear();
            this.details.addAll(Arrays.asList(details));
            return this;
        }

        public Builder setDetails(Collection<StatsDetail> details) {
            this.details.clear();
            this.details.addAll(details);
            return this;
        }

        @Override public String toString() {
            return "Builder{" +
                    "playerId='" + playerId + '\'' +
                    ", opponentId='" + playerId + '\'' +
                    ", playerRank=" + playerRank +
                    ", opponentRank=" + opponentRank +
                    ", breakType=" + breakType +
                    ", playerTurn=" + playerTurn +
                    ", gameType=" + gameType +
                    ", location='" + location + '\'' +
                    ", notes='" + notes + '\'' +
                    ", id=" + id +
                    '}';
        }

        public Builder setPlayerId(String playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder setOpponentId(String opponentId) {
            this.opponentId = opponentId;
            return this;
        }

        public Builder setPlayerNames(String playerName, String opponentName) {
            this.playerName = playerName;
            this.opponentName = opponentName;
            return this;
        }
    }
}
