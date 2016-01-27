package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.os.Bundle;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBreakBallsDialog extends SelectBallsDialog {
    public static SelectBallsDialog create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString("title", "Balls Brookman made on the break");

        SelectBallsDialog fragment = new SelectBallsDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
