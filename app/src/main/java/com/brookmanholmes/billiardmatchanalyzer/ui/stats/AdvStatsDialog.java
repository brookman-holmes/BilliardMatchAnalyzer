package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/11/2016.
 */
public class AdvStatsDialog extends DialogFragment {
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ViewPagerAdapter adapter;

    public AdvStatsDialog() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: 3/11/2016 set transition to slide in from the left if it's PLAYER's turn and slide in from right if it's OPPONENT's turn
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogTransitionTheme);
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

        toolbar.setTitle("Advanced Stats for Brookman");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        return view;
    }

    private static class AdvStats {
        private static Map<String, Integer> map = new HashMap<>();

        private static void func() {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {

            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new AdvSafetyStats();
                case 2:
                    return new AdvBreakingStats();
                case 0:
                    return new AdvShootingStats();
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
