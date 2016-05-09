package com.brookmanholmes.billiards.player.controller;


import com.brookmanholmes.billiards.player.EightBallPlayer;

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
    List<EightBallPlayer> playerList;
    EightBallPlayer expectedPlayer;
    String playerName = "Player 1";

    @Before
    public void setUp() {
        playerList = new ArrayList<>();
        expectedPlayer = new EightBallPlayer(playerName);

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

        assertThat(PlayerController.getPlayerFromList(playerList, createPlayer()), is(expectedPlayer));
    }

    @Test
    public void cumulativeTest() {
        addingUpPlayersInListResultsInCorrectPlayerState();
        addingUpPlayersInListResultsInCorrectPlayerState();
        addingUpPlayersInListResultsInCorrectPlayerState();
    }

    private EightBallPlayer createPlayer() {
        return new EightBallPlayer(playerName);
    }
}
