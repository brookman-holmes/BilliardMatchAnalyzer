package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class ApaEightBallTurnEndHelperTest extends EightBallTurnEndHelperTest {
    @Override
    public void setUp() {
        helper = TurnEndHelper.newTurnEndHelper(GameType.APA_EIGHT_BALL);
        tableStatus = TableStatus.newTable(GameType.APA_EIGHT_BALL);
        gameType = GameType.APA_EIGHT_BALL;
        GAME_BALL = 8;
        showScratchOnDeadBall = true;
        gameBuilder = new GameStatus.Builder(gameType);
    }

}
