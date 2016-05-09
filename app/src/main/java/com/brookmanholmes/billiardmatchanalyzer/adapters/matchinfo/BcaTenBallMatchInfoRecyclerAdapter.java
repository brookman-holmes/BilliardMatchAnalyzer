package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaTenBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<TenBallPlayer> {
    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match) {
        super(match);
    }

    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, ViewType viewType) {
        super(match, viewType);
    }

    @Override BaseViewHolder<TenBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_RUN_OUTS:
                return new RunOutsWithEarlyWinsHolder<>(view, detail);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
