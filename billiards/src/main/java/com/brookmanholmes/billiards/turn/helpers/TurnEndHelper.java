package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
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

    /**
     * Constructs a new TurnEndHelper setting the game status and table status
     * @param game The current status of the game you wish you create a new
     * {@link com.brookmanholmes.billiards.turn.TurnEndOptions} for
     * @param tableStatus The status of the table that you want a new
     * {@link com.brookmanholmes.billiards.turn.TurnEndOptions} for
     */
    TurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        this.tableStatus = tableStatus;
        this.game = game;
    }

    /**
     * Convenience method to create a new TurnEndHelper of the correct type to provide accurate
     * turn endings
     * @param game The current status of the game you wish you create a new
     * {@link com.brookmanholmes.billiards.turn.TurnEndOptions} for
     * @param tableStatus The status of the table that you want a new
     * {@link com.brookmanholmes.billiards.turn.TurnEndOptions} for
     * @return A new TurnEndHelper of the correct subclass
     * @throws InvalidGameTypeException when provided with
     * {@link GameType#AMERICAN_ROTATION} or
     * {@link GameType#STRAIGHT_POOL} because those games
     * are not yet supported
     */
    static TurnEndHelper create(GameStatus game, ITableStatus tableStatus) throws InvalidGameTypeException {
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
            case BCA_GHOST_EIGHT_BALL:
                return new GhostEightBallTurnEndHelper(game, tableStatus);
            case BCA_GHOST_NINE_BALL:
                return new GhostTurnEndHelper(game, tableStatus);
            case BCA_GHOST_TEN_BALL:
                return new GhostTurnEndHelper(game, tableStatus);
            case APA_GHOST_EIGHT_BALL:
                return new ApaGhostEightBallTurnEndHelper(game, tableStatus);
            case APA_GHOST_NINE_BALL:
                return new ApaGhostNineBallTurnEndHelper(game, tableStatus);
            case STRAIGHT_POOL:
                return new StraightPoolTurnEndHelper(game, tableStatus);
            case STRAIGHT_GHOST:
                return new GhostTurnEndHelper(game, tableStatus);
            default:
                throw new InvalidGameTypeException();
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
        TurnEndHelper turnEndHelper = create(game, tableStatus);
        return turnEndHelper.getTurnEndOptions();
    }

    /**
     * Determines whether or not the player could have won the game
     * @return true if the game is won, false if the game is not
     */
    abstract boolean showWin();

    /**
     * Determines whether or not the player could have committed a serious foul
     * @return true if the player committed a serious foul, false otherwise
     */
    abstract boolean seriousFoul();

    /**
     * Determines the default selection for the end of the turn
     * @return One of {@link com.brookmanholmes.billiards.turn.TurnEnd#GAME_WON}
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#BREAK_MISS}
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS}, depending on status of the table
     */
    private TurnEnd selection() {
        if (showWin())
            return TurnEnd.GAME_WON;
        else if (showBreakMiss())
            return TurnEnd.BREAK_MISS;
        else return TurnEnd.MISS;
    }

    /**
     * Determines if the player is allowed to push
     * @return true if they are, false otherwise
     */
    boolean showPush() {
        return ((game.allowPush && !game.newGame)
                || (game.newGame && tableStatus.getBreakBallsMade() > 0))
                && tableStatus.getShootingBallsMade() == 0;
    }

    /**
     * Determines if the player is allowed to skip their turn (because the other player pushed or
     * (in 10 ball) accidentally made a ball)
     * @return True if they are allowed to skip their turn, false otherwise
     */
    boolean showTurnSkip() {
        return game.allowTurnSkip
                && tableStatus.getShootingBallsMade() == 0
                && tableStatus.getDeadBalls() == 0;
    }

    /**
     * Determines if the player could have played safe for their turn end
     * @return true if possible, false otherwise
     */
    boolean showSafety() {
        return !showWin() && !showBreakMiss();
    }

    /**
     * Determines if the player could have failed their safety attempt for their turn end
     * @return True if they potentially missed their safety, false otherwise
     */
    boolean showSafetyMiss() {
        return !showWin() && !showBreakMiss();
    }

    /**
     * Determines if the player could have missed for their turn end
     * @return True if they potentially missed, false otherwise
     */
    boolean showMiss() {
        return !showWin() && !showBreakMiss();
    }

    /**
     * Determines if the player potentially fouled at the end of their turn
     * @return True if a foul was possible, false otherwise
     */
    boolean checkFoul() {
        return tableStatus.getDeadBallsOnBreak() > 0;
    }

    /**
     * Determines if the player could have missed on the break
     * @return True if they potentially missed their break shot, false otherwise
     */
    boolean showBreakMiss() {
        return game.newGame && tableStatus.getBreakBallsMade() == 0;
    }

    /**
     * Determines if the player really lost the game (APA/BCA 8 ball), generally from making the 8
     * ball early
     * @return true if the player really lost the game, false otherwise
     */
    boolean reallyLostGame() {
        return false;
    }

    /**
     * Create a new TurnEndOptions using the status of this TurnEndHelper
     * @return A new TurnEndOptions with the corresponding options based on the provided input to
     * this TurnEndHelper
     */
    TurnEndOptions getTurnEndOptions() {
        if (game.playerAllowedToBreakAgain) {
            return new TurnEndOptions.Builder().allowPlayerToChooseWhoBreaks().defaultOption(TurnEnd.CONTINUE_WITH_GAME).build();
        } else if (allowPlayerToContinueGame()) {
            return new TurnEndOptions.Builder().allowPlayerToChooseToContinueGame().defaultOption(TurnEnd.MISS).build();
        } else {
            return new TurnEndOptions.Builder()
                    .wonGame(showWin())
                    .lostGame(seriousFoul(), reallyLostGame())
                    .safety(showSafety())
                    .safetyError(showSafetyMiss())
                    .miss(showMiss())
                    .missOnBreak(showBreakMiss())
                    .checkFoul(checkFoul())
                    .push(showPush())
                    .skipTurn(showTurnSkip())
                    .defaultOption(selection()).build();
        }
    }

    /**
     * Convenience method for determining if the player should be allowed to choose to break again
     * @return True if the player is allowed the choice of breaking again, false otherwise
     */
    private boolean allowPlayerToContinueGame() {
        return game.gameType == GameType.BCA_EIGHT_BALL &&
                tableStatus.isGameBallMadeOnBreak() &&
                tableStatus.getShootingBallsMade() == 0 &&
                tableStatus.getDeadBalls() == 0;
    }
}
