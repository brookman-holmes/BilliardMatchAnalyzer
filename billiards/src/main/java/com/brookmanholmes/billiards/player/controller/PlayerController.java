package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.player.interfaces.EarlyWins;

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
    Turn turn;
    String playerName, opponentName;

    PlayerController(String playerName, String opponentName) {
        this.playerName = playerName;
        this.opponentName = opponentName;
    }

    public static PlayerController<NineBallPlayer> createNineBallController(String playerName, String opponentName) {
        return new NineBallController(playerName, opponentName);
    }

    public static PlayerController<TenBallPlayer> createTenBallController(String playerName, String opponentName) {
        return new TenBallController(playerName, opponentName);
    }

    public static PlayerController<EightBallPlayer> createEightBallController(String playerName, String opponentName) {
        return new EightBallController(playerName, opponentName);
    }

    public static PlayerController<?> createController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        switch (game.getGameType()) {
            case BCA_NINE_BALL:
                return new NineBallController(playerName, opponentName);
            case BCA_TEN_BALL:
                return new TenBallController(playerName, opponentName);
            case APA_EIGHT_BALL:
                return new ApaEightBallController(playerName, opponentName, playerRank, opponentRank);
            case APA_NINE_BALL:
                return new ApaNineBallController(playerName, opponentName, playerRank, opponentRank);
            case BCA_EIGHT_BALL:
                return new EightBallController(playerName, opponentName);
            default:
                throw new InvalidGameTypeException(game.getGameType().name());
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public Pair<T> updatePlayerStats(GameStatus gameStatus, Turn turn) {
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
            if (player1 instanceof EarlyWins && player2 instanceof EarlyWins) {
                if (gameStatus.turn == PlayerTurn.PLAYER)
                    ((EarlyWins) player2).addEarlyWin();
                if (gameStatus.turn == PlayerTurn.OPPONENT)
                    ((EarlyWins) player1).addEarlyWin();
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
            return Game.changeTurn(gameStatus.turn);
        else throw new IllegalStateException("Should not be called if the game is not over");
    }

    void addStatsToPlayer(T player) {
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
            player.addSafetyAttempt(turn.isScratch());

        if (gameStatus.opponentPlayedSuccessfulSafe && turn.getShootingBallsMade() > 0)
            player.addSafetyEscape();
    }

    void addShootingStats(T player) {
        if (turn.getTurnEnd() == MISS)
            player.addShootingMiss();

        if (setAddTurnToPlayer())
            player.addShootingBallsMade(turn.getShootingBallsMade(), turn.isScratch());
    }

    boolean setAddTurnToPlayer() {
        // if the player made some balls or missed
        return (turn.getShootingBallsMade() > 0 || turn.getTurnEnd() == MISS);
    }

    void addBreakingStats(T player) {
        player.addBreakShot(
                turn.getBreakBallsMade(), // how many balls the player made on the break
                turn.getShootingBallsMade() > 0, // determine if there was continuation or not
                turn.getTurnEnd() == BREAK_MISS && turn.isScratch() // determine if the player scratched on the break
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

    int getTotalBallsMade() {
        return turn.getShootingBallsMade() + turn.getBreakBallsMade();
    }

    int getMaximumBallsMakeable() {
        return gameStatus.MAX_BALLS;
    }

    public abstract T newPlayer();

    public abstract T newOpponent();
}
