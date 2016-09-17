package com.brookmanholmes.bma.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.newmatchwizard.CreateNewMatchActivity;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.PreferencesUtil;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

public class IntroActivity extends BaseActivity {
    public static final String TAG = "IntroActivity";
    private static final String MATCH_LIST_FRAGMENT = "match list fragment";
    private static final String PLAYER_LIST_FRAGMENT = "player list fragment";

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.toolbar) Toolbar toolbar;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.createMatch) FloatingActionButton fab;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        if (preferences.getBoolean("first_run2", true)) {
            new Thread(new Runnable() {
                @Override public void run() {
                    DatabaseAdapter db = new DatabaseAdapter(IntroActivity.this);
                    db.createSampleMatches();
                }
            }).start();

            preferences.edit().putBoolean("first_run2", false).apply();
        }

        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

        setSupportActionBar(toolbar);

        if (findFragmentById() == null) {
            replaceFragment(getMatchListFragment(), MATCH_LIST_FRAGMENT);
        }
    }

    @Override protected void onResume() {
        super.onResume();

        final int animationDelay = 250; // .25 seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                fab.show();
            }
        }, animationDelay); // display fab after activity starts
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        MenuItem item = menu.findItem(R.id.spinner);

        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle));
        @SuppressWarnings("ConstantConditions") ArrayAdapter<String> adapter = new ArrayAdapter<>(getSupportActionBar().getThemedContext(),
                android.R.layout.simple_spinner_item,
                new String[]{getString(R.string.matches), getString(R.string.players)});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // select the item in the spinner that is the currently shown fragment so that rotation doesn't change it to item 0
        final String selectedFragment = findFragmentById() == null ? null : findFragmentById().getTag();
        if (PLAYER_LIST_FRAGMENT.equals(selectedFragment))
            spinner.setSelection(1);
        else if (MATCH_LIST_FRAGMENT.equals(selectedFragment))
            spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    replaceFragment(getPlayerListFragment(), PLAYER_LIST_FRAGMENT);
                    analytics.logEvent("view_player_list", null);
                } else {
                    replaceFragment(getMatchListFragment(), MATCH_LIST_FRAGMENT);
                    analytics.logEvent("view_match_list", null);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createGuide(spinner);

        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset_preferences) {
            PreferencesUtil.resetTutorial(preferences);
        }

        if (item.getItemId() == R.id.action_about) {
            new LibsBuilder().withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.about))
                    .start(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.createMatch) public void createNewMatch() {
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                Intent intent = new Intent(IntroActivity.this, CreateNewMatchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    private Fragment getPlayerListFragment() {
        return findFragmentByTag(PLAYER_LIST_FRAGMENT) == null ?
                new PlayerListFragment() :
                findFragmentByTag(PLAYER_LIST_FRAGMENT);
    }

    private Fragment getMatchListFragment() {
        return findFragmentByTag(MATCH_LIST_FRAGMENT) == null ?
                MatchListFragment.create(null, null) :
                findFragmentByTag(MATCH_LIST_FRAGMENT);
    }

    private Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private Fragment findFragmentById() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private void createGuide(View view) {
        if (preferences.getBoolean("first_run_tutorial_intro", true)) {
            Overlay overlay = new Overlay()
                    .setStyle(Overlay.Style.Circle)
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTransparent))
                    .disableClick(true)
                    .disableClickThroughHole(true);

            ChainTourGuide t2 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                            .setDescription("Click here to change the view between matches and players")
                            .setGravity(Gravity.LEFT|Gravity.BOTTOM))
                    .setOverlay(overlay)
                    .playLater(view);

            ChainTourGuide t1 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTitle("Welcome")
                            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .setDescription("You can create a new match by clicking on this button")
                            .setGravity(Gravity.TOP|Gravity.LEFT))
                    .setOverlay(overlay)
                    .playLater(fab);

            Sequence sequence = new Sequence.SequenceBuilder()
                    .add(t1, t2)
                    .setDefaultPointer(null)
                    .setContinueMethod(Sequence.ContinueMethod.Overlay)
                    .build();

            ChainTourGuide.init(this).playInSequence(sequence);

            preferences.edit().putBoolean("first_run_tutorial_intro", false).apply();
        }
    }

    public static class PlayerListFragment extends BaseRecyclerFragment {
        private GetPlayersTask task;
        public PlayerListFragment() {

        }

        @Override public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            adapter = new RecyclerAdapter(new ArrayList<AbstractPlayer>());

            if (task == null || task.getStatus() != AsyncTask.Status.RUNNING) {
                task = new GetPlayersTask();
                task.execute();
            }
        }

        @Override protected RecyclerView.LayoutManager getLayoutManager() {
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                return new GridLayoutManager(getContext(), 2);
            else {
                return new LinearLayoutManager(getContext());
            }
        }

        @Override public void onDestroy() {
            if (task.getStatus() != AsyncTask.Status.FINISHED)
                task.cancel(true);
            super.onDestroy();
        }

        private static class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
            final int[] colors = new int[]{Color.parseColor("#f44336"), Color.parseColor("#9C27B0"),
                    Color.parseColor("#3F51B5"), Color.parseColor("#2196F3"), Color.parseColor("#00BCD4"),
                    Color.parseColor("#4CAF50"), Color.parseColor("#CDDC39"), Color.parseColor("#FF9800"),
                    Color.parseColor("#FF5722"), Color.parseColor("#795548"), Color.parseColor("#9E9E9E"),
                    Color.parseColor("#607D8B")};
            List<AbstractPlayer> players;

            public RecyclerAdapter(List<AbstractPlayer> players) {
                Collections.sort(players);
                this.players = players;
            }

            public void update(List<AbstractPlayer> players) {
                this.players = players;
                notifyDataSetChanged();
            }

            @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_player, parent, false);
                return new ViewHolder(view);
            }

            @Override public void onBindViewHolder(ViewHolder holder, int position) {
                holder.bind(players.get(position), getColor(position));
            }

            @Override public int getItemCount() {
                return players.size();
            }

            private int getColor(int position) {
                return colors[position % colors.length];
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.playerIndicator) RoundedLetterView playerIcon;
            @Bind(R.id.playerName) TextView playerName;
            @Bind(R.id.gamesPlayed) TextView gamesPlayed;
            @Bind(R.id.shootingPctGrid) GridLayout gridLayout;
            @Bind(R.id.shootingLine) ImageView shootingLine;
            @Bind(R.id.safetyLine) ImageView safetyLine;
            @Bind(R.id.breakingLine) ImageView breakingLine;
            @Bind(R.id.tvSafetyPct) TextView safetyPct;
            @Bind(R.id.tvBreakPct) TextView breakPct;
            @Bind(R.id.tvShootingPct) TextView shootingPct;
            @Bind(R.id.matchesPlayed) TextView matchesPlayed;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void bind(AbstractPlayer player, @ColorInt int color) {
                playerName.setText(player.getName());
                playerIcon.setBackgroundColor(color);
                playerIcon.setTitleText(player.getName().substring(0, 1));
                gridLayout.setColumnCount(2);
                gridLayout.setRowCount(3);
                Context context = itemView.getContext();
                gamesPlayed.setText(context.getResources().getQuantityString(R.plurals.num_games, player.getGameTotal(), player.getGameTotal()));
                DatabaseAdapter db = new DatabaseAdapter(itemView.getContext());
                List<Match> matches = db.getMatches(player.getName(), null);
                matchesPlayed.setText(context.getResources().getQuantityString(R.plurals.num_matches, matches.size(), matches.size()));
                shootingPct.setText(String.format(context.getString(R.string.shooting_pct), player.getShootingPct()));
                safetyPct.setText(String.format(context.getString(R.string.safety_pct), player.getSafetyPct()));
                breakPct.setText(String.format(context.getString(R.string.breaking_pct), player.getBreakPct()));

                shootingLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getShootingPct()));
                safetyLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getSafetyPct()));
                breakingLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getBreakPct()));
            }

            @OnClick(R.id.container) void launchPlayerProfileActivity() {
                Intent intent = new Intent(itemView.getContext(), PlayerProfileActivity.class);
                intent.putExtra(PlayerProfileActivity.ARG_PLAYER_NAME, playerName.getText().toString());
                itemView.getContext().startActivity(intent);
            }
        }

        private class GetPlayersTask extends AsyncTask<Void, Void, List<AbstractPlayer>> {
            @Override protected List<AbstractPlayer> doInBackground(Void... params) {
                if (isAdded() && !isCancelled())
                    return new DatabaseAdapter(getContext()).getPlayers();
                else return new ArrayList<>();
            }

            @Override protected void onPostExecute(List<AbstractPlayer> abstractPlayers) {
                if (adapter != null)
                    ((RecyclerAdapter) adapter).update(abstractPlayers);
            }
        }
    }
}
