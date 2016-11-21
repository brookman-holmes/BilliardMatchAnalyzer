package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class ApaEightBallTurnEndHelperTest extends EightBallTurnEndHelperTest {
    @Override public void setUp() {
        tableStatus = TableStatus.newTable(GameType.APA_EIGHT_BALL);
        showFoulOnDeadBall = true;
        gameBuilder = new GameStatus.Builder(GameType.APA_EIGHT_BALL);
    }

}
