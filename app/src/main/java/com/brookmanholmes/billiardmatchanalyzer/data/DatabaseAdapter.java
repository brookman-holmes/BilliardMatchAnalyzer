package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.GameTurn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.interfaces.Apa;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static final String TAG = "DatabaseAdapter";
    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    private static String tableStatusToString(Turn table) {
        String tableStatus = table.getGameType().toString() + ",";
        for (int i = 1; i <= table.size(); i++) {
            tableStatus += table.getBallStatus(i).name();

            if (i != table.size())
                tableStatus += ",";
        }

        return tableStatus;
    }

    private static TableStatus stringToTableStatus(String tableInStringForm) {
        String[] ballStatuses = StringUtils.splitByWholeSeparator(tableInStringForm, ",");
        TableStatus table = TableStatus.newTable(GameType.valueOf(ballStatuses[0]));

        for (int i = 1; i < ballStatuses.length; i++) {
            table.setBallTo(BallStatus.valueOf(ballStatuses[i]), i);
        }

        return table;
    }

    public DatabaseAdapter open() {
        database = databaseHelper.getReadableDatabase();
        return this;
    }

    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        Cursor c = database.query(PLAYER_TABLE, new String[] {COLUMN_NAME}, null, null, COLUMN_NAME, null, null);

        while (c.moveToNext()) {
            names.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
        }

        c.close();

        return names;
    }

    public Match<?> getMatch(long id) {
        final String selection = "m." + COLUMN_ID + " as _id, "
                + "p." + COLUMN_NAME + " as player_name, "
                + "opp." + COLUMN_NAME + " as opp_name, "
                + "p." + COLUMN_ID + " as player_id, "
                + "opp." + COLUMN_ID + " as opp_id, "
                + COLUMN_PLAYER_TURN + ", "
                + COLUMN_GAME_TYPE + ", "
                + COLUMN_BREAK_TYPE + ", "
                + COLUMN_CREATED_ON + ", "
                + COLUMN_PLAYER_RANK + ", "
                + COLUMN_OPPONENT_RANK + ", "
                + COLUMN_LOCATION + "\n";

        final String query = "SELECT " + selection + "from " + MATCH_TABLE + " m\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + PLAYER_TABLE + " where " + COLUMN_ID + " % 2 = 1) p\n"
                + "on p." + COLUMN_MATCH_ID + "= m._id\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + PLAYER_TABLE + " where " + COLUMN_ID + " % 2 = 0) opp\n"
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID + "\n"
                + "where m._id = " + id;

        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();

        Match<?> match = createMatchFromCursor(c);

        c.close();

        for (Turn turn : getMatchInnings(id))
            match.addTurn(turn);

        return match;
    }

    private Match<?> createMatchFromCursor(Cursor c) {
        Match<?> match = new Match.Builder(
                c.getString(c.getColumnIndex("player_name")),
                c.getString(c.getColumnIndex("opp_name")))
                .setPlayerTurn(getPlayerTurn(c))
                .setBreakType(getBreakType(c))
                .setPlayerRanks(c.getInt(c.getColumnIndex(COLUMN_PLAYER_RANK)), c.getInt(c.getColumnIndex(COLUMN_OPPONENT_RANK)))
                .setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)))
                .build(getGameType(c));

        match.setMatchId(c.getLong(c.getColumnIndex("_id")));
        return match;
    }

    private PlayerTurn getPlayerTurn(Cursor c) {
        return PlayerTurn.valueOf(c.getString(c.getColumnIndex(COLUMN_PLAYER_TURN)));
    }

    private BreakType getBreakType(Cursor c) {
        return BreakType.valueOf(c.getString(c.getColumnIndex(COLUMN_BREAK_TYPE)));
    }

    private GameType getGameType(Cursor c) {
        return GameType.valueOf(c.getString(c.getColumnIndex(COLUMN_GAME_TYPE)));
    }

    public Cursor getMatches() {
        String opponent = "%", player = "%";

        String opp_search;
        String player_search;
        switch (opponent) {
            case "%":
                opp_search = " (player_name like ? or opp_name like ?) ";
                break;
            default:
                opp_search = " (player_name = ? or opp_name = ?) ";
        }

        switch (player) {
            case "%":
                player_search = " (player_name like ? or opp_name like ?) ";
                break;
            default:
                player_search = " (player_name = ? or opp_name = ?) ";
        }
        final String selection = "m." + COLUMN_ID + " as _id, "
                + "p." + COLUMN_NAME + " as player_name, "
                + "opp." + COLUMN_NAME + " as opp_name, "
                + "p." + COLUMN_ID + " as player_id, "
                + "opp." + COLUMN_ID + " as opp_id, "
                + COLUMN_GAME_TYPE + ", "
                + COLUMN_BREAK_TYPE + ", "
                + COLUMN_CREATED_ON + ", "
                + COLUMN_PLAYER_TURN + ", "
                + COLUMN_PLAYER_RANK + ", "
                + COLUMN_OPPONENT_RANK + ", "
                + COLUMN_LOCATION + "\n";

        final String query = "SELECT " + selection + "from " + MATCH_TABLE + " m\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + PLAYER_TABLE + " where " + COLUMN_ID + " % 2 = 1) p\n"
                + "on p.match_id = m._id\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + PLAYER_TABLE + " where " + COLUMN_ID + " % 2 = 0) opp\n"
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID + "\n"
                + "where " + opp_search + " and " + player_search;

        return database.rawQuery(query, new String[]{opponent, opponent, player, player});
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


            long matchId = database.insert(MATCH_TABLE, null, matchValues);
            match.setMatchId(matchId);
            insertPlayer(match.getPlayer(), matchId);
            insertPlayer(match.getOpponent(), matchId);
        }

        return match.getMatchId();
    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public long insertInning(Turn turn, long matchId, int inningId) {
        database.delete(INNINGS_TABLE,
                COLUMN_MATCH_ID + "=? AND "
                        + COLUMN_INNING_NUMBER + " >= ?",
                new String[]{String.valueOf(matchId),
                        String.valueOf(inningId)});

        ContentValues inningValues = new ContentValues();

        inningValues.put(COLUMN_TABLE_STATUS, tableStatusToString(turn));
        inningValues.put(COLUMN_MATCH_ID, matchId);
        inningValues.put(COLUMN_SCRATCH, turn.isScratch());
        inningValues.put(COLUMN_TURN_END, turn.getTurnEnd().toString());
        inningValues.put(COLUMN_INNING_NUMBER, inningId);

        return database.insert(INNINGS_TABLE, null, inningValues);
    }

    public List<Turn> getMatchInnings(long id) {
        List<Turn> innings = new ArrayList<>();

        Cursor c = database.query(
                INNINGS_TABLE,
                null,
                COLUMN_MATCH_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                COLUMN_INNING_NUMBER + " ASC");

        while (c.moveToNext()) {
            innings.add(buildTurnFromCursor(c));
        }
        c.close();

        return innings;
    }

    public Turn buildTurnFromCursor(Cursor cursor) {
        return new GameTurn(
                cursor.getInt(cursor.getColumnIndex(COLUMN_INNING_NUMBER)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_MATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SCRATCH)) == 1,
                TurnEnd.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TURN_END))),
                stringToTableStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_STATUS)))
        );
    }

    public void logDatabase(String table) {
        Log.i(TAG, DatabaseUtils.dumpCursorToString(database.query(table, null, null, null, null, null, null)));
    }
}
