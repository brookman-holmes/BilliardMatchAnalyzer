/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.PlayerNameFragment;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

public class PlayerNamePage extends BranchPage implements UpdatesMatchBuilder, UpdatesPlayerNames {
    public static final String PLAYER_NAME_KEY = "player name";
    public static final String OPPONENT_NAME_KEY = "opponent name";
    public static final String LOCATION_KEY = "location";
    public static final String EXTRA_INFO_KEY = "extras";
    private static final String TAG = "PlayerNamePage";
    private final String reviewTitle;
    private final String reviewTitleAlt;
    private final String reviewPlayer;
    private final String reviewLocation;
    private String name;

    PlayerNamePage(ModelCallbacks callbacks, String title, Context context, String parentKey, String name) {
        super(callbacks, title);

        this.reviewLocation = context.getString(R.string.location);
        this.reviewPlayer = context.getString(R.string.and);
        reviewTitle = context.getString(R.string.players);
        reviewTitleAlt = context.getString(R.string.player_name);
        data.putString(SIMPLE_DATA_KEY, Boolean.FALSE.toString());
        this.parentKey = parentKey;
        this.name = name;
    }

    @Override
    public Fragment createFragment() {
        return PlayerNameFragment.create(getKey(), name);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        if (isPlayTheGhost())
            dest.add(new ReviewItem(reviewTitleAlt, getPlayerName(), getKey()));
        else
            dest.add(new ReviewItem(reviewTitle, String.format(reviewPlayer, getPlayerName(), getOpponentName()), getKey()));
        if (!data.getString(LOCATION_KEY, "").equals(""))
            dest.add(new ReviewItem(reviewLocation, data.getString(LOCATION_KEY), getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(getPlayerName()) && !TextUtils.isEmpty(getOpponentName())
                && !TextUtils.equals(getPlayerName(), getOpponentName());
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setPlayerName(getPlayerName(),
                getOpponentName(),
                data.getString(LOCATION_KEY, ""),
                data.getString(EXTRA_INFO_KEY, ""),
                Boolean.parseBoolean(data.getString(SIMPLE_DATA_KEY, "false")));
    }

    @Override
    public String getPlayerName() {
        return data.getString(PLAYER_NAME_KEY, "");
    }

    @Override
    public String getOpponentName() {
        return data.getString(OPPONENT_NAME_KEY, "");
    }

    @Override
    public void notifyDataChanged() {
        modelCallbacks.onPageDataChanged(this);
    }

    private boolean isPlayTheGhost() {
        return Boolean.TRUE.toString().equals(data.getString(SIMPLE_DATA_KEY));
    }

    public void setPlayTheGhost(boolean value) {
        boolean notifyTree = value != isPlayTheGhost();

        data.putString(SIMPLE_DATA_KEY, Boolean.valueOf(value).toString());

        if (notifyTree) // this guards against recursive entry executePendingTransactions
            modelCallbacks.onPageTreeChanged();
        notifyDataChanged();
    }
}
