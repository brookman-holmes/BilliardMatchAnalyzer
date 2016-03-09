package com.brookmanholmes.billiards.acceptance;

import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

import static com.brookmanholmes.billiards.inning.TurnEnd.CONTINUE_WITH_GAME;
import static com.brookmanholmes.billiards.inning.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.inning.TurnEnd.MISS;

/**
 * Created by Brookman Holmes on 11/13/2015.
 */
public class TurnEndOptionsBuilder {
    private TurnEndOptions.Builder options = new TurnEndOptions.Builder();

    public TurnEndOptions wonGame() {
        return options.wonGame(true).defaultOption(GAME_WON).build();
    }

    public TurnEndOptionsBuilder missOnBreak() {
        options.missOnBreak(true);
        return this;
    }

    public TurnEndOptionsBuilder miss() {
        options.miss(true);
        return this;
    }

    public TurnEndOptionsBuilder lostGame() {
        options.lostGame(true);
        return this;
    }

    public TurnEndOptionsBuilder safetyError() {
        options.safetyError(true);
        return this;
    }

    public TurnEndOptionsBuilder safety() {
        options.safety(true);
        return this;
    }

    public TurnEndOptionsBuilder push() {
        options.push(true);
        return this;
    }

    public TurnEndOptionsBuilder skipTurn() {
        options.skipTurn(true);
        return this;
    }

    public TurnEndOptionsBuilder scratch() {
        options.checkScratch(true);
        return this;
    }

    public TurnEndOptions reBreak() {
        return options.allowPlayerToChooseToContinueGame().defaultOption(CONTINUE_WITH_GAME).build();
    }

    public TurnEndOptions reBreakOnScratch() {
        return options.allowPlayerToChooseWhoBreaks().defaultOption(CONTINUE_WITH_GAME).build();
    }

    public TurnEndOptions select(TurnEnd turnEnd) {
        return options.defaultOption(turnEnd).build();
    }

    public TurnEndOptions defaultSelections() {
        return options.safety(true).miss(true).safetyError(true).defaultOption(MISS).build();
    }
}
