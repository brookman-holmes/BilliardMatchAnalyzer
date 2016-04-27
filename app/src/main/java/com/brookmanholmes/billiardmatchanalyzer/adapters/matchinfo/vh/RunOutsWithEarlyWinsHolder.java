package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh;

import android.view.View;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.interfaces.EarlyWins;

import java.util.Locale;

/**
 * Created by helios on 4/12/2016.
 */
public class RunOutsWithEarlyWinsHolder<T extends AbstractPlayer & EarlyWins> extends RunOutsHolder<T> {
    public RunOutsWithEarlyWinsHolder(View view, Match.StatsDetail detail) {
        super(view, detail);

        tvEarlyWins.setVisibility(View.VISIBLE);
        tvEarlyWinsPlayer.setVisibility(View.VISIBLE);
        tvEarlyWinsOpponent.setVisibility(View.VISIBLE);
    }

    @Override
    public void bind(T player, T opponent) {
        super.bind(player, opponent);

        tvEarlyWinsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getEarlyWins()));
        tvEarlyWinsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getEarlyWins()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvEarlyWinsPlayer, tvEarlyWinsOpponent, player.getEarlyWins(), opponent.getEarlyWins());
    }
}
