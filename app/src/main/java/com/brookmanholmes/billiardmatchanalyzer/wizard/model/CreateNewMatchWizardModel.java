package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.content.Context;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class CreateNewMatchWizardModel extends AbstractWizardModel {
    public CreateNewMatchWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new PlayerNamePage(this, "Players")
                        .setRequired(true),
                new GameChoicePage(this),
                new FirstBreakPage(this)
        );
    }
}
