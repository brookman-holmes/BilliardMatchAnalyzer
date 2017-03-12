package com.brookmanholmes.bma.ui.matchinfo;

import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
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
class ExpandableTurnListAdapter extends AbstractExpandableItemAdapter<ExpandableTurnListAdapter.GameViewHolder, AdvTurnViewHolder> {
    private List<List<ITurn>> data = new ArrayList<>(); // list of list of turns, where each list is a group of turns that corresponds to that game
    private Match match;

    ExpandableTurnListAdapter() {
        setHasStableIds(true);
    }

    void updateMatch(Match match) {
        this.match = match;
        data = getData(match.getTurns());
        notifyDataSetChanged(); // there is a bug thrown by the library this is based on so neat
    }

    private List<List<ITurn>> getData(List<ITurn> turns) {
        if (match.getGameStatus().gameType == GameType.STRAIGHT_POOL)
            return buildStraightPoolDataSource(turns);
        else return buildDataSource(turns);
    }

    private List<List<ITurn>> buildStraightPoolDataSource(List<ITurn> turns) {
        List<List<ITurn>> data = new ArrayList<>();
        ArrayList<ITurn> turnsInGame = new ArrayList<>();

        int ballCount = 0;
        int rackCount = 0;
        for (int i = 0; i < turns.size(); i++) {
            ITurn turn = turns.get(i);
            ballCount += turn.getShootingBallsMade();

            float rack = ((float) ballCount) / 14;

            if (Float.compare(rack, rackCount + 1) < 0) {
                // add any turn where the number of balls made divided by 14 is
                // NOT more than the old rack count by at least 1
                turnsInGame.add(turn);
            } else {
                turnsInGame.add(turn); // add the turn that rolls over to the new rack
                data.add(turnsInGame);
                turnsInGame = new ArrayList<>();
            }

            rackCount = (int) Math.floor(rack);

            if (i + 1 == turns.size())
                data.add(turnsInGame);
        }

        // add group at the end of the list to simulate a footer
        if (data.size() > 0 && data.get(data.size() - 1).size() > 0) {
            // if the last item is empty then it becomes the footer
            // otherwise we newInstance a new one
            data.add(new ArrayList<ITurn>());
        }

        return data;
    }

    private List<List<ITurn>> buildDataSource(List<ITurn> turns) {
        List<List<ITurn>> data = new ArrayList<>();
        ArrayList<ITurn> turnsInGame = new ArrayList<>();
        for (int i = 0; i < turns.size(); i++) {
            ITurn turn = turns.get(i);

            if (!turn.isGameLost() && turn.getTurnEnd() != TurnEnd.GAME_WON) {
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
            // otherwise we newInstance a new one
            data.add(new ArrayList<ITurn>());
        }

        return data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return data.get(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Integer.valueOf(String.valueOf(groupPosition) + String.valueOf(childPosition));
    }

    @Override
    public GameViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_game, parent, false);
        if (match.getGameStatus().gameType == GameType.STRAIGHT_POOL)
            return new RackViewHolder(view);
        else
            return new GameViewHolder(view);
    }

    @Override
    public AdvTurnViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new AdvTurnViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.row_turn, parent, false),
                match);
    }

    @Override
    public void onBindGroupViewHolder(GameViewHolder holder, int groupPosition, int viewType) {
        if (groupPosition == getGroupCount() - 1) {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.setEnabled(false);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(true);

            holder.bind(match.getPlayer(0, getTurnNumber(groupPosition)),
                    match.getOpponent(0, getTurnNumber(groupPosition)));

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
    public void onBindChildViewHolder(AdvTurnViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // set background for top/bottom position
        int turn = getTurnNumber(groupPosition, childPosition);

        holder.bind(data.get(groupPosition).get(childPosition),
                viewType == 1 ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT,
                viewType == 1 ? match.getPlayer(0, turn + 1) : match.getOpponent(0, turn + 1));

        // set margin of bottom view holder to get a cool spacing
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (childPosition == getChildCount(groupPosition) - 1)
            params.bottomMargin = (int) ConversionUtils.convertDpToPx(holder.itemView.getContext(), 8);
        else params.bottomMargin = 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
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
        @Bind(R.id.row_game)
        TextView game;
        @Bind(R.id.playerWins)
        TextView playerWins;
        @Bind(R.id.opponentWins)
        TextView opponentWins;
        @Bind(R.id.imageView)
        ImageView expandIndicator;

        GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Player player, Player opponent) {
            int playerScore = player.getWins();
            int opponentScore = opponent.getWins();
            int gameTotal = playerScore + opponentScore + 1;

            game.setText(itemView.getContext().getString(R.string.row_game, gameTotal));
            playerWins.setText(itemView.getContext().getString(R.string.player_wins, player.getName(), playerScore));
            opponentWins.setText(itemView.getContext().getString(R.string.player_wins, opponent.getName(), opponentScore));

            if (playerScore > opponentScore) {
                playerWins.setTypeface(null, Typeface.BOLD);
                opponentWins.setTypeface(null, Typeface.NORMAL);
            } else if (playerScore < opponentScore) {
                playerWins.setTypeface(null, Typeface.NORMAL);
                opponentWins.setTypeface(null, Typeface.BOLD);
            } else {
                playerWins.setTypeface(null, Typeface.NORMAL);
                opponentWins.setTypeface(null, Typeface.NORMAL);
            }
        }

        private void setIconState(boolean isExpanded, boolean animate) {
            if (animate) {
                expandIndicator.animate().rotationXBy(180f);
            } else {
                @DrawableRes int resId = isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand;
                expandIndicator.setImageResource(resId);
            }

            //noinspection ResourceType
            itemView.setElevation((isExpanded ? ConversionUtils.convertDpToPx(itemView.getContext(), 2) : 0));
        }
    }

    static class RackViewHolder extends GameViewHolder {
        RackViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(Player player, Player opponent) {
            float points = player.getShootingBallsMade() + opponent.getShootingBallsMade();
            int rackTotal = (int) Math.floor(points / 14) + 1;

            game.setText(itemView.getContext().getString(R.string.row_rack, rackTotal));

            if (player.getGameType().isStraightPool()) {
                int playerScore = player.getPoints();
                int opponentScore = opponent.getPoints();

                float playerPct = (float) playerScore / (float) player.getRank();
                float opponentPct = (float) opponentScore / (float) opponent.getRank();

                playerWins.setText(itemView.getContext().getString(R.string.player_points, player.getName(), playerScore));
                opponentWins.setText(itemView.getContext().getString(R.string.player_points, opponent.getName(), opponentScore));

                if (Float.compare(playerPct, opponentPct) > 0) {
                    playerWins.setTypeface(null, Typeface.BOLD);
                    opponentWins.setTypeface(null, Typeface.NORMAL);
                } else if (Float.compare(playerPct, opponentPct) < 0) {
                    playerWins.setTypeface(null, Typeface.NORMAL);
                    opponentWins.setTypeface(null, Typeface.BOLD);
                } else {
                    playerWins.setTypeface(null, Typeface.NORMAL);
                    opponentWins.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }
}
