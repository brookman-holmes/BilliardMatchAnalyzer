package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.PlayerInfoAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.profile.PlayerInfoFragment;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvBreakingStatsFragment;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvSafetyStatsFragment;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvShootingStatsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 4/13/2016.
 */
public class PlayerProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String ARG_PLAYER_NAME = "arg player name";

    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.playerNameLayout) ViewGroup playerNameLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.pager) ViewPager pager;
    @Bind(R.id.tabs) TabLayout tabLayout;
    ViewPagerAdapter adapter;

    String player;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        player = getIntent().getExtras().getString(ARG_PLAYER_NAME);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_player_profile, player));

        playerNameLayout.setVisibility(View.GONE);
        playerName.setText(player);
        opponentName.setText(R.string.opponents);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        playerNameLayout.setVisibility((position == 1 ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchListFragment.create(player, null);
                case 1:
                    return PlayerInfoFragment.create(player);
                case 2:
                    return AdvShootingStatsFragment.create(player);
                case 3:
                    return AdvSafetyStatsFragment.create(player);
                case 4:
                    return AdvBreakingStatsFragment.create(player);
                default:
                    throw new IllegalStateException("View pager out of position (0 - 4): " + position);
            }
        }

        @Override public int getCount() {
            return 5;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Matches";
                case 1:
                    return "Stats";
                case 2:
                    return "Shooting";
                case 3:
                    return "Safeties";
                default:
                    return "Breaking";
            }
        }
    }
}
