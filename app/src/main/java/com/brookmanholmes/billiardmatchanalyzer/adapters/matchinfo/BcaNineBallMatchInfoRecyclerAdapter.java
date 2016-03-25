package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaNineBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<NineBallPlayer> {
    BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match, View.OnClickListener listener) {
        super(match, 9, listener);
    }

    BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match, ViewType viewType, View.OnClickListener listener) {
        super(match, 9, viewType, listener);
    }

    @Override
    BaseViewHolder<NineBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_BREAKS:
                return new MatchInfoHolder.BreaksHolderWithBreakWins<>(view, gameBall, detail, listener);
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsWithEarlyWinsHolder<>(view, detail, listener);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
