package com.brookmanholmes.bma.ui.matchinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.databinding.FragmentMatchInfoBinding;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brookman Holmes on 9/21/2016.
 */
public class MatchInfoFragment extends BaseFragment
        implements MatchInfoActivity.UpdateMatchInfo, Filterable {
    private static final String TAG = "MatchInfoFragment";
    private static final String ARG_PLAYER = "arg_playerName";
    private static final String KEY_APA_EXPANDED = "key_apa_expanded";
    private static final String KEY_OVERVIEW_EXPANDED = "key_overview_expanded";
    private static final String KEY_SHOOTING_EXPANDED = "key_shooting_expanded";
    private static final String KEY_SAFETIES_EXPANDED = "key_safeties_expanded";
    private static final String KEY_BREAKS_EXPANDED = "key_breaks_expanded";
    private static final String KEY_RUNS_EXPANDED = "key_runs_expanded";
    String playerName;
    ApaBinder apa;
    MatchOverviewBinder overview;
    ShootingBinder shooting;
    SafetiesBinder safeties;
    BreaksBinder breaks;
    RunsBinder runs;
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
        apa.update(match.getPlayer(), match.getOpponent(), match.getGameStatus().innings);
        overview.update(match.getPlayer(), match.getOpponent());
        shooting.update(match.getPlayer(), match.getOpponent());
        safeties.update(match.getPlayer(), match.getOpponent());
        breaks.update(match.getPlayer(), match.getOpponent());
        runs.update(match.getPlayer(), match.getOpponent());
    }

    private void update(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
        Pair<CompPlayer, CompPlayer> pair = splitPlayers(pairs);
        AbstractPlayer player = pair.getLeft();
        AbstractPlayer opponent = pair.getRight();

        overview.update(player, opponent);
        shooting.update(player, opponent);
        safeties.update(player, opponent);
        breaks.update(player, opponent);
        runs.update(player, opponent);
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

        AbstractPlayer player;
        AbstractPlayer opponent;
        boolean isGhostGame = true;
        boolean expanded;
        int innings = 0;
        if (getArguments().getLong(BaseActivity.ARG_MATCH_ID, -1L) != -1L) {
            Match match = db.getMatch(getArguments().getLong(BaseActivity.ARG_MATCH_ID));
            player = match.getPlayer();
            opponent = match.getOpponent();
            expanded = false;
            innings = match.getGameStatus().innings;
            isGhostGame = match.getGameStatus().gameType.isGhostGame(); // don't show safeties if it's a game against the ghost
        } else {
            playerName = getArguments().getString(ARG_PLAYER);
            List<Pair<AbstractPlayer, AbstractPlayer>> pairs = db.getPlayerPairs(playerName);
            Pair<CompPlayer, CompPlayer> pair = splitPlayers(pairs);
            player = pair.getLeft();
            opponent = pair.getRight();
            isGhostGame = false;
            expanded = true;
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
    }

    private Pair<CompPlayer, CompPlayer> splitPlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
        Pair<CompPlayer, CompPlayer> newPair = new MutablePair<>(new CompPlayer(playerName), new CompPlayer(""));

        for (Pair<AbstractPlayer, AbstractPlayer> pair : pairs) {
            newPair.getLeft().addPlayerStats(pair.getLeft());
            newPair.getRight().addPlayerStats(pair.getRight());
        }

        return newPair;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, null);

        FragmentMatchInfoBinding binder = FragmentMatchInfoBinding.bind(view);

        binder.setApa(apa);
        binder.setOverview(overview);
        binder.setShooting(shooting);
        binder.setSafeties(safeties);
        binder.setBreaks(breaks);
        binder.setRuns(runs);

        if (getActivity() instanceof PlayerProfileActivity)
            ((PlayerProfileActivity) getActivity()).addListener(this);

        if (getActivity() instanceof MatchInfoActivity)
            ((MatchInfoActivity) getActivity()).registerFragment(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_APA_EXPANDED, apa.visible);
        outState.putBoolean(KEY_OVERVIEW_EXPANDED, overview.visible);
        outState.putBoolean(KEY_BREAKS_EXPANDED, breaks.visible);
        outState.putBoolean(KEY_RUNS_EXPANDED, runs.visible);
        outState.putBoolean(KEY_SHOOTING_EXPANDED, shooting.visible);
        outState.putBoolean(KEY_SAFETIES_EXPANDED, safeties.visible);
        super.onSaveInstanceState(outState);
    }

    private class UpdatePlayersAsync extends AsyncTask<StatFilter, Void, List<Pair<AbstractPlayer, AbstractPlayer>>> {
        @Override
        protected List<Pair<AbstractPlayer, AbstractPlayer>> doInBackground(StatFilter... filter) {
            List<Pair<AbstractPlayer, AbstractPlayer>> players = new DatabaseAdapter(getContext()).getPlayerPairs(playerName);
            List<Pair<AbstractPlayer, AbstractPlayer>> filteredPlayers = new ArrayList<>();

            for (Pair<AbstractPlayer, AbstractPlayer> pair : players) {
                if (filter[0].isPlayerQualified(pair.getRight()) && !isCancelled())
                    filteredPlayers.add(pair);
            }

            return filteredPlayers;
        }

        @Override
        protected void onPostExecute(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            if (!isCancelled() && isAdded())
                update(pairs);
        }
    }
}
