package com.brookmanholmes.bma.ui.profile;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.adaptervh.BaseViewHolder;
import com.brookmanholmes.bma.adaptervh.BreaksHolder;
import com.brookmanholmes.bma.adaptervh.FooterViewHolder;
import com.brookmanholmes.bma.adaptervh.MatchOverviewHolder;
import com.brookmanholmes.bma.adaptervh.RunOutsHolder;
import com.brookmanholmes.bma.adaptervh.SafetiesHolder;
import com.brookmanholmes.bma.adaptervh.ShootingPctHolder;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseRecyclerFragment;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoFragment extends BaseRecyclerFragment implements Filterable {
    private static final String ARG_PLAYER = "arg player";
    private DatabaseAdapter database;
    private String player;

    public PlayerInfoFragment() {
    }

    public static PlayerInfoFragment create(String player) {
        PlayerInfoFragment fragment = new PlayerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER, player);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseAdapter(getContext());
        player = getArguments().getString(ARG_PLAYER);

        adapter = new PlayerInfoAdapter(database.getPlayer(player), player, getString(R.string.opponents));

        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        }
    }

    @Override public void onDestroy() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        super.onDestroy();
    }

    @Override public void setFilter(StatFilter filter) {
        List<Pair<AbstractPlayer, AbstractPlayer>> players = database.getPlayer(player);
        List<Pair<AbstractPlayer, AbstractPlayer>> filteredPlayers = new ArrayList<>();

        for (Pair<AbstractPlayer, AbstractPlayer> pair : players) {
            if (filter.isPlayerQualified(pair.getRight()))
                filteredPlayers.add(pair);
        }

        ((PlayerInfoAdapter) adapter).updatePlayers(filteredPlayers);
    }

    @Override protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * Created by Brookman Holmes on 5/7/2016.
     */
    static class PlayerInfoAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        public static final int ITEM_MATCH_OVERVIEW = 0;
        public static final int ITEM_SHOOTING_PCT = 1;
        public static final int ITEM_SAFETIES = 2;
        public static final int ITEM_BREAKS = 3;
        public static final int ITEM_RUN_OUTS = 4;
        public static final int ITEM_FOOTER = 5;

        final List<AbstractPlayer> players = new ArrayList<>();
        final List<AbstractPlayer> opponents = new ArrayList<>();
        final String playerName;
        final String opponentName;

        public PlayerInfoAdapter(List<Pair<AbstractPlayer, AbstractPlayer>> pairs, String playerName, String opponentName) {
            splitPlayers(pairs);
            this.playerName = playerName;
            this.opponentName = opponentName;
        }

        private void splitPlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            for (Pair<AbstractPlayer, AbstractPlayer> pair : pairs) {
                players.add(pair.getLeft());
                opponents.add(pair.getRight());
            }
        }

        public void updatePlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            players.clear();
            opponents.clear();
            splitPlayers(pairs);
            notifyItemRangeChanged(0, getItemCount());
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
            view.setTag(viewType);
            return getMatchInfoHolderByViewType(view, viewType);
        }

        @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getPlayer(), getOpponent());
        }

        private CompPlayer getPlayer() {
            return getPlayerFromList(players, new CompPlayer(playerName));
        }

        private CompPlayer getOpponent() {
            return getPlayerFromList(opponents, new CompPlayer(opponentName));
        }

        private CompPlayer getPlayerFromList(List<? extends AbstractPlayer> players, CompPlayer newPlayer) {
            for (AbstractPlayer player : players) {
                newPlayer.addPlayerStats(player);
            }

            return newPlayer;
        }

        @LayoutRes int getLayoutResource(int viewType) {
            switch (viewType) {
                case ITEM_RUN_OUTS:
                    return R.layout.card_run_outs;
                case ITEM_BREAKS:
                    return R.layout.card_breaks;
                case ITEM_MATCH_OVERVIEW:
                    return R.layout.card_match_overview;
                case ITEM_SAFETIES:
                    return R.layout.card_safeties;
                case ITEM_SHOOTING_PCT:
                    return R.layout.card_shooting_pct;
                case ITEM_FOOTER:
                    return R.layout.footer;
                default:
                    throw new IllegalArgumentException("No such view type: " + viewType);
            }
        }

        BaseViewHolder getMatchInfoHolderByViewType(View view, int viewType) {
            switch (viewType) {
                case ITEM_MATCH_OVERVIEW:
                    return new MatchOverviewHolder(view);
                case ITEM_SHOOTING_PCT:
                    return new ShootingPctHolder(view);
                case ITEM_BREAKS:
                    return new BreaksHolder(view);
                case ITEM_RUN_OUTS:
                    return new RunOutsHolder(view);
                case ITEM_SAFETIES:
                    return new SafetiesHolder(view);
                case ITEM_FOOTER:
                    return new FooterViewHolder(view);
                default:
                    throw new IllegalArgumentException("No such view type");
            }
        }

        @Override public int getItemCount() {
            return 6;
        }

        @Override public int getItemViewType(int position) {
            return position;
        }
    }
}
