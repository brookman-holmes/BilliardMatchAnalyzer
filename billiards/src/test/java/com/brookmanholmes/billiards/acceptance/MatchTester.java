package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public abstract class MatchTester {
    Game game;
    List<ITurn> turns;
    List<GameStatus> expectedGameStatusList;
    List<TurnEndOptions> expectedTurnEndOptionsList;

    @Before
    public abstract void setUp();

    @Test
    public void verifyGameStatesWithList() {
        List<GameStatus> actualGameStatusList = new ArrayList<>();

        actualGameStatusList.add(game.getGameStatus());

        for (int i = 0; i < turns.size(); i++) {
            actualGameStatusList.add(game.addTurn(turns.get(i)));
        }

        for (int i = 0; i < turns.size(); i++) {
            assertThat("afterTurn" + i, actualGameStatusList.get(i), is(expectedGameStatusList.get(i)));
        }
    }

    @Test
    public void verifyTurnEndOptionsWithList() {
        List<TurnEndOptions> actualTurnEndOptionsList = new ArrayList<>();

        for (int i = 0; i < turns.size(); i++) {
            actualTurnEndOptionsList.add(TurnEndHelper.getTurnEndOptions(expectedGameStatusList.get(i), turns.get(i)));
        }

        for (int i = 0; i < turns.size(); i++) {
            assertThat("on turn" + (i + 1), actualTurnEndOptionsList.get(i), is(expectedTurnEndOptionsList.get(i)));
        }
    }
}
