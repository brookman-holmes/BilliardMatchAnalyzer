package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public class MatchTest {
    Match<?> match;
    String playerName = "Player 1";
    String opponentName = "Player 2";
    long matchId = 1001;
    int playerRank = 4;
    int opponentRank = 5;
    String location = "Sam's";
    BreakType breakType = BreakType.WINNER;
    GameType gameType = GameType.BCA_EIGHT_BALL;
    PlayerTurn turn = PlayerTurn.OPPONENT;
    TurnBuilder turnBuilder;

    List<AbstractPlayer> playerList;
    List<AbstractPlayer> opponentList;

    AbstractPlayer player1;
    AbstractPlayer player2;

    private static List<Turn> turns() {
        TurnBuilder turnBuilder = new TurnBuilder(GameType.BCA_EIGHT_BALL);
        List<Turn> turns = new ArrayList<>();

        turns.add(turnBuilder.breakMiss());
        turns.add(turnBuilder.miss());
        turns.add(turnBuilder.madeBalls(3).miss());
        turns.add(turnBuilder.madeBalls(9, 10, 11, 12, 13, 14, 15, 8).win());
        turns.add(turnBuilder.breakBalls(4).miss());

        return turns;
    }

    private static TurnBuilder turnBuilder() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL);
    }

    @Before
    public void setUp() {
        match = new Match.Builder(playerName, opponentName)
                .setMatchId(matchId)
                .setLocation(location)
                .setPlayerRanks(playerRank, opponentRank)
                .setBreakType(breakType)
                .setPlayerTurn(turn)
                .build(gameType);

        turnBuilder = new TurnBuilder(gameType);

        player1 = new EightBallPlayer(playerName);
        player2 = new EightBallPlayer(opponentName);

        playerList = new ArrayList<>();
        opponentList = new ArrayList<>();
    }

    @Test
    public void updateGameStateUpdatesTheStateOfTheGame() {
        Game game = Game.newGame(gameType, turn, breakType);

        Turn turn = turnBuilder.miss();

        game.addTurn(turn);
        match.updateGameState(turn);

        assertThat(game.getGameStatus(), is(match.getGameStatus()));
    }

    @Test
    public void getTurnCountReturns0() {
        assertThat(match.getTurnCount(), is(0));
    }

    @Test
    public void getTurnCountReturns5() {
        for (Turn turn : turns()) {
            match.addTurn(turn);
        }

        assertThat(match.getTurnCount(), is(5));
    }

    @Test
    public void getMatchIdReturns1001() {
        assertThat(match.getMatchId(), is(matchId));
    }

    @Test
    public void getLocationReturnsSams() {
        assertThat(match.getLocation(), is(location));
    }

    @Test
    public void setMatchIdSetsMatchId() {
        long newMatchId = 1;
        match.setMatchId(newMatchId);

        assertThat(match.getMatchId(), is(newMatchId));
    }

    @Test
    public void getPlayerReturnsCorrectPlayerState0() {
        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    @Test
    public void getPlayerReturnsTheCorrectPlayerState1() {
        getPlayerReturnsCorrectPlayerState0();

        match.addTurn(turnBuilder().breakMiss());

        addTurnOne();

        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    private void addTurnOne() {
        player2.addBreakShot(0, false, false);

    }

    @Test
    public void getPlayerReturnsTheCorrectPlayerState2() {
        getPlayerReturnsTheCorrectPlayerState1();

        match.addTurn(turnBuilder().miss());

        addTurnTwo();

        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    private void addTurnTwo() {
        player1.addShootingMiss();
        player1.addShootingBallsMade(0, false);
    }

    @Test
    public void getPlayerReturnsTheCorrectPlayerState3() {
        getPlayerReturnsTheCorrectPlayerState2();

        match.addTurn(turnBuilder().madeBalls(3).miss());

        addTurnThree();

        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    private void addTurnThree() {
        player2.addShootingBallsMade(1, false);
        player2.addShootingMiss();
    }

    @Test
    public void getPlayerReturnsTheCorrectPlayerState4() {
        getPlayerReturnsTheCorrectPlayerState3();

        match.addTurn(turnBuilder().madeBalls(9, 10, 11, 12, 13, 14, 15, 8).win());

        addTurnFour();


        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    private void addTurnFour() {
        player1.addShootingBallsMade(8, false);
        player1.addTableRun();
        player1.addGameWon();
        player2.addGameLost();
    }

    @Test
    public void getPlayerReturnsTheCorrectPlayerState5() {
        getPlayerReturnsTheCorrectPlayerState4();

        match.addTurn(turnBuilder().breakBalls(4).miss());

        addTurnFive();

        assertThat(match.getPlayer(), is(player1));
        assertThat(match.getOpponent(), is(player2));
    }

    private void addTurnFive() {
        player1.addBreakShot(1, false, false);

        player1.addShootingMiss();
        player1.addShootingBallsMade(0, false);
    }

    @Test
    public void getGameStatusReturnsThirdTurn() {
        for (Turn turn : turns()) {
            match.addTurn(turn);
        }

        assertThat(match.getGameStatus(0).turn, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(1).turn, is(PlayerTurn.PLAYER));
        assertThat(match.getGameStatus(2).turn, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(3).turn, is(PlayerTurn.PLAYER));
        assertThat(match.getGameStatus(4).turn, is(PlayerTurn.PLAYER));

        assertThat(match.getGameStatus(0).breaker, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(1).breaker, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(2).breaker, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(3).breaker, is(PlayerTurn.OPPONENT));
        assertThat(match.getGameStatus(4).breaker, is(PlayerTurn.PLAYER));

        assertThat(match.getPlayer(match.getTurnCount()), is(match.getPlayer()));
        assertThat(match.getOpponent(match.getTurnCount()), is(match.getOpponent()));
    }

    @Test
    public void getPlayerReturnsFirstTurnPlayer() {
        match.addTurn(turnBuilder.breakMiss());

        addTurnOne();
        assertThat(match.getPlayer(match.getTurnCount()), is(player1));
        assertThat(match.getOpponent(match.getTurnCount()), is(player2));

        match.addTurn(turnBuilder.miss());
        match.addTurn(turnBuilder().madeBalls(3).miss());
        match.addTurn(turnBuilder().madeBalls(9, 10, 11, 12, 13, 14, 15, 8).win());

        addTurnTwo();

        assertThat(match.getPlayer(match.getTurnCount() - 2), is(player1));
        assertThat(match.getOpponent(match.getTurnCount() - 2), is(player2));
    }
}
