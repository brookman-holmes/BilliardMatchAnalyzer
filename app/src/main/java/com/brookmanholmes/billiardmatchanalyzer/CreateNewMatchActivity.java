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

package com.brookmanholmes.billiardmatchanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.ReviewFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.StepPagerStrip;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

import java.util.List;

public class CreateNewMatchActivity extends FragmentActivity implements
        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel = new CreateNewMatchWizardModel(this);

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_match);

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    // // TODO: 1/12/2016 insert match and go to MatchInfoActivity
                    if (mPagerAdapter.getPrimaryItem() instanceof ReviewFragment) {
                        createMatchAndLaunchMatchInfoActivity();
                    }
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    private void createMatchAndLaunchMatchInfoActivity() {
        Match match = MatchCreationHelper.createMatch(((ReviewFragment) mPagerAdapter.getPrimaryItem()).getCurrentReviewItems());
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

        long matchId = databaseAdapter.insertMatch(match);

        Intent intent = new Intent(this, MatchInfoActivity.class);
        intent.putExtra("matchId", matchId);

        startActivity(intent);
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText("Create match");
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? "Create match"
                    : "Next");
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    private static class MatchCreationHelper {
        private MatchCreationHelper() {

        }

        public static Match createMatch(List<ReviewItem> reviewItems) {
            GameType gameType = getGameType(reviewItems);
            PlayerTurn playerTurn = getPlayerTurn(reviewItems);
            BreakType breakType = getBreakType(reviewItems);
            String opponentName = getOpponentName(reviewItems);
            String playerName = getPlayerName(reviewItems);
            int playerRank = getPlayerRank(reviewItems);
            int opponentRank = getOpponentRank(reviewItems);

            Log.i("MatchCreationHelper", "Game: " + gameType.toString() + " Turn: " + playerTurn.toString() + " Break Type: " + breakType.toString() + " Player names: " + playerName + " and " + opponentName +
                    " Player rank: " + playerRank + " Opponent Rank: " + opponentRank);

            return new Match.Builder(playerName, opponentName).setBreakType(breakType).setPlayerTurn(playerTurn).setPlayerRanks(playerRank, opponentRank).build(gameType);
        }

        private static GameType getGameType(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals("Game")) {
                    switch (item.getDisplayValue()) {
                        case "APA 8 ball":
                            return GameType.APA_EIGHT_BALL;
                        case "APA 9 ball":
                            return GameType.APA_NINE_BALL;
                        case "BCA 8 ball":
                            return GameType.BCA_EIGHT_BALL;
                        case "BCA 9 ball":
                            return GameType.BCA_NINE_BALL;
                        case "BCA 10 ball":
                            return GameType.BCA_TEN_BALL;
                        case "Straight pool":
                            return GameType.STRAIGHT_POOL;
                        case "American Rotation":
                            return GameType.AMERICAN_ROTATION;
                        default:
                            throw new InvalidGameTypeException(item.getDisplayValue());
                    }
                }
            }

            throw new InvalidGameTypeException("No review item with the title 'Game'");
        }

        private static PlayerTurn getPlayerTurn(List<ReviewItem> reviewItems) {
            String playerName = getPlayerName(reviewItems);
            String opponentName = getOpponentName(reviewItems);

            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals("Who breaks first?")) {
                    if (item.getDisplayValue().equals(playerName))
                        return PlayerTurn.PLAYER;
                    else if (item.getDisplayValue().equals(opponentName))
                        return PlayerTurn.OPPONENT;
                    else
                        throw new IllegalArgumentException("Incorrect player name, was: " + item.getDisplayValue() + " but should be: " + playerName + " or " + opponentName);
                }
            }

            throw new IllegalArgumentException("No review item with the title 'Who breaks first?");
        }

        private static String getPlayerName(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals("Player 1")) {
                    return item.getDisplayValue();
                }
            }

            throw new IllegalArgumentException("No review item with the title 'Player 1'");
        }

        private static String getOpponentName(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals("Player 2")) {
                    return item.getDisplayValue();
                }
            }

            throw new IllegalArgumentException("No review item with the title 'Player 2'");
        }

        private static BreakType getBreakType(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals("The break")) {
                    switch (item.getDisplayValue()) {
                        case "Winner":
                            return BreakType.WINNER;
                        case "Alternate":
                            return BreakType.ALTERNATE;
                        case "Loser":
                            return BreakType.LOSER;
                        default:
                            if (item.getDisplayValue().startsWith(getPlayerName(reviewItems)))
                                return BreakType.PLAYER;
                            else if (item.getDisplayValue().startsWith(getOpponentName(reviewItems)))
                                return BreakType.OPPONENT;
                    }
                }
            }

            throw new IllegalArgumentException("No review item with the title 'The break', or the player name does not exist");
        }

        private static int getPlayerRank(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals(getPlayerName(reviewItems) + "'s Rank"))
                    return Integer.valueOf(item.getDisplayValue().substring(0, 1));
            }

            return 0;
        }

        private static int getOpponentRank(List<ReviewItem> reviewItems) {
            for (ReviewItem item : reviewItems) {
                if (item.getTitle().equals(getOpponentName(reviewItems) + "'s Rank"))
                    return Integer.valueOf(item.getDisplayValue().substring(0, 1));
            }

            return 0;
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
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
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

        public Fragment getPrimaryItem() {
            return mPrimaryItem;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
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