package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.ui.addturnwizard.fragments.CueBallFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 9/26/2016.
 */

public class CueBallPage extends Page implements UpdatesTurnInfo {
    public static final String CB_DISTANCE_KEY = "cb_dist";
    public static final String OB_DISTANCE_KEY = "ob_dist";
    public static final String SPEED_KEY = "speed";
    public static final String CB_X_KEY = "x_key";
    public static final String CB_Y_KEY = "y_key";
    private static final String TAG = "CueBallPage";

    CueBallPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return CueBallFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setObDistance(data.getFloat(OB_DISTANCE_KEY));
        model.setCbDistance(data.getFloat(CB_DISTANCE_KEY));
        model.setSpeed(data.getInt(SPEED_KEY));
        model.setCueing(data.getInt(CB_X_KEY), data.getInt(CB_Y_KEY));
    }
}
