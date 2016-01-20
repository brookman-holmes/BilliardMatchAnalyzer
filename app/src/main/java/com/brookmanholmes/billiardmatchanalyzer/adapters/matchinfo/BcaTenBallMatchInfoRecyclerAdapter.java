package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaTenBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<TenBallPlayer> {
    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match) {
        super(match, 10);
    }

    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, ViewType viewType) {
        super(match, 10, viewType);
    }

    @Override
    MatchInfoHolder<TenBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsWithEarlyWinsHolder<>(view);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
