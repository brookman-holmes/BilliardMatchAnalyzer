package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.turn.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.turn.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;

/**
 * Created by Brookman Holmes on 11/13/2015.
 */
public class TurnEndOptionsList {
    private static final TurnEndOptions option1 = option().wonGame();
    private static final TurnEndOptions option2 = option().missOnBreak().select(BREAK_MISS);
    private static final TurnEndOptions option3 = option().push().safetyError().miss().select(MISS);
    private static final TurnEndOptions option4 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option5 = option().defaultSelections();
    private static final TurnEndOptions option6 = option().safetyError().miss().select(MISS);
    private static final TurnEndOptions option7 = option().wonGame();
    private static final TurnEndOptions option8 = option().missOnBreak().foul().select(BREAK_MISS);
    private static final TurnEndOptions option9 = option().wonGame();
    private static final TurnEndOptions option10 = option().wonGame();
    private static final TurnEndOptions option11 = option().wonGame();
    private static final TurnEndOptions option12 = option().defaultSelections();
    private static final TurnEndOptions option13 = option().defaultSelections();
    private static final TurnEndOptions option14 = option().wonGame();
    private static final TurnEndOptions option15 = option().push().defaultSelections();
    private static final TurnEndOptions option16 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option17 = option().defaultSelections();
    private static final TurnEndOptions option18 = option().defaultSelections();
    private static final TurnEndOptions option19 = option().safetyError().miss().select(MISS);
    private static final TurnEndOptions option20 = option().wonGame();
    private static final TurnEndOptions option21 = option().defaultSelections();
    private static final TurnEndOptions option22 = option().miss().safetyError().select(MISS);
    private static final TurnEndOptions option23 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option24 = option().defaultSelections();
    private static final TurnEndOptions option25 = option().wonGame();
    private static final TurnEndOptions option26 = option().push().defaultSelections();
    private static final TurnEndOptions option27 = option().wonGame();
    private static final TurnEndOptions option28 = option().missOnBreak().select(BREAK_MISS);
    private static final TurnEndOptions option29 = option().wonGame();
    private static final TurnEndOptions option30 = option().push().defaultSelections();
    private static final TurnEndOptions option31 = option().skipTurn().defaultSelections();
    private static final TurnEndOptions option32 = option().defaultSelections();
    private static final TurnEndOptions option33 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18, option19, option20, option21, option22, option23, option24, option25,
                option26, option27, option28, option29, option30, option31, option32, option33
        );
    }

    private static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }


}
