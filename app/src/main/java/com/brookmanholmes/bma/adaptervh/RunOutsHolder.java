package com.brookmanholmes.bma.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
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
        tvBreakAndRunPlayer.setText(String.format(Locale.getDefault(), "%d", player.getRunOuts()));
        tvBreakAndRunOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getRunOuts()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvBreakAndRunPlayer, tvBreakAndRunOpponent, player.getRunOuts(), opponent.getRunOuts());

        // table runs
        tvMaxRunPlayer.setText(String.format(Locale.getDefault(), "%d", player.getRunTierOne()));
        tvMaxRunOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getRunTierOne()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvMaxRunPlayer, tvMaxRunOpponent, player.getRunTierOne(), opponent.getRunTierOne());

        // 5+ ball runs
        tvFiveBallRunsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getRunTierTwo()));
        tvFiveBallRunsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getRunTierTwo()));

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvFiveBallRunsPlayer, tvFiveBallRunsOpponent, player.getRunTierTwo(), opponent.getRunTierTwo());

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
