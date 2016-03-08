package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class MissBranchPage extends BranchPage {
    private String[] howMissTypes = new String[]{"Too thin", "Too thick", "Left of aim point", "Right of aim point"};
    private String[] howMissBankKickTypes = new String[]{"Too thin", "Too thick", "Left of aim point", "Right of aim point"};

    public MissBranchPage(ModelCallbacks callbacks) {
        super(callbacks, "What did you miss?");

        addBranch("Cut shot", new CutTypePage(callbacks),
                new AngleTypePage(callbacks),
                new HowMissPage(callbacks, howMissTypes, "cut miss"));

        addBranch("Long straight shot", new HowMissPage(callbacks, howMissTypes, "straight miss"));

        addBranch("Bank shot", new BankPage(callbacks),
                new HowMissPage(callbacks, howMissBankKickTypes, "bank miss"));

        addBranch("Kick shot", new KickPage(callbacks),
                new HowMissPage(callbacks, howMissBankKickTypes, "kick miss"));

        addBranch("Combo", new HowMissPage(callbacks, howMissTypes, "combo miss"));

        addBranch("Carom", new HowMissPage(callbacks, howMissTypes, "carom miss")); // these choices are probably shit

        addBranch("Jump", new HowMissPage(callbacks, howMissTypes, "jump miss"));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
