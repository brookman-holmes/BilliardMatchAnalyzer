package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 5/1/2016.
 */
class ExpandableTurnListAdapter extends AbstractExpandableItemAdapter<ExpandableTurnListAdapter.GameViewHolder, ExpandableTurnListAdapter.TurnViewHolder> {
    private List<List<ITurn>> data = new ArrayList<>(); // list of list of turns, where each list is a group of turns that corresponds to that game
    private Match match;

    public ExpandableTurnListAdapter(Match match) {
        this.match = match;
        setHasStableIds(true);
        data = buildDataSource(match.getTurns());
        // can't call scrollToLastItem() here because there is no guarantee that the recyclerview has been created yet
    }

    public void updateMatch(Match match) {
        this.match = match;
        data = buildDataSource(match.getTurns());
        notifyDataSetChanged(); // there is a bug thrown by the library this is based on so neat
    }

    private List<List<ITurn>> buildDataSource(List<ITurn> turns) {
        List<List<ITurn>> data = new ArrayList<>();
        ArrayList<ITurn> turnsInGame = new ArrayList<>();
        for (int i = 0; i < turns.size(); i++) {
            ITurn turn = turns.get(i);

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
            data.add(new ArrayList<ITurn>());
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
            holder.itemView.setEnabled(false);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(true);
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
        int turn = getTurnNumber(groupPosition, childPosition);

        holder.bind(data.get(groupPosition).get(childPosition),
                viewType == 1 ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT,
                viewType == 1 ? match.getPlayer(0, turn + 1) : match.getOpponent(0, turn + 1));

        holder.setBalls(data.get(groupPosition).get(childPosition));

        // set margin of bottom view holder to get a cool spacing
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (childPosition == getChildCount(groupPosition) - 1)
            params.bottomMargin = (int) ConversionUtils.convertDpToPx(holder.itemView.getContext(), 8);
        else params.bottomMargin = 0;
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

            itemView.setElevation((isExpanded ? ConversionUtils.convertDpToPx(itemView.getContext(), 2) : 0));
        }
    }

    static class TurnViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.turn) TextView turnString;
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

        private void bind(ITurn turn, PlayerTurn playerTurn, AbstractPlayer player) {
            String color = (playerTurn == PlayerTurn.PLAYER ? "#2196F3" : "#FF3D00");
            TurnStringAdapter turnStringAdapter = new TurnStringAdapter(itemView.getContext(), turn, player, color);

            turnString.setText(turnStringAdapter.getTurnString());

            shootingPct.setText(itemView.getContext().getString(R.string.shooting_pct, player.getShootingPct()));
            safetyPct.setText(itemView.getContext().getString(R.string.safety_pct, player.getSafetyPct()));
            breakPct.setText(itemView.getContext().getString(R.string.breaking_pct, player.getBreakPct()));

            shootingLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getShootingPct()));
            safetyLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getSafetyPct()));
            breakingLine.setImageTintList(ConversionUtils.getPctColor(itemView.getContext(), player.getBreakPct()));
        }

        private void setBalls(ITableStatus tableStatus) {
            for (int ball = 1; ball <= tableStatus.size(); ball++) {
                ImageView childAt = (ImageView) ballContainer.getChildAt(ball - 1);
                if (ballIsMade(tableStatus.getBallStatus(ball))) {
                    childAt.setVisibility(View.VISIBLE);
                    childAt.getBackground().setTint(ContextCompat.getColor(itemView.getContext(), getBallColorTint(ball)));
                } else if (ballIsDead(tableStatus.getBallStatus(ball))) {
                    childAt.setVisibility(View.VISIBLE);
                    childAt.getBackground().setTint(ContextCompat.getColor(itemView.getContext(), R.color.dead_ball));
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
