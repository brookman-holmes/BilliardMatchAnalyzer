package com.brookmanholmes.billiards.acceptance.eightball;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/16/2015.
 */
public class TurnEndOptionsList {
    static TurnEndOptions option1 = option().wonGame();
    static TurnEndOptions option2 = option().defaultSelections();
    static TurnEndOptions option3 = option().defaultSelections();
    static TurnEndOptions option4 = option().lostGame();
    static TurnEndOptions option5 = option().defaultSelections();
    static TurnEndOptions option6 = option().wonGame();
    static TurnEndOptions option7 = option().scratch().missOnBreak().select(TurnEnd.BREAK_MISS);
    static TurnEndOptions option8 = option().reBreakOnScratch();
    static TurnEndOptions option9 = option().defaultSelections();
    static TurnEndOptions option10 = option().wonGame();
    static TurnEndOptions option11 = option().wonGame();
    static TurnEndOptions option12 = option().defaultSelections();
    static TurnEndOptions option13 = option().defaultSelections();
    static TurnEndOptions option14 = option().wonGame();
    static TurnEndOptions option15 = option().scratch().missOnBreak().select(TurnEnd.BREAK_MISS);
    static TurnEndOptions option16 = option().reBreakOnScratch();
    static TurnEndOptions option17 = option().reBreak();
    static TurnEndOptions option18 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18
        );
    }

    static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }
}
