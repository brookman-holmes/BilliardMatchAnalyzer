package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.CreateNewMatchActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.billiardmatchanalyzer.utils.RoundedLetterView;
import com.brookmanholmes.billiards.player.AbstractPlayer;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends BaseActivity {
    public static final String TAG = "IntroActivity";
    private static final String MATCH_LIST_FRAGMENT = "match list fragment";
    private static final String PLAYER_LIST_FRAGMENT = "player list fragment";
    private static final String ARG_SELECTED_FRAGMENT = "selected fragment";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.createMatch) FloatingActionButton fab;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (findFragmentById(R.id.fragment_container) == null) {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getSupportActionBar().getThemedContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Matches", "Players"});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // select the item in the spinner that is the currently shown fragment so that rotation doesn't change it to item 0
        final String selectedFragment = findFragmentById(R.id.fragment_container) == null ?
                null : findFragmentById(R.id.fragment_container).getTag();
        if (PLAYER_LIST_FRAGMENT.equals(selectedFragment))
            spinner.setSelection(1);
        else if (MATCH_LIST_FRAGMENT.equals(selectedFragment))
            spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1)
                    replaceFragment(getPlayerListFragment(), PLAYER_LIST_FRAGMENT);
                else
                    replaceFragment(getMatchListFragment(), MATCH_LIST_FRAGMENT);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    @Override protected void onPause() {
        super.onPause();
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

    private Fragment findFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    public static class PlayerListFragment extends Fragment {
        @Bind(R.id.scrollView) RecyclerView recyclerView;
        private RecyclerAdapter adapter;
        private RecyclerView.LayoutManager layoutManager;

        public PlayerListFragment() {

        }

        @Nullable @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list_view, null);
            ButterKnife.bind(this, view);

            adapter = new RecyclerAdapter(new DatabaseAdapter(getContext()).getPlayers());

            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                layoutManager = new GridLayoutManager(getContext(), 2);
            else {
                layoutManager = new LinearLayoutManager(getContext());
            }
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            return view;
        }

        private static class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
            List<AbstractPlayer> players;
            int[] colors = new int[]{Color.parseColor("#f44336"), Color.parseColor("#9C27B0"),
                    Color.parseColor("#3F51B5"), Color.parseColor("#2196F3"), Color.parseColor("#00BCD4"),
                    Color.parseColor("#4CAF50"), Color.parseColor("#CDDC39"), Color.parseColor("#FF9800"),
                    Color.parseColor("#FF5722"), Color.parseColor("#795548"), Color.parseColor("#9E9E9E"),
                    Color.parseColor("#607D8B")};

            public RecyclerAdapter(List<AbstractPlayer> players) {
                Collections.sort(players);
                this.players = players;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            @Bind(R.id.shootingPct) TextView shootingPct;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void bind(AbstractPlayer player, @ColorInt int color) {
                playerName.setText(player.getName());
                playerIcon.setBackgroundColor(color);
                playerIcon.setTitleText(player.getName().substring(0, 1));
                gamesPlayed.setText(String.format(Locale.getDefault(), "%1$d games", player.getGamesPlayed()));
                shootingPct.setText(String.format(Locale.getDefault(), "Shooting %1$s", player.getShootingPct()));
            }

            @OnClick(R.id.container) void launchPlayerProfileActivity() {
                Intent intent = new Intent(itemView.getContext(), PlayerProfileActivity.class);
                intent.putExtra(PlayerProfileActivity.ARG_PLAYER_NAME, playerName.getText().toString());
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
