package com.brookmanholmes.bma.ui.profile;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.adaptervh.BaseViewHolder;
import com.brookmanholmes.bma.adaptervh.BreaksHolder;
import com.brookmanholmes.bma.adaptervh.FooterViewHolder;
import com.brookmanholmes.bma.adaptervh.MatchOverviewHolder;
import com.brookmanholmes.bma.adaptervh.RunOutsHolder;
import com.brookmanholmes.bma.adaptervh.SafetiesHolder;
import com.brookmanholmes.bma.adaptervh.ShootingPctHolder;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.squareup.leakcanary.RefWatcher;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoFragment extends Fragment {
    private static final String ARG_PLAYER = "arg player";
    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private PlayerInfoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
        DatabaseAdapter database = new DatabaseAdapter(getContext());
        String player = getArguments().getString(ARG_PLAYER);

        adapter = new PlayerInfoAdapter(database.getPlayer(player), player, getString(R.string.opponents));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override public void onDestroyView() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        layoutManager = null;

        ButterKnife.unbind(this);


        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
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

        List<AbstractPlayer> players = new ArrayList<>(), opponents = new ArrayList<>();
        String playerName, opponentName;
        Match.StatsDetail detail = Match.StatsDetail.NORMAL;

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

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
            view.setTag(viewType);
            return getMatchInfoHolderByViewType(view, viewType);
        }

        @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getPlayer(), getOpponent());
        }

        public void updatePlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            splitPlayers(pairs);
            notifyDataSetChanged();
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
                    return new MatchOverviewHolder(view, detail);
                case ITEM_SHOOTING_PCT:
                    return new ShootingPctHolder(view, detail);
                case ITEM_BREAKS:
                    return new BreaksHolder(view, 10, detail);
                case ITEM_RUN_OUTS:
                    return new RunOutsHolder(view, detail);
                case ITEM_SAFETIES:
                    return new SafetiesHolder(view, detail);
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