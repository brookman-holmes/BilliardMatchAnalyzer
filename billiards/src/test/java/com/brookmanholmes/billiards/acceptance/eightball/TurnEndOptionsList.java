package com.brookmanholmes.billiards.acceptance.eightball;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/16/2015.
 */
public class TurnEndOptionsList {
    private static final TurnEndOptions option1 = option().wonGame();
    private static final TurnEndOptions option2 = option().defaultSelections();
    private static final TurnEndOptions option3 = option().defaultSelections();
    private static final TurnEndOptions option4 = option().safetyError().miss().scratch().lostGame().scratch().select(TurnEnd.MISS);
    private static final TurnEndOptions option5 = option().defaultSelections();
    private static final TurnEndOptions option6 = option().wonGame();
    private static final TurnEndOptions option7 = option().scratch().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option8 = option().reBreakOnScratch();
    private static final TurnEndOptions option9 = option().defaultSelections();
    private static final TurnEndOptions option10 = option().wonGame();
    private static final TurnEndOptions option11 = option().wonGame();
    private static final TurnEndOptions option12 = option().defaultSelections();
    private static final TurnEndOptions option13 = option().defaultSelections();
    private static final TurnEndOptions option14 = option().wonGame();
    private static final TurnEndOptions option15 = option().scratch().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option16 = option().reBreakOnScratch();
    private static final TurnEndOptions option17 = option().reBreak();
    private static final TurnEndOptions option18 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18
        );
    }

    private static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }
}
