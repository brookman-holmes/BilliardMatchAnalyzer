package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.player.AbstractPlayer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public abstract class MatchInfoHolder<T extends AbstractPlayer> extends RecyclerView.ViewHolder {
    final View card_layout;
    final int collapsedHeight = (int) Math.floor(72 * Resources.getSystem().getDisplayMetrics().density);
    @Bind(R.id.container)
    GridLayout container;
    @Bind(R.id.infoButton)
    ImageView infoButton;
    @Bind(R.id.collapseExpandButton)
    ImageView collapseExpandButton;
    @Bind(R.id.card_title)
    TextView title;
    int viewType;
    int expandedHeight = 0;

    public MatchInfoHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        card_layout = view;
        collapseExpandButton.setTag(true);
    }

    @OnClick(R.id.collapseExpandButton)
    public void onClick() {
        if (isViewExpanded()) {
            if (expandedHeight < card_layout.getHeight())
                expandedHeight = card_layout.getHeight();
            container.animate().alpha(0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    container.setVisibility(View.GONE);
                }
            });
            animateCollapseExpand(card_layout, expandedHeight, collapsedHeight);
        } else {
            container.animate().alpha(1f).withStartAction(new Runnable() {
                @Override
                public void run() {
                    container.setVisibility(View.VISIBLE);
                }
            });
            animateCollapseExpand(card_layout, collapsedHeight, expandedHeight);
        }

        collapseExpandButton.animate().rotationXBy(180f);

        setViewExpanded(!isViewExpanded());
    }

    private boolean isViewExpanded() {
        return (boolean) collapseExpandButton.getTag();
    }

    private void setViewExpanded(boolean tag) {
        collapseExpandButton.setTag(tag);
    }

    private void animateCollapseExpand(final View view, int from, int to) {
        int alphaDuration = 200;
        int scaleDuration = 300;

        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(scaleDuration);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.start();
        container.animate().alpha(1f).setDuration(alphaDuration).setInterpolator(new DecelerateInterpolator());
    }

    public abstract void bind(T player, T opponent);

    public static class MatchOverviewHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
        final TextView tvWinPctPlayer;
        final TextView tvWinPctOpponent;
        final TextView tvWinTotalPlayer;
        final TextView tvWinTotalOpponent;
        final TextView tvAggressivenessRatingPlayer;
        final TextView tvAggressivenessRatingOpponent;
        final TextView tvTSPPlayer;
        final TextView tvTSPOpponent;
        final TextView tvTotalShotsPlayer;
        final TextView tvTotalShotsOpponent;

        public MatchOverviewHolder(View view) {
            super(view);
            title.setText("Match Overview");

            tvWinPctPlayer = (TextView) view.findViewById(R.id.tvWinPercentPlayer);
            tvWinPctOpponent = (TextView) view.findViewById(R.id.tvWinPercentOpponent);
            tvWinTotalPlayer = (TextView) view.findViewById(R.id.tvWinTotalsPlayer);
            tvWinTotalOpponent = (TextView) view.findViewById(R.id.tvWinTotalsOpponent);
            tvAggressivenessRatingOpponent = (TextView) view.findViewById(R.id.tvAggressivenessRatingOpponent);
            tvAggressivenessRatingPlayer = (TextView) view.findViewById(R.id.tvAggressivenessRatingPlayer);
            tvTSPOpponent = (TextView) view.findViewById(R.id.tvTrueShootingPctOpponent);
            tvTSPPlayer = (TextView) view.findViewById(R.id.tvTrueShootingPctPlayer);
            tvTotalShotsOpponent = (TextView) view.findViewById(R.id.tvTotalShotsOpponent);
            tvTotalShotsPlayer = (TextView) view.findViewById(R.id.tvTotalShotsPlayer);
            viewType = MatchInfoRecyclerAdapter.ITEM_MATCH_OVERVIEW;
        }

        @Override
        public void bind(T player, T opponent) {
            // Games Won Percentage
            tvWinPctPlayer.setText(player.getWinPct());
            tvWinPctOpponent.setText(opponent.getWinPct());
            // highlighting of the player who's doing better in this stat
            if (player.getWins() > opponent.getWins()) {
                tvWinPctPlayer.setTypeface(null, Typeface.BOLD);
                tvWinPctOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (opponent.getWins() > player.getWins()) {
                tvWinPctOpponent.setTypeface(null, Typeface.BOLD);
                tvWinPctPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvWinPctPlayer.setTypeface(null, Typeface.NORMAL);
                tvWinPctOpponent.setTypeface(null, Typeface.NORMAL);
            }

            // Games Won x/y
            tvWinTotalPlayer.setText(player.getWins() + "/" + player.getGamesPlayed());
            tvWinTotalOpponent.setText(opponent.getWins() + "/" + opponent.getGamesPlayed());

            tvTSPPlayer.setText(player.getTrueShootingPct());
            tvTSPOpponent.setText(opponent.getTrueShootingPct());
            tvAggressivenessRatingPlayer.setText(player.getAggressivenessRating());
            tvAggressivenessRatingOpponent.setText(opponent.getAggressivenessRating());

            // highlighting of the player who's doing better in this stat
            if (Double.compare(Double.parseDouble(player.getTrueShootingPct()), Double.parseDouble(opponent.getTrueShootingPct())) == 1) {
                tvTSPPlayer.setTypeface(null, Typeface.BOLD);
                tvTSPOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (Double.compare(Double.parseDouble(player.getTrueShootingPct()), Double.parseDouble(opponent.getTrueShootingPct())) == -1) {
                tvTSPOpponent.setTypeface(null, Typeface.BOLD);
                tvTSPPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvTSPPlayer.setTypeface(null, Typeface.NORMAL);
                tvTSPOpponent.setTypeface(null, Typeface.NORMAL);
            }

            tvTotalShotsPlayer.setText(player.getShotsSucceeded() + "/" + player.getShotsAttempted());
            tvTotalShotsOpponent.setText(opponent.getShotsSucceeded() + "/" + opponent.getShotsAttempted());
        }
    }

    public static class ShootingPctHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
        final TextView tvShootingPctPlayer;
        final TextView tvShootingPctOpponent;
        final TextView tvBallTotalPlayer;
        final TextView tvBallTotalOpponent;
        final TextView tvAvgBallsTurnPlayer;
        final TextView tvAvgBallsTurnOpponent;
        final TextView tvScratchesPlayer;
        final TextView tvScratchesOpponent;

        public ShootingPctHolder(View view) {
            super(view);
            tvShootingPctPlayer = (TextView) view.findViewById(R.id.tvShootingPctPlayer);
            tvShootingPctOpponent = (TextView) view.findViewById(R.id.tvShootingPctOpponent);
            tvBallTotalPlayer = (TextView) view.findViewById(R.id.tvBallTotalPlayer);
            tvBallTotalOpponent = (TextView) view.findViewById(R.id.tvBallTotalOpponent);
            tvAvgBallsTurnPlayer = (TextView) view.findViewById(R.id.tvAvgBallsTurnPlayer);
            tvAvgBallsTurnOpponent = (TextView) view.findViewById(R.id.tvAvgBallsTurnOpponent);
            tvScratchesPlayer = (TextView) view.findViewById(R.id.tvScratchesPlayer);
            tvScratchesOpponent = (TextView) view.findViewById(R.id.tvScratchesOpponent);
        }

        @Override
        public void bind(T player, T opponent) {
            title.setText("Shooting");
            // Shooting Percentage
            tvShootingPctPlayer.setText(player.getShootingPct());
            tvShootingPctOpponent.setText(opponent.getShootingPct());

            // highlighting of the player who's doing better in this stat
            if (Double.parseDouble(player.getShootingPct()) > Double.parseDouble(opponent.getShootingPct())) {
                tvShootingPctPlayer.setTypeface(null, Typeface.BOLD);
                tvShootingPctOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (Double.parseDouble(opponent.getShootingPct()) > Double.parseDouble(player.getShootingPct())) {
                tvShootingPctOpponent.setTypeface(null, Typeface.BOLD);
                tvShootingPctPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvShootingPctPlayer.setTypeface(null, Typeface.NORMAL);
                tvShootingPctOpponent.setTypeface(null, Typeface.NORMAL);
            }

            // Shots succeeded / shots attempted
            tvBallTotalPlayer.setText(player.getShootingBallsMade() + "/" + (player.getShootingAttempts()));
            tvBallTotalOpponent.setText(opponent.getShootingBallsMade() + "/" + (opponent.getShootingAttempts()));

            // Average balls / turn
            tvAvgBallsTurnPlayer.setText(player.getAvgBallsTurn());
            tvAvgBallsTurnOpponent.setText(opponent.getAvgBallsTurn());

            if (Double.parseDouble(player.getAvgBallsTurn()) > Double.parseDouble(opponent.getAvgBallsTurn())) {
                tvAvgBallsTurnPlayer.setTypeface(null, Typeface.BOLD);
                tvAvgBallsTurnOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (Double.parseDouble(opponent.getAvgBallsTurn()) > Double.parseDouble(player.getAvgBallsTurn())) {
                tvAvgBallsTurnOpponent.setTypeface(null, Typeface.BOLD);
                tvAvgBallsTurnPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvAvgBallsTurnPlayer.setTypeface(null, Typeface.NORMAL);
                tvAvgBallsTurnOpponent.setTypeface(null, Typeface.NORMAL);
            }
            // Scratches
            tvScratchesPlayer.setText(String.valueOf(player.getShootingScratches()));
            tvScratchesOpponent.setText(String.valueOf(opponent.getShootingScratches()));

            // highlight the player with fewer scratches
            if (player.getShootingScratches() < opponent.getShootingScratches()) {
                tvScratchesPlayer.setTypeface(null, Typeface.BOLD);
                tvScratchesOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (player.getShootingScratches() > opponent.getShootingScratches()) {
                tvScratchesPlayer.setTypeface(null, Typeface.NORMAL);
                tvScratchesOpponent.setTypeface(null, Typeface.BOLD);
            } else {
                tvScratchesPlayer.setTypeface(null, Typeface.NORMAL);
                tvScratchesOpponent.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public static class SafetiesHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
        final TextView tvSafetiesAttemptedPlayer;
        final TextView tvSafetiesAttemptedOpponent;
        final TextView tvSafetyPctPlayer;
        final TextView tvSafetyPctOpponent;
        final TextView tvSafetyScratchesPlayer;
        final TextView tvSafetyScratchesOpponent;
        final TextView tvSafetyReturnsPlayer;
        final TextView tvSafetyReturnsOpponent;
        final TextView tvSafetyEscapesPlayer;
        final TextView tvSafetyEscapesOpponent;
        final TextView tvForcedErrorsPlayer, tvForcedErrorsOpponent;

        public SafetiesHolder(View view) {
            super(view);
            tvSafetiesAttemptedPlayer = (TextView) view.findViewById(R.id.tvSafetiesAttemptedPlayer);
            tvSafetiesAttemptedOpponent = (TextView) view.findViewById(R.id.tvSafetiesAttemptedOpponent);
            tvSafetyPctPlayer = (TextView) view.findViewById(R.id.tvSafetyPctPlayer);
            tvSafetyPctOpponent = (TextView) view.findViewById(R.id.tvSafetyPctOpponent);
            tvSafetyScratchesOpponent = (TextView) view.findViewById(R.id.tvSafetyScratchesOpponent);
            tvSafetyScratchesPlayer = (TextView) view.findViewById(R.id.tvSafetyScratchesPlayer);
            tvSafetyReturnsPlayer = (TextView) view.findViewById(R.id.tvSafetyReturnsPlayer);
            tvSafetyEscapesPlayer = (TextView) view.findViewById(R.id.tvSafetyEscapesPlayer);
            tvSafetyReturnsOpponent = (TextView) view.findViewById(R.id.tvSafetyReturnsOpponent);
            tvSafetyEscapesOpponent = (TextView) view.findViewById(R.id.tvSafetyEscapesOpponent);
            tvForcedErrorsOpponent = (TextView) view.findViewById(R.id.tvForcedErrorsOpponent);
            tvForcedErrorsPlayer = (TextView) view.findViewById(R.id.tvForcedErrorsPlayer);
        }

        @Override
        public void bind(T player, T opponent) {
            title.setText("Safeties");
            // Safety Percentage
            tvSafetyPctPlayer.setText(player.getSafetyPct());
            tvSafetyPctOpponent.setText(opponent.getSafetyPct());

            // highlighting of the player who's doing better in this stat
            if (Double.parseDouble(player.getSafetyPct()) > Double.parseDouble(opponent.getSafetyPct())) {
                tvSafetyPctPlayer.setTypeface(null, Typeface.BOLD);
                tvSafetyPctOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (Double.parseDouble(opponent.getSafetyPct()) > Double.parseDouble(player.getSafetyPct())) {
                tvSafetyPctOpponent.setTypeface(null, Typeface.BOLD);
                tvSafetyPctPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvSafetyPctPlayer.setTypeface(null, Typeface.NORMAL);
                tvSafetyPctOpponent.setTypeface(null, Typeface.NORMAL);
            }

            // Safeties made / safeties attempted
            tvSafetiesAttemptedPlayer.setText(player.getSafetySuccesses() + "/" + player.getSafetyAttempts());
            tvSafetiesAttemptedOpponent.setText(opponent.getSafetySuccesses() + "/" + opponent.getSafetyAttempts());

            tvSafetyScratchesOpponent.setText(opponent.getSafetyScratches() + "");
            tvSafetyScratchesPlayer.setText(player.getSafetyScratches() + "");

            if (player.getSafetyScratches() < opponent.getSafetyScratches()) {
                tvSafetyScratchesOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyScratchesPlayer.setTypeface(null, Typeface.BOLD);
            } else if (player.getSafetyScratches() > opponent.getSafetyScratches()) {
                tvSafetyScratchesOpponent.setTypeface(null, Typeface.BOLD);
                tvSafetyScratchesPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvSafetyScratchesOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyScratchesPlayer.setTypeface(null, Typeface.NORMAL);
            }

            tvSafetyReturnsPlayer.setText(player.getSafetyReturns() + "");
            tvSafetyReturnsOpponent.setText(opponent.getSafetyReturns() + "");

            if (player.getSafetyReturns() > opponent.getSafetyReturns()) {
                tvSafetyReturnsOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyReturnsPlayer.setTypeface(null, Typeface.BOLD);
            } else if (player.getSafetyReturns() < opponent.getSafetyReturns()) {
                tvSafetyReturnsOpponent.setTypeface(null, Typeface.BOLD);
                tvSafetyReturnsPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvSafetyReturnsOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyReturnsPlayer.setTypeface(null, Typeface.NORMAL);
            }

            tvSafetyEscapesPlayer.setText(player.getSafetyEscapes() + "");
            tvSafetyEscapesOpponent.setText(opponent.getSafetyEscapes() + "");

            if (player.getSafetyEscapes() > opponent.getSafetyEscapes()) {
                tvSafetyEscapesOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyEscapesPlayer.setTypeface(null, Typeface.BOLD);
            } else if (player.getSafetyEscapes() < opponent.getSafetyEscapes()) {
                tvSafetyEscapesOpponent.setTypeface(null, Typeface.BOLD);
                tvSafetyEscapesPlayer.setTypeface(null, Typeface.NORMAL);
            } else {
                tvSafetyEscapesOpponent.setTypeface(null, Typeface.NORMAL);
                tvSafetyEscapesPlayer.setTypeface(null, Typeface.NORMAL);
            }

            tvForcedErrorsOpponent.setText(opponent.getSafetyForcedErrors() + "");
            tvForcedErrorsPlayer.setText(player.getSafetyForcedErrors() + "");
            if (player.getSafetyForcedErrors() > opponent.getSafetyForcedErrors()) {
                tvForcedErrorsPlayer.setTypeface(null, Typeface.BOLD);
                tvForcedErrorsOpponent.setTypeface(null, Typeface.NORMAL);
            } else if (player.getSafetyForcedErrors() < opponent.getSafetyForcedErrors()) {
                tvForcedErrorsPlayer.setTypeface(null, Typeface.NORMAL);
                tvForcedErrorsOpponent.setTypeface(null, Typeface.BOLD);
            } else {
                tvForcedErrorsOpponent.setTypeface(null, Typeface.NORMAL);
                tvForcedErrorsPlayer.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
}
