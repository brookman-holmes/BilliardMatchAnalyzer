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
    public static final String PLAYER_NAME_KEY = "player_name";
    public static final String OPPONENT_ID_KEY = "opponent_name";
    public static final String OPPONENT_NAME_KEY = "opponent_id";
    public static final String PLAY_THE_GHOST_KEY = "play_ghost_key";
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
        data.putString(SIMPLE_DATA_KEY, Boolean.TRUE.toString());
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
            dest.add(new ReviewItem(reviewTitleAlt, name, getKey()));
        else
            dest.add(new ReviewItem(reviewTitle, String.format(reviewPlayer, name, getOpponentName()), getKey()));
        if (!data.getString(LOCATION_KEY, "").equals(""))
            dest.add(new ReviewItem(reviewLocation, data.getString(LOCATION_KEY), getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(getOpponentName());
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setPlayerName(data.getString(OPPONENT_ID_KEY),
                data.getString(OPPONENT_NAME_KEY, ""),
                Boolean.parseBoolean(data.getString(SIMPLE_DATA_KEY, "true")));
    }

    @Override
    public String getOpponentName() {
        return data.getString(OPPONENT_NAME_KEY, "");
    }

    @Override
    public void notifyDataChanged() {
        super.notifyDataChanged();
    }

    private boolean isPlayTheGhost() {
        return Boolean.TRUE.toString().equals(data.getString(SIMPLE_DATA_KEY));
    }
}
