package com.brookmanholmes.bma.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.MatchListFragment;
import com.brookmanholmes.bma.ui.stats.AdvBreakingStatsFragment;
import com.brookmanholmes.bma.ui.stats.AdvSafetyStatsFragment;
import com.brookmanholmes.bma.ui.stats.AdvShootingStatsFragment;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    StatFilter filter = new StatFilter("All opponents");
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
        opponentName.setText(filter.getOpponent());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), player);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(this);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_profile, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            displayFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayFilterDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filter, null);
        final String opponent = filter.getOpponent();

        final Spinner gameSpinner = (Spinner) view.findViewById(R.id.gameTypeSpinner);
        final Spinner opponentSpinner = (Spinner) view.findViewById(R.id.opponentSpinner);
        final Spinner dateSpinner = (Spinner) view.findViewById(R.id.dateSpinner);

        gameSpinner.setAdapter(createAdapter(getGames()));
        opponentSpinner.setAdapter(createAdapter(getOpponents()));
        dateSpinner.setAdapter(createAdapter(Arrays.asList("All time", "Today", "Last week", "Last month", "Last 3 months", "Last 6 months")));

        //gameSpinner.setSelection(((ArrayAdapter<String>)gameSpinner.getAdapter()).getPosition(game));
        opponentSpinner.setSelection(((ArrayAdapter<String>) opponentSpinner.getAdapter()).getPosition(opponent));
        //dateSpinner.setSelection(((ArrayAdapter<String>)dateSpinner.getAdapter()).getPosition(date));

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialog.setTitle("Filter by")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        filter.setOpponent((String) opponentSpinner.getSelectedItem());
                        opponentName.setText(filter.getOpponent());
                    }
                })
                .create().show();
    }

    private List<String> getGames() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.games)));
        Collections.sort(list);
        list.add(0, "All games");

        return list;
    }

    private List<String> getOpponents() {
        ArrayList<String> list = new ArrayList<>(new DatabaseAdapter(this).getOpponentsOf(player));
        Collections.sort(list);
        list.add(0, "All opponents");

        return list;
    }

    private SpinnerAdapter createAdapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        playerNameLayout.setVisibility((position == 1 ? View.VISIBLE : View.GONE));

        Fragment fragment = adapter.getItem(pager.getCurrentItem());

        if (fragment instanceof Filterable) {
            ((Filterable) fragment).setFilter(filter);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        String player;

        public ViewPagerAdapter(FragmentManager fm, String player) {
            super(fm);
            this.player = player;
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlayerInfoGraphicFragment.create(player);
                case 1:
                    return PlayerInfoFragment.create(player);
                case 2:
                    return MatchListFragment.create(player, null);
                case 3:
                    return AdvShootingStatsFragment.create(player);
                case 4:
                    return AdvSafetyStatsFragment.create(player);
                case 5:
                    return AdvBreakingStatsFragment.create(player);
                default:
                    throw new IllegalStateException("View pager out of position (0 - 5): " + position);
            }
        }

        @Override public int getCount() {
            return 6;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return player;
                case 1:
                    return "Stats";
                case 2:
                    return "Matches";
                case 3:
                    return "Shooting";
                case 4:
                    return "Safeties";
                default:
                    return "Breaking";
            }
        }
    }
}
