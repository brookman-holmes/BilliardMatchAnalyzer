package com.brookmanholmes.billiardmatchanalyzer.adaptervh;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public abstract class MatchInfoHolder extends BaseViewHolder {
    int collapsedHeight = (int) Math.floor(48 * Resources.getSystem().getDisplayMetrics().density);
    @Bind(R.id.container) GridLayout container;
    @Bind(R.id.collapseExpandButton) ImageView collapseExpandButton;
    @Bind(R.id.card_title) TextView title;
    int expandedHeight = 0;

    public MatchInfoHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        collapseExpandButton.setTag(true);
    }

    @OnClick(R.id.infoButton) public void onClickInfoButton() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext(), R.style.AlertDialogTheme);
        builder.setView(LayoutInflater.from(itemView.getContext()).inflate(getLayoutRes(), null))
                .setPositiveButton(android.R.string.ok, null).create().show();
    }

    @LayoutRes abstract int getLayoutRes();

    @OnClick(R.id.collapseExpandButton) public void onClick() {
        if (isViewExpanded()) {
            if (expandedHeight < itemView.getHeight())
                expandedHeight = itemView.getHeight();
            container.animate().alpha(0f).withEndAction(new Runnable() {
                @Override public void run() {
                    container.setVisibility(View.GONE);
                }
            });
            animateCollapseExpand(itemView, expandedHeight, collapsedHeight);
        } else {
            container.animate().alpha(1f).withStartAction(new Runnable() {
                @Override public void run() {
                    container.setVisibility(View.VISIBLE);
                }
            });
            animateCollapseExpand(itemView, collapsedHeight, expandedHeight);
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

    <S extends Number> void highlightBetterPlayerStats(TextView playerStatView, TextView opponentStatView, S playerStat, S opponentStat) {
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

    <S extends Number> void highlightPlayerStat(TextView playerStatView, TextView opponentStatView, S playerStat, S opponentStat) {
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

    public abstract void bind(AbstractPlayer player, AbstractPlayer opponent);

    protected abstract void setVisibilities(View view, Match.StatsDetail detail);
}
