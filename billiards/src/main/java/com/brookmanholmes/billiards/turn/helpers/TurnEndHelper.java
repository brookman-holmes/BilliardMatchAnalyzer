package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;


/**
 * Created by Brookman Holmes on 10/30/2015.
 */
abstract public class TurnEndHelper {
    ITableStatus nextInning;
    GameStatus game;

    TurnEndHelper() {
    }

    public static TurnEndHelper createGhostHelper() {
        return new GhostTurnEndHelper();
    }

    public static TurnEndHelper create(GameType gameType) throws InvalidGameTypeException {
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

    abstract boolean lostGame();

    TurnEnd selection() {
        if (showWin())
            return TurnEnd.GAME_WON;
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

    boolean checkFoul() {
        return nextInning.getDeadBallsOnBreak() > 0;
    }

    boolean showBreakMiss() {
        return game.newGame && nextInning.getBreakBallsMade() == 0;
    }

    TurnEndOptions.Builder createTurnEndOptionsBuilder() {
        if (game.playerAllowedToBreakAgain) {
            return new TurnEndOptions.Builder().allowPlayerToChooseWhoBreaks().defaultOption(TurnEnd.CONTINUE_WITH_GAME);
        } else if (game.gameType == GameType.BCA_EIGHT_BALL && nextInning.getGameBallMadeOnBreak()  && nextInning.getShootingBallsMade() == 0 && nextInning.getDeadBalls() == 0) {
            return new TurnEndOptions.Builder().allowPlayerToChooseToContinueGame().defaultOption(TurnEnd.CONTINUE_WITH_GAME);
        } else {
            return new TurnEndOptions.Builder()
                    .wonGame(showWin())
                    .lostGame(lostGame())
                    .safety(showSafety())
                    .safetyError(showSafetyMiss())
                    .miss(showMiss())
                    .missOnBreak(showBreakMiss())
                    .checkScratch(checkFoul())
                    .push(showPush())
                    .skipTurn(showTurnSkip())
                    .defaultOption(selection());
        }
    }

    public TurnEndOptions getTurnEndOptions(GameStatus game, ITableStatus nextInning) {
        this.game = game;
        this.nextInning = nextInning;

        return createTurnEndOptionsBuilder().build();
    }
}
