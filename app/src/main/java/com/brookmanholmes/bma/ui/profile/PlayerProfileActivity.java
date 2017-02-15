package com.brookmanholmes.bma.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoFragment;
import com.brookmanholmes.bma.ui.newmatchwizard.CreateNewMatchActivity;
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
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 4/13/2016.
 */
public class PlayerProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String ARG_PLAYER_NAME = "arg_player_name";
    private static final String TAG = "PlayerProfileAct";
    private static final String ARG_FILTER_OPPONENT = "arg_filter_opponent";
    private static final String ARG_FILTER_GAME = "arg_filter_game";
    private static final String ARG_FILTER_DATE = "arg_filter_date";

    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.playerNameLayout)
    ViewGroup playerNameLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.createMatch)
    FloatingActionButton fab;

    private List<Filterable> listeners = new ArrayList<>();
    private StatFilter filter;
    private String player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        ButterKnife.bind(this);

        analytics.logEvent("viewed_profile", getIntent().getExtras());
        setupFilter(savedInstanceState);

        player = getIntent().getExtras().getString(ARG_PLAYER_NAME);
        setupToolbar();

        playerName.setText(player);
        opponentName.setText(filter.getOpponent());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                new String[]{player, getString(R.string.stats), getString(R.string.matches),
                        getString(R.string.title_shooting), getString(R.string.title_safeties),
                        getString(R.string.title_breaks)});
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pager.getCurrentItem() == ViewPagerAdapter.MATCH_LIST_FRAGMENT)
            fab.show();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_player_profile, player));
        }
    }

    private void setupFilter(Bundle savedInstanceState) {
        filter = new StatFilter("All opponents",
                getGames().toArray(new String[getGames().size()]),
                new String[]{"All time", "Today", "Last week", "Last month", "Last 3 months", "Last 6 months"});

        if (savedInstanceState != null) {
            filter.setDate(savedInstanceState.getString(ARG_FILTER_DATE));
            filter.setGameType(savedInstanceState.getString(ARG_FILTER_GAME));
            filter.setOpponent(savedInstanceState.getString(ARG_FILTER_OPPONENT));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            displayFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_FILTER_DATE, filter.getDate());
        outState.putString(ARG_FILTER_GAME, filter.getGameType());
        outState.putString(ARG_FILTER_OPPONENT, filter.getOpponent());
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.createMatch)
    public void createNewMatch() {
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                Intent intent = new Intent(PlayerProfileActivity.this, CreateNewMatchActivity.class);
                intent.putExtra(ARG_PLAYER_NAME, player);
                startActivity(intent);
            }
        });
    }

    private void displayFilterDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filter, null);

        final Spinner gameSpinner = (Spinner) view.findViewById(R.id.gameTypeSpinner);
        final Spinner opponentSpinner = (Spinner) view.findViewById(R.id.opponentSpinner);
        final Spinner dateSpinner = (Spinner) view.findViewById(R.id.dateSpinner);

        gameSpinner.setAdapter(createAdapter(getGames()));
        opponentSpinner.setAdapter(createAdapter(getOpponents()));
        dateSpinner.setAdapter(createAdapter(Arrays.asList("All time", "Today", "Last week", "Last month", "Last 3 months", "Last 6 months")));

        gameSpinner.setSelection(((ArrayAdapter<String>) gameSpinner.getAdapter()).getPosition(filter.getGameType()));
        opponentSpinner.setSelection(((ArrayAdapter<String>) opponentSpinner.getAdapter()).getPosition(filter.getOpponent()));
        dateSpinner.setSelection(((ArrayAdapter<String>) dateSpinner.getAdapter()).getPosition(filter.getDate()));

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialog.setTitle("Filter by")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter.setOpponent((String) opponentSpinner.getSelectedItem());
                        opponentName.setText(filter.getOpponent());
                        filter.setDate((String) dateSpinner.getSelectedItem());
                        filter.setGameType((String) gameSpinner.getSelectedItem());
                        updateListeners();
                    }
                })
                .create().show();
    }

    public void addListener(Filterable filterable) {
        listeners.add(filterable);
        updateListeners();
    }

    private void updateListeners() {
        for (Filterable filterable : listeners) {
            filterable.setFilter(filter);
        }
    }

    public void removeListener(Filterable filterable) {
        listeners.remove(filterable);
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

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (pager.getCurrentItem() == 1)
                    playerNameLayout.animate().translationY(0).start();
                else if (pager.getCurrentItem() == 2)
                    fab.show();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                fab.hide();
                playerNameLayout.animate().translationY(playerNameLayout.getHeight()).start();
                break;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final int INFO_GRAPHIC_FRAGMENT = 0;
        private static final int INFO_FRAGMENT = 1;
        private static final int MATCH_LIST_FRAGMENT = 2;
        private static final int ADV_SHOOTING_FRAGMENT = 3;
        private static final int ADV_SAFETY_FRAGMENT = 4;
        private static final int ADV_BREAKING_FRAGMENT = 5;
        private final String[] titles;

        ViewPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case INFO_GRAPHIC_FRAGMENT:
                    return PlayerInfoGraphicFragment.create(titles[0]);
                case INFO_FRAGMENT:
                    return MatchInfoFragment.create(titles[0]);
                case MATCH_LIST_FRAGMENT:
                    return MatchListFragment.create(titles[0], null);
                case ADV_SHOOTING_FRAGMENT:
                    return AdvShootingStatsFragment.create(titles[0]);
                case ADV_SAFETY_FRAGMENT:
                    return AdvSafetyStatsFragment.create(titles[0]);
                case ADV_BREAKING_FRAGMENT:
                    return AdvBreakingStatsFragment.create(titles[0]);
                default:
                    throw new IllegalStateException("View pager out of position (0 - 5): " + position);
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
