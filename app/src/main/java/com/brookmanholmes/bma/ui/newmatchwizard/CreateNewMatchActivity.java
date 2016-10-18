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

package com.brookmanholmes.bma.ui.newmatchwizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoActivity;
import com.brookmanholmes.bma.ui.newmatchwizard.model.CreateNewMatchWizardModel;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.bma.wizard.ui.ReviewFragment;
import com.brookmanholmes.bma.wizard.ui.StepPagerStrip;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateNewMatchActivity extends BaseActivity implements
        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {
    private static final String TAG = "CreateNewMatchAct";
    public static final String PLAYER_EXTRA = "player";

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.next_button)
    Button nextButton;
    @Bind(R.id.prev_button)
    Button prevButton;
    @Bind(R.id.strip)
    StepPagerStrip pagerStrip;

    private MyPagerAdapter pagerAdapter;
    private boolean editingAfterReview;
    private CreateNewMatchWizardModel wizardModel;
    private boolean consumePageSelectedEvent;
    private List<Page> currentPageSequence;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_match);

        analytics.logEvent("create_match", null);
        wizardModel = new CreateNewMatchWizardModel(this);

        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            wizardModel.load(savedInstanceState.getBundle("model"));
        }

        wizardModel.registerListener(this);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(pagerAdapter.getCount() - 1, position);
                if (pager.getCurrentItem() != position)
                    pager.setCurrentItem(position);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pagerStrip.setCurrentPage(position);

                if (consumePageSelectedEvent) {
                    consumePageSelectedEvent = false;
                    return;
                }

                editingAfterReview = false;
                updateBottomBar();
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBundle("model", wizardModel.save());
    }

    private void createMatchAndLaunchMatchInfoActivity() {
        Match match = wizardModel.createMatch();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        long matchId = databaseAdapter.insertMatch(match);
        Intent intent = new Intent(this, MatchInfoActivity.class);
        intent.putExtra(ARG_MATCH_ID, matchId);

        startActivity(intent);
        analytics.logEvent("match_created", MatchDialogHelperUtils.getBundle(match));
        finish();
    }

    @Override
    public void onPageTreeChanged() {
        currentPageSequence = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        pagerStrip.setPageCount(currentPageSequence.size() + 1); // + 1 = review step
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem();
        if (position == currentPageSequence.size()) {
            nextButton.setText(R.string.create_match);
        } else {
            nextButton.setText(editingAfterReview
                    ? R.string.create_match
                    : R.string.next);
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
        }

        prevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", wizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return wizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = currentPageSequence.size() - 1; i >= 0; i--) {
            if (currentPageSequence.get(i).getKey().equals(key)) {
                consumePageSelectedEvent = true;
                editingAfterReview = true;
                pager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                pagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return wizardModel.findByKey(key);
    }

    @OnClick(R.id.next_button)
    public void nextPage() {
        if (pager.getCurrentItem() == currentPageSequence.size()) {
            if (pagerAdapter.getPrimaryItem() instanceof ReviewFragment) {
                createMatchAndLaunchMatchInfoActivity();
            }
        } else {
            if (editingAfterReview) {
                pager.setCurrentItem(pagerAdapter.getCount() - 1);
            } else {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        }
    }

    @OnClick(R.id.prev_button)
    public void prevPage() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
        showKeyboard();
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = currentPageSequence.size() + 1;
        for (int i = 0; i < currentPageSequence.size(); i++) {
            Page page = currentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (pagerAdapter.getCutOffPage() != cutOffPage) {
            pagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public void showKeyboard() {
        if (pager.getCurrentItem() == 0) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= currentPageSequence.size()) {
                return new ReviewFragment();
            }

            return currentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        public Fragment getPrimaryItem() {
            return mPrimaryItem;
        }

        @Override
        public int getCount() {
            if (currentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, currentPageSequence.size() + 1);
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }
    }
}