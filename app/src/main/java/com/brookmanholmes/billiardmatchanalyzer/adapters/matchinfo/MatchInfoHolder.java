package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.interfaces.Apa;
import com.brookmanholmes.billiards.player.interfaces.EarlyWins;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public abstract class MatchInfoHolder<T extends AbstractPlayer> extends BaseViewHolder<T> {
    final View card_layout;
    final int collapsedCardHeight = 48;
    final int collapsedHeight = (int) Math.floor(collapsedCardHeight * Resources.getSystem().getDisplayMetrics().density);
    @Bind(R.id.container) GridLayout container;
    @Bind(R.id.infoButton) ImageView infoButton;
    @Bind(R.id.collapseExpandButton) ImageView collapseExpandButton;
    @Bind(R.id.card_title) TextView title;
    int viewType;
    int expandedHeight = 0;
    View.OnClickListener listener;

    public MatchInfoHolder(View view, Match.StatsDetail detailLevel, View.OnClickListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        card_layout = view;
        collapseExpandButton.setTag(true);
        infoButton.setTag(view.getTag());
        this.listener = listener;
    }

    @OnClick(R.id.infoButton) public void onClickInfoButton(View view) {
        listener.onClick(view);
    }

    @OnClick(R.id.collapseExpandButton) public void onClick() {
        if (isViewExpanded()) {
            if (expandedHeight < card_layout.getHeight())
                expandedHeight = card_layout.getHeight();
            container.animate().alpha(0f).withEndAction(new Runnable() {
                @Override public void run() {
                    container.setVisibility(View.GONE);
                }
            });
            animateCollapseExpand(card_layout, expandedHeight, collapsedHeight);
        } else {
            container.animate().alpha(1f).withStartAction(new Runnable() {
                @Override public void run() {
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
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.start();
        container.animate().alpha(1f).setDuration(alphaDuration).setInterpolator(new DecelerateInterpolator());
    }

    public <S extends Number> void highlightBetterPlayerStats(TextView playerStatView, TextView opponentStatView, S playerStat, S opponentStat) {
        if (Double.compare(playerStat.doubleValue(), opponentStat.doubleValue()) == 1) {
            playerStatView.setTypeface(null, Typeface.BOLD);
            opponentStatView.setTypeface(null, Typeface.NORMAL);
        } else if (Double.compare(playerStat.doubleValue(), opponentStat.doubleValue()) == -1) {
            opponentStatView.setTypeface(null, Typeface.BOLD);
            playerStatView.setTypeface(null, Typeface.NORMAL);
        } else {
            playerStatView.setTypeface(null, Typeface.NORMAL);
            opponentStatView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public <S extends Number> void highlightPlayerStat(TextView playerStatView, TextView opponentStatView, S playerStat, S opponentStat) {
        if (Double.compare(playerStat.doubleValue(), opponentStat.doubleValue()) == -1) {
            playerStatView.setTypeface(null, Typeface.BOLD);
            opponentStatView.setTypeface(null, Typeface.NORMAL);
        } else if (Double.compare(playerStat.doubleValue(), opponentStat.doubleValue()) == 1) {
            opponentStatView.setTypeface(null, Typeface.BOLD);
            playerStatView.setTypeface(null, Typeface.NORMAL);
        } else {
            playerStatView.setTypeface(null, Typeface.NORMAL);
            opponentStatView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public abstract void bind(T player, T opponent);

    protected abstract void setVisibilities(View view, Match.StatsDetail detail);

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

        public MatchOverviewHolder(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
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

            setVisibilities(view, detail);
        }

        @Override protected void setVisibilities(View view, Match.StatsDetail detail) {
            if (detail == Match.StatsDetail.SIMPLE) {
                tvAggressivenessRatingOpponent.setVisibility(View.GONE);
                tvAggressivenessRatingPlayer.setVisibility(View.GONE);
                view.findViewById(R.id.tvAggressivenessRating).setVisibility(View.GONE);
                view.findViewById(R.id.tvTotalShotsTitle).setVisibility(View.GONE);
                tvTotalShotsOpponent.setVisibility(View.GONE);
                tvTotalShotsPlayer.setVisibility(View.GONE);
            }
        }

        @Override public void bind(T player, T opponent) {
            // Games Won Percentage
            tvWinPctPlayer.setText(player.getWinPct());
            tvWinPctOpponent.setText(opponent.getWinPct());
            // highlighting of the player who's doing better in this stat

            highlightBetterPlayerStats(tvWinPctPlayer, tvWinPctOpponent, player.getWins(), opponent.getWins());

            // Games Won x/y
            tvWinTotalPlayer.setText(player.getWins() + "/" + player.getGamesPlayed());
            tvWinTotalOpponent.setText(opponent.getWins() + "/" + opponent.getGamesPlayed());

            tvTSPPlayer.setText(player.getTrueShootingPct());
            tvTSPOpponent.setText(opponent.getTrueShootingPct());
            tvAggressivenessRatingPlayer.setText(player.getAggressivenessRating());
            tvAggressivenessRatingOpponent.setText(opponent.getAggressivenessRating());

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvTSPPlayer, tvTSPOpponent, Double.parseDouble(player.getTrueShootingPct()), Double.parseDouble(opponent.getTrueShootingPct()));

            tvTotalShotsPlayer.setText(player.getShotsSucceededOfAllTypes() + "/" + player.getShotAttemptsOfAllTypes());
            tvTotalShotsOpponent.setText(opponent.getShotsSucceededOfAllTypes() + "/" + opponent.getShotAttemptsOfAllTypes());
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

        public ShootingPctHolder(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
            tvShootingPctPlayer = (TextView) view.findViewById(R.id.tvShootingPctPlayer);
            tvShootingPctOpponent = (TextView) view.findViewById(R.id.tvShootingPctOpponent);
            tvBallTotalPlayer = (TextView) view.findViewById(R.id.tvBallTotalPlayer);
            tvBallTotalOpponent = (TextView) view.findViewById(R.id.tvBallTotalOpponent);
            tvAvgBallsTurnPlayer = (TextView) view.findViewById(R.id.tvAvgBallsTurnPlayer);
            tvAvgBallsTurnOpponent = (TextView) view.findViewById(R.id.tvAvgBallsTurnOpponent);
            tvScratchesPlayer = (TextView) view.findViewById(R.id.tvScratchesPlayer);
            tvScratchesOpponent = (TextView) view.findViewById(R.id.tvScratchesOpponent);

            setVisibilities(view, detail);
        }

        @Override protected void setVisibilities(View view, Match.StatsDetail detail) {

        }

        @Override public void bind(T player, T opponent) {
            title.setText("Shooting");
            // Shooting Percentage
            tvShootingPctPlayer.setText(player.getShootingPct());
            tvShootingPctOpponent.setText(opponent.getShootingPct());

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvShootingPctPlayer, tvShootingPctOpponent, Double.parseDouble(player.getShootingPct()), Double.parseDouble(opponent.getShootingPct()));

            // Shots succeeded / shots attempted
            tvBallTotalPlayer.setText(player.getShootingBallsMade() + "/" + (player.getShootingAttempts()));
            tvBallTotalOpponent.setText(opponent.getShootingBallsMade() + "/" + (opponent.getShootingAttempts()));

            // Average balls / turn
            tvAvgBallsTurnPlayer.setText(player.getAvgBallsTurn());
            tvAvgBallsTurnOpponent.setText(opponent.getAvgBallsTurn());

            highlightBetterPlayerStats(tvAvgBallsTurnPlayer, tvAvgBallsTurnOpponent, Double.parseDouble(player.getAvgBallsTurn()), Double.parseDouble(opponent.getAvgBallsTurn()));

            // Scratches
            tvScratchesPlayer.setText(String.valueOf(player.getShootingScratches()));
            tvScratchesOpponent.setText(String.valueOf(opponent.getShootingScratches()));

            // highlight the player with fewer scratches
            highlightPlayerStat(tvScratchesPlayer, tvScratchesOpponent, player.getShootingScratches(), opponent.getShootingScratches());
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

        public SafetiesHolder(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
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

            setVisibilities(view, detail);
        }

        @Override protected void setVisibilities(View view, Match.StatsDetail detail) {
            if (detail == Match.StatsDetail.SIMPLE) {
                tvSafetyReturnsPlayer.setVisibility(View.GONE);
                tvSafetyEscapesPlayer.setVisibility(View.GONE);
                tvSafetyReturnsOpponent.setVisibility(View.GONE);
                tvSafetyEscapesOpponent.setVisibility(View.GONE);
                tvForcedErrorsOpponent.setVisibility(View.GONE);
                tvForcedErrorsPlayer.setVisibility(View.GONE);

                view.findViewById(R.id.tvForcedErrorsTitle).setVisibility(View.GONE);
                view.findViewById(R.id.tvSafetyEscapesTitle).setVisibility(View.GONE);
                view.findViewById(R.id.tvSafetyReturnsTitle).setVisibility(View.GONE);
            }
        }

        @Override public void bind(T player, T opponent) {
            title.setText("Safeties");
            // Safety Percentage
            tvSafetyPctPlayer.setText(player.getSafetyPct());
            tvSafetyPctOpponent.setText(opponent.getSafetyPct());

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvSafetyPctPlayer, tvSafetyPctOpponent, Double.parseDouble(player.getSafetyPct()), Double.parseDouble(opponent.getSafetyPct()));

            // Safeties made / safeties attempted
            tvSafetiesAttemptedPlayer.setText(player.getSafetySuccesses() + "/" + player.getSafetyAttempts());
            tvSafetiesAttemptedOpponent.setText(opponent.getSafetySuccesses() + "/" + opponent.getSafetyAttempts());

            tvSafetyScratchesOpponent.setText(opponent.getSafetyScratches() + "");
            tvSafetyScratchesPlayer.setText(player.getSafetyScratches() + "");

            highlightPlayerStat(tvSafetyScratchesPlayer, tvSafetyScratchesOpponent, player.getSafetyScratches(), opponent.getSafetyScratches());

            tvSafetyReturnsPlayer.setText(player.getSafetyReturns() + "");
            tvSafetyReturnsOpponent.setText(opponent.getSafetyReturns() + "");

            highlightBetterPlayerStats(tvSafetyReturnsPlayer, tvSafetyReturnsOpponent, player.getSafetyReturns(), opponent.getSafetyReturns());

            tvSafetyEscapesPlayer.setText(player.getSafetyEscapes() + "");
            tvSafetyEscapesOpponent.setText(opponent.getSafetyEscapes() + "");
            highlightBetterPlayerStats(tvSafetyEscapesPlayer, tvSafetyEscapesOpponent, player.getSafetyEscapes(), opponent.getSafetyEscapes());

            tvForcedErrorsOpponent.setText(opponent.getSafetyForcedErrors() + "");
            tvForcedErrorsPlayer.setText(player.getSafetyForcedErrors() + "");
            highlightPlayerStat(tvForcedErrorsPlayer, tvForcedErrorsOpponent, player.getSafetyForcedErrors(), opponent.getSafetyForcedErrors());
        }
    }

    public static class RunOutsHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
        final TextView tvBreakAndRunPlayer;
        final TextView tvBreakAndRunOpponent;
        final TextView tvMaxRunPlayer;
        final TextView tvMaxRunOpponent;
        final TextView tvFiveBallRunsPlayer;
        final TextView tvFiveBallRunsOpponent;
        final TextView tvEarlyWins;
        final TextView tvEarlyWinsPlayer;
        final TextView tvEarlyWinsOpponent;
        TextView tvMaxBallRunsTitle;

        public RunOutsHolder(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
            tvBreakAndRunPlayer = (TextView) view.findViewById(R.id.tvMaxBallRunsPlayer);
            tvBreakAndRunOpponent = (TextView) view.findViewById(R.id.tvMaxBallRunsOpponent);
            tvMaxRunPlayer = (TextView) view.findViewById(R.id.tvTierOneRunsPlayer);
            tvMaxRunOpponent = (TextView) view.findViewById(R.id.tvTierOneRunsOpponent);
            tvFiveBallRunsPlayer = (TextView) view.findViewById(R.id.tvFiveBallRunsPlayer);
            tvFiveBallRunsOpponent = (TextView) view.findViewById(R.id.tvFiveBallRunsOpponent);
            tvEarlyWins = (TextView) view.findViewById(R.id.tvEarlyWinsTitle);
            tvEarlyWinsPlayer = (TextView) view.findViewById(R.id.tvEarlyWinsPlayer);
            tvEarlyWinsOpponent = (TextView) view.findViewById(R.id.tvEarlyWinsOpponent);

            tvEarlyWins.setVisibility(View.GONE);
            tvEarlyWinsPlayer.setVisibility(View.GONE);
            tvEarlyWinsOpponent.setVisibility(View.GONE);

            setVisibilities(view, detail);
        }

        @Override protected void setVisibilities(View view, Match.StatsDetail detail) {

        }

        @Override public void bind(T player, T opponent) {
            title.setText("Run Outs");
            // Break and runs
            tvBreakAndRunPlayer.setText(String.format("%d", player.getRunOuts()));
            tvBreakAndRunOpponent.setText(String.format("%d", opponent.getRunOuts()));

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvBreakAndRunPlayer, tvBreakAndRunOpponent, player.getRunOuts(), opponent.getRunOuts());

            // table runs
            tvMaxRunPlayer.setText(String.format("%d", player.getRunTierOne()));
            tvMaxRunOpponent.setText(String.format("%d", opponent.getRunTierOne()));

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvMaxRunPlayer, tvMaxRunOpponent, player.getRunTierOne(), opponent.getRunTierOne());

            // 5+ ball runs
            tvFiveBallRunsPlayer.setText(String.format("%d", player.getRunTierTwo()));
            tvFiveBallRunsOpponent.setText(String.format("%d", opponent.getRunTierTwo()));

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvFiveBallRunsPlayer, tvFiveBallRunsOpponent, player.getRunTierTwo(), opponent.getRunTierTwo());
        }
    }

    public static class RunOutsWithEarlyWinsHolder<T extends AbstractPlayer & EarlyWins> extends RunOutsHolder<T> {
        public RunOutsWithEarlyWinsHolder(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);

            tvEarlyWins.setVisibility(View.VISIBLE);
            tvEarlyWinsPlayer.setVisibility(View.VISIBLE);
            tvEarlyWinsOpponent.setVisibility(View.VISIBLE);
        }

        @Override public void bind(T player, T opponent) {
            super.bind(player, opponent);

            tvEarlyWinsPlayer.setText(String.format("%d", player.getEarlyWins()));
            tvEarlyWinsOpponent.setText(String.format("%d", opponent.getEarlyWins()));

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvEarlyWinsPlayer, tvEarlyWinsOpponent, player.getEarlyWins(), opponent.getEarlyWins());
        }
    }

    public static class BreaksHolder<T extends AbstractPlayer> extends MatchInfoHolder<T> {
        final TextView tvBreakBallsPlayer;
        final TextView tvBreakBallsOpponent;
        final TextView tvBreakWinsPlayer;
        final TextView tvBreakWinsOpponent;
        final TextView tvBreakContinuationsPlayer;
        final TextView tvBreakContinuationsOpponent;
        final TextView tvBreakTotalPlayer;
        final TextView tvBreakTotalOpponent;
        final TextView tvEarlyWinsPlayer;
        final TextView tvEarlyWinsOpponent;
        final TextView tvBreakScratchesPlayer;
        final TextView tvBreakScratchesOpponent;
        final TextView breakWinsTitle;

        public BreaksHolder(View view, int gameBall, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
            tvBreakBallsPlayer = (TextView) view.findViewById(R.id.tvAvgBreakBallsPlayer);
            tvBreakBallsOpponent = (TextView) view.findViewById(R.id.tvAvgBreakBallsOpponent);
            tvBreakTotalPlayer = (TextView) view.findViewById(R.id.tvBreakTotalPlayer);
            tvBreakTotalOpponent = (TextView) view.findViewById(R.id.tvBreakTotalOpponent);
            tvBreakWinsPlayer = (TextView) view.findViewById(R.id.tvBreakWinsPlayer);
            tvBreakWinsOpponent = (TextView) view.findViewById(R.id.tvBreakWinsOpponent);
            tvBreakContinuationsPlayer = (TextView) view.findViewById(R.id.tvBreakContinuationsPlayer);
            tvBreakContinuationsOpponent = (TextView) view.findViewById(R.id.tvBreakContinuationsOpponent);
            tvBreakScratchesPlayer = (TextView) view.findViewById(R.id.tvBreakScratchesPlayer);
            tvBreakScratchesOpponent = (TextView) view.findViewById(R.id.tvBreakScratchesOpponent);
            tvEarlyWinsPlayer = (TextView) view.findViewById(R.id.tvEarlyWinsPlayer);
            tvEarlyWinsOpponent = (TextView) view.findViewById(R.id.tvEarlyWinsOpponent);
            breakWinsTitle = (TextView) view.findViewById(R.id.tvBreakWinsTitle);
            breakWinsTitle.setText(gameBall + " on the break"); // TODO: 3/24/2016 fix this here

            breakWinsTitle.setVisibility(View.GONE);
            tvBreakWinsPlayer.setVisibility(View.GONE);
            tvBreakWinsOpponent.setVisibility(View.GONE);

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
            title.setText("Breaks");
            // Average balls / break
            tvBreakBallsPlayer.setText(player.getAvgBallsBreak());
            tvBreakBallsOpponent.setText(opponent.getAvgBallsBreak());

            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvBreakBallsPlayer, tvBreakBallsOpponent, Double.parseDouble(player.getAvgBallsBreak()), Double.parseDouble(opponent.getAvgBallsBreak()));

            // Successful breaks / breaks attempted
            tvBreakTotalPlayer.setText(player.getBreakSuccesses() + "/" + player.getBreakAttempts());
            tvBreakTotalOpponent.setText(opponent.getBreakSuccesses() + "/" + opponent.getBreakAttempts());

            // Number of breakContinuations()
            tvBreakContinuationsPlayer.setText(String.valueOf(player.getBreakContinuations()));
            tvBreakContinuationsOpponent.setText(String.valueOf(opponent.getBreakContinuations()));
            highlightBetterPlayerStats(tvBreakContinuationsPlayer, tvBreakContinuationsOpponent, player.getBreakContinuations(), opponent.getBreakContinuations());

            tvBreakScratchesOpponent.setText(String.valueOf(opponent.getBreakScratches()));
            tvBreakScratchesPlayer.setText(String.valueOf(player.getBreakScratches()));
            // highlighting of the player who's doing better in this stat
            highlightPlayerStat(tvBreakScratchesPlayer, tvBreakScratchesOpponent, player.getBreakScratches(), opponent.getBreakScratches());
        }
    }

    public static class BreaksHolderWithBreakWins<T extends AbstractPlayer & WinsOnBreak> extends BreaksHolder<T> {
        public BreaksHolderWithBreakWins(View view, int gameBall, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, gameBall, detail, listener);

            breakWinsTitle.setVisibility(View.VISIBLE);
            tvBreakWinsPlayer.setVisibility(View.VISIBLE);
            tvBreakWinsOpponent.setVisibility(View.VISIBLE);
        }

        @Override public void bind(T player, T opponent) {
            super.bind(player, opponent);

            tvBreakWinsPlayer.setText(Integer.toString(player.getWinsOnBreak()));
            tvBreakWinsOpponent.setText(Integer.toString(opponent.getWinsOnBreak()));
            // highlighting of the player who's doing better in this stat
            highlightBetterPlayerStats(tvBreakWinsPlayer, tvBreakWinsOpponent, player.getWinsOnBreak(), opponent.getWinsOnBreak());
        }
    }

    public static class ApaPlayer<T extends AbstractPlayer & Apa> extends MatchInfoHolder<T> {
        Button gameSummaryButton;
        TextView tvInningsOpponent, tvDefensiveShotsPlayer,
                tvDefensiveShotsOpponent, tvPointsPlayer, tvPointsOpponent;
        TextView tvPointsTitle;
        TextView tvMatchPointsPlayer, tvMatchPointsOpponent;
        TextView tvRankPlayer, tvRankOpponent;

        public ApaPlayer(View view, Match.StatsDetail detail, View.OnClickListener listener) {
            super(view, detail, listener);
            title.setText("APA Stats");
            tvPointsTitle = (TextView) view.findViewById(R.id.tvPointsTitle);

            tvDefensiveShotsPlayer = (TextView) view.findViewById(R.id.tvDefensiveShotsPlayer);
            tvPointsPlayer = (TextView) view.findViewById(R.id.tvPlayerPoints);

            tvInningsOpponent = (TextView) view.findViewById(R.id.tvInningsOpponent);
            tvDefensiveShotsOpponent = (TextView) view.findViewById(R.id.tvDefensiveShotsOpponent);
            tvPointsOpponent = (TextView) view.findViewById(R.id.tvOpponentPoints);

            tvMatchPointsOpponent = (TextView) view.findViewById(R.id.tvMatchPointsOpponent);
            tvMatchPointsPlayer = (TextView) view.findViewById(R.id.tvMatchPointsPlayer);

            tvRankOpponent = (TextView) view.findViewById(R.id.tvRankOpponent);
            tvRankPlayer = (TextView) view.findViewById(R.id.tvRankPlayer);

            setVisibilities(view, detail);
        }

        @Override protected void setVisibilities(View view, Match.StatsDetail detail) {

        }

        @Override public void bind(T player, T opponent) {
            if (player instanceof ApaEightBallPlayer) {
                tvPointsTitle.setText("Games / games needed");
            }

            tvRankPlayer.setText(String.format("%d", player.getRank()));
            tvRankOpponent.setText(String.format("%d", opponent.getRank()));

            tvMatchPointsPlayer.setText(player.getMatchPoints(opponent.getPoints(), opponent.getRank()) + "");
            tvMatchPointsOpponent.setText(opponent.getMatchPoints(opponent.getPoints(), opponent.getRank()) + "");

            tvInningsOpponent.setText(opponent.getTurns() + "");
            tvDefensiveShotsOpponent.setText(Integer.toString(opponent.getSafetyAttempts()));
            tvDefensiveShotsPlayer.setText(Integer.toString(player.getSafetyAttempts()));
        }
    }
}
