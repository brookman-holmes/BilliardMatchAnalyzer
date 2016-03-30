package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.AddTurnWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.StepPagerStrip;
import com.brookmanholmes.billiards.match.Match;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class AddTurnDialog extends BottomSheetFragment implements PageFragmentCallbacks, ModelCallbacks {
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.strip)
    StepPagerStrip pagerStrip;
    @Bind(R.id.next_button)
    Button nextButton;
    @Bind(R.id.prev_button)
    Button prevButton;
    @Bind(R.id.title)
    TextView title;

    private MyPagerAdapter pagerAdapter;
    private AddTurnWizardModel wizardModel;
    private List<Page> currentPageSequence;
    private boolean consumePageSelectedEvent;

    private AddTurnListener listener;

    public AddTurnDialog() {
    }

    public static AddTurnDialog create(Match<?> match) {
        Bundle args = new Bundle();

        args.putAll(MatchDialogHelperUtils.createBundleFromMatch(match));
        AddTurnDialog addTurnDialog = new AddTurnDialog();

        addTurnDialog.setArguments(args);
        return addTurnDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wizardModel = new AddTurnWizardModel(getContext(), getArguments());

        if (savedInstanceState != null) {
            wizardModel.load(savedInstanceState.getBundle("model"));
        }

        wizardModel.registerListener(this);

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());

        if (!(getActivity() instanceof AddTurnListener))
            throw new ClassCastException("Activity must implement AddTurnListener");

        listener = (AddTurnListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_turn, container, false);
        ButterKnife.bind(this, view);

        title.setText(getResources().getString(R.string.add_turn_for, getArguments().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY, "null")));

        pager.setAdapter(pagerAdapter);
        pagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(pagerAdapter.getCount(), position);
                if (pager.getCurrentItem() != position) {
                    pager.setCurrentItem(position);
                }
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

                updateBottomBar();
            }
        });

        onPageTreeChanged();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
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
    public void onPageTreeChanged() {
        currentPageSequence = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        pagerStrip.setPageCount(currentPageSequence.size());
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = currentPageSequence.size();
        for (int i = 0; i < currentPageSequence.size(); i++) {
            Page page = currentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i + 1;
                break;
            }
        }

        if (pagerAdapter.getCutOffPage() != cutOffPage) {
            pagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    @Override
    public Page onGetPage(String key) {
        return wizardModel.findByKey(key);
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem() + 1;
        if (position == currentPageSequence.size()) {
            nextButton.setText(R.string.add_turn);
        } else {
            nextButton.setText(R.string.next);
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
        }

        prevButton.setVisibility(position <= 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick(R.id.next_button)
    public void nextButton() {
        if (pager.getCurrentItem() + 1 == currentPageSequence.size()) {
            listener.addTurn(wizardModel.getTurnBuilder());
            dismiss();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
            wizardModel.updatePagesWithTurnInfo();
            Log.i("AddTurnDialog", wizardModel.getCurrentPageSequence().toString());
        }
    }

    @OnClick(R.id.prev_button)
    public void prevButton() {
        wizardModel.updatePagesWithTurnInfo();
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    @OnClick(R.id.imgHelp)
    public void help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(wizardModel.getCurrentPageSequence().get(pager.getCurrentItem()).getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();

    }

    public interface AddTurnListener {
        void addTurn(TurnBuilder turnBuilder);
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
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
}
