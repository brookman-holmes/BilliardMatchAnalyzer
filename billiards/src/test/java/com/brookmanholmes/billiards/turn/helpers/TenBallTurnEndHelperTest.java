package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TenBallTurnEndHelperTest extends RotationTurnEndHelperTest {
    @Override
    public void setUp() {
        helper = TurnEndHelper.newTurnEndHelper(GameType.BCA_TEN_BALL);
        tableStatus = com.brookmanholmes.billiards.turn.TableStatus.newTable(GameType.BCA_TEN_BALL);
        gameType = GameType.BCA_TEN_BALL;
        GAME_BALL = 10;
        gameBuilder = new GameStatus.Builder(gameType);
    }
}
