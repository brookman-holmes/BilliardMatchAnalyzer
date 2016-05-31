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
public class BreaksHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
    @Bind(R.id.tvAvgBreakBallsPlayer) TextView tvBreakBallsPlayer;
    @Bind(R.id.tvAvgBreakBallsOpponent) TextView tvBreakBallsOpponent;
    @Bind(R.id.tvBreakWinsPlayer) TextView tvBreakWinsPlayer;
    @Bind(R.id.tvBreakWinsOpponent) TextView tvBreakWinsOpponent;
    @Bind(R.id.tvBreakContinuationsPlayer) TextView tvBreakContinuationsPlayer;
    @Bind(R.id.tvBreakContinuationsOpponent) TextView tvBreakContinuationsOpponent;
    @Bind(R.id.tvBreakTotalPlayer) TextView tvBreakTotalPlayer;
    @Bind(R.id.tvBreakTotalOpponent) TextView tvBreakTotalOpponent;
    @Bind(R.id.tvBreakScratchesPlayer) TextView tvBreakScratchesPlayer;
    @Bind(R.id.tvBreakScratchesOpponent) TextView tvBreakScratchesOpponent;
    @Bind(R.id.tvBreakWinsTitle) TextView breakWinsTitle;

    public BreaksHolder(View view, int gameBall, Match.StatsDetail detail) {
        super(view);
        breakWinsTitle.setText(view.getContext().getString(R.string.title_game_won_on_break, gameBall));

        breakWinsTitle.setVisibility(View.GONE);
        tvBreakWinsPlayer.setVisibility(View.GONE);
        tvBreakWinsOpponent.setVisibility(View.GONE);

        title.setText(view.getContext().getString(R.string.title_breaks));
        setVisibilities(view, detail);
    }

    public BreaksHolder(View view, Match.StatsDetail detail) {
        super(view);
        breakWinsTitle.setText(view.getContext().getString(R.string.title_game_won_on_break, "8/9/10"));

        breakWinsTitle.setVisibility(View.GONE);
        tvBreakWinsPlayer.setVisibility(View.GONE);
        tvBreakWinsOpponent.setVisibility(View.GONE);

        title.setText(view.getContext().getString(R.string.title_breaks));
        setVisibilities(view, detail);
    }

    @Override protected void setVisibilities(View view, Match.StatsDetail detail) {
        if (detail == Match.StatsDetail.SIMPLE) {
            tvBreakContinuationsOpponent.setVisibility(View.GONE);
            tvBreakContinuationsPlayer.setVisibility(View.GONE);
            view.findViewById(R.id.tvBreakContinuationsTitle).setVisibility(View.GONE);
        }
    }

    @Override public void bind(T player, T opponent) {
        // Average balls / break
        tvBreakBallsPlayer.setText(player.getAvgBallsBreak());
        tvBreakBallsOpponent.setText(opponent.getAvgBallsBreak());

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvBreakBallsPlayer, tvBreakBallsOpponent, Double.parseDouble(player.getAvgBallsBreak()), Double.parseDouble(opponent.getAvgBallsBreak()));

        // Successful breaks / breaks attempted
        tvBreakTotalPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getBreakSuccesses(), player.getBreakAttempts()));
        tvBreakTotalOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getBreakSuccesses(), opponent.getBreakAttempts()));

        // Number of breakContinuations()
        tvBreakContinuationsPlayer.setText(String.valueOf(player.getBreakContinuations()));
        tvBreakContinuationsOpponent.setText(String.valueOf(opponent.getBreakContinuations()));
        highlightBetterPlayerStats(tvBreakContinuationsPlayer, tvBreakContinuationsOpponent, player.getBreakContinuations(), opponent.getBreakContinuations());

        tvBreakScratchesOpponent.setText(String.valueOf(opponent.getBreakFouls()));
        tvBreakScratchesPlayer.setText(String.valueOf(player.getBreakFouls()));
        // highlighting of the player who's doing better in this stat
        highlightPlayerStat(tvBreakScratchesPlayer, tvBreakScratchesOpponent, player.getBreakFouls(), opponent.getBreakFouls());
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_breaks;
    }
}