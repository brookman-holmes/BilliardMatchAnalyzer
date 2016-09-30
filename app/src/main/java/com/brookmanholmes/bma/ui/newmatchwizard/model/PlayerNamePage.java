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

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.brookmanholmes.bma.ui.newmatchwizard.fragments.PlayerNameFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

public class PlayerNamePage extends Page implements UpdatesMatchBuilder, UpdatesPlayerNames {
    public static final String PLAYER_NAME_KEY = "player name";
    public static final String OPPONENT_NAME_KEY = "opponent name";
    public static final String LOCATION_KEY = "location";
    public static final String EXTRA_INFO_KEY = "extras";
    public static final String PLAY_THE_GHOST_KEY = "ghost";

    private final String reviewPlayer;
    private final String reviewLocation;

    PlayerNamePage(ModelCallbacks callbacks, String title, String reviewPlayer, String reviewLocation) {
        super(callbacks, title);

        this.reviewLocation = reviewLocation;
        this.reviewPlayer = reviewPlayer;
        data.putString(PLAY_THE_GHOST_KEY, Boolean.FALSE.toString());
    }

    @Override
    public Fragment createFragment() {
        return PlayerNameFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(String.format(reviewPlayer, 1), getPlayerName(), getKey()));
        dest.add(new ReviewItem(String.format(reviewPlayer, 2), getOpponentName(), getKey()));
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
                Boolean.parseBoolean(data.getString(PLAY_THE_GHOST_KEY, "false")));
    }

    @Override
    public String getPlayerName() {
        return data.getString(PLAYER_NAME_KEY, "");
    }

    @Override
    public String getOpponentName() {
        return data.getString(OPPONENT_NAME_KEY, "");
    }

    public void setPlayTheGhost(boolean value) {
        if (value)
            data.putString(PLAY_THE_GHOST_KEY, Boolean.TRUE.toString());
        else
            data.putString(PLAY_THE_GHOST_KEY, Boolean.FALSE.toString());

        notifyDataChanged();
    }
}
