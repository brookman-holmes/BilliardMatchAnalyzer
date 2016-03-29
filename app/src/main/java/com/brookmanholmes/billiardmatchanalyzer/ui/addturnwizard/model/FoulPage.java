package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.FoulFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;

/**
 * Created by Brookman Holmes on 3/3/2016.
 */
public class FoulPage extends SingleFixedChoicePage implements UpdatesTurnInfo, RequiresUpdatedTurnInfo {
    private static final String[] defaultChoicesWithLoss = new String[]{"Yes, lost game", "Yes", "No"};
    FoulFragment fragment;

    public FoulPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);
        data.putAll(matchData);
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.scratch = data.getString(SIMPLE_DATA_KEY, "");
        turnBuilder.lostGame = data.getString(SIMPLE_DATA_KEY, "");
    }

    @Override
    public Fragment createFragment() {
        return FoulFragment.create(getKey());
    }

    @Override
    public void getNewTurnInfo(TurnBuilder turnBuilder) {
        TurnEndHelper helper = TurnEndHelper.newTurnEndHelper(GameType.valueOf(data.getString(GAME_TYPE_KEY)));

        TurnEndOptions options = helper.create(
                MatchDialogHelperUtils.createGameStatusFromBundle(data), turnBuilder.tableStatus);

        updateFragment(options);
    }

    public void registerListener(FoulFragment fragment) {
        this.fragment = fragment;
    }

    public void unregisterListener() {
        fragment = null;
    }

    // TODO: 3/9/2016 make sure that I'm not checking foul when it's possible to have not scratched
    public void updateFragment(TurnEndOptions options) {
        if (fragment != null) {
            fragment.updateOptions(options);
        }
    }
}
