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
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.interfaces.Apa;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.GameTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class DatabaseAdapter {
    // column names
    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_BREAK_TYPE = "break_type";
    public static final String COLUMN_GAME_TYPE = "game_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STATS_DETAIL = "stats_detail";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_PLAYER_TURN = "player_turn";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_PLAYER_RANK = "player_rank";
    public static final String COLUMN_OPPONENT_RANK = "opponent_rank";
    public static final String TABLE_TURNS = "turns_table";
    public static final String COLUMN_TABLE_STATUS = "table_status";
    public static final String COLUMN_TURN_END = "turn_end";
    public static final String COLUMN_SCRATCH = "foul";
    public static final String COLUMN_MATCH_ID = "match_id";
    public static final String COLUMN_TURN_NUMBER = "turn_number";
    public static final String COLUMN_IS_GAME_LOST = "is_game_lost";
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_NAME = "name";
    public static final String TABLE_ADV_STATS = "adv_stats";
    public static final String COLUMN_SHOT_TYPE = "shot_type";
    public static final String COLUMN_SHOT_SUB_TYPE = "shot_sub_type";
    public static final String TABLE_HOWS = "how_table";
    public static final String TABLE_WHYS = "why_table";
    public static final String COLUMN_ADV_STATS_ID = "adv_stats_id";
    public static final String TABLE_ANGLES = "angle_table";
    public static final String COLUMN_STRING = "string";
    public static final String COLUMN_STARTING_POSITION = "starting_position";
    public static final String EMPTY = "";
    private static final String TAG = "DatabaseAdapter";
    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    private static String tableStatusToString(Turn table) {
        String tableStatus = table.getGameType().name() + ",";
        for (int i = 1; i <= table.size(); i++) {
            tableStatus += table.getBallStatus(i).name();

            if (i != table.size())
                tableStatus += ",";
        }

        return tableStatus;
    }

    private static TableStatus stringToTableStatus(String tableInStringForm) {
        String[] ballStatuses = splitByWholeSeparator(tableInStringForm, ",");
        TableStatus table = TableStatus.newTable(GameType.valueOf(ballStatuses[0]));

        for (int i = 1; i < ballStatuses.length; i++) {
            table.setBallTo(BallStatus.valueOf(ballStatuses[i]), i);
        }

        return table;
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, false);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String separator, int max,
                                                        boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();

        if (len == 0) {
            return new String[]{};
        }

        if ((separator == null) || (EMPTY.equals(separator))) {
            // Split on whitespace.
            return splitWorker(str, null, max, preserveAllTokens);
        }

        int separatorLength = separator.length();

        ArrayList<String> substrings = new ArrayList<>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);

            if (end > -1) {
                if (end > beg) {
                    numberOfSubstrings += 1;

                    if (numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                    } else {
                        // The following is OK, because String.substring( beg, end ) excludes
                        // the character at the position 'end'.
                        substrings.add(str.substring(beg, end));

                        // Set the starting point for the next search.
                        // The following is equivalent to beg = end + (separatorLength - 1) + 1,
                        // which is the right calculation:
                        beg = end + separatorLength;
                    }
                } else {
                    // We found a consecutive occurrence of the separator, so skip it.
                    if (preserveAllTokens) {
                        numberOfSubstrings += 1;
                        if (numberOfSubstrings == max) {
                            end = len;
                            substrings.add(str.substring(beg));
                        } else {
                            substrings.add(EMPTY);
                        }
                    }
                    beg = end + separatorLength;
                }
            } else {
                // String.substring( beg ) goes from 'beg' to the end of the String.
                substrings.add(str.substring(beg));
                end = len;
            }
        }

        return substrings.toArray(new String[substrings.size()]);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[]{};
        }
        List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    public DatabaseAdapter open() {
        database = databaseHelper.getReadableDatabase();
        return this;
    }

    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        Cursor c = database.query(TABLE_PLAYERS, new String[]{COLUMN_NAME}, null, null, COLUMN_NAME, null, null);

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
                + COLUMN_NOTES + ", "
                + COLUMN_STATS_DETAIL + ", "
                + COLUMN_LOCATION + "\n";

        final String query = "SELECT " + selection + "from " + TABLE_MATCHES + " m\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 1) p\n"
                + "on p." + COLUMN_MATCH_ID + "= m._id\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 0) opp\n"
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID + "\n"
                + "where m._id = " + id;

        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();

        Match<?> match = createMatchFromCursor(c);

        c.close();

        for (Turn turn : getMatchTurns(id))
            match.addTurn(turn);

        return match;
    }

    private Match<?> createMatchFromCursor(Cursor c) {
        return new Match.Builder(
                c.getString(c.getColumnIndex("player_name")),
                c.getString(c.getColumnIndex("opp_name")))
                .setPlayerTurn(getPlayerTurn(c))
                .setBreakType(getBreakType(c))
                .setPlayerRanks(c.getInt(c.getColumnIndex(COLUMN_PLAYER_RANK)), c.getInt(c.getColumnIndex(COLUMN_OPPONENT_RANK)))
                .setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)))
                .setMatchId(c.getLong(c.getColumnIndex("_id")))
                .setNotes(c.getString(c.getColumnIndex(COLUMN_NOTES)))
                .setStatsDetail(getStatDetail(c))
                .build(getGameType(c));
    }

    private PlayerTurn getPlayerTurn(Cursor c) {
        return PlayerTurn.valueOf(c.getString(c.getColumnIndex(COLUMN_PLAYER_TURN)));
    }

    private BreakType getBreakType(Cursor c) {
        return BreakType.valueOf(c.getString(c.getColumnIndex(COLUMN_BREAK_TYPE)));
    }

    private Match.StatsDetail getStatDetail(Cursor c) {
        return Match.StatsDetail.valueOf(c.getString(c.getColumnIndex(COLUMN_STATS_DETAIL)));
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

        final String query = "SELECT " + selection + "from " + TABLE_MATCHES + " m\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 1) p\n"
                + "on p.match_id = m._id\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 0) opp\n"
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID + "\n"
                + "where " + opp_search + " and " + player_search;

        return database.rawQuery(query, new String[]{opponent, opponent, player, player});
    }

    public long insertPlayer(AbstractPlayer player, long id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, player.getName());
        contentValues.put(COLUMN_MATCH_ID, id);

        return database.insert(TABLE_PLAYERS, null, contentValues);
    }

    public void undoTurn(long id, int turnNumber) {
        String selection = COLUMN_MATCH_ID + "=? and " + COLUMN_TURN_NUMBER + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id), String.valueOf(turnNumber)};

        database.delete(TABLE_TURNS, selection, selectionArgs);
        Cursor c = database.query(TABLE_ADV_STATS, new String[]{COLUMN_ADV_STATS_ID}, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            long advStatsId = c.getLong(c.getColumnIndex(COLUMN_ADV_STATS_ID));
            String where = COLUMN_ADV_STATS_ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(advStatsId)};
            database.delete(TABLE_ADV_STATS, where, whereArgs);
            database.delete(TABLE_WHYS, where, whereArgs);
            database.delete(TABLE_HOWS, where, whereArgs);
            database.delete(TABLE_ANGLES, where, whereArgs);
            c.close();
        }
    }

    public long insertMatch(Match match) {
        if (match.getMatchId() == 0) {
            ContentValues matchValues = new ContentValues();

            GameStatus status = match.getGameStatus();

            matchValues.put(COLUMN_PLAYER_TURN, status.turn.name());
            matchValues.put(COLUMN_GAME_TYPE, status.gameType.name());
            matchValues.put(COLUMN_CREATED_ON, getCurrentDate());
            matchValues.put(COLUMN_BREAK_TYPE, status.breakType.name());
            matchValues.put(COLUMN_LOCATION, match.getLocation());
            matchValues.put(COLUMN_STATS_DETAIL, match.getStatsLevel().name());
            matchValues.put(COLUMN_NOTES, match.getNotes());

            if (match.getPlayer() instanceof Apa && match.getOpponent() instanceof Apa) {
                matchValues.put(COLUMN_PLAYER_RANK, ((Apa) match.getPlayer()).getRank());
                matchValues.put(COLUMN_OPPONENT_RANK, ((Apa) match.getOpponent()).getRank());
            }


            long matchId = database.insert(TABLE_MATCHES, null, matchValues);
            match.setMatchId(matchId);
            insertPlayer(match.getPlayer(), matchId);
            insertPlayer(match.getOpponent(), matchId);
        }

        return match.getMatchId();
    }

    public void updateMatchNotes(String matchNotes, long matchId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTES, matchNotes);

        database.update(TABLE_MATCHES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(matchId)});
    }

    public void updateMatchLocation(String location, long matchId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);

        database.update(TABLE_MATCHES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(matchId)});
    }


    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public long insertTurn(Turn turn, long matchId, int turnCount) {
        database.delete(TABLE_TURNS,
                COLUMN_MATCH_ID + "=? AND "
                        + COLUMN_TURN_NUMBER + " >= ?",
                new String[]{String.valueOf(matchId),
                        String.valueOf(turnCount)});

        ContentValues turnValues = new ContentValues();

        turnValues.put(COLUMN_TABLE_STATUS, tableStatusToString(turn));
        turnValues.put(COLUMN_MATCH_ID, matchId);
        turnValues.put(COLUMN_SCRATCH, turn.isScratch());
        turnValues.put(COLUMN_TURN_END, turn.getTurnEnd().name());
        turnValues.put(COLUMN_TURN_NUMBER, turnCount);
        turnValues.put(COLUMN_IS_GAME_LOST, turn.isGameLost());

        return database.insert(TABLE_TURNS, null, turnValues);
    }

    public long insertTurn(Turn turn, AdvStats advStats, long matchId, int turnCount) {
        long turnId = insertTurn(turn, matchId, turnCount);

        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOT_TYPE, advStats.getShotType());
        values.put(COLUMN_SHOT_SUB_TYPE, advStats.getShotSubtype());
        values.put(COLUMN_NAME, advStats.getPlayer());
        values.put(COLUMN_TURN_NUMBER, turnCount);
        values.put(COLUMN_MATCH_ID, matchId);
        values.put(COLUMN_STARTING_POSITION, advStats.getStartingPosition());

        long advStatsId = database.insert(TABLE_ADV_STATS, null, values);

        insertAdvStatsList(TABLE_HOWS, advStats.getHowTypes(), advStatsId);
        insertAdvStatsList(TABLE_WHYS, advStats.getWhyTypes(), advStatsId);
        insertAdvStatsList(TABLE_ANGLES, advStats.getAngles(), advStatsId);

        return turnId;
    }

    private void insertAdvStatsList(String table, List<String> values, long advStatsId) {
        for (String value : values) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_STRING, value);
            contentValues.put(COLUMN_ADV_STATS_ID, advStatsId);
            database.insert(table, null, contentValues);
        }
    }

    public List<Turn> getMatchTurns(long id) {
        List<Turn> turns = new ArrayList<>();

        Cursor c = database.query(
                TABLE_TURNS,
                null,
                COLUMN_MATCH_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                COLUMN_TURN_NUMBER + " ASC");

        while (c.moveToNext()) {
            turns.add(buildTurnFromCursor(c));
        }

        c.close();

        return turns;
    }

    public Turn buildTurnFromCursor(Cursor cursor) {
        return new GameTurn(
                cursor.getInt(cursor.getColumnIndex(COLUMN_TURN_NUMBER)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_MATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SCRATCH)) == 1,
                TurnEnd.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TURN_END))),
                stringToTableStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_STATUS))),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_GAME_LOST)) == 1
        );
    }

    public List<AdvStats> getAdvStats(long matchId, String playerName) {
        List<AdvStats> list = new ArrayList<>();


        return list;
    }

    public void deleteMatch(long id) {
        database.delete(TABLE_PLAYERS, COLUMN_MATCH_ID + "=" + id, null);
        database.delete(TABLE_TURNS, COLUMN_MATCH_ID + "=" + id, null);
        database.delete(TABLE_MATCHES, COLUMN_ID + "=" + id, null);
    }

    public void logDatabase(String table) {
        Log.i(TAG, DatabaseUtils.dumpCursorToString(database.query(table, null, null, null, null, null, null)));
    }
}
