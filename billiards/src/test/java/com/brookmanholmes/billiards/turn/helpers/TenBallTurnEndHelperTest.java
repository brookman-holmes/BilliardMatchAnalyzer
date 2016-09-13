package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TenBallTurnEndHelperTest extends RotationTurnEndHelperTest {
    @Override public void setUp() {
        gameBuilder = new GameStatus.Builder(GameType.BCA_TEN_BALL);
        tableStatus = TableStatus.newTable(GameType.BCA_TEN_BALL);
    }
}
