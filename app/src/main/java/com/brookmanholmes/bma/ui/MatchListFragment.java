package com.brookmanholmes.bma.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.FireDataSource;
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoActivity;
import com.brookmanholmes.bma.ui.matchinfo.MatchListener;
import com.brookmanholmes.bma.ui.matchinfo.RunAttemptActivity;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchListFragment extends BaseRecyclerFragment<MatchListFragment.MatchListRecyclerAdapter>
        implements MatchListener {
    private static final String TAG = "MatchListFragment";
    private static final String ARG_PLAYER = "arg_player";
    private static final String ARG_OPPONENT = "arg_opponent";

    private String player, opponent;

    public MatchListFragment() {
    }

    public static MatchListFragment create(String player, String opponent) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PLAYER, player);
        args.putString(ARG_OPPONENT, opponent);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            player = getArguments().getString(ARG_PLAYER, null);
            opponent = getArguments().getString(ARG_OPPONENT, null);
        }

        adapter = new MatchListRecyclerAdapter(player, new ArrayList<Match>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addMatchListener(this);
        }

        Drawable divider = getContext().getDrawable(R.drawable.line_divider);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(divider, divider, false));
        else
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(divider, false));

        recyclerView.setPadding(0, 0, 0, 0);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeMatchListener(this);
        }
        super.onDestroyView();
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && MatchDialogHelperUtils.isTablet(getContext())) {
            return new GridLayoutManager(getContext(), 3);
        } else if (MatchDialogHelperUtils.isTablet(getContext())) {
            return new GridLayoutManager(getContext(), 2);
        } else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return new GridLayoutManager(getContext(), 2);
        else {
            return new LinearLayoutManager(getContext());
        }
    }

    @Override
    public void update(List<Match> matches, Map<String, List<String>> turnIds) {
        adapter.update(matches, turnIds);
    }

    static class MatchListRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        private static final int MATCH_VIEW = 0;
        private static final int FOOTER = 1;
        List<Match> matches;
        Map<String, List<String>> turnIds = new HashMap<>();
        String playerId;

        MatchListRecyclerAdapter(String playerId, List<Match> matches) {
            this.matches = matches;
            this.playerId = playerId;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOTER)
                return new FooterHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.footer, parent, false));
            else
                return new ListItemHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_match, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof ListItemHolder) {
                ((ListItemHolder) holder).bind(matches.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return matches.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? FOOTER : MATCH_VIEW;
        }

        public void update(List<Match> matches, Map<String, List<String>> turnIds) {
            Collections.sort(matches, new Comparator<Match>() {
                @Override
                public int compare(Match o1, Match o2) {
                    // take the negative because I want it sorted newest match first
                    return -o1.getCreatedOn().compareTo(o2.getCreatedOn());
                }
            });

            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MatchListDiffCallback(matches, this.matches));
            this.matches.clear();
            this.matches.addAll(matches);
            this.turnIds = turnIds;
            diffResult.dispatchUpdatesTo(this);
        }

        static class FooterHolder extends BaseViewHolder {
            FooterHolder(View itemView) {
                super(itemView);
            }
        }

        class ListItemHolder extends BaseViewHolder {
            String id;

            @Bind(R.id.players)
            TextView playerNames;
            @Bind(R.id.breakType)
            TextView breakType;
            @Bind(R.id.imgGameType)
            ImageView gameType;
            @Bind(R.id.location)
            TextView location;
            @Bind(R.id.date)
            TextView date;
            @Bind(R.id.ruleSet)
            TextView ruleSet;

            ListItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(Match match) {
                setLocation(match.getLocation());
                date.setText(getDate(match.getCreatedOn()));
                if (match.getGameStatus().gameType.isSinglePlayer()) {
                    playerNames.setText(match.getPlayer().getName());
                    if (match.getGameStatus().gameType.isStraightPool())
                        breakType.setText(getString(R.string.game_straight_ghost));
                    else
                        breakType.setText("vs. The Ghost");
                } else if (match.getGameStatus().gameType == GameType.STRAIGHT_POOL) {
                    playerNames.setText(getString(R.string.and, match.getPlayer().getName(), match.getOpponent().getName()));
                    breakType.setText(getString(R.string.game_straight));
                } else {
                    playerNames.setText(getString(R.string.and, match.getPlayer().getName(), match.getOpponent().getName()));
                    breakType.setText(getBreakType(match.getGameStatus().breakType, match.getPlayer().getName(), match.getOpponent().getName()));
                }
                gameType.setImageResource(getImageId(match.getGameStatus().gameType));
                ruleSet.setText(getRuleSet(match.getGameStatus().gameType));
                id = match.getMatchId();
            }

            void setLocation(String location) {
                if (location == null || location.isEmpty())
                    this.location.setVisibility(View.GONE);
                else {
                    this.location.setVisibility(View.VISIBLE);
                    this.location.setText(location);
                }
            }

            @OnClick(R.id.container)
            public void onClick() {
                final Intent intent = matches.get(getAdapterPosition()).getGameStatus().gameType.isSinglePlayer() ?
                        new Intent(getContext(), RunAttemptActivity.class) :
                        new Intent(getContext(), MatchInfoActivity.class);

                intent.putExtra("match", matches.get(getAdapterPosition()));
                ArrayList<String> turnList = new ArrayList<>((turnIds.get(id) == null ? new ArrayList<String>() : turnIds.get(id)));
                ArrayList<Match> accessoryMatches = new ArrayList<>();
                for (Match match : matches) {
                    if (match.getGameStatus().gameType == matches.get(getAdapterPosition()).getGameStatus().gameType
                            && !match.getMatchId().equals(matches.get(getAdapterPosition()).getMatchId()))
                        accessoryMatches.add(match);
                }
                intent.putExtra("turnIds", turnList);
                intent.putExtra("accessory_matches", accessoryMatches);
                getContext().startActivity(intent);
            }

            @OnLongClick(R.id.container)
            boolean onLongClick() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setMessage(getString(R.string.delete_match))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new FireDataSource().deleteMatch(matches.get(getAdapterPosition()), playerId); // remove from the database
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                return true;
            }

            private Context getContext() {
                return itemView.getContext();
            }

            private String getDate(Date date) {
                return DateFormat.getDateInstance().format(date);
            }

            private String getBreakType(BreakType breakType, String playerName, String opponentName) {
                switch (breakType) {
                    case WINNER:
                        return getString(R.string.break_winner);
                    case LOSER:
                        return getString(R.string.break_loser);
                    case ALTERNATE:
                        return getString(R.string.break_alternate);
                    case PLAYER:
                        return getString(R.string.break_player, playerName);
                    case OPPONENT:
                        return getString(R.string.break_player, opponentName);
                    default:
                        throw new IllegalArgumentException();
                }
            }

            private int getImageId(GameType gameType) {
                switch (gameType) {
                    case BCA_EIGHT_BALL:
                        return R.drawable.eight_ball;
                    case BCA_NINE_BALL:
                        return R.drawable.nine_ball;
                    case BCA_TEN_BALL:
                        return R.drawable.ten_ball;
                    case APA_EIGHT_BALL:
                        return R.drawable.eight_ball;
                    case APA_NINE_BALL:
                        return R.drawable.nine_ball;
                    case BCA_GHOST_EIGHT_BALL:
                        return R.drawable.eight_ball;
                    case BCA_GHOST_NINE_BALL:
                        return R.drawable.nine_ball;
                    case BCA_GHOST_TEN_BALL:
                        return R.drawable.ten_ball;
                    case APA_GHOST_EIGHT_BALL:
                        return R.drawable.eight_ball;
                    case APA_GHOST_NINE_BALL:
                        return R.drawable.nine_ball;
                    case STRAIGHT_POOL:
                        return R.drawable.fourteen_ball;
                    case STRAIGHT_GHOST:
                        return R.drawable.fourteen_ball;
                    case AMERICAN_ROTATION:
                        return R.drawable.fifteen_ball;
                    default:
                        return R.drawable.eight_ball;
                }
            }

            private String getRuleSet(GameType gameType) {
                switch (gameType) {
                    case BCA_EIGHT_BALL:
                        return getString(R.string.bca_rules);
                    case BCA_NINE_BALL:
                        return getString(R.string.bca_rules);
                    case BCA_TEN_BALL:
                        return getString(R.string.bca_rules);
                    case APA_EIGHT_BALL:
                        return getString(R.string.apa_rules);
                    case APA_NINE_BALL:
                        return getString(R.string.apa_rules);
                    case BCA_GHOST_EIGHT_BALL:
                        return getString(R.string.bca_rules);
                    case BCA_GHOST_NINE_BALL:
                        return getString(R.string.bca_rules);
                    case BCA_GHOST_TEN_BALL:
                        return getString(R.string.bca_rules);
                    case APA_GHOST_EIGHT_BALL:
                        return getString(R.string.apa_rules);
                    case APA_GHOST_NINE_BALL:
                        return getString(R.string.apa_rules);
                    default:
                        return "";
                }
            }

            private String getString(@StringRes int resId) {
                return getContext().getString(resId);
            }

            private String getString(@StringRes int resId, Object... formatArgs) {
                return getContext().getString(resId, formatArgs);
            }
        }
    }

    private static class MatchListDiffCallback extends DiffUtil.Callback {
        private List<Match> newList, oldList;

        public MatchListDiffCallback(List<Match> newList, List<Match> oldList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition).getMatchId().equals(oldList.get(oldItemPosition).getMatchId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
        }
    }
}
