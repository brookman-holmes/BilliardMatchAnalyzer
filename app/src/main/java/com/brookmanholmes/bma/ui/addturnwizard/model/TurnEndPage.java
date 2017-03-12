package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.TurnEndFragment;
import com.brookmanholmes.bma.wizard.model.FragmentDependentBranch;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;

import java.util.ArrayList;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_STATUS_KEY;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndPage extends FragmentDependentBranch<TurnEndFragment> implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    public static final String FOUL_KEY = "foul_key";
    private final GameStatus gameStatus;

    TurnEndPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);
        data.putAll(matchData);
        gameStatus = (GameStatus) matchData.getSerializable(GAME_STATUS_KEY);
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        TurnEndOptions options = TurnEndHelper.getTurnEndOptions(gameStatus,
                TableStatus.newTable(gameStatus.gameType, gameStatus.ballsOnTable));
        ArrayList<String> stringList = new ArrayList<>();
        for (TurnEnd ending : options.possibleEndings) {
            stringList.add(ending.name());
        }

        return TurnEndFragment.create(getKey(), stringList, options.defaultCheck.name());
    }

    @Override
    public void getNewTurnInfo(AddTurnWizardModel model) {
        updateFragment();
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

    @Override
    public void registerFragment(TurnEndFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void unregisterFragment() {
        fragment = null;
    }

    @Override
    public void updateFragment() {
        if (fragment != null)
            fragment.updateOptions(getTurnEndOptions());
    }

    public TurnEndOptions getTurnEndOptions() {
        return TurnEndHelper.getTurnEndOptions(gameStatus, ((AddTurnWizardModel) modelCallbacks).getTableStatus());
    }

    @Override
    public void resetData(Bundle data) {
        super.resetData(data);
        updateFragment();
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
