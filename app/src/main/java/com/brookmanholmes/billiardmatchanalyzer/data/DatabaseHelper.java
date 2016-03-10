package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.brookmanholmes.billiards.match.Match;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "matches_db";
    private static final int DATABASE_VERSION = 10;
    private static DatabaseHelper sInstance;

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static addWinOnBreak "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @NonNull
    private static String getCreateTurnsTableQuery() {
        return "CREATE TABLE " + DatabaseAdapter.TURN_TABLE + "("
                + DatabaseAdapter.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + DatabaseAdapter.COLUMN_MATCH_ID + " INTEGER NOT NULL, "
                + DatabaseAdapter.COLUMN_TURN_END + " INTEGER NOT NULL, "
                + DatabaseAdapter.COLUMN_TABLE_STATUS + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_SCRATCH + " INTEGER DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_IS_GAME_LOST + " INTEGER DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_INNING_NUMBER + " INTEGER DEFAULT 0"
                + ");";
    }

    @NonNull
    private static String getCreateMatchTableQuery() {
        return "CREATE TABLE " + DatabaseAdapter.MATCH_TABLE + "("
                + DatabaseAdapter.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseAdapter.COLUMN_GAME_TYPE + " TEXT COLLATE NOCASE NOT NULL DEFAULT BCA_EIGHT_BALL, "
                + DatabaseAdapter.COLUMN_BREAK_TYPE + " TEXT COLLATE NOCASE NOT NULL DEFAULT WINNER, "
                + DatabaseAdapter.COLUMN_LOCATION + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_PLAYER_TURN + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_CREATED_ON + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_PLAYER_RANK + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_OPPONENT_RANK + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_NOTES + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_STATS_DETAIL + " TEXT COLLATE NOCASE DEFAULT " + Match.StatsDetail.NORMAL.name()
                + ");";
    }

    @NonNull
    private static String getCreatePlayerTableQuery() {
        return "CREATE TABLE " + DatabaseAdapter.PLAYER_TABLE + "("
                + DatabaseAdapter.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseAdapter.COLUMN_NAME + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + DatabaseAdapter.COLUMN_MATCH_ID + " INTEGER NOT NULL"
                + ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTurnsTableQuery());
        db.execSQL(getCreateMatchTableQuery());
        db.execSQL(getCreatePlayerTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.TURN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.MATCH_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.PLAYER_TABLE);
        onCreate(db);
    }
}
