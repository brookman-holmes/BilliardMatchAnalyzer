package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListRecyclerAdapter extends CursorRecyclerAdapter<MatchListRecyclerAdapter.ListItemHolder> {
    private ListItemClickListener listener;

    public MatchListRecyclerAdapter(ListItemClickListener listener) {
        super(null);
        setHasStableIds(true);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
        holder.container.setTag(getColumnId(cursor));
        holder.location.setText(getLocation(cursor));
        holder.date.setText(getDate(cursor));
        holder.playerNames.setText(getPlayerNames(cursor));
        holder.breakType.setText(getBreakType(cursor));
        holder.gameType.setImageResource(getImageId(cursor));
    }

    @Override
    public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plain_match_row, parent, false));
    }

    private String getBreakType(Cursor cursor) {
        switch (BreakType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_BREAK_TYPE)))) {
            case WINNER:
                return "Winner breaks";
            case LOSER:
                return "Loser breaks";
            case ALTERNATE:
                return "Alternating breaks";
            case PLAYER:
                return getPlayerName(cursor) + " breaks";
            case OPPONENT:
                return getOpponentName(cursor) + " breaks";
            case GHOST:
                return "Not sure what to put in here";
            default:
                throw new IllegalArgumentException();
        }
    }

    private String getDate(Cursor cursor) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, MMM d");
        Date date;
        try {
            date = oldFormat.parse(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_CREATED_ON)));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return newFormat.format(date);
    }

    private String getLocation(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_LOCATION));
    }

    private int getImageId(Cursor cursor) {
        switch (GameType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE)))) {
            case BCA_EIGHT_BALL:
                return R.drawable.eight_ball;
            default:
                return R.drawable.eight_ball;
        }
    }

    private String getPlayerNames(Cursor cursor) {
        return getPlayerName(cursor) + " & " + getOpponentName(cursor);
    }

    private String getPlayerName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex("player_name"));
    }

    private String getOpponentName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex("opp_name"));
    }

    private long getColumnId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.COLUMN_ID));
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.players)
        TextView playerNames;
        @Bind(R.id.breakType)
        TextView breakType;
        @Bind(R.id.imgGameType)
        ImageView gameType;
        @Bind(R.id.container)
        View container;
        @Bind(R.id.dateLoc)
        TextView location;
        @Bind(R.id.date)
        TextView date;

        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.container)
        public void onClick() {
            Log.i("ListItemHolder", "onClick container id: " + container.getTag());
            listener.onSelectMatch(getMatchId());
        }

        @OnLongClick(R.id.container)
        public boolean onLongClick() {
            Log.i("ListItemHolder", "onLongClick container id: " + container.getTag());
            listener.onLongSelectMatch(getMatchId());
            return true;
        }

        private long getMatchId() {
            return (long) container.getTag();
        }
    }

}
