package com.brookmanholmes.bma.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
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
import com.brookmanholmes.bma.ui.matchinfo.MatchInfoActivity;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;

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
public class MatchListFragment extends BaseRecyclerFragment implements Filterable {
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

        adapter = new MatchListRecyclerAdapter(getContext(), database.getMatches(player, opponent));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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
        ((MatchListRecyclerAdapter) adapter).update(database.getMatches(player, opponent));
    }

    @Override
    public void setFilter(StatFilter filter) {
        new FilterPlayers().execute(filter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    static class MatchListRecyclerAdapter extends RecyclerView.Adapter<MatchListRecyclerAdapter.ListItemHolder> {
        List<Match> matches;
        Context context;

        public MatchListRecyclerAdapter(Context context, List<Match> matches) {
            this.matches = matches;
            this.context = context;
        }

        @Override
        public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListItemHolder(LayoutInflater.from(context)
                    .inflate(R.layout.card_match_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ListItemHolder holder, int position) {
            Match match = matches.get(position);
            holder.setLocation(match.getLocation());
            holder.date.setText(getDate(match.getCreatedOn()));
            holder.playerNames.setText(getString(R.string.and, match.getPlayer().getName(), match.getOpponent().getName()));
            holder.breakType.setText(getBreakType(match.getGameStatus().breakType, match.getPlayer().getName(), match.getOpponent().getName()));
            holder.gameType.setImageResource(getImageId(match.getGameStatus().gameType));
            holder.ruleSet.setText(getRuleSet(match.getGameStatus().gameType));
            holder.itemView.setTag(match.getMatchId());
            holder.id = match.getMatchId();
        }

        @Override
        public void onBindViewHolder(ListItemHolder holder, int position, List<Object> payloads) {
            if (payloads.size() > position) {
                Bundle bundle = (Bundle) payloads.get(position);
                if (bundle.containsKey(DatabaseAdapter.COLUMN_LOCATION))
                    holder.location.setText(bundle.getString(DatabaseAdapter.COLUMN_LOCATION));
                if (bundle.containsKey("player_name"))
                    holder.playerNames.setText(getString(R.string.and, bundle.getString("player_name"), bundle.getString("opp_name")));
            } else
                super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return matches.size();
        }

        private void update(List<Match> matches) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MatchListDiffCallback(this.matches, matches));
            this.matches = matches;
            diffResult.dispatchUpdatesTo(this);
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
                case GHOST:
                    return getString(R.string.break_player, playerName);
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
                case STRAIGHT_POOL:
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
                default:
                    return "";
            }
        }

        private String getString(@StringRes int resId) {
            return context.getString(resId);
        }

        private String getString(@StringRes int resId, Object... formatArgs) {
            return context.getString(resId, formatArgs);
        }

        private static class MatchListDiffCallback extends DiffUtil.Callback {
            private List<Match> oldList;
            private List<Match> newList;

            public MatchListDiffCallback(List<Match> oldList, List<Match> newList) {
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

        class ListItemHolder extends RecyclerView.ViewHolder {
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

            public ListItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
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
                final Intent intent = new Intent(getContext(), MatchInfoActivity.class);
                intent.putExtra(BaseActivity.ARG_MATCH_ID, id);
                getContext().startActivity(intent);
            }

            @OnLongClick(R.id.container)
            public boolean onLongClick() {
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
        }
    }

    private class FilterPlayers extends AsyncTask<StatFilter, Void, List<Match>> {
        @Override
        protected void onPostExecute(List<Match> matches) {
            if (isAdded() && !isCancelled())
                ((MatchListRecyclerAdapter) adapter).update(matches);
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
