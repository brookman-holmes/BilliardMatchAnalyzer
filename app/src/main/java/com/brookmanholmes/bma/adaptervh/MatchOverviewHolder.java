package com.brookmanholmes.bma.adaptervh;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
public class MatchOverviewHolder extends MatchInfoHolder {
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvWinPercentPlayer) TextView tvWinPctPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvWinPercentOpponent) TextView tvWinPctOpponent;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvWinTotalsPlayer) TextView tvWinTotalPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvWinTotalsOpponent) TextView tvWinTotalOpponent;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvAggressivenessRatingPlayer) TextView tvAggressivenessRatingPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvAggressivenessRatingOpponent) TextView tvAggressivenessRatingOpponent;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTrueShootingPctPlayer) TextView tvTSPPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTrueShootingPctOpponent) TextView tvTSPOpponent;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTotalShotsPlayer) TextView tvTotalShotsPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTotalShotsOpponent) TextView tvTotalShotsOpponent;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTotalFoulsPlayer) TextView tvTotalFoulsPlayer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.tvTotalFoulsOpponent) TextView tvTotalFoulsOpponent;

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
        } else {
            tvAggressivenessRatingOpponent.setVisibility(View.VISIBLE);
            tvAggressivenessRatingPlayer.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvAggressivenessRating).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvTotalShotsTitle).setVisibility(View.VISIBLE);
            tvTotalShotsOpponent.setVisibility(View.VISIBLE);
            tvTotalShotsPlayer.setVisibility(View.VISIBLE);
        }
    }

    @Override public void bind(@NonNull AbstractPlayer player, @NonNull AbstractPlayer opponent) {
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

        tvTotalFoulsOpponent.setText(String.valueOf(opponent.getTotalFouls()));
        tvTotalFoulsPlayer.setText(String.valueOf(player.getTotalFouls()));
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_match_overview;
    }
}
