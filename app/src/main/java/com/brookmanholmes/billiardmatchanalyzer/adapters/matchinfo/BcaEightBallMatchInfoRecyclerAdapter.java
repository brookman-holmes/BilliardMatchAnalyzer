package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.EightBallPlayer;

/**
 * Created by Brookman Holmes on 1/17/2016.
 */
public class BcaEightBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<EightBallPlayer> {
    public BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match) {
        super(match, 8);
    }
}
