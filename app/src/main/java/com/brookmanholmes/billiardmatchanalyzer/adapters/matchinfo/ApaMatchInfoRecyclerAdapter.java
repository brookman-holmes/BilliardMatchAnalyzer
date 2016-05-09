package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.ApaPlayer;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IApa;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class ApaMatchInfoRecyclerAdapter<T extends AbstractPlayer & IApa> extends MatchInfoRecyclerAdapter<T> {

    ApaMatchInfoRecyclerAdapter(Match<T> match) {
        super(match);
    }

    ApaMatchInfoRecyclerAdapter(Match<T> match, ViewType viewType) {
        super(match, viewType);
    }

    @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ApaPlayer)
            ((ApaPlayer) holder).setTvInningsOpponent(match.getGameStatus().innings);
    }

    @Override BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_APA_STATS:
                return new ApaPlayer<>(view, detail);
            case ITEM_BREAKS:
                return new BreaksWithWinsHolder<>(view, gameBall, detail);
            case ITEM_RUN_OUTS:
                return new RunOutsWithEarlyWinsHolder<>(view, detail);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }

    @Override int getLayoutResource(int viewType) {
        if (viewTypeToggle == ViewType.CARDS) {
            switch (viewType) {
                case ITEM_APA_STATS:
                    return R.layout.card_apa_stats;
                default:
                    return super.getLayoutResource(viewType);
            }
        } else {
            switch (viewType) {
                case ITEM_APA_STATS:
                    return R.layout.plain_apa_stats;
                default:
                    return super.getLayoutResource(viewType);
            }
        }
    }

    @Override public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_APA_STATS;
            case 1:
                return ITEM_SHOOTING_PCT;
            case 2:
                return ITEM_SAFETIES;
            case 3:
                return ITEM_BREAKS;
            case 4:
                return ITEM_RUN_OUTS;
            case 5:
                return ITEM_MATCH_OVERVIEW;
            case 6:
                return ITEM_FOOTER;
            default:
                throw new IllegalArgumentException("Cannot have position: " + position);
        }
    }
}
