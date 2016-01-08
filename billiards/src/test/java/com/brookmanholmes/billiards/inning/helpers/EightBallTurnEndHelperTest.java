package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnBuilder;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class EightBallTurnEndHelperTest extends AbstractTurnEndHelperTest {
    static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL);
    }

    @Override
    public void setUp() {
        helper = TurnEndHelper.newTurnEndHelper(GameType.BCA_EIGHT_BALL);
        tableStatus = TableStatus.newTable(GameType.BCA_EIGHT_BALL);
        gameType = GameType.BCA_EIGHT_BALL;
        GAME_BALL = 8;
        gameBuilder = new GameStatus.Builder(gameType);
    }

    @Override
    void setupLossStuff() {
        tableStatus.setBallTo(BallStatus.DEAD, GAME_BALL);
    }
}
