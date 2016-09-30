package com.brookmanholmes.bma.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.brookmanholmes.billiards.match.Match;

import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_ADV_STATS_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_BREAK_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_CREATED_ON;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_GAME_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_IS_GAME_LOST;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_LOCATION;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_MATCH_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_NAME;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_NOTES;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_OPPONENT_RANK;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_PLAYER_RANK;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_PLAYER_TURN;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SCRATCH;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SHOT_SUB_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SHOT_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_STARTING_POSITION;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_STATS_DETAIL;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_STRING;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_TABLE_STATUS;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_TURN_END;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_TURN_NUMBER;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_ADV_STATS;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_ANGLES;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_HOWS;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_MATCHES;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_PLAYERS;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_TURNS;
import static com.brookmanholmes.bma.data.DatabaseAdapter.TABLE_WHYS;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "matches_db";
    private static final int DATABASE_VERSION = 22;
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
    private static String getCreateAdvStatsTableQuery() {
        return "CREATE TABLE " + TABLE_ADV_STATS + "("
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_SHOT_TYPE + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_SHOT_SUB_TYPE + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_NAME + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_TURN_NUMBER + " INTEGER NOT NULL, "
                + DatabaseAdapter.COLUMN_CB_TO_OB + " REAL NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_OB_TO_POCKET + " REAL NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_SPEED + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_CUE_X + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseAdapter.COLUMN_CUE_Y + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_STARTING_POSITION + " TEXT COLLATE NOCASE DEFAULT OPEN, "
                + COLUMN_MATCH_ID + " INTEGER NOT NULL"
                + ");";
    }

    @NonNull
    private static String getCreateHowTable() {
        return "CREATE TABLE " + TABLE_HOWS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_STRING + " TEXT COLLATE NOCASE"
                + ");";
    }

    @NonNull
    private static String getCreateWhyTable() {
        return "CREATE TABLE " + TABLE_WHYS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_STRING + " TEXT COLLATE NOCASE"
                + ");";
    }

    @NonNull
    private static String getCreateAngleTable() {
        return "CREATE TABLE " + TABLE_ANGLES + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ADV_STATS_ID + " INTEGER NOT NULL, "
                + COLUMN_STRING + " TEXT COLLATE NOCASE"
                + ");";
    }

    @NonNull
    private static String getCreateTurnsTableQuery() {
        return "CREATE TABLE " + TABLE_TURNS + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_MATCH_ID + " INTEGER NOT NULL, "
                + COLUMN_TURN_END + " TEXT COLLATE NOCASE NOT NULL, "
                + COLUMN_TABLE_STATUS + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_SCRATCH + " INTEGER DEFAULT NULL, "
                + COLUMN_IS_GAME_LOST + " INTEGER DEFAULT NULL, "
                + COLUMN_TURN_NUMBER + " INTEGER DEFAULT 0"
                + ");";
    }

    @NonNull private static String getCreateMatchTableQuery() {
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

    @NonNull private static String getCreatePlayerTableQuery() {
        return "CREATE TABLE " + TABLE_PLAYERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT COLLATE NOCASE DEFAULT NULL, "
                + COLUMN_MATCH_ID + " INTEGER NOT NULL"
                + ");";
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTurnsTableQuery());
        db.execSQL(getCreateMatchTableQuery());
        db.execSQL(getCreatePlayerTableQuery());
        db.execSQL(getCreateAdvStatsTableQuery());
        db.execSQL(getCreateAngleTable());
        db.execSQL(getCreateHowTable());
        db.execSQL(getCreateWhyTable());
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADV_STATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANGLES);
        onCreate(db);
    }
}
