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
package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectBreakBallsDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectTurnEndDialog;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PageList;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SelectBallsPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SelectBreakBallsPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.TurnEndPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.StepPagerStrip;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.match.Match;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.BALLS_ON_TABLE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.NEW_GAME_KEY;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class AddInningFragment extends BottomSheetFragment implements ModelCallbacks {
    private static final String TAG = "AddInningFragment";
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.next_button)
    Button nextButton;
    @Bind(R.id.prev_button)
    Button prevButton;
    @Bind(R.id.strip)
    StepPagerStrip stepPagerStrip;
    MyPagerAdapter pagerAdapter;
    AbstractWizardModel wizardModel;
    boolean consumePageSelectedEvent;
    List<Page> currentPageSequence;

    TableStatus tableStatus;

    public static AddInningFragment newInning(Match<?> match) {
        Bundle args = MatchHelperUtils.createBundleFromMatch(match);

        AddInningFragment fragment = new AddInningFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isNewGame = getArguments().getBoolean(NEW_GAME_KEY, false);

        wizardModel = getModel(isNewGame);

        if (savedInstanceState != null) {
            wizardModel.load(savedInstanceState.getBundle("model"));
        }
        wizardModel.registerListener(this);

        tableStatus = TableStatus.newTable(GameType.valueOf(getArguments().getString(GAME_TYPE_KEY)),
                getArguments().getIntegerArrayList(BALLS_ON_TABLE_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inning, container, false);
        ButterKnife.bind(this, view);

        wizardModel.registerListener(this);

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        stepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(pagerAdapter.getCount() - 1, position);
                if (pager.getCurrentItem() != position) {
                    pager.setCurrentItem(position);
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                stepPagerStrip.setCurrentPage(position);

                if (consumePageSelectedEvent) {
                    consumePageSelectedEvent = false;
                    return;
                }

                updateBottomBar();
            }
        });

        onPageTreeChanged();
        updateBottomBar();
        return view;
    }

    @Override
    public void onPageTreeChanged() {
        currentPageSequence = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        stepPagerStrip.setPageCount(currentPageSequence.size());
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem() + 1;
        if (position == currentPageSequence.size()) {
            nextButton.setText("Add inning");
            nextButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            nextButton.setTextAppearance(getContext(), R.style.TextAppearanceFinish);
        } else {
            nextButton.setText("Next");
            nextButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            nextButton.setTextAppearance(getContext(), R.style.TextAppearanceUnfinished);
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
        }

        prevButton.setVisibility(position <= 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wizardModel.unregisterListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", wizardModel.save());
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

    public void onEvent(SelectBreakBallsDialog.BreakStatus update) {
        Log.i(TAG, "SelectBreakBallsDialog.BreakStatus called in " + TAG);
    }

    public void onEvent(SelectTurnEndDialog.TurnEndSelected turnEndSelected) {
        Log.i(TAG, "SelectTurnEndDialog.TurnEndSelected called in " + TAG);
        EventBus.getDefault().post(new Update(tableStatus));
    }

    @OnClick(R.id.next_button)
    public void nextPage(TextView textView) {
        if (textView.getText().equals("Add inning"))
            EventBus.getDefault().post(this);

        pager.setCurrentItem(pager.getCurrentItem() + 1);

    }

    @OnClick(R.id.prev_button)
    public void previousPage() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    private AbstractWizardModel getModel(boolean isNewGame) {
        if (isNewGame)
            return new AddInningWizardWithBreak(getContext());
        else return new AddInningWizard(getContext());
    }

    public static class Update {
        public final TableStatus tableStatus;

        public Update(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return currentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
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

        @Override
        public int getCount() {
            if (currentPageSequence == null) {
                return 0;
            }

            return Math.min(mCutOffPage, currentPageSequence.size());
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

    public class AddInningWizardWithBreak extends AbstractWizardModel {
        public AddInningWizardWithBreak(Context context) {
            super(context);

        }

        @Override
        protected PageList onNewRootPageList() {
            return new PageList(
                    new SelectBreakBallsPage(this, getArguments()),
                    new SelectBallsPage(this, getArguments()),
                    new TurnEndPage(this, getArguments()));

        }
    }

    public class AddInningWizard extends AbstractWizardModel {
        public AddInningWizard(Context context) {
            super(context);
        }

        @Override
        protected PageList onNewRootPageList() {
            return new PageList(
                    new SelectBallsPage(this, getArguments()),
                    new TurnEndPage(this, getArguments()));
        }
    }
}
