package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IApa;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.GameTurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        String[] ballStatuses = StringUtils.splitByWholeSeparator(tableInStringForm, ",");
        TableStatus table = TableStatus.newTable(GameType.valueOf(ballStatuses[0]));

        for (int i = 1; i < ballStatuses.length; i++) {
            table.setBallTo(BallStatus.valueOf(ballStatuses[i]), i);
        }

        return table;
    }

    public List<String> getNames() {
        database = databaseHelper.getReadableDatabase();
        ArrayList<String> names = new ArrayList<>();
        Cursor c = database.query(TABLE_PLAYERS, new String[]{COLUMN_NAME}, null, null, COLUMN_NAME, null, null);

        while (c.moveToNext()) {
            names.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
        }

        c.close();
        database.close();

        return names;
    }

    public List<AbstractPlayer> getPlayers() {
        database = databaseHelper.getReadableDatabase();
        List<AbstractPlayer> players = new ArrayList<>();

        for (Match<?> match : getMatches()) {
            combinePlayerInList(players, match.getPlayer());
            combinePlayerInList(players, match.getOpponent());
        }

        return players;
    }

    private void combinePlayerInList(List<AbstractPlayer> players, AbstractPlayer playerToAdd) {
        boolean addPlayer = true;

        for (AbstractPlayer player : players) {
            if (player.getName().equals(playerToAdd.getName())) {
                addPlayer = false;
                player.addPlayerStats(playerToAdd);
                break;
            }
        }

        if (addPlayer)
            players.add(playerToAdd);
    }


    public List<Pair<AbstractPlayer, AbstractPlayer>> getPlayer(String playerName) {
        database = databaseHelper.getReadableDatabase();
        List<Pair<AbstractPlayer, AbstractPlayer>> players = new ArrayList<>();

        Cursor c = database.query(TABLE_PLAYERS,
                new String[]{COLUMN_MATCH_ID},
                COLUMN_NAME + "=?",
                new String[]{playerName},
                null,
                null,
                null);

        while (c.moveToNext()) {
            Match<?> match = getMatch(c.getLong(c.getColumnIndex(COLUMN_MATCH_ID)));
            Pair<AbstractPlayer, AbstractPlayer> pair;

            if (match.getPlayer().getName().equals(playerName)) {
                pair = new ImmutablePair<>(match.getPlayer(), match.getOpponent());
            } else {
                pair = new ImmutablePair<>(match.getOpponent(), match.getPlayer());
            }

            pair.getLeft().setMatchDate(match.getCreatedOn());
            pair.getRight().setMatchDate(match.getCreatedOn());

            players.add(pair);
        }

        c.close();
        database.close();

        return players;
    }

    private List<Match<?>> getMatches() {
        List<Match<?>> matches = new ArrayList<>();
        database = databaseHelper.getReadableDatabase();
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
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID;

        Cursor c = database.rawQuery(query, null);

        while (c.moveToNext()) {
            matches.add(createMatchFromCursor(c));
        }

        return matches;
    }

    public Match<?> getMatch(long id) {
        database = databaseHelper.getReadableDatabase();
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
        database.close();

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
                .setDate(getDate(c))
                .setPlayerRanks(c.getInt(c.getColumnIndex(COLUMN_PLAYER_RANK)), c.getInt(c.getColumnIndex(COLUMN_OPPONENT_RANK)))
                .setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)))
                .setMatchId(c.getLong(c.getColumnIndex("_id")))
                .setNotes(c.getString(c.getColumnIndex(COLUMN_NOTES)))
                .setStatsDetail(getStatDetail(c))
                .build(getGameType(c));
    }

    private Date getDate(Cursor c) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);

        Date date;
        try {
            date = dateFormat.parse(c.getString(c.getColumnIndex(COLUMN_CREATED_ON)));
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return date;
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

    public Cursor getMatches(@Nullable String player, @Nullable String opponent) {
        database = databaseHelper.getReadableDatabase();

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

        String query = "SELECT " + selection + "from " + TABLE_MATCHES + " m\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 1) p\n"
                + "on p.match_id = m._id\n"
                + "left join (select " + COLUMN_NAME + ", " + COLUMN_MATCH_ID + ", " + COLUMN_ID + " from "
                + TABLE_PLAYERS + " where " + COLUMN_ID + " % 2 = 0) opp\n"
                + "on p." + COLUMN_MATCH_ID + "=" + "opp." + COLUMN_MATCH_ID;

        if (player != null && opponent != null)
            query += " where (player_name = ? or opp_name = ?) AND (player_name = ? or opp_name =?)";
        else if (opponent != null || player != null)
            query += " where (player_name = ? or opp_name = ?)";

        String[] queryArgs;
        if (player == null && opponent == null)
            queryArgs = new String[0];
        else {
            queryArgs = new String[(player != null && opponent != null ? 4 : 2)]; // set it to 4 or 2

            if (player != null) {
                queryArgs[0] = player;
                queryArgs[1] = player;
                if (opponent != null) {
                    queryArgs[2] = opponent;
                    queryArgs[3] = opponent;
                }
            } else {
                queryArgs[0] = opponent;
                queryArgs[1] = opponent;
            }
        }

        return database.rawQuery(query, queryArgs);
    }

    public long insertPlayer(AbstractPlayer player, long id) {
        database = databaseHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, player.getName());
        contentValues.put(COLUMN_MATCH_ID, id);

        long playerId = database.insert(TABLE_PLAYERS, null, contentValues);
        database.close();
        return playerId;
    }

    public void undoTurn(long id, int turnNumber) {
        database = databaseHelper.getReadableDatabase();
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
        database.close();
    }

    public long insertMatch(Match match) {
        database = databaseHelper.getReadableDatabase();
        if (match.getMatchId() == 0) {
            ContentValues matchValues = new ContentValues();

            GameStatus status = match.getGameStatus();

            matchValues.put(COLUMN_PLAYER_TURN, status.turn.name());
            matchValues.put(COLUMN_GAME_TYPE, status.gameType.name());
            matchValues.put(COLUMN_CREATED_ON, getCurrentDate());
            matchValues.put(COLUMN_BREAK_TYPE, status.breakType.name());
            matchValues.put(COLUMN_LOCATION, match.getLocation());
            matchValues.put(COLUMN_STATS_DETAIL, match.getAdvStats().name());
            matchValues.put(COLUMN_NOTES, match.getNotes());

            if (match.getPlayer() instanceof IApa && match.getOpponent() instanceof IApa) {
                matchValues.put(COLUMN_PLAYER_RANK, ((IApa) match.getPlayer()).getRank());
                matchValues.put(COLUMN_OPPONENT_RANK, ((IApa) match.getOpponent()).getRank());
            }


            long matchId = database.insert(TABLE_MATCHES, null, matchValues);
            match.setMatchId(matchId);
            insertPlayer(match.getPlayer(), matchId);
            insertPlayer(match.getOpponent(), matchId);
        }

        database.close();
        return match.getMatchId();
    }

    public void updateMatchNotes(String matchNotes, long matchId) {
        database = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTES, matchNotes);

        database.update(TABLE_MATCHES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(matchId)});
        database.close();
    }

    public void updateMatchLocation(String location, long matchId) {
        database = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);

        database.update(TABLE_MATCHES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(matchId)});
        database.close();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
        return dateFormat.format(new Date());
    }

    public void editPlayerName(long matchId, String name, String newName) {
        database = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        int num = database.update(TABLE_PLAYERS,
                values,
                COLUMN_NAME + "=? AND " + COLUMN_MATCH_ID + "=?",
                new String[]{name, Long.toString(matchId)});
        database.close();
    }

    public long insertTurn(Turn turn, long matchId, int turnCount) {
        database = databaseHelper.getReadableDatabase();
        database.delete(TABLE_TURNS,
                COLUMN_MATCH_ID + "=? AND "
                        + COLUMN_TURN_NUMBER + " >= ?",
                new String[]{String.valueOf(matchId),
                        String.valueOf(turnCount)});

        ContentValues turnValues = new ContentValues();

        turnValues.put(COLUMN_TABLE_STATUS, tableStatusToString(turn));
        turnValues.put(COLUMN_MATCH_ID, matchId);
        turnValues.put(COLUMN_SCRATCH, turn.isFoul());
        turnValues.put(COLUMN_TURN_END, turn.getTurnEnd().name());
        turnValues.put(COLUMN_TURN_NUMBER, turnCount);
        turnValues.put(COLUMN_IS_GAME_LOST, turn.isGameLost());

        long turnId = database.insert(TABLE_TURNS, null, turnValues);

        if (turn.getAdvStats().use()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOT_TYPE, turn.getAdvStats().getShotType());
            values.put(COLUMN_SHOT_SUB_TYPE, turn.getAdvStats().getShotSubtype());
            values.put(COLUMN_NAME, turn.getAdvStats().getPlayer());
            values.put(COLUMN_TURN_NUMBER, turnCount);
            values.put(COLUMN_MATCH_ID, matchId);
            values.put(COLUMN_STARTING_POSITION, turn.getAdvStats().getStartingPosition());

            long advStatsId = database.insert(TABLE_ADV_STATS, null, values);

            insertAdvStatsList(TABLE_HOWS, turn.getAdvStats().getHowTypes(), advStatsId);
            insertAdvStatsList(TABLE_WHYS, turn.getAdvStats().getWhyTypes(), advStatsId);
            insertAdvStatsList(TABLE_ANGLES, turn.getAdvStats().getAngles(), advStatsId);
        }

        database.close();
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

    private AdvStats getAdvStat(long matchId, long turnNumber) {
        Cursor c = database.query(TABLE_ADV_STATS,
                null,
                COLUMN_MATCH_ID + "=? AND " + COLUMN_TURN_NUMBER + "=?",
                new String[]{String.valueOf(matchId), String.valueOf(turnNumber)},
                null,
                null,
                null);

        if (c.moveToFirst()) {
            return buildAdvStatsFromCursor(c);
        } else
            return null;
    }

    public List<Turn> getMatchTurns(long id) {
        List<Turn> turns = new ArrayList<>();
        Cursor c = getMatchTurnsCursor(id);

        while (c.moveToNext()) {
            turns.add(buildTurnFromCursor(c, getAdvStat(id, c.getLong(c.getColumnIndex(COLUMN_TURN_NUMBER)))));
        }

        c.close();
        database.close();

        return turns;
    }

    private Cursor getMatchTurnsCursor(long id) {
        database = databaseHelper.getReadableDatabase();

        return database.query(
                TABLE_TURNS,
                null,
                COLUMN_MATCH_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                COLUMN_TURN_NUMBER + " ASC");
    }

    public Turn buildTurnFromCursor(Cursor cursor, AdvStats advStats) {
        return new GameTurn(
                cursor.getInt(cursor.getColumnIndex(COLUMN_TURN_NUMBER)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_MATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SCRATCH)) == 1,
                TurnEnd.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TURN_END))),
                stringToTableStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_STATUS))),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_GAME_LOST)) == 1,
                advStats
        );
    }

    public List<AdvStats> getAdvStats(String playerName, String[] shotTypes) {
        database = databaseHelper.getReadableDatabase();
        List<AdvStats> list = new ArrayList<>();

        String query = COLUMN_NAME + "=?";

        String shotTypesQuery = " AND (";
        for (int i = 0; i < shotTypes.length; i++) {
            shotTypesQuery += COLUMN_SHOT_TYPE + "=?";

            if (i != shotTypes.length - 1)
                shotTypesQuery += " OR ";
        }
        shotTypesQuery += ")";

        Cursor c = database.query(TABLE_ADV_STATS,
                null,
                query + shotTypesQuery,
                ArrayUtils.addAll(new String[]{playerName}, shotTypes),
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(buildAdvStatsFromCursor(c));
        }

        c.close();
        database.close();

        return list;
    }

    public List<AdvStats> getAdvStats(long matchId, String playerName, String[] shotTypes) {
        database = databaseHelper.getReadableDatabase();
        List<AdvStats> list = new ArrayList<>();

        String query = COLUMN_MATCH_ID + "=? AND " + COLUMN_NAME + "=?";

        String shotTypesQuery = " AND (";
        for (int i = 0; i < shotTypes.length; i++) {
            shotTypesQuery += COLUMN_SHOT_TYPE + "=?";

            if (i != shotTypes.length - 1)
                shotTypesQuery += " OR ";
        }
        shotTypesQuery += ")";

        Cursor c = database.query(TABLE_ADV_STATS,
                null,
                query + shotTypesQuery,
                ArrayUtils.addAll(new String[]{String.valueOf(matchId), playerName}, shotTypes),
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(buildAdvStatsFromCursor(c));
        }

        c.close();
        database.close();

        return list;
    }

    private AdvStats buildAdvStatsFromCursor(Cursor c) {
        long advStatsId = c.getLong(c.getColumnIndex(COLUMN_ADV_STATS_ID));

        AdvStats.Builder builder = new AdvStats.Builder(c.getString(c.getColumnIndex(COLUMN_NAME)));
        builder.startingPosition(c.getString(c.getColumnIndex(COLUMN_STARTING_POSITION)))
                .shotType(c.getString(c.getColumnIndex(COLUMN_SHOT_TYPE)))
                .subType(c.getString(c.getColumnIndex(COLUMN_SHOT_SUB_TYPE)))
                .angle(getAdvStatList(TABLE_ANGLES, advStatsId))
                .howTypes(getAdvStatList(TABLE_HOWS, advStatsId))
                .whyTypes(getAdvStatList(TABLE_WHYS, advStatsId));

        return builder.build();
    }

    private List<String> getAdvStatList(String table, long id) {
        List<String> list = new ArrayList<>();

        Cursor c = database.query(table, null, COLUMN_ADV_STATS_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex(COLUMN_STRING)));
        }

        c.close();

        return list;
    }

    public void deleteMatch(long id) {
        database = databaseHelper.getReadableDatabase();
        database.delete(TABLE_PLAYERS, COLUMN_MATCH_ID + "=" + id, null);
        database.delete(TABLE_TURNS, COLUMN_MATCH_ID + "=" + id, null);
        database.delete(TABLE_MATCHES, COLUMN_ID + "=" + id, null);
        database.close();
    }

    public void logDatabase(String table) {
        database = databaseHelper.getReadableDatabase();
        Log.i(TAG, DatabaseUtils.dumpCursorToString(database.query(table, null, null, null, null, null, null)));
        database.close();
    }
}
