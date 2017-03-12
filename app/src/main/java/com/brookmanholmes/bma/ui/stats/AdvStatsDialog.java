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
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseDialogFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/11/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvStatsDialog extends BaseDialogFragment {
    static final String ARG_MATCH_ID = "match_id";
    static final String ARG_PLAYER_TURN = "player_turn";
    private static final String TAG = "AdvStatsDialog";
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    String playerName;
    PlayerTurn turn;
    List<ITurn> turns = new ArrayList<>();
    List<TurnListener> listeners = new ArrayList<>();

    public AdvStatsDialog() {
    }

    public static AdvStatsDialog create(Match match, PlayerTurn turn) {
        Bundle args = new Bundle();

        AdvStatsDialog dialog = new AdvStatsDialog();
        args.putSerializable("match", match);
        args.putSerializable(ARG_PLAYER_TURN, turn);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Match match = (Match) getArguments().getSerializable("match");
        turn = (PlayerTurn) getArguments().getSerializable(ARG_PLAYER_TURN);

        if (MatchDialogHelperUtils.isTablet(getContext()))
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogTheme);
        else
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyAppTheme);

        if (match == null)
            throw new IllegalStateException("Match must be passed into this dialog");

        playerName = match.getPlayer(turn).getName();
        turns.addAll(match.getPlayer(turn).getTurns());
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

        toolbar.setTitle(getString(R.string.title_advanced_stats, playerName));
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

    public void addAdvStatListener(TurnListener listener) {
        listeners.add(listener);
        updateListeners();
    }

    public void removeAdvStatListener(TurnListener listener) {
        listeners.remove(listener);
    }

    public void updateListeners() {
        for (TurnListener listener : listeners) {
            listener.setAdvStats(turns);
        }
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
                    return new AdvShootingStatsFragment();
                case 1:
                    return new AdvSafetyStatsFragment();
                case 2:
                    return new AdvBreakingStatsFragment();
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
