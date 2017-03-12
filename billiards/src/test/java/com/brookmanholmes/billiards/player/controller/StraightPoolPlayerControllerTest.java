package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 12/6/2016.
 */

public class StraightPoolPlayerControllerTest {
    StraightPoolController playerController;
    String playerName = "player", opponentName = "opponent";
    int playerRank = 100, opponentRank = 100;
    TurnBuilder turnBuilder;
    Game game;
    GameStatus.Builder statusBuilder;

    Player actualPlayer, expectedPlayer;

    @Before
    public void setup() {
        GameType gameType = GameType.STRAIGHT_POOL;
        playerController = new StraightPoolController(playerName, opponentName, playerName, opponentName, playerRank, opponentRank);
        turnBuilder = new TurnBuilder(gameType);
        game = Game.newGame(gameType, PlayerTurn.PLAYER, BreakType.WINNER, 100);
        actualPlayer = new Player(playerName, playerName, gameType, playerRank);
        expectedPlayer = new Player(playerName, playerName, gameType, playerRank);
        statusBuilder = new GameStatus.Builder(gameType);
    }


    /**
     * Shooting tests
     */
    @Test
    public void addTurnToPlayerReturnsTrue() {
        // made a few balls and then play a safe
        playerController.turn = turnBuilder.madeBalls(1).safety();

        assertThat(playerController.addTurnToPlayer(), is(true));

        // miss your shot and make no balls
        playerController.turn = turnBuilder.miss();

        assertThat(playerController.addTurnToPlayer(), is(true));
    }

    @Test
    public void missTurnEndGivesShootingMiss() {
        game.addTurn(turnBuilder.miss());
        playerController.turn = turnBuilder.miss();
        playerController.gameStatus = game.getGameStatus();
        playerController.addShootingStats(actualPlayer);

        expectedPlayer.addShootingMiss();
        expectedPlayer.addShootingBallsMade(0, false);

        testPlayerEquality();
    }

    @Test
    public void seriousFoulAddsSeriousFoulToPlayer() {
        playerController.turn = turnBuilder.seriousFoul().miss();
        playerController.gameStatus = statusBuilder
                .turn(PlayerTurn.PLAYER)
                .breaker(PlayerTurn.PLAYER)
                .consecutivePlayerFouls(2)
                .build();
        playerController.addShootingStats(actualPlayer);

        expectedPlayer.addShootingMiss();
        expectedPlayer.addShootingBallsMade(0, false);
        expectedPlayer.addSeriousFoul();

        testPlayerEquality();
    }

    @Test
    public void breakingFoulAddsBreakingFoulToPlayer() {
        playerController.turn = turnBuilder.fouled().miss();
        playerController.gameStatus = statusBuilder
                .turn(PlayerTurn.PLAYER)
                .newGame()
                .breaker(PlayerTurn.PLAYER)
                .build();
        playerController.addBreakingStats(actualPlayer);

        expectedPlayer.addBreakShot(0, false, true);

        testPlayerEquality();
    }

    @Test
    public void foulAfterMakingBallOnBreakDoesNotAddBreakingFoul() {
        playerController.turn = turnBuilder.madeBalls(1, 2, 3).fouled().miss();

        playerController.gameStatus = statusBuilder
                .turn(PlayerTurn.PLAYER)
                .newGame()
                .breaker(PlayerTurn.PLAYER)
                .build();

        playerController.addBreakingStats(actualPlayer);

        testPlayerEquality();
    }

    private void testPlayerEquality() {
        assertThat(actualPlayer, is(expectedPlayer));
    }
}
