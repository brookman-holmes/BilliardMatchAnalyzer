package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/20/2016.
 */
class GhostBreakPage extends BreakPage {
    GhostBreakPage(ModelCallbacks callbacks, String title, String title2, Bundle matchData) {
        super(callbacks, title, title2, matchData);
    }

    @Override String showShotPage() {
        return "";
    }
}
