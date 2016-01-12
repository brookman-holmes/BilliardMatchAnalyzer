package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Apa;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class DatabaseAdapter {
    // column names
    public static final String MATCH_TABLE = "matches";
    public static final String COLUMN_BREAK_TYPE = "break_type";
    public static final String COLUMN_GAME_TYPE = "game_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PLAYER_TURN = "player_turn";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_PLAYER_RANK = "player_rank";
    public static final String COLUMN_OPPONENT_RANK = "opponent_rank";

    public static final String INNINGS_TABLE = "innings";
    public static final String COLUMN_TABLE_STATUS = "table_status";
    public static final String COLUMN_TURN_END = "turn_end";
    public static final String COLUMN_SCRATCH = "scratch";
    public static final String COLUMN_MATCH_ID = "match_id";
    public static final String COLUMN_INNING_NUMBER = "inning_number";

    public static final String PLAYER_TABLE = "players";
    public static final String COLUMN_NAME = "name";

    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public DatabaseAdapter open() {
        database = databaseHelper.getReadableDatabase();
        return this;
    }

    public long insertPlayer(AbstractPlayer player, long id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, player.getName());
        contentValues.put(COLUMN_MATCH_ID, id);

        return database.insert(PLAYER_TABLE, null, contentValues);
    }

    public long insertMatch(Match match) {
        if (match.getMatchId() == 0) {
            ContentValues matchValues = new ContentValues();

            GameStatus status = match.getGameStatus();

            matchValues.put(COLUMN_PLAYER_TURN, status.turn.toString());
            matchValues.put(COLUMN_GAME_TYPE, status.gameType.toString());
            matchValues.put(COLUMN_CREATED_ON, getCurrentDate());
            matchValues.put(COLUMN_BREAK_TYPE, status.breakType.toString());
            matchValues.put(COLUMN_LOCATION, match.getLocation());

            if (match.getPlayer() instanceof Apa && match.getOpponent() instanceof Apa) {
                matchValues.put(COLUMN_PLAYER_RANK, ((Apa) match.getPlayer()).getRank());
                matchValues.put(COLUMN_OPPONENT_RANK, ((Apa) match.getOpponent()).getRank());
            }


            long matchId = myDb.insert(MATCH_TABLE, null, matchValues);
            match.setId(matchId);
            insertPlayer(match.getPlayer(), matchId);
            insertPlayer(match.getOpponent(), matchId);
        }

        return match.id();
    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}
