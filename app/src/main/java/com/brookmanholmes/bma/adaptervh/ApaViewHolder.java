package com.brookmanholmes.bma.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.IApa;
import com.brookmanholmes.bma.R;

import java.util.Locale;

import butterknife.Bind;

/**
 * Created by helios on 4/12/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ApaViewHolder extends MatchInfoHolder{
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
    @Bind(R.id.tvDeadBallsOpponent) TextView tvDeadBalls;
    @Bind(R.id.tvDeadBallsTitle) TextView tvDeadBallsTitle;

    public ApaViewHolder(View view, Match.StatsDetail detail) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_apa_stats));
        setVisibilities(view, detail);
    }

    @Override protected void setVisibilities(View view, Match.StatsDetail detail) {

    }

    @Override public void bind(AbstractPlayer player, AbstractPlayer opponent) {
        if (player instanceof ApaEightBallPlayer) {
            tvPointsTitle.setText(itemView.getContext().getString(R.string.title_games_needed));
        } else if (player instanceof ApaNineBallPlayer) {
            tvDeadBalls.setVisibility(View.VISIBLE);
            tvDeadBallsTitle.setVisibility(View.VISIBLE);
            tvDeadBalls.setText(String.format(Locale.getDefault(), "%d", ((ApaNineBallPlayer) player).getDeadBalls()));
        }

        if (player instanceof IApa && opponent instanceof IApa) {
            tvPointsPlayer.setText(itemView.getContext().getString(R.string.out_of, ((IApa) player).getPoints(), ((IApa) player).getPointsNeeded(((IApa) opponent).getRank())));
            tvPointsOpponent.setText(itemView.getContext().getString(R.string.out_of, ((IApa) opponent).getPoints(), ((IApa) opponent).getPointsNeeded(((IApa) player).getRank())));
            tvRankPlayer.setText(String.format(Locale.getDefault(), "%d", ((IApa) player).getRank()));
            tvRankOpponent.setText(String.format(Locale.getDefault(), "%d", ((IApa) opponent).getRank()));

            tvMatchPointsPlayer.setText(String.format(Locale.getDefault(), "%d", ((IApa) player).getMatchPoints(((IApa) opponent).getPoints(), ((IApa) opponent).getRank())));

            tvMatchPointsOpponent.setText(String.format(Locale.getDefault(), "%d", ((IApa) opponent).getMatchPoints(((IApa) opponent).getPoints(), ((IApa) opponent).getRank())));

            tvDefensiveShotsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getSafetyAttempts()));
            tvDefensiveShotsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getSafetyAttempts()));
        }
    }

    public void setTvInningsOpponent(int innings) {
        tvInningsOpponent.setText(String.format(Locale.getDefault(), "%d", innings));
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_apa;
    }
}
