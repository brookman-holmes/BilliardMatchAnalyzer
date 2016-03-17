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
import com.brookmanholmes.billiards.match.MatchInterface;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchInfoRecyclerAdapter<T extends AbstractPlayer> extends RecyclerView.Adapter<BaseViewHolder<T>>
        implements MatchInterface<T> {
    static final int ITEM_MATCH_OVERVIEW = 0;
    static final int ITEM_SHOOTING_PCT = 1;
    static final int ITEM_SAFETIES = 2;
    static final int ITEM_BREAKS = 3;
    static final int ITEM_RUN_OUTS = 4;
    static final int ITEM_FOOTER = 10;
    final int gameBall;
    Match.StatsDetail detail;
    ViewType viewTypeToggle = ViewType.LIST;
    Match<T> match;

    MatchInfoRecyclerAdapter(Match<T> match, int gameBall) {
        this.match = match;
        detail = match.getStatsLevel();
        this.gameBall = gameBall;
    }

    MatchInfoRecyclerAdapter(Match<T> match, int gameBall, ViewType viewType) {
        this(match, gameBall);
        viewTypeToggle = viewType;
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

    public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter<?> createMatchAdapterWithCardViews(Match<T> match) {
        // this is probably fucking retarded?
        switch (match.getGameStatus().gameType) {
            case BCA_NINE_BALL:
                return new BcaNineBallMatchInfoRecyclerAdapter((Match<NineBallPlayer>) match, ViewType.CARDS);
            case BCA_EIGHT_BALL:
                return new BcaEightBallMatchInfoRecyclerAdapter((Match<EightBallPlayer>) match, ViewType.CARDS);
            case BCA_TEN_BALL:
                return new BcaTenBallMatchInfoRecyclerAdapter((Match<TenBallPlayer>) match, ViewType.CARDS);
            case APA_NINE_BALL:
                return new ApaMatchInfoRecyclerAdapter<>((Match<ApaNineBallPlayer>) match, ViewType.CARDS);
            case APA_EIGHT_BALL:
                return new ApaMatchInfoRecyclerAdapter<>((Match<ApaEightBallPlayer>) match, ViewType.CARDS);
            default:
                throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
        }
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        return getMatchInfoHolderByViewType(view, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.bind(getPlayer(), getOpponent());
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != getItemCount() - 1)
            return position;
        else return ITEM_FOOTER;
    }

    @Override
    public T getPlayer() {
        return match.getPlayer();
    }

    @Override
    public T getOpponent() {
        return match.getOpponent();
    }

    @Override
    public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost) {
        Turn turn = match.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost);
        notifyDataSetChanged();
        return turn;
    }

    @Override
    public boolean undoTurn() {
        return match.undoTurn();
    }

    @Override
    public long getMatchId() {
        return match.getMatchId();
    }

    @Override
    public String getLocation() {
        return match.getLocation();
    }

    @Override
    public String getCurrentPlayersName() {
        return match.getCurrentPlayersName();
    }

    @Override
    public int getTurnCount() {
        return match.getTurnCount();
    }

    @LayoutRes
    int getLayoutResource(int viewType) {
        if (viewTypeToggle == ViewType.CARDS) {
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
                case ITEM_FOOTER:
                    return R.layout.footer;
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
                case ITEM_FOOTER:
                    return R.layout.footer;
                default:
                    throw new IllegalArgumentException("No such view type: " + viewType);
            }
        }
    }

    BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_MATCH_OVERVIEW:
                return new MatchInfoHolder.MatchOverviewHolder<>(view, detail);
            case ITEM_SHOOTING_PCT:
                return new MatchInfoHolder.ShootingPctHolder<>(view, detail);
            case ITEM_BREAKS:
                return new MatchInfoHolder.BreaksHolder<>(view, match.getGameStatus().GAME_BALL, detail);
            case ITEM_RUN_OUTS:
                return new MatchInfoHolder.RunOutsHolder<>(view, detail);
            case ITEM_SAFETIES:
                return new MatchInfoHolder.SafetiesHolder<>(view, detail);
            case ITEM_FOOTER:
                return new FooterViewHolder<>(view);
            default:
                throw new IllegalArgumentException("No such view type");
        }
    }

    public enum ViewType {
        CARDS,
        LIST
    }
}
