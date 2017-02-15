package com.brookmanholmes.billiards.player.controller;


import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 2/3/2016.
 */
public class ControllerHelperTest {
    private final String playerName = "Player 1";
    private List<Player> playerList;
    private Player expectedPlayer;

    @Before
    public void setUp() {
        playerList = new ArrayList<>();
        expectedPlayer = new Player(playerName, GameType.BCA_EIGHT_BALL);

    }

    @Test
    public void addingUpPlayersInListResultsInCorrectPlayerState() {
        expectedPlayer.addBreakShot(3, true, false);
        expectedPlayer.addGameLost();
        expectedPlayer.addGameWon();
        expectedPlayer.addTableRun();
        expectedPlayer.addShootingBallsMade(10, false);
        expectedPlayer.addShootingMiss();
        expectedPlayer.addShootingBallsMade(0, true);


        playerList.add(createPlayer());
        playerList.add(createPlayer());
        playerList.add(createPlayer());
        playerList.add(createPlayer());
        playerList.add(createPlayer());
        playerList.add(createPlayer());
        playerList.add(createPlayer());

        playerList.get(0).addBreakShot(3, true, false);
        playerList.get(1).addGameLost();
        playerList.get(2).addGameWon();
        playerList.get(3).addTableRun();
        playerList.get(4).addShootingBallsMade(10, false);
        playerList.get(5).addShootingMiss();
        playerList.get(6).addShootingBallsMade(0, true);

        assertThat(createPlayer(playerList), is(expectedPlayer));
    }

    @Test
    public void cumulativeTest() {
        addingUpPlayersInListResultsInCorrectPlayerState();
        addingUpPlayersInListResultsInCorrectPlayerState();
        addingUpPlayersInListResultsInCorrectPlayerState();
    }

    private Player createPlayer(List<Player> players) {
        return new Player(playerName, GameType.BCA_EIGHT_BALL, players);
    }

    private Player createPlayer() {
        return new Player(playerName, GameType.BCA_EIGHT_BALL);
    }
}
