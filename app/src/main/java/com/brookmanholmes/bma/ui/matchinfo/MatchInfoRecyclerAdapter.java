package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.adaptervh.ApaViewHolder;
import com.brookmanholmes.bma.adaptervh.BaseViewHolder;
import com.brookmanholmes.bma.adaptervh.BreaksHolder;
import com.brookmanholmes.bma.adaptervh.FooterViewHolder;
import com.brookmanholmes.bma.adaptervh.MatchOverviewHolder;
import com.brookmanholmes.bma.adaptervh.RunOutsHolder;
import com.brookmanholmes.bma.adaptervh.SafetiesHolder;
import com.brookmanholmes.bma.adaptervh.ShootingPctHolder;


/**
 * Created by Brookman Holmes on 1/13/2016.
 */
class MatchInfoRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int ITEM_MATCH_OVERVIEW = 0;
    private static final int ITEM_SHOOTING_PCT = 1;
    private static final int ITEM_SAFETIES = 2;
    private static final int ITEM_BREAKS = 3;
    private static final int ITEM_RUN_OUTS = 4;
    private static final int ITEM_APA_STATS = 5;
    private static final int ITEM_FOOTER = 10;

    final int gameBall;
    final Match.StatsDetail detail;
    private final BreakType breakType;
    private AbstractPlayer player;
    private AbstractPlayer opponent;

    MatchInfoRecyclerAdapter(Match match) {
        detail = match.getAdvStats();
        breakType = match.getGameStatus().breakType;
        this.gameBall = match.getGameStatus().GAME_BALL;
        player = match.getPlayer();
        opponent = match.getOpponent();
    }

    public static MatchInfoRecyclerAdapter createMatchAdapter(Match<?> match) {
        switch (match.getGameStatus().gameType) {
            case BCA_NINE_BALL:
                return new MatchInfoRecyclerAdapter(match);
            case BCA_EIGHT_BALL:
                return new MatchInfoRecyclerAdapter(match);
            case BCA_TEN_BALL:
                return new MatchInfoRecyclerAdapter(match);
            case APA_NINE_BALL:
                return new ApaMatchInfoRecyclerAdapter(match);
            case APA_EIGHT_BALL:
                return new ApaMatchInfoRecyclerAdapter(match);
            default:
                throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
        }
    }

    @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);

        return getMatchInfoHolderByViewType(view, viewType);
    }

    @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(player, opponent);
    }

    @Override public int getItemCount() {
        if (breakType == BreakType.GHOST)
            return 4;
        else
            return 6;
    }

    @LayoutRes int getLayoutResource(int viewType) {
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
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }


    @Override public int getItemViewType(int position) {
        if (breakType == BreakType.GHOST) {
            switch (position) {
                case 0:
                    return ITEM_MATCH_OVERVIEW;
                case 1:
                    return ITEM_SHOOTING_PCT;
                case 2:
                    return ITEM_BREAKS;
                default:
                    return ITEM_FOOTER;
            }
        }

        if (position == getItemCount() - 1)
            return ITEM_FOOTER;
        else return position;
    }

    BaseViewHolder getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_MATCH_OVERVIEW:
                return new MatchOverviewHolder(view, detail);
            case ITEM_SHOOTING_PCT:
                return new ShootingPctHolder(view, detail);
            case ITEM_BREAKS:
                return new BreaksHolder(view, gameBall, detail);
            case ITEM_RUN_OUTS:
                return new RunOutsHolder(view, detail);
            case ITEM_SAFETIES:
                return new SafetiesHolder(view, detail);
            case ITEM_FOOTER:
                return new FooterViewHolder(view);
            default:
                throw new IllegalArgumentException("No such view type");
        }
    }

    public void updatePlayers(Match<?> match) {
        player = match.getPlayer();
        opponent = match.getOpponent();

        notifyItemRangeChanged(0, getItemCount());
    }

    static class ApaMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter {
        int innings;

        ApaMatchInfoRecyclerAdapter(Match match) {
            super(match);
            this.innings = match.getGameStatus().innings;
        }

        @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            if (holder instanceof ApaViewHolder)
                ((ApaViewHolder) holder).setTvInningsOpponent(innings);
        }

        @Override BaseViewHolder getMatchInfoHolderByViewType(View view, int viewType) {
            switch (viewType) {
                case ITEM_APA_STATS:
                    return new ApaViewHolder(view, detail);
                case ITEM_BREAKS:
                    return new BreaksHolder(view, gameBall, detail);
                case ITEM_RUN_OUTS:
                    return new RunOutsHolder(view, detail);
                default:
                    return super.getMatchInfoHolderByViewType(view, viewType);
            }
        }

        @Override int getLayoutResource(int viewType) {
            switch (viewType) {
                case ITEM_APA_STATS:
                    return R.layout.card_apa_stats;
                default:
                    return super.getLayoutResource(viewType);
            }

        }

        @Override public int getItemCount() {
            return super.getItemCount() + 1;
        }

        @Override public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return ITEM_APA_STATS;
                case 1:
                    return ITEM_SHOOTING_PCT;
                case 2:
                    return ITEM_SAFETIES;
                case 3:
                    return ITEM_BREAKS;
                case 4:
                    return ITEM_RUN_OUTS;
                case 5:
                    return ITEM_MATCH_OVERVIEW;
                case 6:
                    return ITEM_FOOTER;
                default:
                    throw new IllegalArgumentException("Cannot have position: " + position);
            }
        }

        public void updatePlayers(Match<?> match) {
            innings = match.getGameStatus().innings;
            super.updatePlayers(match);
        }
    }
}
