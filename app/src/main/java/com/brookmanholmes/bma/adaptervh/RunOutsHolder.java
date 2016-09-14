package com.brookmanholmes.bma.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IEarlyWins;
import com.brookmanholmes.bma.R;

import java.util.Locale;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class RunOutsHolder extends MatchInfoHolder {
    @Bind(R.id.tvMaxBallRunsPlayer) TextView tvBreakAndRunPlayer;
    @Bind(R.id.tvMaxBallRunsOpponent) TextView tvBreakAndRunOpponent;
    @Bind(R.id.tvTierOneRunsPlayer) TextView tvMaxRunPlayer;
    @Bind(R.id.tvTierOneRunsOpponent) TextView tvMaxRunOpponent;
    @Bind(R.id.tvFiveBallRunsPlayer) TextView tvFiveBallRunsPlayer;
    @Bind(R.id.tvFiveBallRunsOpponent) TextView tvFiveBallRunsOpponent;
    @Bind(R.id.tvEarlyWinsTitle) TextView tvEarlyWins;
    @Bind(R.id.tvEarlyWinsPlayer) TextView tvEarlyWinsPlayer;
    @Bind(R.id.tvEarlyWinsOpponent) TextView tvEarlyWinsOpponent;

    public RunOutsHolder(View view) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_run_outs));
    }

    @Override public void bind(AbstractPlayer player, AbstractPlayer opponent) {
        // Break and runs
        tvBreakAndRunPlayer.setText(String.format(Locale.getDefault(), "%d", player.getBreakAndRuns()));
        tvBreakAndRunOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getBreakAndRuns()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvBreakAndRunPlayer, tvBreakAndRunOpponent, player.getBreakAndRuns(), opponent.getBreakAndRuns());

        // table runs
        tvMaxRunPlayer.setText(String.format(Locale.getDefault(), "%d", player.getTableRuns()));
        tvMaxRunOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getTableRuns()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvMaxRunPlayer, tvMaxRunOpponent, player.getTableRuns(), opponent.getTableRuns());

        // 5+ ball runs
        tvFiveBallRunsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getFiveBallRun()));
        tvFiveBallRunsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getFiveBallRun()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvFiveBallRunsPlayer, tvFiveBallRunsOpponent, player.getFiveBallRun(), opponent.getFiveBallRun());

        if (player instanceof IEarlyWins && opponent instanceof IEarlyWins) {
            tvEarlyWins.setVisibility(View.VISIBLE);
            tvEarlyWinsPlayer.setVisibility(View.VISIBLE);
            tvEarlyWinsOpponent.setVisibility(View.VISIBLE);

            tvEarlyWinsPlayer.setText(String.format(Locale.getDefault(), "%d", ((IEarlyWins) player).getEarlyWins()));
            tvEarlyWinsOpponent.setText(String.format(Locale.getDefault(), "%d", ((IEarlyWins) opponent).getEarlyWins()));

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvEarlyWinsPlayer, tvEarlyWinsOpponent, ((IEarlyWins) player).getEarlyWins(), ((IEarlyWins) opponent).getEarlyWins());
        } else {
            tvEarlyWins.setVisibility(View.GONE);
            tvEarlyWinsPlayer.setVisibility(View.GONE);
            tvEarlyWinsOpponent.setVisibility(View.GONE);
        }
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_runs;
    }
}
