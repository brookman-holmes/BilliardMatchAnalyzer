package com.brookmanholmes.bma.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.matchinfo.HighRunAttemptActivity;
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoActivity;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchListFragment extends BaseRecyclerFragment<MatchListFragment.MatchListRecyclerAdapter> implements Filterable {
    private static final String TAG = "MatchListFragment";
    private static final String ARG_PLAYER = "arg_player";
    private static final String ARG_OPPONENT = "arg_opponent";
    private String player, opponent;
    private DatabaseAdapter database;

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
        database = new DatabaseAdapter(getContext());
        if (getArguments() != null) {
            player = getArguments().getString(ARG_PLAYER, null);
            opponent = getArguments().getString(ARG_OPPONENT, null);
        }

        adapter = new MatchListRecyclerAdapter(database.getMatches(player, opponent));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        }
        View view = super.onCreateView(inflater, container, savedInstanceState);


        Drawable divider = getContext().getDrawable(R.drawable.line_divider);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(divider, divider, false));
        else
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(divider, false));

        recyclerView.setPadding(0, 0, 0, 0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onDestroy() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        super.onDestroy();
    }

    public void update() {
        adapter.update(database.getMatches(player, opponent));
    }

    @Override
    public void setFilter(StatFilter filter) {
        new FilterPlayers().execute(filter);
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

    static class MatchListRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        private static final int MATCH_VIEW = 0;
        private static final int FOOTER = 1;
        List<Match> matches;

        MatchListRecyclerAdapter(List<Match> matches) {
            this.matches = matches;
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
        public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
            if (payloads.size() > position) {
                if (holder instanceof ListItemHolder) {
                    Bundle bundle = (Bundle) payloads.get(position);
                    if (bundle.containsKey(DatabaseAdapter.COLUMN_LOCATION))
                        ((ListItemHolder) holder).location.setText(bundle.getString(DatabaseAdapter.COLUMN_LOCATION));
                    if (bundle.containsKey("player_name"))
                        ((ListItemHolder) holder).playerNames.setText(((ListItemHolder) holder).getString(R.string.and, bundle.getString("player_name"), bundle.getString("opp_name")));
                }
            } else
                super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return matches.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? FOOTER : MATCH_VIEW;
        }

        private void update(List<Match> matches) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MatchListDiffCallback(this.matches, matches));
            this.matches = matches;
            diffResult.dispatchUpdatesTo(this);
        }



        private static class MatchListDiffCallback extends DiffUtil.Callback {
            private List<Match> oldList;
            private List<Match> newList;

            MatchListDiffCallback(List<Match> oldList, List<Match> newList) {
                this.oldList = oldList;
                this.newList = newList;
            }

            @Override
            public int getOldListSize() {
                return oldList != null ? oldList.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newList != null ? newList.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Long.compare(newList.get(newItemPosition).getMatchId(), oldList.get(oldItemPosition).getMatchId()) == 0;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Match oldMatch = oldList.get(oldItemPosition);
                Match newMatch = newList.get(newItemPosition);

                return newMatch.getPlayer().getName().equals(oldMatch.getPlayer().getName()) &&
                        newMatch.getOpponent().getName().equals(oldMatch.getOpponent().getName()) &&
                        newMatch.getNotes().equals(oldMatch.getNotes()) &&
                        newMatch.getLocation().equals(oldMatch.getLocation());
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                Match oldMatch = oldList.get(oldItemPosition);
                Match newMatch = newList.get(newItemPosition);

                Bundle diff = new Bundle();

                if (!newMatch.getPlayer().getName().equals(oldMatch.getPlayer().getName()) ||
                        !newMatch.getOpponent().getName().equals(oldMatch.getOpponent().getName())) {
                    diff.putString("player_name", newMatch.getPlayer().getName());
                    diff.putString("opp_name", newMatch.getOpponent().getName());
                }

                if (!newMatch.getLocation().equals(oldMatch.getLocation()))
                    diff.putString(DatabaseAdapter.COLUMN_LOCATION, newMatch.getLocation());

                if (diff.size() == 0)
                    return null;

                return diff;
            }
        }

        static class FooterHolder extends BaseViewHolder {
            FooterHolder(View itemView) {
                super(itemView);
            }
        }

        class ListItemHolder extends BaseViewHolder {
            long id;

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
                if (match.getGameStatus().gameType == GameType.STRAIGHT_GHOST) {
                    playerNames.setText(match.getPlayer().getName());
                    breakType.setText(getString(R.string.game_straight_ghost));
                } else if (match.getGameStatus().gameType == GameType.STRAIGHT_POOL) {
                    playerNames.setText(getString(R.string.and, match.getPlayer().getName(), match.getOpponent().getName()));
                } else {
                    playerNames.setText(getString(R.string.and, match.getPlayer().getName(), match.getOpponent().getName()));
                    breakType.setText(getBreakType(match.getGameStatus().breakType, match.getPlayer().getName(), match.getOpponent().getName()));
                }
                gameType.setImageResource(getImageId(match.getGameStatus().gameType));
                ruleSet.setText(getRuleSet(match.getGameStatus().gameType));
                id = match.getMatchId();
            }

            void setLocation(String location) {
                if (location.isEmpty())
                    this.location.setVisibility(View.GONE);
                else {
                    this.location.setVisibility(View.VISIBLE);
                    this.location.setText(location);
                }
            }

            @OnClick(R.id.container)
            public void onClick() {
                final Intent intent = matches.get(getAdapterPosition()).getGameStatus().gameType.isSinglePlayer() ?
                        new Intent(getContext(), HighRunAttemptActivity.class) :
                        new Intent(getContext(), MatchInfoActivity.class);
                intent.putExtra(BaseActivity.ARG_MATCH_ID, id);
                getContext().startActivity(intent);
            }

            @OnLongClick(R.id.container)
            boolean onLongClick() {
                // only display delete dialog from IntroActivity otherwise the user can wrap
                // around to deleting all their matches and then going back to a nonexistent match
                if (getContext() instanceof IntroActivity) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                    builder.setMessage(getString(R.string.delete_match))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DatabaseAdapter(getContext()).deleteMatch(id); // remove from the database
                                    // update recyclerView
                                    matches.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());

                                    // remove the share match string if it exists
                                    IntroActivity activity = (IntroActivity) getContext();
                                    activity.getPreferences().edit().remove(IntroActivity.ARG_MATCH_ID + id).apply();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
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

    private class FilterPlayers extends AsyncTask<StatFilter, Void, List<Match>> {
        @Override
        protected void onPostExecute(List<Match> matches) {
            if (isAdded() && !isCancelled())
                adapter.update(matches);
        }

        @Override
        protected List<Match> doInBackground(StatFilter... filter) {
            List<Match> filteredMatches = new ArrayList<>();

            String opponent = filter[0].getOpponent();
            if (opponent.equals("All opponents"))
                opponent = null;

            List<Match> matches = new DatabaseAdapter(getContext()).getMatches(player, opponent);
            for (Match match : matches) {
                if (filter[0].isMatchQualified(match)) {
                    filteredMatches.add(match);
                }
            }

            return filteredMatches;
        }
    }
}
