package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.interfaces.Apa;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
public class ApaPlayer<T extends AbstractPlayer & Apa> extends MatchInfoHolder<T> {
    @Bind(R.id.tvInningsOpponent) TextView tvInningsOpponent;
    @Bind(R.id.tvDefensiveShotsPlayer) TextView tvDefensiveShotsPlayer;
    @Bind(R.id.tvDefensiveShotsOpponent) TextView tvDefensiveShotsOpponent;
    @Bind(R.id.tvPlayerPoints) TextView tvPointsPlayer;
    @Bind(R.id.tvOpponentPoints) TextView tvPointsOpponent;
    @Bind(R.id.tvPointsTitle) TextView tvPointsTitle;
    @Bind(R.id.tvMatchPointsPlayer) TextView tvMatchPointsPlayer;
    @Bind(R.id.tvMatchPointsOpponent) TextView tvMatchPointsOpponent;
    @Bind(R.id.tvRankPlayer) TextView tvRankPlayer;
    @Bind(R.id.tvRankOpponent) TextView tvRankOpponent;

    public ApaPlayer(View view, Match.StatsDetail detail) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_apa_stats));
        setVisibilities(view, detail);
    }

    @Override
    protected void setVisibilities(View view, Match.StatsDetail detail) {

    }

    @Override
    public void bind(T player, T opponent) {
        if (player instanceof ApaEightBallPlayer) {
            tvPointsTitle.setText(itemView.getContext().getString(R.string.title_games_needed));
        }

        tvPointsPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getPoints(), player.getPointsNeeded(opponent.getRank())));
        tvPointsOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getPoints(), opponent.getPointsNeeded(player.getRank())));
        tvRankPlayer.setText(String.format("%d", player.getRank()));
        tvRankOpponent.setText(String.format("%d", opponent.getRank()));

        tvMatchPointsPlayer.setText(String.format("%d", player.getMatchPoints(opponent.getPoints(), opponent.getRank())));

        tvMatchPointsOpponent.setText(String.format("%d", opponent.getMatchPoints(opponent.getPoints(), opponent.getRank())));

        tvInningsOpponent.setText(String.format("%d", opponent.getTurns()));
        tvDefensiveShotsOpponent.setText(String.format("%d", opponent.getSafetyAttempts()));
        tvDefensiveShotsPlayer.setText(String.format("%d", player.getSafetyAttempts()));
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_apa;
    }
}
