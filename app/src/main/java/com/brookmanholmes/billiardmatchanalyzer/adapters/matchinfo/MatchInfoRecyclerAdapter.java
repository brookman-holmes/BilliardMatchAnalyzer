package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchInfoRecyclerAdapter<T extends AbstractPlayer> extends RecyclerView.Adapter<MatchInfoHolder<T>> {
    static final int ITEM_MATCH_OVERVIEW = 0;
    static final int ITEM_SHOOTING_PCT = 1;
    static final int ITEM_SAFETIES = 2;
    static final int ITEM_BREAKS = 3;
    static final int ITEM_RUN_OUTS = 4;
    final int gameBall;
    boolean viewTypeToggle = false;
    Match<T> match;


    public MatchInfoRecyclerAdapter(Match<T> match, int gameBall) {
        this.match = match;
        this.gameBall = gameBall;
    }

    public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter<?> createMatchAdapter(Match<T> match) {
        // this is probably fucking retarded?
        switch (match.getGameStatus().gameType) {
            case BCA_NINE_BALL:
                return new BcaNineBallMatchInfoRecyclerAdapter((Match<NineBallPlayer>) match);
            case BCA_EIGHT_BALL:
                return new BcaEightBallMatchInfoRecyclerAdapter((Match<EightBallPlayer>) match);
            case BCA_TEN_BALL:
                return new BcaTenBallMatchInfoRecyclerAdapter((Match<TenBallPlayer>) match);
            case APA_NINE_BALL:
                return new ApaMatchInfoRecyclerAdapter<>((Match<ApaNineBallPlayer>) match);
            case APA_EIGHT_BALL:
                return new ApaMatchInfoRecyclerAdapter<>((Match<ApaEightBallPlayer>) match);
            default:
                throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
        }
    }

    @Override
    public MatchInfoHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        return getMatchInfoHolderByViewType(view, viewType);
    }

    @Override
    public void onBindViewHolder(MatchInfoHolder<T> holder, int position) {
        holder.bind(getPlayer(), getOpponent());
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Turn addTurn(TableStatus table, TurnEnd turnEnd, boolean scratch) {
        Turn turn = match.createAndAddTurnToMatch(table, turnEnd, scratch);
        notifyDataSetChanged();
        return turn;
    }

    T getPlayer() {
        return match.getPlayer();
    }

    T getOpponent() {
        return match.getOpponent();
    }

    @LayoutRes
    int getLayoutResource(int viewType) {
        if (viewTypeToggle) {
            switch (viewType) {
                case ITEM_RUN_OUTS:
                    return R.layout.card_run_outs;
                case ITEM_BREAKS:
                    return R.layout.card_breaks;
                case ITEM_MATCH_OVERVIEW:
                    return R.layout.card_match_overview;
                case ITEM_SAFETIES:
                    return R.layout.card_safeties;
                case ITEM_SHOOTING_PCT:
                    return R.layout.card_shooting_pct;
                default:
                    throw new IllegalArgumentException("No such view type");
            }
        } else {
            switch (viewType) {
                case ITEM_RUN_OUTS:
                    return R.layout.plain_runs;
                case ITEM_BREAKS:
                    return R.layout.plain_breaks;
                case ITEM_MATCH_OVERVIEW:
                    return R.layout.plain_match_overview;
                case ITEM_SAFETIES:
                    return R.layout.plain_safeties;
                case ITEM_SHOOTING_PCT:
                    return R.layout.plain_shooting;
                default:
                    throw new IllegalArgumentException("No such view type");
            }
        }
    }

    MatchInfoHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_MATCH_OVERVIEW:
                return new MatchInfoHolder.MatchOverviewHolder<>(view);
            case ITEM_SHOOTING_PCT:
                return new MatchInfoHolder.ShootingPctHolder<>(view);
            case ITEM_BREAKS:
                return new MatchInfoHolder.BreaksHolder<>(view, match.getGameStatus().GAME_BALL);
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsHolder<>(view);
            case ITEM_SAFETIES:
                return new MatchInfoHolder.SafetiesHolder<>(view);
            default:
                throw new IllegalArgumentException("No such view type");
        }
    }

    public interface ListItemClickListener {
        void onSelectMatch(long id);
        void onLongSelectMatch(long id);
    }
}
