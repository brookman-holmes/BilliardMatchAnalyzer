package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.FoulFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;

/**
 * Created by Brookman Holmes on 3/3/2016.
 */
public class FoulPage extends SingleFixedChoicePage implements UpdatesTurnInfo, RequiresUpdatedTurnInfo {
    private static final String[] defaultChoicesWithLoss = new String[]{"Yes, lost game", "Yes", "No"};
    FoulFragment fragment;
    boolean dataAutoUpdated = false;

    public FoulPage(ModelCallbacks callbacks, Bundle matchData) {
        super(callbacks, "Did you foul?");

        data.putAll(matchData);

        setChoices(defaultChoicesWithLoss);
        setValue("No");
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.scratch = data.getString(SIMPLE_DATA_KEY, "").startsWith("Yes");
        turnBuilder.lostGame = data.getString(SIMPLE_DATA_KEY, "").equals("Yes, lost game");
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
            dataAutoUpdated = true;
            fragment.updateOptions(getPossibleChoices(options), getDefaultCheck(options));
        }
    }

    private List<String> getPossibleChoices(TurnEndOptions options) {
        List<String> list = new ArrayList<>();

        if (options.lostGame) {
            list.add("Yes, lost game");

            if (!isGameLostForReal())
                list.add("Yes");

            if (options.possibleEndings.contains(TurnEnd.SAFETY))
                list.add("No");
        } else if (options.foul) {
            list.add("Yes");
        } else {
            list.add("Yes");
            list.add("No");
        }

        return list;
    }

    private String getDefaultCheck(TurnEndOptions options) {
        if (options.lostGame && options.foul)
            return "Yes, lost game";
        else return options.foul ? "Yes" : "No";
    }

    private boolean isGameLostForReal() {
        return GameType.valueOf(data.getString(GAME_TYPE_KEY)) == GameType.BCA_EIGHT_BALL ||
                GameType.valueOf(data.getString(GAME_TYPE_KEY)) == GameType.APA_EIGHT_BALL;
    }

    @Override
    public void notifyDataChanged() {
        if (!dataAutoUpdated) {
            super.notifyDataChanged();
            dataAutoUpdated = false;
        }
    }
}
