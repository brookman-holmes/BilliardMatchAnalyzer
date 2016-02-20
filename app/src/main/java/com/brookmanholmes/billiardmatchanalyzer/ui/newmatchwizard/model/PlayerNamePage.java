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

package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.fragments.PlayerNameFragment;

import java.util.ArrayList;

/**
 * A page asking for a name and an email.
 */
public class PlayerNamePage extends Page {
    public static final String PLAYER_NAME_KEY = "player name";
    public static final String OPPONENT_NAME_KEY = "opponent name";

    public PlayerNamePage(ModelCallbacks callbacks) {
        super(callbacks, "Players");
    }

    @Override
    public Fragment createFragment() {
        return PlayerNameFragment.create(getKey());
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Player 1", data.getString(PLAYER_NAME_KEY), getKey(), -1));
        dest.add(new ReviewItem("Player 2", data.getString(OPPONENT_NAME_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(data.getString(PLAYER_NAME_KEY)) && !TextUtils.isEmpty(data.getString(OPPONENT_NAME_KEY))
                && !TextUtils.equals(data.getString(PLAYER_NAME_KEY), data.getString(OPPONENT_NAME_KEY));
    }

    public String getPlayerName() {
        return data.getString(PLAYER_NAME_KEY, "error no player name");
    }

    public String getOpponentName() {
        return data.getString(OPPONENT_NAME_KEY, "error no opp name");
    }
}
