package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.content.Context;
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

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListRecyclerAdapter extends CursorRecyclerAdapter<MatchListRecyclerAdapter.ListItemHolder> {
    public MatchListRecyclerAdapter(Context context) {
        super(null);

        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
        holder.container.setTag(getColumnId(cursor));
        holder.infoButton.setTag(getColumnId(cursor));
        holder.dateLoc.setText(getDateAndLocation(cursor));
        holder.playerNames.setText(getPlayerNames(cursor));
        holder.breakType.setText(getBreakType(cursor));
        holder.gameType.setImageResource(getImageId(cursor));
        Log.i("MatchListRecycler", "Image id: " + getImageId(cursor));
    }

    @Override
    public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_match_row, parent, false));
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

    private String getDateAndLocation(Cursor cursor) {
        if (getLocation(cursor).equals(""))
            return getDate(cursor);
        else
            return getDate(cursor) + "\n@ " + getLocation(cursor);
    }

    private String getDate(Cursor cursor) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
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
        Log.i("MatchListRecycler", cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE))
                + " == " + GameType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE))));

        switch (GameType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE)))) {
            case BCA_EIGHT_BALL:
                return R.drawable.eight_ball;
            case BCA_NINE_BALL:
                return R.drawable.nine_ball;
            case APA_EIGHT_BALL:
                return R.drawable.eight_ball;
            case APA_NINE_BALL:
                return R.drawable.nine_ball;
            case BCA_TEN_BALL:
                return R.drawable.ten_ball;
            case STRAIGHT_POOL:
                return R.drawable.fourteen_ball;
            case AMERICAN_ROTATION:
                return R.drawable.fifteen_ball;
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

    public static class ListItemHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.players)
        TextView playerNames;
        @Bind(R.id.breakType)
        TextView breakType;
        @Bind(R.id.imgGameType)
        ImageView gameType;
        @Bind(R.id.container)
        View container;
        @Bind(R.id.dateLoc)
        TextView dateLoc;
        @Bind(R.id.infoButton)
        ImageView infoButton;

        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
