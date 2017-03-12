package com.brookmanholmes.bma.ui.newmatchwizard.model;


import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.brookmanholmes.bma.ui.newmatchwizard.fragments.LocationFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 3/7/2017.
 */

public class LocationPage extends Page implements UpdatesMatchBuilder {
    public LocationPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return LocationFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if (!TextUtils.isEmpty(getLocation()))
            dest.add(new ReviewItem("Location", getLocation(), getKey()));
        if (!TextUtils.isEmpty(getNotes()))
            dest.add(new ReviewItem("Notes", getNotes(), getKey()));
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setLocation(getLocation());
        model.setNotes(getNotes());
    }

    private String getLocation() {
        return getData().getString("location", "");
    }

    private String getNotes() {
        return getData().getString("notes", "");
    }
}
