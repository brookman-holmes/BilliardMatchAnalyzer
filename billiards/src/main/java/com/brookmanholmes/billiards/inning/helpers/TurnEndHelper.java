package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
abstract public class TurnEndHelper {
    TableStatus nextInning;
    GameStatus game;

    TurnEndHelper() {
    }

    public static TurnEndHelper newTurnEndHelper(GameType gameType) throws InvalidGameTypeException {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return new ApaEightBallTurnEndHelper();
            case APA_NINE_BALL:
                return new RotationTurnEndHelper();
            case BCA_EIGHT_BALL:
                return new EightBallTurnEndHelper();
            case BCA_NINE_BALL:
                return new RotationTurnEndHelper();
            case BCA_TEN_BALL:
                return new TenBallTurnEndHelper();
            default:
                throw new InvalidGameTypeException();
        }
    }

    abstract boolean showWin();

    abstract boolean showLoss();

    TurnEnd selection() {
        if (showWin())
            return TurnEnd.GAME_WON;
        else if (showLoss())
            return TurnEnd.GAME_LOST;
        else if (showBreakMiss())
            return TurnEnd.BREAK_MISS;
        else return TurnEnd.MISS;
    }

    boolean showPush() {
        return ((game.allowPush && !game.newGame)
                || (game.newGame && nextInning.getBreakBallsMade() > 0))
                && nextInning.getShootingBallsMade() == 0;
    }

    boolean showTurnSkip() {
        return game.allowTurnSkip
                && nextInning.getShootingBallsMade() == 0
                && nextInning.getDeadBalls() == 0;
    }

    boolean showSafety() {
        return !showWin() && !showBreakMiss();
    }

    boolean showSafetyMiss() {
        return !showWin() && !showBreakMiss();
    }

    boolean showMiss() {
        return !showWin() && !showBreakMiss();
    }

    boolean checkScratch() {
        return nextInning.getDeadBallsOnBreak() > 0;
    }

    boolean showBreakMiss() {
        return game.newGame && nextInning.getBreakBallsMade() == 0;
    }

    TurnEndOptions.Builder createTurnEndOptionsBuilder() {
        if (game.playerAllowedToBreakAgain) {
            return new TurnEndOptions.Builder().allowPlayerToChooseWhoBreaks(true).defaultOption(TurnEnd.CONTINUE_WITH_GAME);
        } else if (game.gameType == GameType.BCA_EIGHT_BALL && nextInning.getBallStatus(8) == BallStatus.MADE_ON_BREAK && nextInning.getShootingBallsMade() == 0 && nextInning.getDeadBalls() == 0) {
            return new TurnEndOptions.Builder().allowPlayerToChooseToContinueGame(true).defaultOption(TurnEnd.CONTINUE_WITH_GAME);
        } else {
            return new TurnEndOptions.Builder()
                    .wonGame(showWin())
                    .lostGame(showLoss())
                    .safety(showSafety())
                    .safetyError(showSafetyMiss())
                    .miss(showMiss())
                    .missOnBreak(showBreakMiss())
                    .checkScratch(checkScratch())
                    .push(showPush())
                    .skipTurn(showTurnSkip())
                    .defaultOption(selection());
        }
    }

    public TurnEndOptions create(GameStatus game, TableStatus nextInning) {
        this.game = game;
        this.nextInning = nextInning;

        return createTurnEndOptionsBuilder().build();
    }

}
