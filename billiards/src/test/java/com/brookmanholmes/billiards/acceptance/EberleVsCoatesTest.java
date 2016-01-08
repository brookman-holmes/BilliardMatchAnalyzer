package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.eberlecoates.GameStatusList;
import com.brookmanholmes.billiards.acceptance.eberlecoates.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.eberlecoates.TurnList;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class EberleVsCoatesTest extends MatchTester {
    @Override
    public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.ALTERNATE);
    }
}
