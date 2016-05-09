package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IWinsOnBreak;

import java.util.Locale;

/**
 * Created by helios on 4/12/2016.
 */
public class BreaksWithWinsHolder<T extends AbstractPlayer & IWinsOnBreak> extends BreaksHolder<T> {
    public BreaksWithWinsHolder(View view, int gameBall, Match.StatsDetail detail) {
        super(view, gameBall, detail);

        breakWinsTitle.setVisibility(View.VISIBLE);
        tvBreakWinsPlayer.setVisibility(View.VISIBLE);
        tvBreakWinsOpponent.setVisibility(View.VISIBLE);
    }

    @Override
    public void bind(T player, T opponent) {
        super.bind(player, opponent);

        tvBreakWinsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getWinsOnBreak()));
        tvBreakWinsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getWinsOnBreak()));
        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvBreakWinsPlayer, tvBreakWinsOpponent, player.getWinsOnBreak(), opponent.getWinsOnBreak());
    }
}
