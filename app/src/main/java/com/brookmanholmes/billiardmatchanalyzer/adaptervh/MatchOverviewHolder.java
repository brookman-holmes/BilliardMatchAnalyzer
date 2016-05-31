package com.brookmanholmes.billiardmatchanalyzer.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
public class MatchOverviewHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
    @Bind(R.id.tvWinPercentPlayer) TextView tvWinPctPlayer;
    @Bind(R.id.tvWinPercentOpponent) TextView tvWinPctOpponent;
    @Bind(R.id.tvWinTotalsPlayer) TextView tvWinTotalPlayer;
    @Bind(R.id.tvWinTotalsOpponent) TextView tvWinTotalOpponent;
    @Bind(R.id.tvAggressivenessRatingPlayer) TextView tvAggressivenessRatingPlayer;
    @Bind(R.id.tvAggressivenessRatingOpponent) TextView tvAggressivenessRatingOpponent;
    @Bind(R.id.tvTrueShootingPctPlayer) TextView tvTSPPlayer;
    @Bind(R.id.tvTrueShootingPctOpponent) TextView tvTSPOpponent;
    @Bind(R.id.tvTotalShotsPlayer) TextView tvTotalShotsPlayer;
    @Bind(R.id.tvTotalShotsOpponent) TextView tvTotalShotsOpponent;

    public MatchOverviewHolder(View view, Match.StatsDetail detail) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_match_overview));
        setVisibilities(view, detail);
    }

    @Override protected void setVisibilities(View view, Match.StatsDetail detail) {
        if (detail == Match.StatsDetail.SIMPLE) {
            tvAggressivenessRatingOpponent.setVisibility(View.GONE);
            tvAggressivenessRatingPlayer.setVisibility(View.GONE);
            view.findViewById(R.id.tvAggressivenessRating).setVisibility(View.GONE);
            view.findViewById(R.id.tvTotalShotsTitle).setVisibility(View.GONE);
            tvTotalShotsOpponent.setVisibility(View.GONE);
            tvTotalShotsPlayer.setVisibility(View.GONE);
        }
    }

    @Override public void bind(T player, T opponent) {
        // Games Won Percentage
        tvWinPctPlayer.setText(player.getWinPct());
        tvWinPctOpponent.setText(opponent.getWinPct());
        // highlighting of the player who's doing better in this stat

        highlightBetterPlayerStats(tvWinPctPlayer, tvWinPctOpponent, player.getWins(), opponent.getWins());

        // Games Won x/y
        tvWinTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getWins(), player.getGamesPlayed()));
        tvWinTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getWins(), opponent.getGamesPlayed()));

        tvTSPPlayer.setText(player.getTrueShootingPct());
        tvTSPOpponent.setText(opponent.getTrueShootingPct());
        tvAggressivenessRatingPlayer.setText(player.getAggressivenessRating());
        tvAggressivenessRatingOpponent.setText(opponent.getAggressivenessRating());

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvTSPPlayer, tvTSPOpponent, Double.parseDouble(player.getTrueShootingPct()), Double.parseDouble(opponent.getTrueShootingPct()));

        tvTotalShotsPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getShotsSucceededOfAllTypes(), player.getShotAttemptsOfAllTypes()));
        tvTotalShotsOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getShotsSucceededOfAllTypes(), opponent.getShotAttemptsOfAllTypes()));
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_match_overview;
    }
}
