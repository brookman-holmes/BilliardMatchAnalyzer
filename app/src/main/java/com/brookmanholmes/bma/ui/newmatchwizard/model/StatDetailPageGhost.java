package com.brookmanholmes.bma.ui.newmatchwizard.model;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 3/9/2016.
 */
class StatDetailPageGhost extends SingleFixedChoicePage implements UpdatesMatchBuilder {
    StatDetailPageGhost(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Data collection", data.getString(SIMPLE_DATA_KEY), getKey()));
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setStatDetail(data.getString(SIMPLE_DATA_KEY));
    }
}
