package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/11/2016.
 */
public class AdvStatsDialog extends DialogFragment {
    static final String ARG_MATCH_ID = "match id";
    static final String ARG_PLAYER_NAME = "player name";
    static final String ARG_PLAYER_TURN = "player turn";

    @Bind(R.id.pager) ViewPager pager;
    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private ViewPagerAdapter adapter;

    public AdvStatsDialog() {
    }

    public static AdvStatsDialog create(long matchId, String player, PlayerTurn playerTurn) {
        Bundle args = new Bundle();

        AdvStatsDialog dialog = new AdvStatsDialog();
        args.putString(ARG_PLAYER_NAME, player);
        args.putLong(ARG_MATCH_ID, matchId);
        args.putString(ARG_PLAYER_TURN, playerTurn.toString());

        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getString(ARG_PLAYER_TURN, "").equals(PlayerTurn.PLAYER.toString()))
            getDialog().getWindow().setWindowAnimations(R.style.SlideInFromLeftDialogTransitionTheme);
        else
            getDialog().getWindow().setWindowAnimations(R.style.SlideInFromRightDialogTransitionTheme);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adv_stats, container, false);
        ButterKnife.bind(this, view);

        toolbar.setTitle("Advanced Stats for " + getArguments().getString(ARG_PLAYER_NAME, "ERROR NO NAME PRESENT IN ARGUMENTS"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        backArrow.setTint(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return AdvSafetyStatsFragment.create(getArguments());
                case 2:
                    return AdvBreakingStatsFragment.create(getArguments());
                case 0:
                    return AdvShootingStatsFragment.create(getArguments());
                default:
                    throw new IllegalStateException("View pager out of position (0, 1, 2): " + position);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Shooting";
                case 1:
                    return "Safeties";
                default:
                    return "Breaks";
            }
        }
    }
}
