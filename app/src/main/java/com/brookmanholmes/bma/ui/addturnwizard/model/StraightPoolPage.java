package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.StraightPoolShotFragment;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_STATUS_KEY;

/**
 * Created by Brookman Holmes on 12/1/2016.
 */
public class StraightPoolPage extends BranchPage implements UpdatesTurnInfo {
    public static final String FOUL_KEY = "foul_key";
    public static final String BALLS_MADE_KEY = "balls_made_key";
    private static final String TAG = "StraightPoolPage";
    private static final String TABLE_STATUS_KEY = "table_status";

    private final GameStatus gameStatus;

    StraightPoolPage(ModelCallbacks model, String title, Bundle matchData) {
        super(model, title);

        gameStatus = (GameStatus) matchData.getSerializable(GAME_STATUS_KEY);
        data.putAll(matchData);
        data.putInt(BALLS_MADE_KEY, 0);
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
        int ballsMade = data.getInt(BALLS_MADE_KEY);
        for (int i = 0; i < ballsMade; i++) {
            turnWizardModel.getTableStatus().setBallTo(BallStatus.MADE, gameStatus.ballsOnTable.get(i));
        }

        turnWizardModel.setTurnEnd(data.getString(SIMPLE_DATA_KEY), data.getString(FOUL_KEY));
    }

    private void clearTable(ITableStatus tableStatus) {
        for (int ball = 1; ball <= tableStatus.size(); ++ball) {
            if (tableStatus.getBallStatus(ball) == BallStatus.MADE)
                tableStatus.setBallTo(BallStatus.ON_TABLE, ball);
        }
    }

    public void setTurnEnd(String turnEnd) {
        boolean notifyTree = !turnEnd.equals(getData().getString(SIMPLE_DATA_KEY));

        data.putString(SIMPLE_DATA_KEY, turnEnd);

        if (notifyTree)
            modelCallbacks.onPageTreeChanged();
        notifyDataChanged();
    }
}
