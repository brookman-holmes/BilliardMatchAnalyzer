package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.interfaces.Apa;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class ApaMatchInfoRecyclerAdapter<T extends AbstractPlayer & Apa> extends MatchInfoRecyclerAdapter<T> {
    static final int ITEM_APA_STATS = 5;

    ApaMatchInfoRecyclerAdapter(Match<T> match) {
        super(match, match.getGameStatus().GAME_BALL);
    }

    ApaMatchInfoRecyclerAdapter(Match<T> match, ViewType viewType) {
        super(match, match.getGameStatus().GAME_BALL, viewType);
    }

    @Override
    BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_APA_STATS:
                return new MatchInfoHolder.ApaPlayer<>(view);
            case ITEM_BREAKS:
                return new MatchInfoHolder.BreaksHolderWithBreakWins<>(view, gameBall);
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsWithEarlyWinsHolder<>(view);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }

    @Override
    int getLayoutResource(int viewType) {
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

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
