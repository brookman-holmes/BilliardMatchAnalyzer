package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.MatchOverviewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.SafetiesHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.ShootingPctHolder;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 5/7/2016.
 */
public class PlayerInfoAdapter extends RecyclerView.Adapter<BaseViewHolder<CompPlayer>> {
    public static final int ITEM_MATCH_OVERVIEW = 0;
    public static final int ITEM_SHOOTING_PCT = 1;
    public static final int ITEM_SAFETIES = 2;
    public static final int ITEM_BREAKS = 3;
    public static final int ITEM_RUN_OUTS = 4;

    List<AbstractPlayer> players = new ArrayList<>(), opponents = new ArrayList<>();
    String playerName, opponentName;
    Match.StatsDetail detail;

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

    @Override public BaseViewHolder<CompPlayer> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        view.setTag(viewType);
        return getMatchInfoHolderByViewType(view, viewType);
    }

    @Override public void onBindViewHolder(BaseViewHolder<CompPlayer> holder, int position) {
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
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    BaseViewHolder<CompPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_MATCH_OVERVIEW:
                return new MatchOverviewHolder<>(view, detail);
            case ITEM_SHOOTING_PCT:
                return new ShootingPctHolder<>(view, detail);
            case ITEM_BREAKS:
                return new BreaksWithWinsHolder<>(view, 10, detail);
            case ITEM_RUN_OUTS:
                return new RunOutsWithEarlyWinsHolder<>(view, detail);
            case ITEM_SAFETIES:
                return new SafetiesHolder<>(view, detail);
            default:
                throw new IllegalArgumentException("No such view type");
        }
    }

    @Override public int getItemCount() {
        return 5;
    }

    @Override public int getItemViewType(int position) {
        return position;
    }
}
