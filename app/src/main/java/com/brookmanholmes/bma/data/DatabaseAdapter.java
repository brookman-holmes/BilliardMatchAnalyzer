package com.brookmanholmes.bma.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.ui.stats.StatFilter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class DatabaseAdapter {
    public static final String COLUMN_LOCATION = "location";
    public static final String EMPTY = "";
    // column names
    static final String TABLE_MATCHES = "matches";
    static final String COLUMN_BREAK_TYPE = "break_type";
    static final String COLUMN_GAME_TYPE = "game_type";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_STATS_DETAIL = "stats_detail";
    static final String COLUMN_NOTES = "notes";
    static final String COLUMN_PLAYER_TURN = "player_turn";
    static final String COLUMN_CREATED_ON = "created_on";
    static final String COLUMN_PLAYER_RANK = "player_rank";
    static final String COLUMN_OPPONENT_RANK = "opponent_rank";
    static final String TABLE_TURNS = "turns_table";
    static final String COLUMN_TABLE_STATUS = "table_status";
    static final String COLUMN_TURN_END = "turn_end";
    static final String COLUMN_SCRATCH = "foul";
    static final String COLUMN_MATCH_ID = "match_id";
    static final String COLUMN_TURN_NUMBER = "turn_number";
    static final String COLUMN_IS_GAME_LOST = "is_game_lost";
    static final String TABLE_PLAYERS = "players";
    static final String COLUMN_NAME = "name";
    static final String TABLE_ADV_STATS = "adv_stats";
    static final String COLUMN_SHOT_TYPE = "shot_type";
    static final String COLUMN_SHOT_SUB_TYPE = "shot_sub_type";
    static final String TABLE_HOWS = "how_table";
    static final String TABLE_WHYS = "why_table";
    static final String COLUMN_ADV_STATS_ID = "adv_stats_id";
    static final String TABLE_ANGLES = "angle_table";
    static final String COLUMN_STRING = "string";
    static final String COLUMN_STARTING_POSITION = "starting_position";
    static final String COLUMN_OB_TO_POCKET = "ob_to_pocket";
    static final String COLUMN_SPEED = "speed";
    static final String COLUMN_CUE_X = "cue_x";
    static final String COLUMN_CUE_Y = "cue_y";
    static final String COLUMN_CB_TO_OB = "cb_to_ob";
    private static final String TAG = "DatabaseAdapter";
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        if (database == null || !database.isOpen()) {
            database = DatabaseHelper.getInstance(context).getReadableDatabase();

            //database.execSQL("PRAGMA temp_store = MEMORY", null); // test performance of this compared to FILE
            database.execSQL("PRAGMA temp_store = FILE;");
            database.execSQL("PRAGMA temp_store_directory = \"" +
                    context.getDatabasePath(DatabaseHelper.DATABASE_NAME).getParentFile().toString()
                    + "\";");
        }
    }

    private static String tableStatusToString(ITurn table) {
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

    public void createSampleMatches() {
        // TODO: 8/19/2016 REMOVE THESE BEFORE RELEASE, I might not have permission to use these people's names
        long match = insertMatch(SampleMatchProvider.getHohmannSvbMatch());
        int count = 0;
        for (ITurn turn : SampleMatchProvider.getHohmannSvbTurns()) {
            insertTurn(turn, match, count++);
        }

        match = insertMatch(SampleMatchProvider.getShawRobertsMatch());
        count = 0;
        for (ITurn turn : SampleMatchProvider.getShawRobertsTurns()) {
            insertTurn(turn, match, count++);
        }
    }

    public List<String> getOpponentsOf(String playerName) {
        List<String> names = new ArrayList<>();
        List<Match> matches = getMatches(playerName, null);

        for (Match match : matches) {
            String player = match.getPlayer().getName();
            String opponent = match.getOpponent().getName();

            if (!playerName.equals(player) && !names.contains(player))
                names.add(player);
            if (!playerName.equals(opponent) && !names.contains(opponent))
                names.add(opponent);
        }

        return names;
    }

    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        List<Match> matches = getMatches();

        for (Match match : matches) {
            if (!names.contains(match.getPlayer().getName()))
                names.add(match.getPlayer().getName());
            if (!match.getGameStatus().gameType.isGhostGame() && !names.contains(match.getOpponent().getName()))
                names.add(match.getOpponent().getName());
        }

        return names;
    }

    public List<AbstractPlayer> getPlayers() {
        List<AbstractPlayer> players = new ArrayList<>();

        for (Match match : getMatches()) {
            combinePlayerInList(players, match.getPlayer());
            if (!match.getGameStatus().gameType.isGhostGame())
                combinePlayerInList(players, match.getOpponent());
        }

        Collections.sort(players, new Comparator<AbstractPlayer>() {
            @Override
            public int compare(AbstractPlayer o1, AbstractPlayer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

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

        if (addPlayer) {
            players.add(playerToAdd);
        }
    }

    public List<Pair<AbstractPlayer, AbstractPlayer>> getPlayerPairs(String playerName) {
        List<Pair<AbstractPlayer, AbstractPlayer>> players = new ArrayList<>();

        Cursor c = database.query(TABLE_PLAYERS,
                new String[]{COLUMN_MATCH_ID},
                COLUMN_NAME + "=?",
                new String[]{playerName},
                null,
                null,
                null);

        while (c.moveToNext()) {
            Match match = getMatch(c.getLong(c.getColumnIndex(COLUMN_MATCH_ID)));
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

        return players;
    }

    private List<Match> getMatches() {
        List<Match> matches = new ArrayList<>();

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
            Match match = createMatchFromCursor(c);
            for (ITurn turn : getMatchTurns(match.getMatchId())) {
                match.addTurn(turn);
            }
            matches.add(match);
        }

        c.close();

        return matches;
    }

    public Match getMatch(long id) {
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

        Match match = createMatchFromCursor(c);

        c.close();

        for (ITurn turn : getMatchTurns(id))
            match.addTurn(turn);

        return match;
    }

    private Match createMatchFromCursor(Cursor c) {
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

    public List<Match> getMatches(@Nullable String player, @Nullable String opponent) {
        List<Match> matches = new ArrayList<>();


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

        Cursor cursor = database.rawQuery(query, queryArgs);
        while (cursor.moveToNext()) {
            matches.add(createMatchFromCursor(cursor));
        }

        cursor.close();
        return matches;
    }

    private void insertPlayer(AbstractPlayer player, long id) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, player.getName());
        contentValues.put(COLUMN_MATCH_ID, id);

        database.insert(TABLE_PLAYERS, null, contentValues);
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
        }
        c.close();
    }

    public long insertMatch(Match match) {

        if (match.getMatchId() == 0) {
            ContentValues matchValues = new ContentValues();

            GameStatus status = match.getGameStatus();

            matchValues.put(COLUMN_PLAYER_TURN, status.turn.name());
            matchValues.put(COLUMN_GAME_TYPE, status.gameType.name());
            matchValues.put(COLUMN_CREATED_ON, formatDate(match.getCreatedOn()));
            matchValues.put(COLUMN_BREAK_TYPE, status.breakType.name());
            matchValues.put(COLUMN_LOCATION, match.getLocation());
            matchValues.put(COLUMN_STATS_DETAIL, match.getStatDetailLevel().name());
            matchValues.put(COLUMN_NOTES, match.getNotes());
            matchValues.put(COLUMN_PLAYER_RANK, match.getPlayer().getRank());
            matchValues.put(COLUMN_OPPONENT_RANK, match.getOpponent().getRank());


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

    private String formatDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
        return dateFormat.format(date);
    }

    public void editPlayerName(long matchId, String name, String newName) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        database.update(TABLE_PLAYERS,
                values,
                COLUMN_NAME + "=? AND " + COLUMN_MATCH_ID + "=?",
                new String[]{name, Long.toString(matchId)});
    }

    public void insertTurn(ITurn turn, long matchId, int turnCount) {

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

        database.insert(TABLE_TURNS, null, turnValues);

        if (turn.getAdvStats() != null && turn.getAdvStats().use()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOT_TYPE, turn.getAdvStats().getShotType().name());
            values.put(COLUMN_SHOT_SUB_TYPE, turn.getAdvStats().getShotSubtype().name());
            values.put(COLUMN_NAME, turn.getAdvStats().getPlayer());
            values.put(COLUMN_TURN_NUMBER, turnCount);
            values.put(COLUMN_MATCH_ID, matchId);
            values.put(COLUMN_CUE_X, turn.getAdvStats().getCueX());
            values.put(COLUMN_CUE_Y, turn.getAdvStats().getCueY());
            values.put(COLUMN_SPEED, turn.getAdvStats().getSpeed());
            values.put(COLUMN_OB_TO_POCKET, turn.getAdvStats().getObToPocket());
            values.put(COLUMN_CB_TO_OB, turn.getAdvStats().getCbToOb());
            values.put(COLUMN_STARTING_POSITION, turn.getAdvStats().getStartingPosition());

            long advStatsId = database.insert(TABLE_ADV_STATS, null, values);

            insertHowList(turn.getAdvStats().getHowTypes(), advStatsId);
            insertWhyList(turn.getAdvStats().getWhyTypes(), advStatsId);
            insertAngleList(turn.getAdvStats().getAngles(), advStatsId);
        }
    }

    private void insertAngleList(List<AdvStats.Angle> values, long advStatsId) {
        for (AdvStats.Angle value : values) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_STRING, value.name());
            contentValues.put(COLUMN_ADV_STATS_ID, advStatsId);
            database.insert(TABLE_ANGLES, null, contentValues);
        }
    }

    private void insertHowList(List<AdvStats.HowType> values, long advStatsId) {
        for (AdvStats.HowType value : values) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_STRING, value.name());
            contentValues.put(COLUMN_ADV_STATS_ID, advStatsId);
            database.insert(TABLE_HOWS, null, contentValues);
        }
    }

    private void insertWhyList(List<AdvStats.WhyType> values, long advStatsId) {
        for (AdvStats.WhyType value : values) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_STRING, value.name());
            contentValues.put(COLUMN_ADV_STATS_ID, advStatsId);
            database.insert(TABLE_WHYS, null, contentValues);
        }
    }

    private AdvStats getAdvStat(long matchId, long turnNumber) {
        AdvStats stat = null;
        Cursor c = database.query(TABLE_ADV_STATS,
                null,
                COLUMN_MATCH_ID + "=? AND " + COLUMN_TURN_NUMBER + "=?",
                new String[]{String.valueOf(matchId), String.valueOf(turnNumber)},
                null,
                null,
                null);

        if (c.moveToFirst()) {
            stat = buildAdvStatsFromCursor(c);
        }

        c.close();
        return stat;
    }

    private List<ITurn> getMatchTurns(long id) {
        List<ITurn> turns = new ArrayList<>();
        Cursor c = getMatchTurnsCursor(id);

        while (c.moveToNext()) {
            turns.add(buildTurnFromCursor(c, getAdvStat(id, c.getLong(c.getColumnIndex(COLUMN_TURN_NUMBER)))));
        }

        c.close();

        return turns;
    }

    private Cursor getMatchTurnsCursor(long id) {


        return database.query(
                TABLE_TURNS,
                null,
                COLUMN_MATCH_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                COLUMN_TURN_NUMBER + " ASC");
    }

    private ITurn buildTurnFromCursor(Cursor cursor, AdvStats advStats) {
        return new Turn(
                TurnEnd.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TURN_END))),
                stringToTableStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_STATUS))),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SCRATCH)) == 1,
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_GAME_LOST)) == 1,
                advStats
        );
    }

    public List<AdvStats> getAdvStats(String playerName, String[] shotTypes) {

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

        return list;
    }

    public List<AdvStats> getAdvStats(String playerName, String[] shotTypes, StatFilter filter) {
        String opponent = null;
        if (!filter.getOpponent().equals("All opponents"))
            opponent = filter.getOpponent();
        List<Match> matches = getMatches(playerName, opponent);

        Set<Long> matchIds = new HashSet<>(matches.size());

        for (Match match : matches) {
            if (filter.isMatchQualified(match))
                matchIds.add(match.getMatchId());
        }


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
            if (matchIds.contains(c.getLong(c.getColumnIndex(COLUMN_MATCH_ID))))
                list.add(buildAdvStatsFromCursor(c));
        }

        c.close();

        return list;
    }

    public List<AdvStats> getAdvStats(long matchId, String playerName, String[] shotTypes) {

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

        return list;
    }

    private AdvStats buildAdvStatsFromCursor(Cursor c) {
        long advStatsId = c.getLong(c.getColumnIndex(COLUMN_ADV_STATS_ID));

        int columnName = c.getColumnIndex(COLUMN_NAME) == -1 ?
                c.getColumnIndex("player_name") :
                c.getColumnIndex(COLUMN_NAME);

        AdvStats.Builder builder = new AdvStats.Builder(c.getString(columnName));
        builder.startingPosition(c.getString(c.getColumnIndex(COLUMN_STARTING_POSITION)))
                .shotType(AdvStats.ShotType.valueOf(c.getString(c.getColumnIndex(COLUMN_SHOT_TYPE))))
                .subType(AdvStats.SubType.valueOf(c.getString(c.getColumnIndex(COLUMN_SHOT_SUB_TYPE))))
                .speed(c.getInt(c.getColumnIndex(COLUMN_SPEED)))
                .cueing(c.getInt(c.getColumnIndex(COLUMN_CUE_X)), c.getInt(c.getColumnIndex(COLUMN_CUE_Y)))
                .cbDistance(c.getFloat(c.getColumnIndex(COLUMN_CB_TO_OB)))
                .obDistance(c.getFloat(c.getColumnIndex(COLUMN_OB_TO_POCKET)))
                .angle(getAngleList(advStatsId))
                .howTypes(getHowList(advStatsId))
                .whyTypes(getWhyList(advStatsId));

        return builder.build();
    }

    private List<AdvStats.Angle> getAngleList(long id) {
        List<AdvStats.Angle> list = new ArrayList<>();

        Cursor c = database.query(TABLE_ANGLES, null, COLUMN_ADV_STATS_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(AdvStats.Angle.valueOf(c.getString(c.getColumnIndex(COLUMN_STRING))));
        }

        c.close();

        return list;
    }

    private List<AdvStats.HowType> getHowList(long id) {
        List<AdvStats.HowType> list = new ArrayList<>();

        Cursor c = database.query(TABLE_HOWS, null, COLUMN_ADV_STATS_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(AdvStats.HowType.valueOf(c.getString(c.getColumnIndex(COLUMN_STRING))));
        }

        c.close();

        return list;
    }

    private List<AdvStats.WhyType> getWhyList(long id) {
        List<AdvStats.WhyType> list = new ArrayList<>();

        Cursor c = database.query(TABLE_WHYS, null, COLUMN_ADV_STATS_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        while (c.moveToNext()) {
            list.add(AdvStats.WhyType.valueOf(c.getString(c.getColumnIndex(COLUMN_STRING))));
        }

        c.close();

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
