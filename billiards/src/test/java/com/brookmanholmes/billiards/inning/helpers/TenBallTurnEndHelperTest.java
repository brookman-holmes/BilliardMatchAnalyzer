package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TenBallTurnEndHelperTest extends RotationTurnEndHelperTest {
    @Override
    public void setUp() {
        helper = TurnEndHelper.newTurnEndHelper(GameType.BCA_TEN_BALL);
        tableStatus = TableStatus.newTable(GameType.BCA_TEN_BALL);
        gameType = GameType.BCA_TEN_BALL;
        GAME_BALL = 10;
        gameBuilder = new GameStatus.Builder(gameType);
    }
}
