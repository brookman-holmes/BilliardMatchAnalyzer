package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.brookmanholmes.billiards.match.Match;

import static com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter.*;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "matches_db";
    private static final int DATABASE_VERSION = 11;
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
    public static String getCreateAdvStatsTableQuery() {
        return "CREATE TABLE " + TABLE_ADV_STATS + "("
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_SHOT_TYPE + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_SHOT_SUB_TYPE + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_MATCH_ID + "INTEGER NOT NULL, "
                + ");";
    }

    @NonNull
    public static String getCreateHowTable() {
        return "CREATE TABLE " + TABLE_HOWS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_HOW + " TEXT COLLATE NO CASE"
                + ");";
    }

    @NonNull
    public static String getCreateWhyTable() {
        return "CREATE TABLE " + TABLE_WHYS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_WHY + " TEXT COLLATE NO CASE"
                + ");";
    }

    @NonNull
    public static String getCreateAngleTable() {
        return "CREATE TABLE " + TABLE_ANGLES + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_ANGLE + " TEXT COLLATE NO CASE"
                + ");";
    }

    @NonNull
    public static String getCreateTurnsTableQuery() {
        return "CREATE TABLE " + TABLE_TURNS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_MATCH_ID + " INTEGER NOT NULL, "
                + COLUMN_TURN_END + " INTEGER NOT NULL, "
                + COLUMN_TABLE_STATUS + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_SCRATCH + " INTEGER DEFAULT NULL, "
                + COLUMN_IS_GAME_LOST + " INTEGER DEFAULT NULL, "
                + COLUMN_TURN_NUMBER + " INTEGER DEFAULT 0"
                + ");";
    }

    @NonNull
    private static String getCreateMatchTableQuery() {
        return "CREATE TABLE " + TABLE_MATCHES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GAME_TYPE + " TEXT COLLATE NOCASE NOT NULL DEFAULT BCA_EIGHT_BALL, "
                + COLUMN_BREAK_TYPE + " TEXT COLLATE NOCASE NOT NULL DEFAULT WINNER, "
                + COLUMN_LOCATION + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_PLAYER_TURN + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_CREATED_ON + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_PLAYER_RANK + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_OPPONENT_RANK + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_NOTES + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_STATS_DETAIL + " TEXT COLLATE NOCASE DEFAULT " + Match.StatsDetail.NORMAL.name()
                + ");";
    }

    @NonNull
    private static String getCreatePlayerTableQuery() {
        return "CREATE TABLE " + TABLE_PLAYERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_MATCH_ID + " INTEGER NOT NULL"
                + ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTurnsTableQuery());
        db.execSQL(getCreateMatchTableQuery());
        db.execSQL(getCreatePlayerTableQuery());
        db.execSQL(getCreateAdvStatsTableQuery());
        db.execSQL(getCreateAngleTable());
        db.execSQL(getCreateHowTable());
        db.execSQL(getCreateWhyTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }
}
