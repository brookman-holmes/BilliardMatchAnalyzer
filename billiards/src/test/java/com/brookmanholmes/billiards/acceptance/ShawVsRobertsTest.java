package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.shawroberts.GameStatusList;
import com.brookmanholmes.billiards.acceptance.shawroberts.PlayerList;
import com.brookmanholmes.billiards.acceptance.shawroberts.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.shawroberts.TurnList;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.player.controller.PlayerController;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public class ShawVsRobertsTest extends MatchTester {
    private PlayerController controller;
    private List<Pair<Player>> expectedPairs;
    @Override public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        expectedPairs = PlayerList.getPlayerPairs();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_TEN_BALL, PlayerTurn.OPPONENT, BreakType.ALTERNATE, 100);
        controller = PlayerController.createTenBallController("Shaw", "Roberts", "Shaw", "Roberts", 7, 7);
    }

    @Test
    public void verifyPlayerPairsWithList() {
        Pair<Player> actualPair;
        for (int i = 0; i < turns.size(); i++) {
            actualPair = controller.addTurn(expectedGameStatusList.get(i), turns.get(i));
            assertThat("afterTurn" + i, actualPair.getPlayer(), is(expectedPairs.get(i).getPlayer()));
            assertThat("afterTurn" + i, actualPair.getOpponent(), is(expectedPairs.get(i).getOpponent()));
        }
    }
}
