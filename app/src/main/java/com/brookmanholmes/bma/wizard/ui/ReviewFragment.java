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

package com.brookmanholmes.bma.wizard.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.billiards.player.Players;
import com.brookmanholmes.billiards.player.RaceTo;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReviewFragment extends ListFragment implements ModelCallbacks {
    private Callbacks callbacks;
    private AbstractWizardModel wizardModel;
    private List<ReviewItem> reviewItems;

    private ReviewAdapter reviewAdapter;

    public ReviewFragment() {
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewAdapter = new ReviewAdapter();
    }


    // // TODO: 5/21/2016 Use Context to setup this page
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        wizardModel = callbacks.onGetModel();
        wizardModel.registerListener(this);
        onPageTreeChanged();

        View rootView = inflater.inflate(R.layout.fragment_page, container, false);

        TextView titleView = (TextView) rootView.findViewById(android.R.id.title);
        titleView.setText(R.string.title_review);
        titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(reviewAdapter);
        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        callbacks = (Callbacks) getActivity();
    }

    @Override public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;

        wizardModel.unregisterListener(this);
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        for (Page page : wizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }

        if (!wizardModel.getCurrentPageSequence().get(0).getData().getString(PlayerNamePage.EXTRA_INFO_KEY, "").equals("")) {
            Page page = wizardModel.getCurrentPageSequence().get(0);
            reviewItems.add(new ReviewItem("Match notes", page.getData().getString(PlayerNamePage.EXTRA_INFO_KEY), page.getKey()));
        }
        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            @Override public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
        this.reviewItems = reviewItems;

        for (ReviewItem item : this.reviewItems) {
            if (item.getDisplayValue().equals("APA 8 ball")) {
                int playerRank = getPlayerRank();
                int opponentRank = getOpponentRank();

                RaceTo raceTo = Players.apa8BallRaceTo(playerRank, opponentRank);

                setPlayerRaceToReviewItem(raceTo.getPlayerRaceTo());
                setOpponentRaceToReviewItem(raceTo.getOpponentRaceTo());
            }
        }

        if (reviewAdapter != null) {
            reviewAdapter.notifyDataSetInvalidated();
        }
    }

    private void setPlayerRaceToReviewItem(int games) {
        for (ReviewItem item : reviewItems) {
            if (item.getTitle().equals(getPlayerName() + "'s Rank")) {
                item.setDisplayValue(item.getDisplayValue() + ", wins with " + games + " games");
            }
        }
    }

    private void setOpponentRaceToReviewItem(int games) {
        for (ReviewItem item : reviewItems) {
            if (item.getTitle().equals(getOpponentName() + "'s Rank")) {
                item.setDisplayValue(item.getDisplayValue() + ", wins with " + games + " games");
            }
        }
    }

    private int getPlayerRank() {
        int rank = 0;
        for (ReviewItem item : reviewItems) {
            if (item.getTitle().equals(getPlayerName() + "'s Rank")) {
                rank = Integer.valueOf(item.getDisplayValue());
            }
        }

        return rank;
    }

    private int getOpponentRank() {
        int rank = 0;
        for (ReviewItem item : reviewItems) {
            if (item.getTitle().equals(getOpponentName() + "'s Rank")) {
                rank = Integer.valueOf(item.getDisplayValue());
            }
        }

        return rank;
    }

    private String getPlayerName() {
        return reviewItems.get(0).getDisplayValue();
    }

    private String getOpponentName() {
        return reviewItems.get(1).getDisplayValue();
    }

    public List<ReviewItem> getCurrentReviewItems() {
        return reviewItems;
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        callbacks.onEditScreenAfterReview(reviewItems.get(position).getPageKey());
    }

    public interface Callbacks {
        AbstractWizardModel onGetModel();

        void onEditScreenAfterReview(String pageKey);
    }

    private class ReviewAdapter extends BaseAdapter {
        @Override public boolean hasStableIds() {
            return true;
        }

        @Override public int getItemViewType(int position) {
            return 0;
        }

        @Override public int getViewTypeCount() {
            return 1;
        }

        @Override public boolean areAllItemsEnabled() {
            return true;
        }

        @Override public Object getItem(int position) {
            return reviewItems.get(position);
        }

        @Override public long getItemId(int position) {
            return reviewItems.get(position).hashCode();
        }

        @Override public View getView(int position, View view, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.list_item_review, container, false);

            ReviewItem reviewItem = reviewItems.get(position);
            String value = reviewItem.getDisplayValue();
            if (TextUtils.isEmpty(value)) {
                value = "(None)";
            }
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(reviewItem.getTitle());
            ((TextView) rootView.findViewById(android.R.id.text2)).setText(value);
            return rootView;
        }

        @Override public int getCount() {
            return reviewItems.size();
        }
    }
}
