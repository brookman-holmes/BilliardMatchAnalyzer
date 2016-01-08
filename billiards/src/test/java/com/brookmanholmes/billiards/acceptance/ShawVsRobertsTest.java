package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.shawroberts.GameStatusList;
import com.brookmanholmes.billiards.acceptance.shawroberts.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.shawroberts.TurnList;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public class ShawVsRobertsTest extends MatchTester {
    @Override
    public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_TEN_BALL, PlayerTurn.OPPONENT, BreakType.ALTERNATE);
    }
}
