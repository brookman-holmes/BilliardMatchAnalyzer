package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaNineBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<NineBallPlayer> {
    BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match) {
        super(match);
    }

    BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match, ViewType viewType) {
        super(match, viewType);
    }

    @Override BaseViewHolder<NineBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_BREAKS:
                return new BreaksWithWinsHolder<>(view, gameBall, detail);
            case ITEM_RUN_OUTS:
                return new RunOutsWithEarlyWinsHolder<>(view, detail);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
