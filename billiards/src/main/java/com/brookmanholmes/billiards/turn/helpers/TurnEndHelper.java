package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;


/**
 * Created by Brookman Holmes on 10/30/2015.
 * Helper class that creates a TurnEndOptions object based on the status of the table
 */
public abstract class TurnEndHelper {
    ITableStatus tableStatus;
    GameStatus game;

    TurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        this.tableStatus = tableStatus;
        this.game = game;
    }

    static TurnEndHelper create(GameStatus game, ITableStatus tableStatus) throws InvalidGameTypeException {
        if (game.breakType == BreakType.GHOST)
            return new GhostTurnEndHelper(game, tableStatus);

        switch (game.gameType) {
            case APA_EIGHT_BALL:
                return new ApaEightBallTurnEndHelper(game, tableStatus);
            case APA_NINE_BALL:
                return new RotationTurnEndHelper(game, tableStatus);
            case BCA_EIGHT_BALL:
                return new EightBallTurnEndHelper(game, tableStatus);
            case BCA_NINE_BALL:
                return new RotationTurnEndHelper(game, tableStatus);
            case BCA_TEN_BALL:
                return new TenBallTurnEndHelper(game, tableStatus);
            default:
                throw new InvalidGameTypeException();
        }
    }

    abstract boolean showWin();

    abstract boolean lostGame();

    private TurnEnd selection() {
        if (showWin())
            return TurnEnd.GAME_WON;
        else if (showBreakMiss())
            return TurnEnd.BREAK_MISS;
        else return TurnEnd.MISS;
    }

    boolean showPush() {
        return ((game.allowPush && !game.newGame)
                || (game.newGame && tableStatus.getBreakBallsMade() > 0))
                && tableStatus.getShootingBallsMade() == 0;
    }

    boolean showTurnSkip() {
        return game.allowTurnSkip
                && tableStatus.getShootingBallsMade() == 0
                && tableStatus.getDeadBalls() == 0;
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
        return tableStatus.getDeadBallsOnBreak() > 0;
    }

    boolean showBreakMiss() {
        return game.newGame && tableStatus.getBreakBallsMade() == 0;
    }

    TurnEndOptions.Builder createTurnEndOptionsBuilder() {
        if (game.playerAllowedToBreakAgain) {
            return new TurnEndOptions.Builder().allowPlayerToChooseWhoBreaks().defaultOption(TurnEnd.CONTINUE_WITH_GAME);
        } else if (game.gameType == GameType.BCA_EIGHT_BALL && tableStatus.getGameBallMadeOnBreak()  && tableStatus.getShootingBallsMade() == 0 && tableStatus.getDeadBalls() == 0) {
            return new TurnEndOptions.Builder().allowPlayerToChooseToContinueGame().defaultOption(TurnEnd.MISS);
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

    /**
     * Creates a new {@link com.brookmanholmes.billiards.turn.TurnEndOptions} object with a list of
     * the possible turn endings based on the current status of the game and the status of the table
     * for the next turn
     * @param game The current status of the game
     * @param tableStatus The status of the table for the next turn
     * @return A new {@link com.brookmanholmes.billiards.turn.TurnEndOptions} object
     */
    public static TurnEndOptions getTurnEndOptions(GameStatus game, ITableStatus tableStatus) {
        TurnEndHelper turnEndHelper = TurnEndHelper.create(game, tableStatus);

        return turnEndHelper.createTurnEndOptionsBuilder().build();
    }
}
