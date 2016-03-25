package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.EightBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
class BcaEightBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<EightBallPlayer> {
    BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match, View.OnClickListener listener) {
        super(match, 8, listener);
    }

    BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match, ViewType viewType, View.OnClickListener listener) {
        super(match, 8, viewType, listener);
    }
}
