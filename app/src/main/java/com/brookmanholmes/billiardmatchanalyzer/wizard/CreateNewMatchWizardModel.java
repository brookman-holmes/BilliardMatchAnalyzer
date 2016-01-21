package com.brookmanholmes.billiardmatchanalyzer.wizard;

import android.content.Context;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.FirstBreakPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.GameChoicePage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PageList;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PlayerNamePage;

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
