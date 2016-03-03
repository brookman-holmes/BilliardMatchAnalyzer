package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PageList;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class CreateNewMatchWizardModel extends AbstractWizardModel {
    String playerName, opponentName;

    public CreateNewMatchWizardModel(Context context) {
        super(context);
        rootPageList = onNewRootPageList();
    }

    @Override
    public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof PlayerNamePage) {
            playerName = ((PlayerNamePage) page).getPlayerName();
            opponentName = ((PlayerNamePage) page).getOpponentName();
        }

        updatePlayerNames();
    }

    @Override
    public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updatePlayerNames();
    }

    private void updatePlayerNames() {
        for (Page page : getCurrentPageSequence()) {
            if (page instanceof RequiresPlayerNames) {
                ((RequiresPlayerNames) page).setPlayerNames(playerName, opponentName);
            }
        }
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new PlayerNamePage(this),
                new GameChoicePage(this),
                new SingleFixedChoicePage(this, "Stat Detail Level").setChoices("Simple", "Normal", "Advanced").setValue("Normal")
        );
    }
}
