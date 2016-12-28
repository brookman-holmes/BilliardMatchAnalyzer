package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.StraightPoolShotFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 12/1/2016.
 */
class StraightPoolPage extends Page implements UpdatesTurnInfo {
    private static final String TAG = "StraightPoolPage";
    private static final String TABLE_STATUS_KEY = "table_status";

    StraightPoolPage(ModelCallbacks model, String title, Bundle matchData) {
        super(model, title);

        data.putAll(matchData);
        data.putInt(SIMPLE_DATA_KEY, 0);
    }

    @Override
    public Fragment createFragment() {
        return StraightPoolShotFragment.create(getKey(), data);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel turnWizardModel) {
        clearTable(turnWizardModel.getTableStatus()); // reset all the balls to back to on the table
        int ballsMade = data.getInt(SIMPLE_DATA_KEY);
        ArrayList<Integer> ballsOnTable = data.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY);
        for (int i = 0; i < ballsMade; i++) {
            turnWizardModel.getTableStatus().setBallTo(BallStatus.MADE, ballsOnTable.get(i));
        }
    }

    private void clearTable(ITableStatus tableStatus) {
        for (int ball = 1; ball <= tableStatus.size(); ++ball) {
            if (tableStatus.getBallStatus(ball) == BallStatus.MADE)
                tableStatus.setBallTo(BallStatus.ON_TABLE, ball);
        }
    }
}
