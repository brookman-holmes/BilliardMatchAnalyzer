package com.brookmanholmes.billiardmatchanalyzer;

import android.content.Context;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Apa8BallRankPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Apa9BallRankPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BreakTypePage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.FirstBreakPage;
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
                new BranchPage(this, "Game")
                        .addBranch("American Rotation")
                        .addBranch("APA 8 ball",
                                new Apa8BallRankPage(this, 1),
                                new Apa8BallRankPage(this, 2))
                        .addBranch("APA 9 ball",
                                new Apa9BallRankPage(this, 1),
                                new Apa9BallRankPage(this, 2))
                        .addBranch("BCA 8 ball",
                                new BreakTypePage(this))
                        .addBranch("BCA 9 ball",
                                new BreakTypePage(this))
                        .addBranch("BCA 10 ball",
                                new BreakTypePage(this))
                        .addBranch("Straight pool")
                        .setRequired(true),
                new FirstBreakPage(this)
                        .setRequired(true)
        );
    }
}
