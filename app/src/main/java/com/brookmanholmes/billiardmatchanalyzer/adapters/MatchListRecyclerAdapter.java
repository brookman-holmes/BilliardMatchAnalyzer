package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.StringRes;
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

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListRecyclerAdapter extends CursorRecyclerAdapter<MatchListRecyclerAdapter.ListItemHolder> {
    Context context;

    public MatchListRecyclerAdapter(Context context, Cursor cursor) {
        super(cursor);

        this.context = context;
        setHasStableIds(true);
    }

    @Override public void onBindViewHolderCursor(ListItemHolder holder, Cursor cursor) {
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

        DateFormat format = DateFormat.getDateInstance();
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException exception) {
            exception.printStackTrace();
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, -1);

        if (DateUtils.isSameDay(date, new Date()))
            return getString(R.string.today);
        else if (DateUtils.isSameDay(date, cal.getTime()))
            return getString(R.string.yesterday);

        return dateString;
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
        @Bind(R.id.dateLoc) TextView location;
        @Bind(R.id.date) TextView date;
        @Bind(R.id.ruleSet) TextView ruleSet;

        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.container) public void onClick() {
            Intent intent = new Intent(context, MatchInfoActivity.class);
            intent.putExtra(BaseActivity.ARG_MATCH_ID, getItemId());
            context.startActivity(intent);
        }

        @OnLongClick(R.id.container) public boolean onLongClick() {
            final DatabaseAdapter database = new DatabaseAdapter(context);

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setMessage(getString(R.string.delete_match))
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
