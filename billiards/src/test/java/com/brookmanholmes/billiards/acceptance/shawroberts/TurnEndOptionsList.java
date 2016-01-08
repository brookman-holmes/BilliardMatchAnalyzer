package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.inning.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.MISS;

/**
 * Created by Brookman Holmes on 11/13/2015.
 */
public class TurnEndOptionsList {
    static TurnEndOptions option1 = option().wonGame();
    static TurnEndOptions option2 = option().missOnBreak().select(BREAK_MISS);
    static TurnEndOptions option3 = option().push().safetyError().miss().select(MISS);
    static TurnEndOptions option4 = option().skipTurn().defaultSelections();
    static TurnEndOptions option5 = option().defaultSelections();
    static TurnEndOptions option6 = option().safetyError().miss().select(MISS);
    static TurnEndOptions option7 = option().wonGame();
    static TurnEndOptions option8 = option().missOnBreak().scratch().select(BREAK_MISS);
    static TurnEndOptions option9 = option().wonGame();
    static TurnEndOptions option10 = option().wonGame();
    static TurnEndOptions option11 = option().wonGame();
    static TurnEndOptions option12 = option().defaultSelections();
    static TurnEndOptions option13 = option().defaultSelections();
    static TurnEndOptions option14 = option().wonGame();
    static TurnEndOptions option15 = option().push().defaultSelections();
    static TurnEndOptions option16 = option().skipTurn().defaultSelections();
    static TurnEndOptions option17 = option().defaultSelections();
    static TurnEndOptions option18 = option().defaultSelections();
    static TurnEndOptions option19 = option().safetyError().miss().select(MISS);
    static TurnEndOptions option20 = option().wonGame();
    static TurnEndOptions option21 = option().defaultSelections();
    static TurnEndOptions option22 = option().miss().safetyError().select(MISS);
    static TurnEndOptions option23 = option().skipTurn().defaultSelections();
    static TurnEndOptions option24 = option().defaultSelections();
    static TurnEndOptions option25 = option().wonGame();
    static TurnEndOptions option26 = option().push().defaultSelections();
    static TurnEndOptions option27 = option().wonGame();
    static TurnEndOptions option28 = option().missOnBreak().select(BREAK_MISS);
    static TurnEndOptions option29 = option().wonGame();
    static TurnEndOptions option30 = option().push().defaultSelections();
    static TurnEndOptions option31 = option().skipTurn().defaultSelections();
    static TurnEndOptions option32 = option().defaultSelections();
    static TurnEndOptions option33 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18, option19, option20, option21, option22, option23, option24, option25,
                option26, option27, option28, option29, option30, option31, option32, option33
        );
    }

    static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }


}
