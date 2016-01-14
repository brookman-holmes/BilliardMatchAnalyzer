package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListRecyclerAdapter extends CursorRecyclerAdapter<MatchListRecyclerAdapter.ListItemHolder> {
    private final Drawable[] images;

    public MatchListRecyclerAdapter(Context context) {
        super(null);
        images = new Drawable[2];
        images[0] = ContextCompat.getDrawable(context, R.drawable.test_8_ball);
        images[1] = ContextCompat.getDrawable(context, R.drawable.test_9_ball);
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
        holder.container.setTag(getColumnId(cursor));
        holder.infoButton.setTag(getColumnId(cursor));
        holder.dateLoc.setText(getDateAndLocation(cursor));
        holder.playerNames.setText(getPlayerNames(cursor));
        holder.breakType.setText(getBreakType(cursor));
    }

    @Override
    public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_match_row, parent, false));
    }

    private String getBreakType(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_BREAK_TYPE));
    }

    private String getDateAndLocation(Cursor cursor) {
        if (getLocation(cursor).equals(""))
            return getDate(cursor);
        else
            return getDate(cursor) + " @ " + getLocation(cursor);
    }

    private String getDate(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_CREATED_ON));
    }

    private String getLocation(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_LOCATION));
    }

    private int getImage(Cursor cursor) {
        switch (cursor.getString(cursor.getColumnIndex(DatabaseAdapter.COLUMN_GAME_TYPE))) {
            case "BCA_EIGHT_BALL":
                return 0;
            case "BCA_NINE_BALL":
                return 1;
            case "APA_EIGHT_BALL":
                return 0;
            case "APA_NINE_BALL":
                return 1;
            default:
                return 0;
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
