package com.brookmanholmes.bma.ui.stats;

import android.content.Context;
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

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseDialogFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/11/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvStatsDialog extends BaseDialogFragment {
    static final String ARG_MATCH_ID = "match id";
    static final String ARG_PLAYER_NAME = "player name";
    static final String ARG_PLAYER_TURN = "player turn";

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    PlayerTurn turn;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MatchDialogHelperUtils.isTablet(getContext()))
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogTheme);
        else
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyAppTheme);

        turn = PlayerTurn.valueOf(getArguments().getString(ARG_PLAYER_TURN));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (turn == PlayerTurn.PLAYER)
            getDialog().getWindow().setWindowAnimations(R.style.SlideInFromLeftDialogTransitionTheme);
        else
            getDialog().getWindow().setWindowAnimations(R.style.SlideInFromRightDialogTransitionTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adv_stats, container, false);
        ButterKnife.bind(this, view);

        toolbar.setTitle(getString(R.string.title_advanced_stats, getArguments().getString(ARG_PLAYER_NAME, "ERROR NO NAME PRESENT IN ARGUMENTS")));
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getArguments(), getContext());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        return view;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        final String shooting;
        final String safeties;
        final String breaks;
        final Bundle args;

        public ViewPagerAdapter(FragmentManager fm, Bundle args, Context context) {
            super(fm);
            shooting = context.getString(R.string.title_shooting);
            safeties = context.getString(R.string.title_safeties);
            breaks = context.getString(R.string.title_breaks);
            this.args = args;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AdvShootingStatsFragment.create(args);
                case 1:
                    return AdvSafetyStatsFragment.create(args);
                case 2:
                    return AdvBreakingStatsFragment.create(args);
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
                    return shooting;
                case 1:
                    return safeties;
                default:
                    return breaks;
            }
        }
    }
}
