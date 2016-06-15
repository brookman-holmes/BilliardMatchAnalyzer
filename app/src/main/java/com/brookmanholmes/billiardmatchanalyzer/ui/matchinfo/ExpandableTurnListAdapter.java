package com.brookmanholmes.billiardmatchanalyzer.ui.matchinfo;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.utils.RoundedLetterView;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 5/1/2016.
 */
class ExpandableTurnListAdapter extends AbstractExpandableItemAdapter<ExpandableTurnListAdapter.GameViewHolder, ExpandableTurnListAdapter.TurnViewHolder> {
    List<List<Turn>> data = new ArrayList<>(); // list of list of turns, where each list is a group of turns that corresponds to that game
    Match<?> match;

    public ExpandableTurnListAdapter(Match<?> match) {
        this.match = match;
        setHasStableIds(true);
        data = buildDataSource(match.getTurns());
        // can't call scrollToLastItem() here because there is no guarantee that the recyclerview has been created yet
    }

    private static GradientDrawable getTopBackground(@ColorInt int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{2, 2, 2, 2, 0, 0, 0, 0});
        shape.setColor(color);
        return shape;
    }

    private static GradientDrawable getBottomBackground(@ColorInt int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 2, 2, 2, 2});
        shape.setColor(color);
        return shape;
    }

    private static GradientDrawable getCompleteBackground(@ColorInt int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{2, 2, 2, 2, 2, 2, 2, 2});
        shape.setColor(color);
        return shape;
    }

    public void updateMatch(Match<?> match) {
        this.match = match;
        data = buildDataSource(match.getTurns());
        notifyDataSetChanged();
    }

    private List<List<Turn>> buildDataSource(List<Turn> turns) {
        List<List<Turn>> data = new ArrayList<>();
        ArrayList<Turn> turnsInGame = new ArrayList<>();
        for (int i = 0; i < turns.size(); i++) {
            Turn turn = turns.get(i);

            if ((!turn.isGameLost() && turn.getTurnEnd() != TurnEnd.GAME_WON)) {
                turnsInGame.add(turn); // add any turn that doesn't end the game
            } else {
                turnsInGame.add(turn); // add the turn that won the game
                data.add(turnsInGame); // add the game to the list of lists of turn
                turnsInGame = new ArrayList<>();
            }

            if (i + 1 == turns.size())
                data.add(turnsInGame);
        }

        // add group at the end of the list to simulate a footer
        if (data.size() > 0 && data.get(data.size() - 1).size() > 0) {
            // if the last item is empty then it becomes the footer
            // otherwise we create a new one
            data.add(new ArrayList<Turn>());
        }

        return data;
    }

    @Override public int getGroupCount() {
        return data.size();
    }

    @Override public int getChildCount(int groupPosition) {
        return data.get(groupPosition).size();
    }

    @Override public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override public long getChildId(int groupPosition, int childPosition) {
        return Integer.valueOf(String.valueOf(groupPosition) + String.valueOf(childPosition));
    }

    @Override
    public GameViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new GameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_game, parent, false));
    }

    @Override
    public TurnViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new TurnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_turn, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GameViewHolder holder, int groupPosition, int viewType) {
        if (groupPosition == getGroupCount() - 1) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.expandIndicator.setVisibility(View.VISIBLE);
            holder.game.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(true);

            holder.bind(groupPosition + 1,
                    match.getPlayer(0, getTurnNumber(groupPosition)).getWins(),
                    match.getOpponent(0, getTurnNumber(groupPosition)).getWins());

            // set background resource (target view ID: container)
            final int expandState = holder.getExpandStateFlags();

            if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
                boolean isExpanded;
                boolean animateIndicator = ((expandState & ExpandableItemConstants.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

                isExpanded = (expandState & ExpandableItemConstants.STATE_FLAG_IS_EXPANDED) != 0;

                holder.setIconState(isExpanded, animateIndicator);
            }
        }
    }

    @Override
    public void onBindChildViewHolder(TurnViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // set background for top/bottom position
        setBackground(holder.itemView, groupPosition, childPosition);

        int turn = getTurnNumber(groupPosition, childPosition);

        holder.bind(data.get(groupPosition).get(childPosition),
                viewType == 1 ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT,
                viewType == 1 ? match.getPlayer(0, turn + 1) : match.getOpponent(0, turn + 1));

        holder.setBalls(data.get(groupPosition).get(childPosition));
    }

    private void setBackground(View view, int groupPosition, int childPosition) {
        // set background for top/bottom position
        if (childPosition == 0 && getChildCount(groupPosition) == 1) {
            view.setBackground(getCompleteBackground(
                    ContextCompat.getColor(view.getContext(), R.color.cardview_light_background))
            );
        } else if (childPosition == 0) {
            view.setBackground(getTopBackground(
                    ContextCompat.getColor(view.getContext(), R.color.cardview_light_background))
            );
        } else if (childPosition == getChildCount(groupPosition) - 1) {
            view.setBackground(getBottomBackground(
                    ContextCompat.getColor(view.getContext(), R.color.cardview_light_background))
            );
        }
    }

    @Override public int getChildItemViewType(int groupPosition, int childPosition) {
        return match.getGameStatus(getTurnNumber(groupPosition, childPosition)).turn == PlayerTurn.PLAYER ? 1 : 0;
    }

    private int getTurnNumber(int groupPosition, int childPosition) {
        int count = 0;
        for (int i = 0; i < groupPosition; i++) {
            count += data.get(i).size();
        }

        count += childPosition;

        return count;
    }

    private int getTurnNumber(int groupPosition) {
        int count = 0;
        for (int i = 0; i < groupPosition; i++)
            count += data.get(i).size();

        return count;
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GameViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    static class GameViewHolder extends AbstractExpandableItemViewHolder {
        @Bind(R.id.row_game) TextView game;
        @Bind(R.id.imageView) ImageView expandIndicator;

        public GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int gameTotal, int playerScore, int opponentScore) {
            setText(gameTotal, playerScore, opponentScore);
        }

        private void setText(int games, int playerGamesWon, int oppGamesWon) {
            game.setText(itemView.getContext().getString(R.string.row_game, games, playerGamesWon, oppGamesWon));
        }

        private void setIconState(boolean isExpanded, boolean animate) {
            if (animate) {
                expandIndicator.animate().rotationXBy(180f);
            } else {
                @DrawableRes int resId = isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand;
                expandIndicator.setImageResource(resId);
            }
        }


        private void setBgColor(@ColorInt int color) {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
        }
    }

    static class TurnViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.turn) TextView turnString;
        @Bind(R.id.playerIndicator) RoundedLetterView indicator;
        @Bind(R.id.tvSafetyPct) TextView safetyPct;
        @Bind(R.id.tvBreakPct) TextView breakPct;
        @Bind(R.id.tvShootingPct) TextView shootingPct;
        @Bind(R.id.ballContainer) ViewGroup ballContainer;
        @Bind(R.id.shootingLine) ImageView shootingLine;
        @Bind(R.id.safetyLine) ImageView safetyLine;
        @Bind(R.id.breakingLine) ImageView breakingLine;

        public TurnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Turn turn, PlayerTurn playerTurn, AbstractPlayer player) {
            @ColorInt int color = (playerTurn == PlayerTurn.PLAYER ? ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary) : ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            TurnStringAdapter turnStringAdapter = new TurnStringAdapter(turn, player);
            indicator.setTitleText(player.getName().substring(0, 1));
            indicator.setBackgroundColor(color);

            turnString.setText(turnStringAdapter.getTurnString());

            shootingPct.setText(String.format(Locale.getDefault(), "%1$s %2$s", "Shooting", player.getShootingPct()));
            safetyPct.setText(String.format(Locale.getDefault(), "%1$s %2$s", "Safeties", player.getSafetyPct()));
            breakPct.setText(String.format(Locale.getDefault(), "%1$s %2$s", "Breaking", player.getBreakPct()));

            //shootingPct.setTextColor(getPctColor(player.getShootingPct()));
            //safetyPct.setTextColor(getPctColor(player.getSafetyPct()));
            //breakPct.setTextColor(getPctColor(player.getBreakPct()));

            shootingLine.setImageTintList(getPctColor(player.getShootingPct()));
            safetyLine.setImageTintList(getPctColor(player.getSafetyPct()));
            breakingLine.setImageTintList(getPctColor(player.getBreakPct()));
        }

        private ColorStateList getPctColor(String pctString) {
            float pct = Float.valueOf(pctString);
            @ColorRes int color;
            if (pct > .9)
                color = R.color.good;
            else if (pct > .75)
                color = R.color.almost_good;
            else if (pct > .66)
                color = R.color.okay;
            else if (pct > .5)
                color = R.color.just_above_bad;
            else
                color = R.color.bad;

            return new ColorStateList(new int[][]{new int[0]}, new int[]{ContextCompat.getColor(itemView.getContext(), color)});
        }

        private void setBalls(ITableStatus tableStatus) {
            for (int ball = 1; ball <= tableStatus.size(); ball++) {
                ImageView childAt = (ImageView) ballContainer.getChildAt(ball - 1);
                if (ballIsMade(tableStatus.getBallStatus(ball))) {
                    childAt.setVisibility(View.VISIBLE);
                    childAt.getDrawable().setTint(ContextCompat.getColor(itemView.getContext(), getBallColorTint(ball)));
                } else if (ballIsDead(tableStatus.getBallStatus(ball))) {
                    childAt.setVisibility(View.VISIBLE);
                    childAt.getDrawable().setTint(ContextCompat.getColor(itemView.getContext(), R.color.dead_ball));
                } else {
                    ballContainer.getChildAt(ball - 1).setVisibility(View.GONE);
                }
            }
        }

        @ColorRes private int getBallColorTint(int ball) {
            switch (ball) {
                case 1:
                    return R.color.one_ball;
                case 2:
                    return R.color.two_ball;
                case 3:
                    return R.color.three_ball;
                case 4:
                    return R.color.four_ball;
                case 5:
                    return R.color.five_ball;
                case 6:
                    return R.color.six_ball;
                case 7:
                    return R.color.seven_ball;
                case 8:
                    return R.color.eight_ball;
                case 9:
                    return R.color.one_ball;
                case 10:
                    return R.color.two_ball;
                case 11:
                    return R.color.three_ball;
                case 12:
                    return R.color.four_ball;
                case 13:
                    return R.color.five_ball;
                case 14:
                    return R.color.six_ball;
                case 15:
                    return R.color.seven_ball;
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
}
