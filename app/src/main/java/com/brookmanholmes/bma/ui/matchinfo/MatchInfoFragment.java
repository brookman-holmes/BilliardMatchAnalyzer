package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.databinding.FragmentMatchInfoBinding;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.utils.ConversionUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Brookman Holmes on 9/21/2016.
 */
public class MatchInfoFragment extends BaseFragment implements MatchInfoListener, PlayersListener {
    private static final String TAG = "MatchInfoFragment";

    private static final String KEY_CARD_EXPANDED = "key_card_expanded";

    ApaBinder apa;
    MatchOverviewBinder overview;
    ShootingBinder shooting;
    SafetiesBinder safeties;
    BreaksBinder breaks;
    RunsBinder runs;
    StraightPoolBinder straightPool;
    StraightPoolRunsBinder straightPoolRuns;
    BallsOnTableBinder ballsOnTable;
    WinIndicatorBinder winIndicator;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchInfoFragment() {
    }

    public static MatchInfoFragment newInstance(GameType gameType) {
        MatchInfoFragment fragment = new MatchInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable("gameType", gameType);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameType gameType = (GameType) getArguments().getSerializable("gameType");
        boolean expanded = (gameType == GameType.ALL) || getResources().getBoolean(R.bool.expanded_match_info_cards);

        boolean[] expandedCards = null;
        if (savedInstanceState != null)
            expandedCards = savedInstanceState.getBooleanArray(KEY_CARD_EXPANDED);

        if (expandedCards == null) {
            expandedCards = new boolean[8];
            Arrays.fill(expandedCards, false);
        }


        apa = new ApaBinder(getString(R.string.title_apa_stats), expanded || expandedCards[0], gameType);
        overview = new MatchOverviewBinder(getString(R.string.title_match_overview), expanded || expandedCards[1], gameType);
        shooting = new ShootingBinder(getString(R.string.title_shooting), expanded || expandedCards[2], gameType);
        safeties = new SafetiesBinder(getString(R.string.title_safeties), expanded || expandedCards[3], gameType);
        breaks = new BreaksBinder(getString(R.string.title_breaks), expanded || expandedCards[4], gameType);
        runs = new RunsBinder(getString(R.string.title_run_outs), expanded || expandedCards[5], gameType);
        straightPool = new StraightPoolBinder(getString(R.string.title_match_overview), expanded || expandedCards[6], gameType);
        straightPoolRuns = new StraightPoolRunsBinder(getString(R.string.title_straight_pool_runs), expanded || expandedCards[7], gameType);
        ballsOnTable = new BallsOnTableBinder(gameType, ConversionUtils.convertPxToDp(getContext(), getResources().getDisplayMetrics().widthPixels));
        winIndicator = new WinIndicatorBinder();
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
        binder.setBallsOnTable(ballsOnTable);
        binder.setWinIndicator(winIndicator);

        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addStatListener(this);
        }
        if (getActivity() instanceof MatchInfoActivity) {
            ((MatchInfoActivity) getActivity()).registerFragment(this);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray(KEY_CARD_EXPANDED, new boolean[]{
                apa.expanded, overview.expanded, breaks.expanded, runs.expanded, shooting.expanded,
                safeties.expanded, straightPool.expanded, straightPoolRuns.expanded
        });
        outState.putBoolean(apa.title, apa.expanded);
        outState.putBoolean(overview.title, overview.expanded);
        outState.putBoolean(breaks.title, breaks.expanded);
        outState.putBoolean(runs.title, runs.expanded);
        outState.putBoolean(shooting.title, shooting.expanded);
        outState.putBoolean(safeties.title, safeties.expanded);
        outState.putBoolean(straightPool.title, straightPool.expanded);
        outState.putBoolean(straightPoolRuns.title, straightPoolRuns.expanded);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        if (getActivity() instanceof MatchInfoActivity)
            ((MatchInfoActivity) getActivity()).removeFragment(this);

        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeStatListener(this);
        }

        super.onDetach();
    }

    @Override
    public void updatePlayers(List<Player> players, List<Player> opponents) {
        Player player = new Player("", "", GameType.ALL);
        Player opponent = new Player("", "", GameType.ALL);

        player.addPlayerStats(players);
        opponent.addPlayerStats(opponents);

        update(player, opponent, null);
    }

    @Override
    public void update(Match match) {
        update(match.getPlayer(), match.getOpponent(), match.getGameStatus());
    }

    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        apa.update(player, opponent, gameStatus);
        overview.update(player, opponent, gameStatus);
        shooting.update(player, opponent, gameStatus);
        safeties.update(player, opponent, gameStatus);
        breaks.update(player, opponent, gameStatus);
        runs.update(player, opponent, gameStatus);
        straightPool.update(player, opponent, gameStatus);
        straightPoolRuns.update(player, opponent, gameStatus);
        ballsOnTable.update(player, opponent, gameStatus);
        winIndicator.update(player, opponent, gameStatus);
    }
}
