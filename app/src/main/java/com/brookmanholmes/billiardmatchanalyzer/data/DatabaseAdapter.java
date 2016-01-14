package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.InvalidBallException;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.Apa;

import org.apache.commons.lang3.StringUtils;

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

    private static String tableStatusToString(TableStatus table) {
        String tableStatus = "";
        for (int i = 1; i <= table.size(); i++) {
            tableStatus += table.getBallStatus(i).name() + ",";
        }

        return tableStatus;
    }

    private static TableStatus stringToTableStatus(String tableInStringForm) {
        String[] ballStatuses = StringUtils.splitByWholeSeparator(tableInStringForm, ",");
        TableStatus table = TableStatus.newTable(getGameTypeFromTableSize(ballStatuses.length));

        for (int i = 0; i < ballStatuses.length; i++) {
            table.setBallTo(BallStatus.valueOf(ballStatuses[i]), i);
        }


        return table;
    }

    private static GameType getGameTypeFromTableSize(int size) {
        switch (size) {
            case 9:
                return GameType.APA_NINE_BALL;
            case 10:
                return GameType.BCA_TEN_BALL;
            case 15:
                return GameType.BCA_EIGHT_BALL;
            default:
                throw new InvalidBallException("Invalid size: " + size);
        }
    }

    public DatabaseAdapter open() {
        database = databaseHelper.getReadableDatabase();
        return this;
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

        inningValues.put(COLUMN_TABLE_STATUS, tableStatusToString(turn.getTableStatus()));
        inningValues.put(COLUMN_MATCH_ID, matchId);
        inningValues.put(COLUMN_SCRATCH, turn.isScratch());
        inningValues.put(COLUMN_TURN_END, turn.getTurnEnd().toString());
        inningValues.put(COLUMN_INNING_NUMBER, inningId);

        return database.insert(INNINGS_TABLE, null, inningValues);
    }
}
