package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.acceptance.TurnEndOptionsBuilder;
import com.brookmanholmes.billiards.inning.TurnEndOptions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/13/2015.
 */
public class TurnEndOptionsList {
    static TurnEndOptions option1 = option().push().defaultSelections();
    static TurnEndOptions option2 = option().skipTurn().defaultSelections();
    static TurnEndOptions option3 = option().wonGame();
    static TurnEndOptions option4 = option().push().defaultSelections();
    static TurnEndOptions option5 = option().defaultSelections();
    static TurnEndOptions option6 = option().wonGame();
    static TurnEndOptions option7 = option().wonGame();
    static TurnEndOptions option8 = option().push().defaultSelections();
    static TurnEndOptions option9 = option().wonGame();
    static TurnEndOptions option10 = option().push().defaultSelections();
    static TurnEndOptions option11 = option().defaultSelections();
    static TurnEndOptions option12 = option().defaultSelections();
    static TurnEndOptions option13 = option().defaultSelections();
    static TurnEndOptions option14 = option().defaultSelections();
    static TurnEndOptions option15 = option().wonGame();
    static TurnEndOptions option16 = option().push().defaultSelections();
    static TurnEndOptions option17 = option().defaultSelections();
    static TurnEndOptions option18 = option().defaultSelections();
    static TurnEndOptions option19 = option().wonGame();
    static TurnEndOptions option20 = option().defaultSelections();
    static TurnEndOptions option21 = option().wonGame();
    static TurnEndOptions option22 = option().push().defaultSelections();
    static TurnEndOptions option23 = option().defaultSelections();
    static TurnEndOptions option24 = option().defaultSelections();
    static TurnEndOptions option25 = option().defaultSelections();
    static TurnEndOptions option26 = option().defaultSelections();
    static TurnEndOptions option27 = option().defaultSelections();
    static TurnEndOptions option28 = option().wonGame();
    static TurnEndOptions option29 = option().push().defaultSelections();
    static TurnEndOptions option30 = option().defaultSelections();
    static TurnEndOptions option31 = option().defaultSelections();
    static TurnEndOptions option32 = option().wonGame();
    static TurnEndOptions option33 = option().push().defaultSelections();
    static TurnEndOptions option34 = option().defaultSelections();
    static TurnEndOptions option35 = option().defaultSelections();
    static TurnEndOptions option36 = option().defaultSelections();
    static TurnEndOptions option37 = option().wonGame();
    static TurnEndOptions option38 = option().push().defaultSelections();
    static TurnEndOptions option39 = option().defaultSelections();
    static TurnEndOptions option40 = option().defaultSelections();
    static TurnEndOptions option41 = option().defaultSelections();
    static TurnEndOptions option42 = option().wonGame();
    static TurnEndOptions option43 = option().defaultSelections();
    static TurnEndOptions option44 = option().wonGame();
    static TurnEndOptions option45 = option().push().defaultSelections();
    static TurnEndOptions option46 = option().defaultSelections();
    static TurnEndOptions option47 = option().wonGame();
    static TurnEndOptions option48 = option().defaultSelections();
    static TurnEndOptions option49 = option().defaultSelections();
    static TurnEndOptions option50 = option().defaultSelections();
    static TurnEndOptions option51 = option().defaultSelections();
    static TurnEndOptions option52 = option().defaultSelections();
    static TurnEndOptions option53 = option().wonGame();
    static TurnEndOptions option54 = option().push().defaultSelections();
    static TurnEndOptions option55 = option().defaultSelections();
    static TurnEndOptions option56 = option().wonGame();

    public static List<TurnEndOptions> getOptionsList() {
        return Arrays.asList(
                option1, option2, option3, option4, option5, option6, option7, option8, option9,
                option10, option11, option12, option13, option14, option15, option16, option17,
                option18, option19, option20, option21, option22, option23, option24, option25,
                option26, option27, option28, option29, option30, option31, option32, option33,
                option34, option35, option36, option37, option38, option39, option40, option41,
                option42, option43, option44, option45, option46, option47, option48, option49,
                option50, option51, option52, option53, option54, option55, option56
        );
    }

    static TurnEndOptionsBuilder option() {
        return new TurnEndOptionsBuilder();
    }
}
