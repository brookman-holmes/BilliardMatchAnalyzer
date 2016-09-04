package com.brookmanholmes.bma.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class ShootingPctHolder extends MatchInfoHolder {
    @Bind(R.id.tvShootingPctPlayer) TextView tvShootingPctPlayer;
    @Bind(R.id.tvShootingPctOpponent) TextView tvShootingPctOpponent;
    @Bind(R.id.tvBallTotalPlayer) TextView tvBallTotalPlayer;
    @Bind(R.id.tvBallTotalOpponent) TextView tvBallTotalOpponent;
    @Bind(R.id.tvAvgBallsTurnPlayer) TextView tvAvgBallsTurnPlayer;
    @Bind(R.id.tvAvgBallsTurnOpponent) TextView tvAvgBallsTurnOpponent;
    @Bind(R.id.tvScratchesPlayer) TextView tvScratchesPlayer;
    @Bind(R.id.tvScratchesOpponent) TextView tvScratchesOpponent;

    public ShootingPctHolder(View view) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_shooting));
    }

    @Override public void bind(AbstractPlayer player, AbstractPlayer opponent) {
        // Shooting Percentage
        tvShootingPctPlayer.setText(player.getShootingPct());
        tvShootingPctOpponent.setText(opponent.getShootingPct());

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvShootingPctPlayer, tvShootingPctOpponent, Double.parseDouble(player.getShootingPct()), Double.parseDouble(opponent.getShootingPct()));

        // Shots succeeded / shots attempted
        tvBallTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getShootingBallsMade(), (player.getShootingAttempts())));
        tvBallTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getShootingBallsMade(), (opponent.getShootingAttempts())));

        // Average balls / turn
        tvAvgBallsTurnPlayer.setText(player.getAvgBallsTurn());
        tvAvgBallsTurnOpponent.setText(opponent.getAvgBallsTurn());

        highlightBetterPlayerStats(tvAvgBallsTurnPlayer, tvAvgBallsTurnOpponent, Double.parseDouble(player.getAvgBallsTurn()), Double.parseDouble(opponent.getAvgBallsTurn()));

        // Scratches
        tvScratchesPlayer.setText(String.valueOf(player.getShootingFouls()));
        tvScratchesOpponent.setText(String.valueOf(opponent.getShootingFouls()));

        // highlight the player with fewer scratches
        highlightPlayerStat(tvScratchesPlayer, tvScratchesOpponent, player.getShootingFouls(), opponent.getShootingFouls());
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_shooting;
    }
}
