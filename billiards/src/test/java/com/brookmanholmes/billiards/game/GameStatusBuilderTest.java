package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/14/2015.
 */
public class GameStatusBuilderTest {
    private GameStatus.Builder builder;

    @Before
    public void setUp() {
        builder = new GameStatus.Builder(GameType.BCA_TEN_BALL);
    }

    @Test
    public void testNewGame() {
        builder.newGame();

        assertThat(builder.build().newGame, is(true));
    }

    @Test
    public void testPush() {
        builder.allowPush();

        assertThat(builder.build().allowPush, is(true));
    }

    @Test
    public void testSkip() {
        builder.allowSkip();

        assertThat(builder.build().allowTurnSkip, is(true));
    }

    @Test
    public void testSafetyLastTurn() {
        builder.safetyLastTurn();
        assertThat(builder.build().opponentPlayedSuccessfulSafe, is(true));
    }

    @Test
    public void testTurnSelector() {
        builder.turn(PlayerTurn.PLAYER).breaker(PlayerTurn.PLAYER);
        assertThat(builder.build().turn, is(PlayerTurn.PLAYER));
        assertThat(builder.build().breaker, is(PlayerTurn.PLAYER));
    }

    @Test
    public void testReBreak() {
        builder.reBreak();
        assertThat(builder.build().playerAllowedToBreakAgain, is(true));
    }

    @Test
    public void testColor() {
        builder.currentPlayerColor(PlayerColor.SOLIDS);
        assertThat(builder.build().currentPlayerColor, is(PlayerColor.SOLIDS));
    }

    @Test
    public void testRemoveBalls() {
        builder.removeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertThat(builder.build().ballsOnTable.size(), is(0));
    }

    @Test
    public void testCurrentPlayerFouls() {
        builder.currentPlayerConsecutiveFouls(4);
        assertThat(builder.build().currentPlayerConsecutiveFouls, is(4));
    }

    @Test
    public void testBuilderConstructor() {
        List<GameType> gameTypes = Arrays.asList(
                GameType.BCA_EIGHT_BALL, GameType.BCA_TEN_BALL, GameType.BCA_NINE_BALL,
                GameType.APA_EIGHT_BALL, GameType.APA_NINE_BALL
        );

        for (GameType type : gameTypes) {
            Game game = Game.newGame(type, PlayerTurn.PLAYER, BreakType.WINNER);
            GameStatus gameStatus = new GameStatus.Builder(type).build();

            assertThat(gameStatus.MAX_BALLS, is(game.MAX_BALLS));
            assertThat(gameStatus.GAME_BALL, is(game.GAME_BALL));
            assertThat(gameStatus.winOnBreak, is(game.winOnBreak()));
        }
    }
}
