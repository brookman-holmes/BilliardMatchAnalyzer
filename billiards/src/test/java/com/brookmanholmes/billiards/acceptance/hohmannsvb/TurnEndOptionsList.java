package com.brookmanholmes.billiards.acceptance.hohmannsvb;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/13/2015.
 */
public class TurnEndOptionsList {
    private static final TurnEndOptions option1 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option2 = option().defaultSelections();
    private static final TurnEndOptions option3 = option().wonGame();
    private static final TurnEndOptions option4 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option5 = option().defaultSelections();
    private static final TurnEndOptions option6 = option().defaultSelections();
    private static final TurnEndOptions option7 = option().defaultSelections();
    private static final TurnEndOptions option8 = option().defaultSelections();
    private static final TurnEndOptions option9 = option().wonGame();
    private static final TurnEndOptions option10 = option().push().defaultSelections();
    private static final TurnEndOptions option11 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option12 = option().wonGame();
    private static final TurnEndOptions option13 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option14 = option().push().defaultSelections();
    private static final TurnEndOptions option15 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option16 = option().defaultSelections();
    private static final TurnEndOptions option17 = option().defaultSelections();
    private static final TurnEndOptions option18 = option().defaultSelections();
    private static final TurnEndOptions option19 = option().wonGame();
    private static final TurnEndOptions option20 = option().defaultSelections();
    private static final TurnEndOptions option21 = option().defaultSelections();
    private static final TurnEndOptions option22 = option().defaultSelections();
    private static final TurnEndOptions option23 = option().defaultSelections();
    private static final TurnEndOptions option24 = option().wonGame();
    private static final TurnEndOptions option25 = option().push().defaultSelections();
    private static final TurnEndOptions option26 = option().defaultSelections();
    private static final TurnEndOptions option27 = option().miss().safetyError().select(TurnEnd.MISS);
    private static final TurnEndOptions option28 = option().wonGame();
    private static final TurnEndOptions option29 = option().scratch().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option30 = option().defaultSelections();
    private static final TurnEndOptions option31 = option().defaultSelections();
    private static final TurnEndOptions option32 = option().wonGame();
    private static final TurnEndOptions option33 = option().wonGame();
    private static final TurnEndOptions option34 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option35 = option().wonGame();
    private static final TurnEndOptions option36 = option().wonGame();
    private static final TurnEndOptions option37 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option38 = option().defaultSelections();
    private static final TurnEndOptions option39 = option().wonGame();
    private static final TurnEndOptions option40 = option().wonGame();
    private static final TurnEndOptions option41 = option().missOnBreak().select(TurnEnd.BREAK_MISS);
    private static final TurnEndOptions option42 = option().push().defaultSelections();
    private static final TurnEndOptions option43 = option().defaultSelections();
    private static final TurnEndOptions option44 = option().wonGame();
    private static final TurnEndOptions option45 = option().push().defaultSelections();
    private static final TurnEndOptions option46 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option47 = option().defaultSelections();
    private static final TurnEndOptions option48 = option().defaultSelections();
    private static final TurnEndOptions option49 = option().defaultSelections();
    private static final TurnEndOptions option50 = option().defaultSelections();
    private static final TurnEndOptions option51 = option().defaultSelections();
    private static final TurnEndOptions option52 = option().defaultSelections();
    private static final TurnEndOptions option53 = option().defaultSelections();
    private static final TurnEndOptions option54 = option().defaultSelections();
    private static final TurnEndOptions option55 = option().defaultSelections();
    private static final TurnEndOptions option56 = option().defaultSelections();
    private static final TurnEndOptions option57 = option().defaultSelections();
    private static final TurnEndOptions option58 = option().defaultSelections();
    private static final TurnEndOptions option59 = option().wonGame();
    private static final TurnEndOptions option60 = option().wonGame();
    private static final TurnEndOptions option61 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18, option19, option20, option21, option22, option23, option24, option25,
                option26, option27, option28, option29, option30, option31, option32, option33,
                option34, option35, option36, option37, option38, option39, option40, option41,
                option42, option43, option44, option45, option46, option47, option48, option49,
                option50, option51, option52, option53, option54, option55, option56, option57,
                option58, option59, option60, option61
        );
    }

    private static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }
}
