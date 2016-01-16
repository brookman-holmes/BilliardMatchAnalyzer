package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchInfoRecyclerAdapter<T extends AbstractPlayer> extends RecyclerView.Adapter<MatchInfoHolder<T>> {
    static final int ITEM_MATCH_OVERVIEW = 0;
    static final int ITEM_SHOOTING_PCT = 1;
    static final int ITEM_SAFETIES = 2;
    static final int ITEM_BREAKS = 3;
    static final int ITEM_RUN_OUTS = 4;
    static final int ITEM_MATCH_DEBUG = 5;
    static final int ITEM_APA_STATS = 6;

    Match<T> match;

    public MatchInfoRecyclerAdapter(Match<T> match) {
        this.match = match;
    }

    @Override
    public MatchInfoHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_MATCH_OVERVIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_match_overview, parent, false);
            return new MatchInfoHolder.MatchOverviewHolder<>(view);
        } else if (viewType == ITEM_SHOOTING_PCT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shooting_pct, parent, false);
            return new MatchInfoHolder.ShootingPctHolder<>(view);
        } else if (viewType == ITEM_SAFETIES) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_safeties, parent, false);
            return new MatchInfoHolder.SafetiesHolder<>(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(MatchInfoHolder<T> holder, int position) {
        holder.bind(getPlayer(), getOpponent());
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Turn addTurn(TableStatus table, TurnEnd turnEnd, boolean scratch) {
        Turn turn = match.createAndAddTurnToMatch(table, turnEnd, scratch);
        notifyDataSetChanged();
        return turn;
    }

    T getPlayer() {
        return match.getPlayer();
    }

    T getOpponent() {
        return match.getOpponent();
    }

    public interface ListItemClickListener {
        void onSelectMatch(long id);

        void onLongSelectMatch(long id);
    }
}
