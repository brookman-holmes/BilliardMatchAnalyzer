package com.brookmanholmes.bma.ui.profile;

import android.content.Context;
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

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.MatchModel;
import com.brookmanholmes.bma.data.TurnModel;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.MatchListFragment;
import com.brookmanholmes.bma.ui.SignInActivity;
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoFragment;
import com.brookmanholmes.bma.ui.matchinfo.MatchListener;
import com.brookmanholmes.bma.ui.matchinfo.PlayersListener;
import com.brookmanholmes.bma.ui.newmatchwizard.CreateNewMatchActivity;
import com.brookmanholmes.bma.ui.stats.AdvBreakingStatsFragment;
import com.brookmanholmes.bma.ui.stats.AdvSafetyStatsFragment;
import com.brookmanholmes.bma.ui.stats.AdvShootingStatsFragment;
import com.brookmanholmes.bma.ui.stats.StatFilter;
import com.brookmanholmes.bma.ui.stats.TurnListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 4/13/2016.
 */
public class PlayerProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String ARG_ACCOUNT_ID = "arg_player_id";
    public static final String ARG_ACCOUNT_NAME = "arg_player_name";
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
    DatabaseReference matchRef;
    private List<TurnListener> advStatsListeners = new ArrayList<>();
    private List<PlayersListener> statListeners = new ArrayList<>();
    private List<MatchListener> matchListeners = new ArrayList<>();
    private StatFilter filter;
    private Map<String, List<String>> matchTurnIds = new HashMap<>();
    private Map<String, Match> matches = new HashMap<>();
    private final ChildEventListener turnChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            matches.get(dataSnapshot.getRef().getParent().getKey()).addTurn(TurnModel.getTurn(dataSnapshot.getValue(TurnModel.class)));
            matchTurnIds.get(dataSnapshot.getRef().getParent().getKey()).add(dataSnapshot.getKey());
            updateStats();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            matches.get(dataSnapshot.getRef().getParent().getKey()).undoTurn();
            matchTurnIds.get(dataSnapshot.getRef().getParent().getKey()).remove(dataSnapshot.getKey());
            updateStats();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private final ChildEventListener matchChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FirebaseDatabase.getInstance().getReference()
                    .child("matches")
                    .child(dataSnapshot.getKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MatchModel model = dataSnapshot.getValue(MatchModel.class);
                            matches.put(dataSnapshot.getKey(), MatchModel.createMatch(model));
                            matchTurnIds.put(dataSnapshot.getKey(), new ArrayList<String>());
                            FirebaseDatabase.getInstance().getReference()
                                    .child("turns")
                                    .child(dataSnapshot.getKey())
                                    .addChildEventListener(turnChildEventListener);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FirebaseDatabase.getInstance().getReference().child("turns").child(dataSnapshot.getKey()).removeEventListener(turnChildEventListener);
            matches.remove(dataSnapshot.getKey());
            matchTurnIds.remove(dataSnapshot.getKey());
            updateStats();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private String id, name;

    public static Intent newInstance(Context context, String id, String name) {
        Intent intent = new Intent(context, PlayerProfileActivity.class);
        intent.putExtra(ARG_ACCOUNT_ID, id);
        intent.putExtra(ARG_ACCOUNT_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        ButterKnife.bind(this);

        if (auth.getCurrentUser() != null) {
            id = auth.getCurrentUser().getUid();
            name = auth.getCurrentUser().getDisplayName();
            matchRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(id)
                    .child("matches");
            analytics.logEvent("viewed_profile", getIntent().getExtras());
            setupFilter(savedInstanceState);

            playerName.setText(name);
            opponentName.setText(filter.getOpponent());
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                    new String[]{name, getString(R.string.stats), getString(R.string.matches),
                            getString(R.string.title_shooting), getString(R.string.title_safeties),
                            getString(R.string.title_breaks)});
            pager.setAdapter(adapter);
            tabLayout.setupWithViewPager(pager);
            pager.addOnPageChangeListener(this);
            pager.setOffscreenPageLimit(3);
            setupToolbar();
        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        matchRef.addChildEventListener(matchChildEventListener);
        fab.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        matchRef.removeEventListener(matchChildEventListener);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_player_profile, auth.getCurrentUser().getDisplayName()));
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
                startActivity(new Intent(PlayerProfileActivity.this, CreateNewMatchActivity.class));
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
                        updateStats();
                    }
                })
                .create().show();
    }

    public void addAdvStatListener(TurnListener listener) {
        advStatsListeners.add(listener);
    }

    public void removeAdvStatListener(TurnListener listener) {
        advStatsListeners.remove(listener);
    }

    public void addMatchListener(MatchListener listener) {
        matchListeners.add(listener);
        updateStats();
    }

    public void removeMatchListener(MatchListener listener) {
        matchListeners.remove(listener);
    }

    public void addStatListener(PlayersListener listener) {
        statListeners.add(listener);
        updateStats();
    }

    private void updateStats() {
        List<Player> players = new ArrayList<>(), opponents = new ArrayList<>();
        List<ITurn> turns = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        for (Match match : this.matches.values()) {
            if (filter.isMatchQualified(match)) {
                matches.add(match);
                if (match.getPlayer().getId().equals(auth.getCurrentUser().getUid())) {
                    turns.addAll(match.getPlayer().getTurns());
                    players.add(match.getPlayer());
                    opponents.add(match.getOpponent());
                } else {
                    turns.addAll(match.getOpponent().getTurns());
                    players.add(match.getOpponent());
                    opponents.add(match.getPlayer());
                }
            }
        }

        // sort the players by date, oldest first
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getMatchDate().compareTo(o2.getMatchDate());
            }
        });

        for (PlayersListener listener : statListeners)
            listener.updatePlayers(players, opponents);

        for (TurnListener listener : advStatsListeners) {
            listener.setAdvStats(turns);
        }

        for (MatchListener listener : matchListeners) {
            listener.update(matches, matchTurnIds);
        }
    }

    public void removeStatListener(PlayersListener updateStats) {
        statListeners.remove(updateStats);
    }

    private List<String> getGames() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.games)));
        list.add(0, "All games");

        return list;
    }

    private List<String> getOpponents() {
        List<String> list = new ArrayList<>();

        for (Match match : matches.values()) {
            if (!match.getPlayer().getId().equals(id))
                list.add(match.getPlayer().getName());
            if (!match.getOpponent().getId().equals(id))
                list.add(match.getOpponent().getName());
        }

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
                if (pager.getCurrentItem() == 1) {
                    playerNameLayout.animate().translationY(0).start();
                    fab.animate().translationY(-playerNameLayout.getHeight()).start();
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                playerNameLayout.animate().translationY(playerNameLayout.getHeight()).start();
                fab.animate().translationY(0).start();
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
        private String id;

        ViewPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
                id = user.getUid();
            else throw new IllegalStateException("User must be signed in");
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case INFO_GRAPHIC_FRAGMENT:
                    return new PlayerInfoGraphicFragment();
                case INFO_FRAGMENT:
                    return MatchInfoFragment.newInstance(GameType.ALL);
                case MATCH_LIST_FRAGMENT:
                    return MatchListFragment.create(id, null);
                case ADV_SHOOTING_FRAGMENT:
                    return new AdvShootingStatsFragment();
                case ADV_SAFETY_FRAGMENT:
                    return new AdvSafetyStatsFragment();
                case ADV_BREAKING_FRAGMENT:
                    return new AdvBreakingStatsFragment();
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
