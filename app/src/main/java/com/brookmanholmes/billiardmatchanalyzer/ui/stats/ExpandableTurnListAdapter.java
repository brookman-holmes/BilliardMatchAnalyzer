package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
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
public class ExpandableTurnListAdapter extends AbstractExpandableItemAdapter<ExpandableTurnListAdapter.GameViewHolder, ExpandableTurnListAdapter.TurnViewHolder> {
    List<List<Turn>> data = new ArrayList<>(); // list of lists of turns, where each list is a group of turns that corresponds to that game
    LayoutInflater inflater;
    Match<?> match;
    int[] colors = new int[]{R.color.good, R.color.almost_good, R.color.okay, R.color.bad};

    public ExpandableTurnListAdapter(Context context, Match<?> match) {
        inflater = LayoutInflater.from(context);
        this.match = match;
        setHasStableIds(true);

        buildDataSource(match.getTurns());
    }

    private void buildDataSource(List<Turn> data) {
        ArrayList<Turn> turnsInGame = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Turn turn = data.get(i);

            if ((!turn.isGameLost() && turn.getTurnEnd() != TurnEnd.GAME_WON)) {
                turnsInGame.add(turn); // add any turn that doesn't end the game
            } else {
                turnsInGame.add(turn); // add the turn that won the game
                this.data.add(turnsInGame); // add the game to the list of lists of turn
                turnsInGame = new ArrayList<>();
            }

            if (i + 1 == data.size())
                this.data.add(turnsInGame);
        }
    }

    public void setColors(int color1, int color2, int color3, int color4) {
        colors[0] = color1;
        colors[1] = color2;
        colors[2] = color3;
        colors[3] = color4;
        notifyDataSetChanged();
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
        return new GameViewHolder(inflater.inflate(R.layout.row_game, parent, false));
    }

    @Override
    public TurnViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new TurnViewHolder(inflater.inflate(R.layout.row_turn, parent, false), colors);
    }

    @Override
    public void onBindGroupViewHolder(GameViewHolder holder, int groupPosition, int viewType) {
        holder.bind(groupPosition + 1,
                match.getPlayer(0, getTurnNumber(groupPosition)).getWins(),
                match.getOpponent(0, getTurnNumber(groupPosition)).getWins(),
                data.get(groupPosition).size() > 0);

        final int expandState = holder.getExpandStateFlags();

        holder.setIconState((expandState & ExpandableItemConstants.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

        holder.setBgColor();

        if (data.get(groupPosition).size() == 0) {
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public void onBindChildViewHolder(TurnViewHolder holder, int groupPosition, int childPosition, int viewType) {
        int turn = getTurnNumber(groupPosition, childPosition);

        holder.bind(data.get(groupPosition).get(childPosition),
                viewType == 1 ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT,
                viewType == 1 ? match.getPlayer(0, turn + 1) : match.getOpponent(0, turn + 1));

        holder.setBalls(data.get(groupPosition).get(childPosition));
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

        private void bind(int gameTotal, int playerScore, int opponentScore, boolean gameStarted) {
            setText(gameTotal, playerScore, opponentScore);
            if (!gameStarted) {
                game.setText(game.getText().toString() + "\nNot yet started");
            }
        }

        private void setText(int games, int playerGamesWon, int oppGamesWon) {
            game.setText(itemView.getContext().getString(R.string.row_game, games, playerGamesWon, oppGamesWon));
        }

        private void setIconState(boolean animateIndicator) {
            if (animateIndicator) {
                expandIndicator.animate().rotationXBy(180f);
            }
        }


        private void setBgColor() {
            @ColorRes int color;
            if ((getExpandStateFlags() & ExpandableItemConstants.STATE_FLAG_IS_EXPANDED) != 0)
                color = R.color.colorPrimaryLight;
            else
                color = android.R.color.white;


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
        int[] colors;

        public TurnViewHolder(View itemView, int[] colors) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.colors = colors;
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

            shootingPct.setBackgroundTintList(getPctColor(player.getShootingPct()));
            safetyPct.setBackgroundTintList(getPctColor(player.getSafetyPct()));
            breakPct.setBackgroundTintList(getPctColor(player.getBreakPct()));
        }

        private ColorStateList getPctColor(String pctString) {
            float pct = Float.valueOf(pctString);
            @ColorRes int color;
            if (pct > .85)
                color = colors[0];
            else if (pct > .66)
                color = colors[1];
            else if (pct > .5)
                color = colors[2];
            else
                color = colors[3];

            return new ColorStateList(new int[][]{new int[0]}, new int[]{color});
        }

        private ColorStateList getColorStateList(@ColorRes int color) {
            return ContextCompat.getColorStateList(itemView.getContext(), color);
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
