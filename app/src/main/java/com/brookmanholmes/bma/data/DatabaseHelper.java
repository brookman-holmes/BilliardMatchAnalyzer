package com.brookmanholmes.bma.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

import java.util.EnumSet;

import static com.brookmanholmes.billiards.match.Match.StatsDetail.BALL_DISTANCES_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.BALL_DISTANCES_PLAYER;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.CUEING_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.CUEING_PLAYER;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.HOW_MISS_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.HOW_MISS_PLAYER;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SAFETIES_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SAFETIES_PLAYER;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SHOT_TYPE_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SHOT_TYPE_PLAYER;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SPEED_OPPONENT;
import static com.brookmanholmes.billiards.match.Match.StatsDetail.SPEED_PLAYER;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_ADV_STATS_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_BREAK_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_CB_TO_OB;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_CREATED_ON;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_CUE_X;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_CUE_Y;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_FOUL;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_GAME_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_IS_GAME_LOST;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_LOCATION;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_MATCH_ID;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_NAME;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_NOTES;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_OB_TO_POCKET;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_OPPONENT_RANK;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_PLAYER_RANK;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_PLAYER_TURN;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SHOT_SUB_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SHOT_TYPE;
import static com.brookmanholmes.bma.data.DatabaseAdapter.COLUMN_SPEED;
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
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 25;
    private static DatabaseHelper sInstance;

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static addWinOnBreak "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static synchronized DatabaseHelper getInstance(Context context) {
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
                + DatabaseAdapter.COLUMN_CB_TO_OB + " REAL NOT NULL DEFAULT -1, "
                + DatabaseAdapter.COLUMN_OB_TO_POCKET + " REAL NOT NULL DEFAULT -1, "
                + DatabaseAdapter.COLUMN_SPEED + " INTEGER NOT NULL DEFAULT -1, "
                + DatabaseAdapter.COLUMN_CUE_X + " INTEGER NOT NULL DEFAULT -200, "
                + DatabaseAdapter.COLUMN_CUE_Y + " INTEGER NOT NULL DEFAULT -200, "
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
                + COLUMN_FOUL + " INTEGER DEFAULT NULL, "
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

    private static ContentValues getEightBallCV() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAME_TYPE, GameType.BCA_GHOST_EIGHT_BALL.name());
        contentValues.put(COLUMN_BREAK_TYPE, PlayerTurn.PLAYER.name());
        return contentValues;
    }

    private static ContentValues getNineBallCV() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAME_TYPE, GameType.BCA_GHOST_NINE_BALL.name());
        contentValues.put(COLUMN_BREAK_TYPE, PlayerTurn.PLAYER.name());
        return contentValues;
    }

    private static ContentValues getTenBallCV() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAME_TYPE, GameType.BCA_GHOST_TEN_BALL.name());
        contentValues.put(COLUMN_BREAK_TYPE, PlayerTurn.PLAYER.name());
        return contentValues;
    }

    private static ContentValues getNormalStatValueConversion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATS_DETAIL, DatabaseAdapter.encodeEnumSet(EnumSet.noneOf(Match.StatsDetail.class)));
        return contentValues;
    }

    private static ContentValues getAdvancedPlayerStatValuesConversion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATS_DETAIL, DatabaseAdapter.encodeEnumSet(
                EnumSet.of(SHOT_TYPE_PLAYER,
                        CUEING_PLAYER,
                        HOW_MISS_PLAYER,
                        SAFETIES_PLAYER,
                        SPEED_PLAYER,
                        BALL_DISTANCES_PLAYER)));

        return contentValues;
    }

    private static ContentValues getAdvancedOppStatValuesConversion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATS_DETAIL, DatabaseAdapter.encodeEnumSet(
                EnumSet.of(SHOT_TYPE_OPPONENT,
                        CUEING_OPPONENT,
                        HOW_MISS_OPPONENT,
                        SAFETIES_OPPONENT,
                        SPEED_OPPONENT,
                        BALL_DISTANCES_OPPONENT)));

        return contentValues;
    }

    private static ContentValues getAdvancedStatValuesConversion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATS_DETAIL, DatabaseAdapter.encodeEnumSet(
                EnumSet.of(SHOT_TYPE_PLAYER,
                        CUEING_PLAYER,
                        HOW_MISS_PLAYER,
                        SAFETIES_PLAYER,
                        SPEED_PLAYER,
                        BALL_DISTANCES_PLAYER,
                        SHOT_TYPE_OPPONENT,
                        CUEING_OPPONENT,
                        HOW_MISS_OPPONENT,
                        SAFETIES_OPPONENT,
                        SPEED_OPPONENT,
                        BALL_DISTANCES_OPPONENT)));

        return contentValues;
    }

    private static ContentValues getNewDefaultValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CB_TO_OB, -1);
        contentValues.put(COLUMN_OB_TO_POCKET, -1);
        contentValues.put(COLUMN_SPEED, -1);
        contentValues.put(COLUMN_CUE_X, -200);
        contentValues.put(COLUMN_CUE_Y, -200);

        return contentValues;
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
        boolean notUpdated = true;

        if (oldVersion == 22 && newVersion >= 23) {
            db.update(TABLE_MATCHES, getEightBallCV(),
                    COLUMN_GAME_TYPE + "=? AND " + COLUMN_BREAK_TYPE + "=?",
                    new String[]{GameType.BCA_EIGHT_BALL.name(), "GHOST"});

            db.update(TABLE_MATCHES, getNineBallCV(),
                    COLUMN_GAME_TYPE + "=? AND " + COLUMN_BREAK_TYPE + "=?",
                    new String[]{GameType.BCA_NINE_BALL.name(), "GHOST"});

            db.update(TABLE_MATCHES, getTenBallCV(),
                    COLUMN_GAME_TYPE + "=? AND " + COLUMN_BREAK_TYPE + "=?",
                    new String[]{GameType.BCA_TEN_BALL.name(), "GHOST"});
            notUpdated = false;
        }

        if (newVersion >= 24 && oldVersion >= 22) {
            db.update(TABLE_MATCHES, getNormalStatValueConversion(),
                    COLUMN_STATS_DETAIL + "=?",
                    new String[]{Match.StatsDetail.NORMAL.name()});
            db.update(TABLE_MATCHES, getAdvancedPlayerStatValuesConversion(),
                    COLUMN_STATS_DETAIL + "=?",
                    new String[]{Match.StatsDetail.ADVANCED_PLAYER.name()});
            db.update(TABLE_MATCHES, getAdvancedOppStatValuesConversion(),
                    COLUMN_STATS_DETAIL + "=?",
                    new String[]{Match.StatsDetail.ADVANCED_OPPONENT.name()});
            db.update(TABLE_MATCHES, getAdvancedStatValuesConversion(),
                    COLUMN_STATS_DETAIL + "=?",
                    new String[]{Match.StatsDetail.ADVANCED.name()});

            // set the old values down to the new values
            db.update(TABLE_ADV_STATS, getNewDefaultValues(),
                    COLUMN_CB_TO_OB + "=? AND " +
                            COLUMN_OB_TO_POCKET + "=? AND " +
                            COLUMN_SPEED + "=? AND " +
                            COLUMN_CUE_X + "=? AND " +
                            COLUMN_CUE_Y + "=?",
                    new String[]{"0", "0", "0", "0", "0",});

            notUpdated = false;
        }

        if (notUpdated) {
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
}
