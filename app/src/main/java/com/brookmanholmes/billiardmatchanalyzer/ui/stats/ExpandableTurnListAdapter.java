package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.RoundedLetterView;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatusInterface;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 5/1/2016.
 */
public class ExpandableTurnListAdapter extends AbstractExpandableItemAdapter<ExpandableTurnListAdapter.GameViewHolder, ExpandableTurnListAdapter.TurnViewHolder> {
    List<List<Pair<Turn, AdvStats>>> data = new ArrayList<>(); // list of lists of turns, where each list is a group of turns that corresponds to that game
    LayoutInflater inflater;
    Match<?> match;

    public ExpandableTurnListAdapter(Context context, Match<?> match, List<Pair<Turn, AdvStats>> data) {
        inflater = LayoutInflater.from(context);
        this.match = match;
        setHasStableIds(true);

        buildDataSource(data);
    }

    private void buildDataSource(List<Pair<Turn, AdvStats>> data) {
        ArrayList<Pair<Turn, AdvStats>> turnsInGame = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Pair<Turn, AdvStats> turn = data.get(i);

            if ((!turn.getLeft().isGameLost() && turn.getLeft().getTurnEnd() != TurnEnd.GAME_WON)) {
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
        return new TurnViewHolder(inflater.inflate(R.layout.row_turn, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GameViewHolder holder, int groupPosition, int viewType) {
        holder.bind(groupPosition + 1,
                match.getPlayer(getTurnNumber(groupPosition)).getWins(),
                match.getOpponent(getTurnNumber(groupPosition)).getWins(),
                data.get(groupPosition).size() > 0);

        final int expandState = holder.getExpandStateFlags();

        holder.setIconState((expandState & ExpandableItemConstants.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

        if (data.get(groupPosition).size() == 0) {
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public void onBindChildViewHolder(TurnViewHolder holder, int groupPosition, int childPosition, int viewType) {
        int turn = getTurnNumber(groupPosition, childPosition);

        holder.bind(data.get(groupPosition).get(childPosition),
                viewType == 1 ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT,
                viewType == 1 ? match.getPlayer(turn + 1) : match.getOpponent(turn + 1));
        holder.setBalls(data.get(groupPosition).get(childPosition).getLeft());
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
    }

    static class TurnViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.turn) TextView turnString;
        @Bind(R.id.playerIndicator) RoundedLetterView indicator;
        @Bind(R.id.tvSafetyPct) TextView safetyPct;
        @Bind(R.id.tvBreakPct) TextView breakPct;
        @Bind(R.id.tvShootingPct) TextView shootingPct;
        @Bind(R.id.shotType) TextView shotType;
        @Bind(R.id.shotSubType) TextView subType;
        @Bind(R.id.angel) TextView angels;
        @Bind(R.id.howMiss) TextView howMiss;
        @Bind(R.id.whyMiss) TextView whyMiss;
        @Bind(R.id.advStatsContainer) View container;
        @Bind(R.id.ballContainer) LinearLayout ballContainer;

        public TurnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Pair<Turn, AdvStats> stats, PlayerTurn playerTurn, AbstractPlayer player) {
            @ColorInt int color = (playerTurn == PlayerTurn.PLAYER ? ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary) : ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            TurnStringAdapter turnStringAdapter = new TurnStringAdapter(stats.getLeft(), player, playerTurn);
            indicator.setTitleText(player.getName().substring(0, 1));
            indicator.setBackgroundColor(color);
            turnString.setText(Html.fromHtml(turnStringAdapter.getString()));

            String title = "%1$s <font color='";
            title += playerTurn == PlayerTurn.PLAYER ? "#2196F3" : "#FF3D00";
            title += "'>%2$s</font>";

            shootingPct.setText(Html.fromHtml(String.format(Locale.getDefault(), title, "Shooting", player.getShootingPct())));
            safetyPct.setText(Html.fromHtml(String.format(Locale.getDefault(), title, "Safeties", player.getSafetyPct())));
            breakPct.setText(Html.fromHtml(String.format(Locale.getDefault(), title, "Breaking", player.getBreakPct())));

            if (stats.getRight() != null) {
                bind(stats.getRight());
                container.setVisibility(View.GONE); // setting visibility to gone to help with testing TurnStringAdapter
            } else
                container.setVisibility(View.GONE);
        }

        private void setBalls(TableStatusInterface tableStatus) {
            for (int ball = 1; ball <= tableStatus.size(); ball++) {
                if (ballIsMade(tableStatus.getBallStatus(ball))) {
                    ballContainer.getChildAt(ball - 1).setVisibility(View.VISIBLE);
                } else if (ballIsDead(tableStatus.getBallStatus(ball))) {
                    TextView textView = (TextView) ballContainer.getChildAt(ball - 1);
                    textView.getBackground().setTint(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
                } else {
                    ballContainer.getChildAt(ball - 1).setVisibility(View.GONE);
                }
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
                    status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ||
                    status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
        }

        private void bind(AdvStats stats) {
            shotType.setText(stats.getShotType());
            subType.setText(stats.getShotSubtype());
            angels.setText(stats.getAngles().toString());
            howMiss.setText(stats.getHowTypes().toString());
            whyMiss.setText(stats.getWhyTypes().toString());
        }
    }
}
