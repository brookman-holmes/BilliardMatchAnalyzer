package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnBuilder;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    public void madeBallOnBreakDoesNotShowPush() {
        tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, 1);
        GameStatus status = Game.newGame(gameType, PlayerTurn.PLAYER, BreakType.WINNER).getGameStatus();

        TurnEndOptions options = helper.create(status, tableStatus);

        assertThat(options.possibleEndings.contains(TurnEnd.PUSH_SHOT), is(false));
    }
}
