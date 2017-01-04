package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.TurnEndFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndPage extends BranchPage implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    public static final String FOUL_KEY = "foul_key";
    private TurnEndFragment fragment;

    TurnEndPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);
        data.putAll(matchData);
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(MatchDialogHelperUtils.getGameStatus(data),
                TableStatus.newTable(GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)),
                        data.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY)));
        ArrayList<String> stringList = new ArrayList<>();
        for (TurnEnd ending : options.possibleEndings) {
            stringList.add(ending.name());
        }

        return TurnEndFragment.create(getKey(), stringList, options.defaultCheck.name());
    }

    @Override
    public void getNewTurnInfo(AddTurnWizardModel model) {
        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(MatchDialogHelperUtils.getGameStatus(data),
                model.getTableStatus());
        updateFragment(options);
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setTurnEnd(data.getString(SIMPLE_DATA_KEY), data.getString(FOUL_KEY));
    }

    public boolean isFoulPossible(String turnEnd) {
        if (turnEnd.equals(branches.get(0).choice))
            return true;
        else if (turnEnd.equals(branches.get(1).choice))
            return true;
        else if (turnEnd.equals(branches.get(2).choice))
            return true;
        else return turnEnd.equals(branches.get(3).choice);
    }

    public void registerListener(TurnEndFragment fragment) {
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

    public TurnEndOptions getTurnEndOptions() {
        return TurnEndHelper.getTurnEndOptions(MatchDialogHelperUtils.getGameStatus(data), ((AddTurnWizardModel) modelCallbacks).getTableStatus());
    }

    @Override
    public void resetData(Bundle data) {
        super.resetData(data);
        updateFragment(getTurnEndOptions());
    }

    public void setTurnEnd(String turnEnd) {
        boolean notifyTree = !turnEnd.equals(getData().getString(SIMPLE_DATA_KEY));

        data.putString(SIMPLE_DATA_KEY, turnEnd);

        if (notifyTree)
            modelCallbacks.onPageTreeChanged();
        notifyDataChanged();
    }

    @Override
    public void notifyDataChanged() {
        modelCallbacks.onPageDataChanged(this);
    }
}
