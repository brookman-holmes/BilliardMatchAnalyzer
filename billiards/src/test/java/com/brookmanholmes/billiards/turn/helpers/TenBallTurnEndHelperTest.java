package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TenBallTurnEndHelperTest extends RotationTurnEndHelperTest {
    @Override public void setUp() {
        gameBuilder = new GameStatus.Builder(GameType.BCA_TEN_BALL);
        tableStatus = TableStatus.newTable(GameType.BCA_TEN_BALL);
    }
}
