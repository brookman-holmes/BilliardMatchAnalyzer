package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaTenBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<TenBallPlayer> {
    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, View.OnClickListener listener) {
        super(match, 10, listener);
    }

    BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, ViewType viewType, View.OnClickListener listener) {
        super(match, 10, viewType, listener);
    }

    @Override BaseViewHolder<TenBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsWithEarlyWinsHolder<>(view, detail, listener);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
