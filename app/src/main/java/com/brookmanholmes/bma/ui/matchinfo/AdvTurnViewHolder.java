package com.brookmanholmes.bma.ui.matchinfo;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */
class AdvTurnViewHolder extends MinimalTurnViewHolder {
    @Bind(R.id.tvSafetyPct)
    TextView safetyPct;
    @Bind(R.id.tvBreakPct)
    TextView breakPct;
    @Bind(R.id.tvShootingPct)
    TextView shootingPct;
    @Bind(R.id.ballContainer)
    ViewGroup ballContainer;
    @Bind(R.id.shootingLine)
    ImageView shootingLine;
    @Bind(R.id.safetyLine)
    ImageView safetyLine;
    @Bind(R.id.breakingLine)
    ImageView breakingLine;

    AdvTurnViewHolder(View itemView, Match match) {
        super(itemView, match);
    }

    @Override
    void bind(ITurn turn, PlayerTurn playerTurn, AbstractPlayer player) {
        super.bind(turn, playerTurn, player);
        shootingPct.setText(itemView.getContext().getString(R.string.shooting_pct, player.getShootingPct()));
        safetyPct.setText(itemView.getContext().getString(R.string.safety_pct, player.getSafetyPct()));
        breakPct.setText(itemView.getContext().getString(R.string.breaking_pct, player.getBreakPct()));

        shootingLine.setColorFilter(ConversionUtils.getPctColor(itemView.getContext(), player.getShootingPct()));
        safetyLine.setColorFilter(ConversionUtils.getPctColor(itemView.getContext(), player.getSafetyPct()));
        breakingLine.setColorFilter(ConversionUtils.getPctColor(itemView.getContext(), player.getBreakPct()));

        if (!(player instanceof StraightPoolPlayer)) // set balls for non-straight-pool games
            setBalls(turn);
    }

    void setBalls(ITableStatus tableStatus) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            ImageView childAt = (ImageView) ballContainer.getChildAt(ball - 1);
            Drawable background = DrawableCompat.wrap(childAt.getBackground());
            background.mutate();

            if (ballIsMade(tableStatus.getBallStatus(ball))) {
                childAt.setVisibility(View.VISIBLE);
                background.setTint(getBallColorTint(ball));
            } else if (ballIsDead(tableStatus.getBallStatus(ball))) {
                childAt.setVisibility(View.VISIBLE);
                background.setTint(getColor(R.color.dead_ball));
            } else {
                ballContainer.getChildAt(ball - 1).setVisibility(View.GONE);
            }
        }
    }

    @ColorInt
    private int getBallColorTint(int ball) {
        switch (ball) {
            case 1:
                return getColor(R.color.one_ball);
            case 2:
                return getColor(R.color.two_ball);
            case 3:
                return getColor(R.color.three_ball);
            case 4:
                return getColor(R.color.four_ball);
            case 5:
                return getColor(R.color.five_ball);
            case 6:
                return getColor(R.color.six_ball);
            case 7:
                return getColor(R.color.seven_ball);
            case 8:
                return getColor(R.color.eight_ball);
            case 9:
                return getColor(R.color.one_ball);
            case 10:
                return getColor(R.color.two_ball);
            case 11:
                return getColor(R.color.three_ball);
            case 12:
                return getColor(R.color.four_ball);
            case 13:
                return getColor(R.color.five_ball);
            case 14:
                return getColor(R.color.six_ball);
            case 15:
                return getColor(R.color.seven_ball);
            default:
                throw new IllegalArgumentException("Ball cannot be outside of 1-15, was: " + ball);
        }
    }

    private boolean ballIsMade(BallStatus status) {
        return status == BallStatus.MADE ||
                status == BallStatus.MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
    }

    private boolean ballIsDead(BallStatus status) {
        return status == BallStatus.DEAD ||
                status == BallStatus.DEAD_ON_BREAK ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }
}
