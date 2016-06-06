package com.brookmanholmes.billiardmatchanalyzer.ui.matchinfo;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.ApaPlayer;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BaseViewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BreaksHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.FooterViewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.MatchOverviewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.RunOutsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.SafetiesHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.ShootingPctHolder;
import com.brookmanholmes.billiardmatchanalyzer.ui.BaseActivity;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.match.IMatch;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.IApa;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by helios on 6/2/2016.
 */
public abstract class AbstractMatchInfoFragment extends Fragment implements IMatch {
    protected MatchInfoRecyclerAdapter adapter;

    @Override
    public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats) {
        return adapter.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost, advStats);
    }

    @Override public String getCurrentPlayersName() {
        return adapter.getCurrentPlayersName();
    }

    @Override public String getNonCurrentPlayersName() {
        return adapter.getNonCurrentPlayersName();
    }

    @Override public void setPlayerName(String newName) {
        adapter.setPlayerName(newName);
    }

    @Override public void setOpponentName(String newName) {
        adapter.setOpponentName(newName);
    }

    @Override public void undoTurn() {
        adapter.undoTurn();
    }

    @Override public boolean isRedoTurn() {
        return adapter.isRedoTurn();
    }

    @Override public boolean isUndoTurn() {
        return adapter.isUndoTurn();
    }

    @Override public Turn redoTurn() {
        return adapter.redoTurn();
    }

    @Override public AbstractPlayer getPlayer() {
        return adapter.getPlayer();
    }

    @Override public AbstractPlayer getOpponent() {
        return adapter.getOpponent();
    }

    @Override public String getLocation() {
        return adapter.getLocation();
    }

    @Override public int getTurnCount() {
        return adapter.getTurnCount();
    }

    @Override public long getMatchId() {
        return adapter.getMatchId();
    }

    /**
     * Created by Brookman Holmes on 1/13/2016.
     */
    static class MatchInfoRecyclerAdapter<T extends AbstractPlayer> extends RecyclerView.Adapter<BaseViewHolder<T>>
            implements IMatch<T> {
        public static final int ITEM_MATCH_OVERVIEW = 0;
        public static final int ITEM_SHOOTING_PCT = 1;
        public static final int ITEM_SAFETIES = 2;
        public static final int ITEM_BREAKS = 3;
        public static final int ITEM_RUN_OUTS = 4;
        public static final int ITEM_FOOTER = 10;
        public static final int ITEM_APA_STATS = 5;
        final int gameBall;
        Match.StatsDetail detail;
        ViewType viewTypeToggle = ViewType.CARDS;
        Match<T> match;

        MatchInfoRecyclerAdapter(Match<T> match) {
            this.match = match;
            detail = match.getAdvStats();
            this.gameBall = match.getGameStatus().GAME_BALL;
        }

        MatchInfoRecyclerAdapter(Match<T> match, MatchInfoRecyclerAdapter.ViewType viewType) {
            this(match);
            viewTypeToggle = viewType;
        }

        @SuppressWarnings("unchecked")
        public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter createMatchAdapter(Match<T> match) {
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

        @SuppressWarnings("unchecked")
        public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter createMatchAdapterWithCardViews(Match<T> match) {
            // this is probably fucking retarded?
            switch (match.getGameStatus().gameType) {
                case BCA_NINE_BALL:
                    return new BcaNineBallMatchInfoRecyclerAdapter((Match<NineBallPlayer>) match, AbstractMatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                case BCA_EIGHT_BALL:
                    return new BcaEightBallMatchInfoRecyclerAdapter((Match<EightBallPlayer>) match, AbstractMatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                case BCA_TEN_BALL:
                    return new BcaTenBallMatchInfoRecyclerAdapter((Match<TenBallPlayer>) match, AbstractMatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                case APA_NINE_BALL:
                    return new ApaMatchInfoRecyclerAdapter<>((Match<ApaNineBallPlayer>) match, AbstractMatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                case APA_EIGHT_BALL:
                    return new ApaMatchInfoRecyclerAdapter<>((Match<ApaEightBallPlayer>) match, AbstractMatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                default:
                    throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
            }
        }

        @Override public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
            view.setTag(viewType);
            return getMatchInfoHolderByViewType(view, viewType);
        }

        @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
            holder.bind(getPlayer(), getOpponent());
        }

        @Override public int getItemCount() {
            if (match.getGameStatus().breakType == BreakType.GHOST)
                return 4;
            else
                return 6;
        }

        @Override public void setPlayerName(String newName) {
            match.setPlayerName(newName);
        }

        @Override public void setOpponentName(String newName) {
            match.setOpponentName(newName);
        }

        @Override public int getItemViewType(int position) {
            if (match.getGameStatus().breakType == BreakType.GHOST) {
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

        @Override public T getPlayer() {
            return match.getPlayer();
        }

        @Override public T getOpponent() {
            return match.getOpponent();
        }

        @Override
        public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats) {
            Turn turn = match.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost, advStats);
            notifyDataSetChanged();
            return turn;
        }

        @Override public void undoTurn() {
            match.undoTurn();
            notifyDataSetChanged();
        }

        @Override public boolean isRedoTurn() {
            return match.isRedoTurn();
        }

        @Override public boolean isUndoTurn() {
            return match.isUndoTurn();
        }

        @Override public Turn redoTurn() {
            Turn turn = match.redoTurn();
            notifyDataSetChanged();
            return turn;
        }

        @Override public long getMatchId() {
            return match.getMatchId();
        }

        @Override public String getLocation() {
            return match.getLocation();
        }

        @Override public String getCurrentPlayersName() {
            return match.getCurrentPlayersName();
        }

        @Override public String getNonCurrentPlayersName() {
            return match.getNonCurrentPlayersName();
        }

        @Override public int getTurnCount() {
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
                        throw new IllegalArgumentException("No such view type: " + viewType);
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
                    return new MatchOverviewHolder<>(view, detail);
                case ITEM_SHOOTING_PCT:
                    return new ShootingPctHolder<>(view, detail);
                case ITEM_BREAKS:
                    return new BreaksHolder<>(view, match.getGameStatus().GAME_BALL, detail);
                case ITEM_RUN_OUTS:
                    return new RunOutsHolder<>(view, detail);
                case ITEM_SAFETIES:
                    return new SafetiesHolder<>(view, detail);
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

        /**
         * Created by Brookman Holmes on 1/17/2016.
         */
        static class BcaTenBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<TenBallPlayer> {
            BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match) {
                super(match);
            }

            BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, ViewType viewType) {
                super(match, viewType);
            }

            @Override
            BaseViewHolder<TenBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
                switch (viewType) {
                    case ITEM_RUN_OUTS:
                        return new RunOutsWithEarlyWinsHolder<>(view, detail);
                    default:
                        return super.getMatchInfoHolderByViewType(view, viewType);
                }
            }
        }

        /**
         * Created by Brookman Holmes on 1/17/2016.
         */
        static class BcaNineBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<NineBallPlayer> {
            BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match) {
                super(match);
            }

            BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match, ViewType viewType) {
                super(match, viewType);
            }

            @Override
            BaseViewHolder<NineBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
                switch (viewType) {
                    case ITEM_BREAKS:
                        return new BreaksWithWinsHolder<>(view, gameBall, detail);
                    case ITEM_RUN_OUTS:
                        return new RunOutsWithEarlyWinsHolder<>(view, detail);
                    default:
                        return super.getMatchInfoHolderByViewType(view, viewType);
                }
            }
        }

        /**
         * Created by Brookman Holmes on 1/17/2016.
         */
        static class BcaEightBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<EightBallPlayer> {
            BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match) {
                super(match);
            }

            BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match, ViewType viewType) {
                super(match, viewType);
            }
        }

        /**
         * Created by Brookman Holmes on 1/17/2016.
         */
        static class ApaMatchInfoRecyclerAdapter<T extends AbstractPlayer & IApa> extends MatchInfoRecyclerAdapter<T> {

            ApaMatchInfoRecyclerAdapter(Match<T> match) {
                super(match);
            }

            ApaMatchInfoRecyclerAdapter(Match<T> match, ViewType viewType) {
                super(match, viewType);
            }

            @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
                super.onBindViewHolder(holder, position);

                if (holder instanceof ApaPlayer)
                    ((ApaPlayer) holder).setTvInningsOpponent(match.getGameStatus().innings);
            }

            @Override BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
                switch (viewType) {
                    case ITEM_APA_STATS:
                        return new ApaPlayer<>(view, detail);
                    case ITEM_BREAKS:
                        return new BreaksWithWinsHolder<>(view, gameBall, detail);
                    case ITEM_RUN_OUTS:
                        return new RunOutsWithEarlyWinsHolder<>(view, detail);
                    default:
                        return super.getMatchInfoHolderByViewType(view, viewType);
                }
            }

            @Override int getLayoutResource(int viewType) {
                if (viewTypeToggle == ViewType.CARDS) {
                    switch (viewType) {
                        case ITEM_APA_STATS:
                            return R.layout.card_apa_stats;
                        default:
                            return super.getLayoutResource(viewType);
                    }
                } else {
                    switch (viewType) {
                        case ITEM_APA_STATS:
                            return R.layout.plain_apa_stats;
                        default:
                            return super.getLayoutResource(viewType);
                    }
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
        }
    }
}
