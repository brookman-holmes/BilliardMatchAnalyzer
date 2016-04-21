package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.BaseActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.MatchInfoActivity;
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
    public MatchListRecyclerAdapter(Cursor cursor) {
        super(cursor);
        setHasStableIds(true);
    }

    @Override public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
        // TODO: modify these set texts to grab string resources
        holder.location.setText(getLocation(cursor));
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
                return getPlayerName(cursor) + " breaks";
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
                return "BCA";
            case BCA_NINE_BALL:
                return "BCA";
            case BCA_TEN_BALL:
                return "BCA";
            case APA_EIGHT_BALL:
                return "APA";
            case APA_NINE_BALL:
                return "APA";
            case STRAIGHT_POOL:
                return "";
            case AMERICAN_ROTATION:
                return "";
            default:
                return "";
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

    public class ListItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.players) TextView playerNames;
        @Bind(R.id.breakType) TextView breakType;
        @Bind(R.id.imgGameType) ImageView gameType;
        @Bind(R.id.dateLoc) TextView location;
        @Bind(R.id.date) TextView date;
        @Bind(R.id.ruleSet) TextView ruleSet;

        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.container) public void onClick() {
            Intent intent = new Intent(itemView.getContext(), MatchInfoActivity.class);
            intent.putExtra(BaseActivity.ARG_MATCH_ID, getItemId());
            itemView.getContext().startActivity(intent);
        }

        @OnLongClick(R.id.container) public boolean onLongClick() {
            final DatabaseAdapter database = new DatabaseAdapter(itemView.getContext());
            database.open();

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext(), R.style.AlertDialogTheme);
            builder.setMessage("Would you like to delete this match?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            database.deleteMatch(getItemId());
                            swapCursor(database.getMatches());
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
    }

}
