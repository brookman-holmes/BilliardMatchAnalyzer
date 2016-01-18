package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
public class BcaNineBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<NineBallPlayer> {
    public BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match) {
        super(match, 9);
    }

    @Override
    MatchInfoHolder<NineBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_BREAKS:
                return new MatchInfoHolder.BreaksHolderWithBreakWins<>(view, gameBall);
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsWithEarlyWinsHolder<>(view);
            default:
                return super.getMatchInfoHolderByViewType(view, viewType);
        }
    }
}
