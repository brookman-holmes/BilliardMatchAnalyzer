package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.PlayerPair;

import static com.brookmanholmes.billiards.inning.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.GAME_LOST;
import static com.brookmanholmes.billiards.inning.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.inning.TurnEnd.MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.SAFETY;
import static com.brookmanholmes.billiards.inning.TurnEnd.SAFETY_ERROR;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
public class PlayerController<T extends AbstractPlayer> {
    T player1, player2;
    GameStatus gameStatus;
    Turn turn;

    PlayerController(Game game) {
        gameStatus = game.getGameStatus();
    }

    public static PlayerController<?> createController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        switch (game.getGameType()) {
            case BCA_NINE_BALL:
                return new NineBallController(game, playerName, opponentName);
            case BCA_TEN_BALL:
                return new TenBallController(game, playerName, opponentName);
            case APA_EIGHT_BALL:
                return new ApaEightBallController(game, playerName, opponentName, playerRank, opponentRank);
            case APA_NINE_BALL:
                return new ApaNineBallController(game, playerName, opponentName, playerRank, opponentRank);
            case BCA_EIGHT_BALL:
                return new EightBallController(game, playerName, opponentName);
            default:
                throw new InvalidGameTypeException(game.getGameType().name());
        }
    }

    public T getPlayer1() {
        return player1;
    }

    public T getPlayer2() {
        return player2;
    }

    public PlayerPair<T> updatePlayerStats(GameStatus gameStatus, Turn turn) {
        this.gameStatus = gameStatus;
        this.turn = turn;

        switch (gameStatus.turn) {
            case PLAYER:
                addStatsToPlayer(player1);
                break;
            case OPPONENT:
                addStatsToPlayer(player2);
                break;
        }

        if (isGameOver()) {
            addGamesToPlayers();
        }

        return new PlayerPair<>(player1, player2);
    }

    void addGamesToPlayers() {
        if (getGameWinner() == PlayerTurn.PLAYER) {
            player1.addGameWon();
            player2.addGameLost();
        } else {
            player1.addGameLost();
            player2.addGameWon();
        }
    }

    boolean isGameOver() {
        return turn.getTurnEnd() == GAME_WON || turn.getTurnEnd() == GAME_LOST;
    }

    PlayerTurn getGameWinner() {
        if (turn.getTurnEnd() == GAME_WON)
            return gameStatus.turn;
        else return Game.changeTurn(gameStatus.turn);
    }

    T addStatsToPlayer(T player) {
        player = addSafetyStats(player);
        player = addShootingStats(player);

        if (turn.getTurnEnd() == GAME_WON)
            player = addRunOutStats(player);

        if (gameStatus.newGame)
            player = addBreakingStats(player);

        return player;
    }

    T addSafetyStats(T player) {
        if (turn.getTurnEnd() == SAFETY)
            player.addSafety(gameStatus.opponentPlayedSuccessfulSafe);
        else if (turn.getTurnEnd() == SAFETY_ERROR)
            player.addSafetyAttempt(turn.isScratch());

        return player;
    }

    T addShootingStats(T player) {
        if (setAddTurnToPlayer())
            player.addShootingTurn();

        if (turn.getTurnEnd() == MISS)
            player.addShootingMiss();

        player.addShootingBallsMade(turn.getShootingBallsMade(), turn.isScratch());

        return player;
    }

    boolean setAddTurnToPlayer() {
        // if the player made some balls or missed
        return (turn.getShootingBallsMade() > 0 || turn.getTurnEnd() == MISS);
    }

    T addBreakingStats(T player) {
        player.addBreakShot(
                turn.getBreakBallsMade(), // how many balls the player made on the break
                turn.getShootingBallsMade() > 0, // determine if there was continuation or not
                turn.getTurnEnd() == BREAK_MISS && turn.isScratch() // determine if the player scratched on the break
        );

        return player;
    }

    T addRunOutStats(T player) {


        return player;
    }
}
