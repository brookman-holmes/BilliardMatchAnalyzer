package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.IEarlyWins;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.turn.ITurn;

import java.util.Collection;

import static com.brookmanholmes.billiards.turn.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY_ERROR;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
public abstract class PlayerController<T extends AbstractPlayer> {
    GameStatus gameStatus;
    ITurn turn;
    String playerName, opponentName;
    int playerRank, opponentRank;

    PlayerController(String playerName, String opponentName, int playerRank, int opponentRank) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
    }

    public static PlayerController<NineBallPlayer> createNineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new NineBallController(playerName, opponentName, playerRank, opponentRank);
    }

    public static PlayerController<TenBallPlayer> createTenBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new TenBallController(playerName, opponentName, playerRank, opponentRank);
    }

    public static PlayerController<EightBallPlayer> createEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        return new EightBallController(playerName, opponentName, playerRank, opponentRank);
    }

    public static PlayerController<?> createController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        switch (game.getGameType()) {
            case BCA_NINE_BALL:
                return new NineBallController(playerName, opponentName, playerRank, opponentRank);
            case BCA_TEN_BALL:
                return new TenBallController(playerName, opponentName, playerRank, opponentRank);
            case APA_EIGHT_BALL:
                return new ApaEightBallController(playerName, opponentName, playerRank, opponentRank);
            case APA_NINE_BALL:
                return new ApaNineBallController(playerName, opponentName, playerRank, opponentRank);
            case BCA_EIGHT_BALL:
                return new EightBallController(playerName, opponentName, playerRank, opponentRank);
            default:
                throw new InvalidGameTypeException(game.getGameType().name());
        }
    }

    public static <T extends AbstractPlayer> T getPlayerFromList(Collection<T> players, T newPlayer) {
        for (T player : players) {
            newPlayer.addPlayerStats(player);
        }

        return newPlayer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String newName) {
        playerName = newName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String newName) {
        opponentName = newName;
    }

    public Pair<T> updatePlayerStats(GameStatus gameStatus, ITurn turn) {
        assert gameStatus != null;
        assert turn != null;

        this.gameStatus = gameStatus;
        this.turn = turn;

        T player1 = newPlayer();
        T player2 = newOpponent();

        switch (gameStatus.turn) {
            case PLAYER:
                addStatsToPlayer(player1);
                break;
            case OPPONENT:
                addStatsToPlayer(player2);
                break;
            default:
                throw new IllegalStateException("I'm not sure if this is possible to reach");
        }

        if (isGameOver()) {
            addGamesToPlayers(player1, player2);
        }

        if (turn.isGameLost()) {
            if (player1 instanceof IEarlyWins && player2 instanceof IEarlyWins) {
                if (gameStatus.turn == PlayerTurn.PLAYER)
                    ((IEarlyWins) player2).addEarlyWin();
                if (gameStatus.turn == PlayerTurn.OPPONENT)
                    ((IEarlyWins) player1).addEarlyWin();
            }


        }

        return new Pair<>(player1, player2);
    }

    void addGamesToPlayers(T player1, T player2) {
        if (getGameWinner() == PlayerTurn.PLAYER) {
            player1.addGameWon();
            player2.addGameLost();
        } else {
            player1.addGameLost();
            player2.addGameWon();
        }
    }

    boolean isGameOver() {
        return turn.getTurnEnd() == GAME_WON || turn.isGameLost();
    }

    PlayerTurn getGameWinner() {
        if (turn.getTurnEnd() == GAME_WON)
            return gameStatus.turn;
        else if (turn.isGameLost())
            return gameStatus.turn.nextPlayer();
        else throw new IllegalStateException("Should not be called if the game is not over");
    }

    private void addStatsToPlayer(T player) {
        addSafetyStats(player);
        addShootingStats(player);

        if (turn.getTurnEnd() == GAME_WON)
            addRunOutStats(player);

        if (gameStatus.newGame)
            addBreakingStats(player);
    }

    void addSafetyStats(T player) {
        if (turn.getTurnEnd() == SAFETY)
            player.addSafety(gameStatus.opponentPlayedSuccessfulSafe);
        else if (turn.getTurnEnd() == SAFETY_ERROR)
            player.addSafetyAttempt(turn.isFoul());

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.getShootingBallsMade() > 0)
            player.addSafetyEscape();

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.isFoul()) {
            player.addSafetyForcedError();
        }
    }

    void addShootingStats(T player) {
        if (turn.getTurnEnd() == MISS)
            player.addShootingMiss();

        if (setAddTurnToPlayer())
            player.addShootingBallsMade(turn.getShootingBallsMade(), turn.isFoul());
    }

    boolean setAddTurnToPlayer() {
        // if the player made some balls or missed
        return (turn.getShootingBallsMade() > 0 || turn.getTurnEnd() == MISS);
    }

    void addBreakingStats(T player) {
        if (gameStatus.breakType == BreakType.GHOST) {
            player.addBreakShot(turn.getBreakBallsMade(),
                    turn.getShootingBallsMade() > 0,
                    turn.getDeadBallsOnBreak() > 0);
        } else
            player.addBreakShot(
                    turn.getBreakBallsMade(), // how many balls the player made on the break
                    turn.getShootingBallsMade() > 0, // determine if there was continuation or not
                    turn.getTurnEnd() == BREAK_MISS && turn.isFoul()  // determine if the player scratched on the break
            );
    }

    void addRunOutStats(T player) {
        if (gameStatus.newGame && getTotalBallsMade() >= getMaximumBallsMakeable()) // break and run
            player.addBreakAndRun();
        else if (turn.getShootingBallsMade() == getMaximumBallsMakeable()) // table run
            player.addTableRun();
        else if (turn.getShootingBallsMade() <= 5) // four ball run
            player.addFourBallRun();
    }

    private int getTotalBallsMade() {
        return turn.getShootingBallsMade() + turn.getBreakBallsMade();
    }

    int getMaximumBallsMakeable() {
        return gameStatus.MAX_BALLS;
    }

    public abstract T newPlayer();

    public abstract T newOpponent();
}
