package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/20/2016.
 */
public class GhostBreakPage extends BreakPage {
    public GhostBreakPage(ModelCallbacks callbacks, String title, String title2, Bundle matchData) {
        super(callbacks, title, title2, matchData);
    }

    @Override String showShotPage() {
        return "";
    }
}
