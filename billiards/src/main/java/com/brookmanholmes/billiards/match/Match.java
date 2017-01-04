package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Players;
import com.brookmanholmes.billiards.player.controller.PlayerController;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

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
public class Match implements IMatch {
    // TODO: 8/26/2016 test this class more thoroughly
    private final PlayerController playerController;
    private final Date createdOn;
    private final Game game;
    private final Game initialGameState;
    private final LinkedList<AbstractPlayer> player1 = new LinkedList<>();
    private final LinkedList<AbstractPlayer> player2 = new LinkedList<>();
    private final LinkedList<ITurn> turns = new LinkedList<>();
    private final LinkedList<ITurn> undoneTurns = new LinkedList<>();
    private final LinkedList<GameStatus> games = new LinkedList<>();
    private final EnumSet<StatsDetail> details;
    private long matchId;
    private String location;
    private String notes;
    private boolean matchOver;

    private Match(Builder builder, PlayerController playerController) {
        location = builder.location;
        notes = builder.notes;
        matchId = builder.id;
        game = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        initialGameState = Game.newGame(builder.gameType, builder.playerTurn, builder.breakType);
        this.playerController = playerController;
        createdOn = (builder.date == null ? new Date() : builder.date);
        details = EnumSet.copyOf(builder.details);
    }

    @Override
    public long getMatchId() {
        return matchId;
    }

    @Override
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public GameStatus getGameStatus() {
        return game.getGameStatus();
    }

    @Override
    public GameStatus getGameStatus(int turn) {
        if (games.size() == 0)
            return game.getGameStatus();
        return games.get(turn);
    }

    @Override
    public AbstractPlayer getPlayer() {
        return PlayerController.getPlayerFromList(player1, playerController.newPlayer());
    }

    @Override
    public AbstractPlayer getOpponent() {
        return PlayerController.getPlayerFromList(player2, playerController.newOpponent());
    }

    @Override
    public AbstractPlayer getPlayer(int from, int to) {
        return PlayerController.getPlayerFromList(player1.subList(from, to), playerController.newPlayer());
    }

    @Override
    public AbstractPlayer getOpponent(int from, int to) {
        return PlayerController.getPlayerFromList(player2.subList(from, to), playerController.newOpponent());
    }

    @Override
    public String getCurrentPlayersName() {
        if (game.getTurn() == PlayerTurn.PLAYER)
            return playerController.getPlayerName();
        else return playerController.getOpponentName();
    }

    @Override
    public String getNonCurrentPlayersName() {
        if (game.getTurn() == PlayerTurn.OPPONENT)
            return playerController.getPlayerName();
        else return playerController.getOpponentName();
    }

    @Override
    public void setPlayerName(String newName) {
        playerController.setPlayerName(newName);
        for (AbstractPlayer player : player1)
            player.setName(newName);
    }

    @Override
    public void setOpponentName(String newName) {
        playerController.setOpponentName(newName);
        for (AbstractPlayer player : player2)
            player.setName(newName);
    }

    @Override
    public ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean foul, boolean isGameLost, AdvStats advStats) {
        ITurn turn = new Turn(turnEnd, tableStatus, foul, isGameLost, advStats);
        undoneTurns.clear();
        addTurn(turn);

        return turn;
    }

    @Override
    public ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean foul, boolean isGameLost) {
        ITurn turn = new Turn(turnEnd, tableStatus, foul, isGameLost, new AdvStats.Builder("").build());
        undoneTurns.clear();
        addTurn(turn);

        return turn;
    }

    @Override
    public boolean isMatchOver() {
        return matchOver;
    }

    @Override
    public void addTurn(ITurn turn) {
        updatePlayerStats(turn);
        updateGameState(turn);
        turns.addLast(turn);

        if (getGameStatus().gameType.isGhostGame() && turn.getTurnEnd() != TurnEnd.GAME_WON) {
            insertGameWonForGhost();
        }

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
     * Creates a new turn for the ghost (where they win) and adds it to the match
     */
    private void insertGameWonForGhost() {
        ITableStatus tableStatus = game.getCurrentTableStatus();
        tableStatus.setBallTo(BallStatus.MADE, game.getGhostBallsToWinGame());

        ITurn turn = new Turn(TurnEnd.GAME_WON, tableStatus, false, false, null);

        addTurn(turn);
    }

    /**
     * Adds stats for each player to the list of players
     * @param turn The turn being added to the match
     */
    private void updatePlayerStats(ITurn turn) {
        Pair<AbstractPlayer> pair = playerController.addTurn(getGameStatus(), turn);

        player1.addLast(pair.getPlayer());
        player2.addLast(pair.getOpponent());
    }

    @Override
    public int getTurnCount() {
        return turns.size();
    }

    @Override
    public List<ITurn> getTurns() {
        return turns;
    }

    @Override
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

    @Override
    public boolean isRedoTurn() {
        return undoneTurns.size() > 0 && !matchOver;
    }

    @Override
    public ArrayList<ITurn> getUndoneTurns() {
        ArrayList<ITurn> turns = new ArrayList<>();
        turns.addAll(undoneTurns);
        return turns;
    }

    @Override
    public void setUndoneTurns(List undoneTurns) {
        for (Object item : undoneTurns) {
            if (item instanceof ITurn) {
                this.undoneTurns.addLast((ITurn) item);
            }
        }
    }

    @Override
    public boolean isUndoTurn() {
        return turns.size() > 0;
    }

    @Override
    public ITurn redoTurn() {
        if (isRedoTurn()) {
            addTurn(undoneTurns.removeLast());

            return turns.peekLast();
        } else return null;
    }

    @Override
    public void undoTurn() {
        if (isUndoTurn()) {
            player1.removeLast();
            player2.removeLast();

            game.setGameStatus(games.removeLast());

            undoneTurns.addLast(turns.removeLast());
            matchOver = isPlayersRaceFinished();

            if (game.getGameStatus().gameType.isGhostGame()) {
                if (game.getGameStatus().turn == PlayerTurn.OPPONENT)
                    undoTurn(); // repeat undoing the turn if it's the ghost's turn
            }
        }
    }

    @Override
    public EnumSet<StatsDetail> getDetails() {
        return details;
    }

    @Override
    public Date getCreatedOn() {
        return new Date(createdOn.getTime());
    }

    public GameStatus getInitialGameStatus() {
        return initialGameState.getGameStatus();
    }

    @Override
    public String toString() {
        return "Match{" +
                "createdOn=" + createdOn +
                "\n game=" + game +
                "\n details=" + details +
                "\n matchId=" + matchId +
                "\n location='" + location + '\'' +
                "\n notes='" + notes + '\'' +
                '}';
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
        private String playerName, opponentName;
        private int playerRank = 100, opponentRank = 100;
        private BreakType breakType = BreakType.ALTERNATE;
        private PlayerTurn playerTurn = PlayerTurn.PLAYER;
        private GameType gameType = GameType.BCA_EIGHT_BALL;
        private String location = "";
        private String notes = "";
        private long id;
        private Date date;
        private EnumSet<StatsDetail> details = EnumSet.noneOf(StatsDetail.class);

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

        public Match build(GameType gameType) {
            this.gameType = gameType;
            return new Match(this, PlayerController.createController(Game.newGame(gameType, playerTurn, breakType), playerName, opponentName, playerRank, opponentRank));
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
                    '}';
        }
    }
}
