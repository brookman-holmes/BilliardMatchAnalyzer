package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.acceptance.eightball.GameStatusList;
import com.brookmanholmes.billiards.acceptance.eightball.TurnEndOptionsList;
import com.brookmanholmes.billiards.acceptance.eightball.TurnList;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;

/**
 * Created by Brookman Holmes on 11/16/2015.
 */
public class EightBallTest extends MatchTester {
    @Override public void setUp() {
        expectedGameStatusList = GameStatusList.getGameStatuses();
        expectedTurnEndOptionsList = TurnEndOptionsList.getOptionsList();
        turns = TurnList.getTurns();

        game = Game.newGame(GameType.BCA_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.ALTERNATE, 100);
    }
}
