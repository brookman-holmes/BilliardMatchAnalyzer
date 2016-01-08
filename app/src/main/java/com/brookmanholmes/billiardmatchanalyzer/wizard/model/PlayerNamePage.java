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

package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PlayerNameFragment;

import java.util.ArrayList;

/**
 * A page asking for a name and an email.
 */
public class PlayerNamePage extends Page {
    public static final String PLAYER_NAME_KEY = "player name";
    public static final String OPPONENT_NAME_KEY = "opponent name";

    public PlayerNamePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return PlayerNameFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Player 1", mData.getString(PLAYER_NAME_KEY), getKey(), -1));
        dest.add(new ReviewItem("Player 2", mData.getString(OPPONENT_NAME_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(PLAYER_NAME_KEY)) && !TextUtils.isEmpty(mData.getString(OPPONENT_NAME_KEY));
    }

    @Override
    public void setPlayerNames(String player, String opponent) {

    }
}
