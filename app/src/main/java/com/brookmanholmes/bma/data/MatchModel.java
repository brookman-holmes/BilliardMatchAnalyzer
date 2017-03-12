package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

import java.util.Date;
import java.util.EnumSet;

/**
 * Created by Brookman Holmes on 2/19/2017.
 */

public class MatchModel {
    private static final String TAG = "MatchModel";
    private static final GameType[] gameTypes = GameType.values();
    private static final PlayerTurn[] playerTurns = PlayerTurn.values();
    private static final BreakType[] breakTypes = BreakType.values();

    public String playerId, opponentId;
    public String playerName, opponentName;
    public int gameType,
            turn,
            breakType,
            playerRank,
            opponentRank;
    public String notes;
    public String location;
    public long date;
    public int details;
    public String id;
    public int maxAttemptsPerGame = 1;

    public MatchModel() {

    }

    MatchModel(Match match, String id) {
        this.id = id;
        playerId = match.getPlayer().getId();
        opponentId = match.getOpponent().getId();
        playerName = match.getPlayer().getName();
        opponentName = match.getOpponent().getName();
        GameStatus gameStatus = match.getInitialGameStatus();
        gameType = gameStatus.gameType.ordinal();
        turn = gameStatus.turn.ordinal();
        breakType = gameStatus.breakType.ordinal();
        playerRank = match.getPlayer().getRank();
        opponentRank = match.getOpponent().getRank();
        notes = match.getNotes();
        location = match.getLocation();
        date = match.getCreatedOn().getTime();
        details = encodeEnumSet(match.getDetails());
        maxAttemptsPerGame = gameStatus.maxAttemptsPerGame;
    }

    public static Match createMatch(MatchModel model) {
        return new Match.Builder(model.playerId, model.opponentId)
                .setMaxAttemptsPerGhostGame(model.maxAttemptsPerGame)
                .setPlayerNames(model.playerName, model.opponentName)
                .setDetails(decodeEnumSet(model.details))
                .setBreakType(breakTypes[model.breakType])
                .setPlayerTurn(playerTurns[model.turn])
                .setPlayerRanks(model.playerRank, model.opponentRank)
                .setNotes(model.notes)
                .setLocation(model.location)
                .setDate(new Date(model.date))
                .setMatchId(model.id)
                .build(gameTypes[model.gameType]);
    }

    private static int encodeEnumSet(EnumSet<Match.StatsDetail> set) {
        int ret = 0;

        for (Match.StatsDetail val : set)
            ret |= 1 << val.ordinal();

        return ret;
    }

    private static EnumSet<Match.StatsDetail> decodeEnumSet(int code) {
        EnumSet<Match.StatsDetail> result = EnumSet.noneOf(Match.StatsDetail.class);
        Match.StatsDetail[] values = Match.StatsDetail.values();
        while (code != 0) {
            int ordinal = Integer.numberOfTrailingZeros(code);
            code ^= Integer.lowestOneBit(code);
            result.add(values[ordinal]);
        }

        return result;
    }
}
