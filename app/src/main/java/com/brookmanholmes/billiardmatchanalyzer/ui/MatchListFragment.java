package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.matchinfo.MatchInfoActivity;
import com.brookmanholmes.billiardmatchanalyzer.utils.CursorRecyclerAdapter;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.squareup.leakcanary.RefWatcher;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static int LOADER_ID = 100;
    private static String ARG_PLAYER = "arg player";
    private static String ARG_OPPONENT = "arg opponent";

    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MatchListRecyclerAdapter adapter;

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
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, null);
        ButterKnife.bind(this, view);

        adapter = new MatchListRecyclerAdapter(getContext(), null, player, opponent);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, getArguments(), this);

        return view;
    }

    @Override public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override public void onDestroyView() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        layoutManager = null;
        getLoaderManager().destroyLoader(LOADER_ID);

        ButterKnife.unbind(this);


        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    /**
     * Loader methods
     */
    @Override public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        return new MatchListLoader(getContext(), player, opponent);
    }

    @Override public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private static class MatchListLoader extends CursorLoader {
        String player, opponent;
        public MatchListLoader(Context context, String player, String opponent) {
            super(context);
            this.player = player;
            this.opponent = opponent;
        }

        @Override public Cursor loadInBackground() {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(getContext());
            return databaseAdapter.getMatches(player, opponent);
        }
    }

    /**
     * Created by Brookman Holmes on 1/13/2016.
     */
    static class MatchListRecyclerAdapter extends CursorRecyclerAdapter<MatchListRecyclerAdapter.ListItemHolder> {
        final Context context;
        private String player, opponent;


        public MatchListRecyclerAdapter(Context context, Cursor cursor, @Nullable String player, @Nullable String opponent) {
            this(context, cursor);

            this.player = player;
            this.opponent = opponent;
        }

        public MatchListRecyclerAdapter(Context context, Cursor cursor) {
            super(cursor);

            this.context = context;
            setHasStableIds(true);
        }

        @Override public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
            holder.setLocation(getLocation(cursor));
            holder.date.setText(getDate(cursor));
            holder.playerNames.setText(getPlayerNames(cursor));
            holder.breakType.setText(getBreakType(cursor));
            holder.gameType.setImageResource(getImageId(cursor));
            holder.ruleSet.setText(getRuleSet(cursor));
        }

        @Override public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListItemHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_match_row, parent, false));
        }

        private String getBreakType(Cursor cursor) {
            switch (BreakType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_BREAK_TYPE)))) {
                case WINNER:
                    return getString(R.string.break_winner);
                case LOSER:
                    return getString(R.string.break_loser);
                case ALTERNATE:
                    return getString(R.string.break_alternate);
                case PLAYER:
                    return getString(R.string.break_player, getPlayerName(cursor));
                case OPPONENT:
                    return getString(R.string.break_player, getOpponentName(cursor));
                case GHOST:
                    return getString(R.string.break_player, getPlayerName(cursor));
                default:
                    throw new IllegalArgumentException();
            }
        }

        private String getDate(Cursor cursor) {
            String dateString = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_CREATED_ON));

            DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
            Date date;
            try {
                date = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }

            return DateFormat.getDateInstance().format(date);
        }

        private String getLocation(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_LOCATION));
        }

        private int getImageId(Cursor cursor) {
            switch (GameType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE)))) {
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

        private String getRuleSet(Cursor cursor) {
            switch (GameType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE)))) {
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

        private String getPlayerNames(Cursor cursor) {
            return getString(R.string.and, getPlayerName(cursor), getOpponentName(cursor));
        }

        private String getPlayerName(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex("player_name"));
        }

        private String getOpponentName(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex("opp_name"));
        }

        private String getString(@StringRes int resId) {
            return context.getString(resId);
        }

        private String getString(@StringRes int resId, Object... formatArgs) {
            return context.getString(resId, formatArgs);
        }

        class ListItemHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.players) TextView playerNames;
            @Bind(R.id.breakType) TextView breakType;
            @Bind(R.id.imgGameType) ImageView gameType;
            @Bind(R.id.location) TextView location;
            @Bind(R.id.date) TextView date;
            @Bind(R.id.ruleSet) TextView ruleSet;

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

            @OnClick(R.id.container) public void onClick() {
                final Intent intent = new Intent(getContext(), MatchInfoActivity.class);
                intent.putExtra(BaseActivity.ARG_MATCH_ID, getItemId());
                getContext().startActivity(intent);
            }

            @OnLongClick(R.id.container) public boolean onLongClick() {
                final DatabaseAdapter database = new DatabaseAdapter(getContext());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setMessage(getString(R.string.delete_match))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                database.deleteMatch(getItemId());
                                swapCursor(database.getMatches(player, opponent));
                                notifyItemRemoved((int) getItemId());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return true;
            }

            private Context getContext() {
                return itemView.getContext();
            }
        }
    }
}
