package com.brookmanholmes.bma.ui.matchinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.databinding.FragmentMatchInfoBinding;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;
import com.brookmanholmes.bma.utils.ConversionUtils;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Brookman Holmes on 9/21/2016.
 */
public class MatchInfoFragment extends BaseFragment implements MatchInfoActivity.UpdateMatchInfo, Filterable {
    private static final String TAG = "MatchInfoFragment";

    private static final String ARG_PLAYER = "arg_playerName";
    private static final String KEY_APA_EXPANDED = "key_apa_expanded";
    private static final String KEY_OVERVIEW_EXPANDED = "key_overview_expanded";
    private static final String KEY_SHOOTING_EXPANDED = "key_shooting_expanded";
    private static final String KEY_SAFETIES_EXPANDED = "key_safeties_expanded";
    private static final String KEY_BREAKS_EXPANDED = "key_breaks_expanded";
    private static final String KEY_RUNS_EXPANDED = "key_runs_expanded";
    private static final String KEY_STRAIGHT_TABLE_EXPANDED = "key_straight_table_expanded";
    private static final String KEY_STRAIGHT_POOL_EXPANDED = "key_straight_pool_expanded";
    private static final String KEY_STRAIGHT_RUNS_EXPANDED = "key_straight_runs_expanded";

    String playerName;

    ApaBinder apa;
    MatchOverviewBinder overview;
    ShootingBinder shooting;
    SafetiesBinder safeties;
    BreaksBinder breaks;
    RunsBinder runs;
    StraightPoolBinder straightPool;
    StraightPoolRunsBinder straightPoolRuns;
    StraightPoolTableBinder straightPoolTable;
    BallsOnTableBinder ballsOnTable;
    WinIndicatorBinder winIndicator;

    private UpdatePlayersAsync task;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchInfoFragment() {
    }


    public static MatchInfoFragment create(long matchId) {
        MatchInfoFragment fragment = new MatchInfoFragment();

        Bundle args = new Bundle();
        args.putLong(BaseActivity.ARG_MATCH_ID, matchId);

        fragment.setArguments(args);

        return fragment;
    }

    public static MatchInfoFragment create(String player) {
        MatchInfoFragment fragment = new MatchInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER, player);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (getActivity() instanceof MatchInfoActivity)
            ((MatchInfoActivity) getActivity()).removeFragment(this);

        if (getActivity() instanceof PlayerProfileActivity)
            ((PlayerProfileActivity) getActivity()).removeListener(this);

