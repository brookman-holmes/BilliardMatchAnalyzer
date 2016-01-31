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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectBallsDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectBreakBallsDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectTurnEndDialog;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.StepPagerStrip;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.match.Match;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.BALLS_ON_TABLE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.NEW_GAME_KEY;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class AddInningFragment extends BottomSheetFragment {
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

    TableStatus tableStatus;
    SelectTurnEndDialog.TurnEndSelected turnEndSelected;

    public static AddInningFragment newInning(Match<?> match) {
        Bundle args = MatchDialogHelperUtils.createBundleFromMatch(match);

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
        tableStatus = TableStatus.newTable(GameType.valueOf(getArguments().getString(GAME_TYPE_KEY)),
                getArguments().getIntegerArrayList(BALLS_ON_TABLE_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inning, container, false);
        ButterKnife.bind(this, view);

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
                updateBottomBar();
            }
        });

        updateBottomBar();
        return view;
    }

    private void updateBottomBar() {
        if (pager.getCurrentItem() + 1 == pagerAdapter.getCount()) {
            nextButton.setText("Add inning");
            nextButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            nextButton.setTextAppearance(getContext(), R.style.TextAppearanceFinish);
        } else {
            nextButton.setText("Next");
            nextButton.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            nextButton.setTextAppearance(getContext(), R.style.TextAppearanceUnfinished);
        }

        prevButton.setVisibility(pager.getCurrentItem() <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    public void onEvent(SelectBreakBallsDialog.BreakStatus breakStatus) {
        this.tableStatus = breakStatus.tableStatus;
    }

    public void onEvent(SelectBallsDialog.BallStatus ballStatus) {
        this.tableStatus = ballStatus.tableStatus;
    }

    public void onEvent(SelectTurnEndDialog.TurnEndSelected turnEndSelected) {
        Log.i(TAG, "turnEndSelected called");
        this.turnEndSelected = turnEndSelected;
    }

    @OnClick(R.id.next_button)
    public void nextPage(TextView textView) {
        if (textView.getText().equals("Add inning"))
            EventBus.getDefault().post(new AddTurnToMatchInfo(tableStatus, turnEndSelected.turnEnd, turnEndSelected.scratch));
        else {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
            EventBus.getDefault().post(new Update(tableStatus));
        }
    }

    @OnClick(R.id.prev_button)
    public void previousPage() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    public static class Update {
        public final TableStatus tableStatus;

        public Update(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }
    }

    public static class AddTurnToMatchInfo {
        public final TableStatus tableStatus;
        public final TurnEnd turnEnd;
        public final boolean scratch;

        public AddTurnToMatchInfo(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch) {
            this.tableStatus = tableStatus;
            this.turnEnd = turnEnd;
            this.scratch = scratch;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (getArguments().getBoolean(NEW_GAME_KEY))
                switch (i) {
                    case 0:
                        return SelectBreakBallsDialog.create(getArguments());
                    case 1:
                        return SelectBallsDialog.create(getArguments());
                    case 2:
                        return SelectTurnEndDialog.create(getArguments());
                    default:
                        throw new IllegalStateException("Invalid position in pager adapter");
                }
            else
                switch (i) {
                    case 0:
                        return SelectBallsDialog.create(getArguments());
                    case 1:
                        return SelectTurnEndDialog.create(getArguments());
                    default:
                        throw new IllegalStateException("Invalid position in pager adapter");
                }
        }

        @Override
        public int getCount() {
            if (getArguments().getBoolean(NEW_GAME_KEY))
                return 3;
            else return 2;
        }
    }
}
