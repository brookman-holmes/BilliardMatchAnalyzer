package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.FoulFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;

/**
 * Created by Brookman Holmes on 3/3/2016.
 */
public class FoulPage extends SingleFixedChoicePage implements UpdatesTurnInfo, RequiresUpdatedTurnInfo {
    private FoulFragment fragment;

    public FoulPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);
        data.putAll(matchData);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.setFoul(data.getString(SIMPLE_DATA_KEY, ""));
    }

    @Override public Fragment createFragment() {
        return FoulFragment.create(getKey());
    }

    @Override public void getNewTurnInfo(AddTurnWizardModel model) {

        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(
                MatchDialogHelperUtils.createGameStatusFromBundle(data), model.getTableStatus()
        );

        updateFragment(options);
    }

    public void registerListener(FoulFragment fragment) {
        this.fragment = fragment;
    }

    public void unregisterListener() {
        fragment = null;
    }

    private void updateFragment(TurnEndOptions options) {
        if (fragment != null) {
            fragment.updateOptions(options);
        }
    }
}
