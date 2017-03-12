package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.hohmannsvb.GameStatusList;
import com.brookmanholmes.billiards.acceptance.hohmannsvb.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.hohmannsvb.TurnList;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class HohmannVsSvb extends MatchTester {
    @Override public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_TEN_BALL, PlayerTurn.PLAYER, BreakType.ALTERNATE, 100);
    }
}