        super.onDetach();
    }

    @Override
    public void update(Match match) {
        updateBinders(match.getPlayer(), match.getOpponent(), match.getGameStatus().innings);
        ballsOnTable.update(match.getGameStatus().ballsOnTable);
    }

    private void update(List<Pair<Player, Player>> pairs) {
        Pair<Player, Player> pair = splitPlayers(pairs);
        Player player = pair.getLeft();
        Player opponent = pair.getRight();

        updateBinders(player, opponent, 0);
    }

    private void updateBinders(Player player, Player opponent, int innings) {
        apa.update(player, opponent, innings);
        overview.update(player, opponent);
        shooting.update(player, opponent);
        safeties.update(player, opponent);
        breaks.update(player, opponent);
        runs.update(player, opponent);
        straightPool.update(player, opponent);
        straightPoolTable.update(player, opponent);
        straightPoolRuns.update(player, opponent);
        winIndicator.update(player, opponent);
    }

    @Override
    public void setFilter(StatFilter filter) {
        if (task == null) {
            task = new UpdatePlayersAsync();
            task.execute(filter);
        }

        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            task.cancel(true);
            task = new UpdatePlayersAsync();
            task.execute(filter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseAdapter db = new DatabaseAdapter(getContext());

        Player player;
        Player opponent;
        boolean expanded;
        boolean isGhostGame = false;
        int innings = 0;
        String straightPoolTitle = getString(R.string.title_straight_pool_runs),
                matchOverviewTitle = getString(R.string.title_match_overview);
        if (getArguments().getLong(BaseActivity.ARG_MATCH_ID, -1L) != -1L) {
            Match match = db.getMatchWithTurns(getArguments().getLong(BaseActivity.ARG_MATCH_ID));
            player = match.getPlayer();
            opponent = match.getOpponent();
            expanded = getResources().getBoolean(R.bool.expanded_match_info_cards);
            isGhostGame = match.getGameStatus().gameType.isGhostGame();
            innings = match.getGameStatus().innings;
            ballsOnTable = new BallsOnTableBinder(match.getGameStatus(), ConversionUtils.convertPxToDp(getContext(), getResources().getDisplayMetrics().widthPixels));
        } else {
            playerName = getArguments().getString(ARG_PLAYER);
            List<Pair<Player, Player>> pairs = db.getPlayerPairs(playerName);
            Pair<Player, Player> pair = splitPlayers(pairs);
            player = pair.getLeft();
            opponent = pair.getRight();
            expanded = true;
            ballsOnTable = new BallsOnTableBinder();
            matchOverviewTitle = getString(R.string.matches);
            straightPoolTitle = getString(R.string.title_straight_pool_runs_alt);
        }

        if (savedInstanceState != null) {
            getArguments().putAll(savedInstanceState);
        }

        apa = new ApaBinder(player, opponent, getString(R.string.title_apa_stats), getArguments().getBoolean(KEY_APA_EXPANDED, expanded), innings);
        overview = new MatchOverviewBinder(player, opponent, getString(R.string.title_match_overview), getArguments().getBoolean(KEY_OVERVIEW_EXPANDED, expanded));
        shooting = new ShootingBinder(player, opponent, getString(R.string.title_shooting), getArguments().getBoolean(KEY_SHOOTING_EXPANDED, expanded));
        safeties = new SafetiesBinder(player, opponent, getString(R.string.title_safeties), getArguments().getBoolean(KEY_SAFETIES_EXPANDED, expanded), !isGhostGame);
        breaks = new BreaksBinder(player, opponent, getString(R.string.title_breaks), getArguments().getBoolean(KEY_BREAKS_EXPANDED, expanded));
        runs = new RunsBinder(player, opponent, getString(R.string.title_run_outs), getArguments().getBoolean(KEY_RUNS_EXPANDED, expanded), !isGhostGame);
        straightPool = new StraightPoolBinder(player, opponent, matchOverviewTitle, getArguments().getBoolean(KEY_STRAIGHT_POOL_EXPANDED, expanded));
        straightPoolRuns = new StraightPoolRunsBinder(player, opponent, straightPoolTitle, getArguments().getBoolean(KEY_STRAIGHT_RUNS_EXPANDED, expanded));
        straightPoolTable = new StraightPoolTableBinder(player, opponent, getArguments().getBoolean(KEY_STRAIGHT_TABLE_EXPANDED, expanded));
        winIndicator = new WinIndicatorBinder(player, opponent);
    }

    private Pair<Player, Player> splitPlayers(List<Pair<Player, Player>> pairs) {
        Pair<Player, Player> newPair = new MutablePair<>(new Player(playerName, GameType.ALL), new Player("", GameType.ALL));

        for (Pair<Player, Player> pair : pairs) {
            newPair.getLeft().addPlayerStats(pair.getLeft());
            newPair.getRight().addPlayerStats(pair.getRight());
        }

        return newPair;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, null);
        ButterKnife.bind(this, view);
        FragmentMatchInfoBinding binder = FragmentMatchInfoBinding.bind(view);

        binder.setApa(apa);
        binder.setOverview(overview);
        binder.setShooting(shooting);
        binder.setSafeties(safeties);
        binder.setBreaks(breaks);
        binder.setRuns(runs);
        binder.setStraight(straightPool);
        binder.setStraightRuns(straightPoolRuns);
        binder.setStraightTable(straightPoolTable);
        binder.setBallsOnTable(ballsOnTable);
        binder.setWinIndicator(winIndicator);

        if (getActivity() instanceof PlayerProfileActivity)
            ((PlayerProfileActivity) getActivity()).addListener(this);

        if (getActivity() instanceof MatchInfoActivity)
            ((MatchInfoActivity) getActivity()).registerFragment(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_APA_EXPANDED, apa.expanded);
        outState.putBoolean(KEY_OVERVIEW_EXPANDED, overview.expanded);
        outState.putBoolean(KEY_BREAKS_EXPANDED, breaks.expanded);
        outState.putBoolean(KEY_RUNS_EXPANDED, runs.expanded);
        outState.putBoolean(KEY_SHOOTING_EXPANDED, shooting.expanded);
        outState.putBoolean(KEY_SAFETIES_EXPANDED, safeties.expanded);
        outState.putBoolean(KEY_STRAIGHT_POOL_EXPANDED, straightPool.expanded);
        outState.putBoolean(KEY_STRAIGHT_RUNS_EXPANDED, straightPoolRuns.expanded);
        outState.putBoolean(KEY_STRAIGHT_TABLE_EXPANDED, straightPoolTable.expanded);

        super.onSaveInstanceState(outState);
    }

    private class UpdatePlayersAsync extends AsyncTask<StatFilter, Void, List<Pair<Player, Player>>> {
        @Override
        protected List<Pair<Player, Player>> doInBackground(StatFilter... filter) {
            List<Pair<Player, Player>> players = new DatabaseAdapter(getContext()).getPlayerPairs(playerName);
            List<Pair<Player, Player>> filteredPlayers = new ArrayList<>();

            for (Pair<Player, Player> pair : players) {
                if (filter[0].isPlayerQualified(pair.getRight()) && !isCancelled())
                    filteredPlayers.add(pair);
            }

            return filteredPlayers;
        }

        @Override
        protected void onPostExecute(List<Pair<Player, Player>> pairs) {
            if (!isCancelled() && isAdded())
                update(pairs);
        }
    }
}
