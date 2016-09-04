package com.brookmanholmes.bma.adaptervh;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.IApa;
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

    public MatchOverviewHolder(View view) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_match_overview));
    }

    @Override public void bind(@NonNull AbstractPlayer player, @NonNull AbstractPlayer opponent) {
        // Games Won Percentage
        tvWinPctPlayer.setText(player.getWinPct());
        tvWinPctOpponent.setText(opponent.getWinPct());
        // highlighting of the player who's doing better in this stat

        highlightBetterPlayerStats(tvWinPctPlayer, tvWinPctOpponent, player.getWins(), opponent.getWins());

        // Games Won / games needed
        if (player instanceof ApaEightBallPlayer && opponent instanceof ApaEightBallPlayer) {
            tvWinTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getWins(), ((ApaEightBallPlayer) player).getPointsNeeded(opponent.getRank())));
            tvWinTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getWins(), ((ApaEightBallPlayer) opponent).getPointsNeeded(player.getRank())));
        } else if (player instanceof ApaNineBallPlayer && opponent instanceof ApaNineBallPlayer){
            ((TextView)itemView.findViewById(R.id.tvWinTotalTitle)).setText(itemView.getContext().getString(R.string.title_games_won_apa));
            tvWinTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getWins(), player.getGamesPlayed()));
            tvWinTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getWins(), opponent.getGamesPlayed()));
        } else {
            tvWinTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getWins(), player.getRank()));
            tvWinTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getWins(), opponent.getRank()));
        }

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
