package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class EightBallTurnEndHelperTest extends AbstractTurnEndHelperTest {
    @Override
    public void setUp() {
        tableStatus = TableStatus.newTable(GameType.BCA_EIGHT_BALL);
        gameBuilder = new GameStatus.Builder(GameType.BCA_EIGHT_BALL);
    }

    @Override
    void setupLossStuff() {
        tableStatus.setBallTo(BallStatus.DEAD, gameBuilder.build().gameType.getGameBall());
    }

    @Test
    public void madeBallOnBreakDoesNotShowPush() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, 1);
        GameStatus status = Game.newGame(gameBuilder.build().gameType, PlayerTurn.PLAYER, BreakType.WINNER, 100).getGameStatus();

        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(status, tableStatus);

        assertThat(options.possibleEndings.contains(com.brookmanholmes.billiards.turn.TurnEnd.PUSH_SHOT), is(false));
    }
}
