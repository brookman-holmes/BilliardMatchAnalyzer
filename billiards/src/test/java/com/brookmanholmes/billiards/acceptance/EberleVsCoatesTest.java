package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.eberlecoates.GameStatusList;
import com.brookmanholmes.billiards.acceptance.eberlecoates.PlayerList;
import com.brookmanholmes.billiards.acceptance.eberlecoates.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.eberlecoates.TurnList;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.controller.PlayerController;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class EberleVsCoatesTest extends MatchTester {
    private PlayerController controller;
    private List<Pair<AbstractPlayer>> expectedPairs;

    @Override public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        expectedPairs = PlayerList.getPlayerPairs();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.ALTERNATE);
        controller = PlayerController.createNineBallController("Max", "Jeff", 0, 0);
    }

    @Test
    public void verifyPlayerPairsWithList() {
        Pair<NineBallPlayer> actualPair;
        for (int i = 0; i < turns.size(); i++) {
            actualPair = controller.addTurn(expectedGameStatusList.get(i), turns.get(i));
            assertThat("afterTurn" + i, actualPair.getPlayer(), is(expectedPairs.get(i).getPlayer()));
            assertThat("afterTurn" + i, actualPair.getOpponent(), is(expectedPairs.get(i).getOpponent()));
        }
    }
}
