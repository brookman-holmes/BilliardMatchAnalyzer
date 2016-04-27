package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;

import java.util.Locale;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
public class RunOutsHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
    @Bind(R.id.tvMaxBallRunsPlayer) TextView tvBreakAndRunPlayer;
    @Bind(R.id.tvMaxBallRunsOpponent) TextView tvBreakAndRunOpponent;
    @Bind(R.id.tvTierOneRunsPlayer) TextView tvMaxRunPlayer;
    @Bind(R.id.tvTierOneRunsOpponent) TextView tvMaxRunOpponent;
    @Bind(R.id.tvFiveBallRunsPlayer) TextView tvFiveBallRunsPlayer;
    @Bind(R.id.tvFiveBallRunsOpponent) TextView tvFiveBallRunsOpponent;
    @Bind(R.id.tvEarlyWinsTitle) TextView tvEarlyWins;
    @Bind(R.id.tvEarlyWinsPlayer) TextView tvEarlyWinsPlayer;
    @Bind(R.id.tvEarlyWinsOpponent) TextView tvEarlyWinsOpponent;

    public RunOutsHolder(View view, Match.StatsDetail detail) {
        super(view);
        tvEarlyWins.setVisibility(View.GONE);
        tvEarlyWinsPlayer.setVisibility(View.GONE);
        tvEarlyWinsOpponent.setVisibility(View.GONE);
        title.setText(view.getContext().getString(R.string.title_run_outs));

        setVisibilities(view, detail);
    }

    @Override protected void setVisibilities(View view, Match.StatsDetail detail) {

    }

    @Override public void bind(T player, T opponent) {
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
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_runs;
    }
}
