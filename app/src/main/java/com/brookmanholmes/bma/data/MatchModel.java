package com.brookmanholmes.bma.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/11/2016.
 */

public class MatchModel implements Parcelable {
    public static final Creator<MatchModel> CREATOR = new Creator<MatchModel>() {
        @Override
        public MatchModel createFromParcel(Parcel source) {
            return new MatchModel(source);
        }

        @Override
        public MatchModel[] newArray(int size) {
            return new MatchModel[size];
        }
    };
    private static final String TAG = "MatchModel";

    private String playerName, opponentName;
    private int playerRank = 100, opponentRank = 100;
    private int breakType = BreakType.ALTERNATE.ordinal();
    private int playerTurn = PlayerTurn.PLAYER.ordinal();
    private int gameType = GameType.BCA_EIGHT_BALL.ordinal();
    private String location = "";
    private String notes = "";
    private Date date;
    private int details = 0;
    private List<TurnModel> turns = new ArrayList<>();
    private List<TurnModel> undoneTurns = new ArrayList<>();

    public MatchModel(Match match) {
        playerName = match.getPlayer().getName();
        opponentName = match.getOpponent().getName();
        playerRank = match.getPlayer().getRank();
        opponentRank = match.getOpponent().getRank();

        GameStatus gameStatus = match.getInitialGameStatus();

        breakType = gameStatus.breakType.ordinal();
        playerTurn = gameStatus.turn.ordinal();
        gameType = gameStatus.gameType.ordinal();
        location = match.getLocation();
        notes = match.getNotes();
        date = match.getCreatedOn();
        details = DatabaseAdapter.encodeEnumSet(match.getDetails());

        if (gameStatus.gameType.isGhostGame())
            addTurnsForGhostGame(match);
        else
            addTurns(match);

        for (ITurn turn : match.getUndoneTurns()) {
            undoneTurns.add(new TurnModel(turn));
        }
    }

    protected MatchModel(Parcel in) {
        this.playerName = in.readString();
        this.opponentName = in.readString();
        this.playerRank = in.readInt();
        this.opponentRank = in.readInt();
        this.breakType = in.readInt();
        this.playerTurn = in.readInt();
        this.gameType = in.readInt();
        this.location = in.readString();
        this.notes = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.details = in.readInt();
        this.turns = in.createTypedArrayList(TurnModel.CREATOR);
        this.undoneTurns = in.createTypedArrayList(TurnModel.CREATOR);
    }

    public static byte[] marshall(Match match) {
        return marshall(new MatchModel(match));
    }

    public static byte[] marshall(MatchModel model) {
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static MatchModel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);

        MatchModel result = CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

    private void addTurns(Match match) {
        for (int i = 0; i < match.getTurns().size(); i++) {
            turns.add(new TurnModel(match.getTurns().get(i)));
        }
    }

    private void addTurnsForGhostGame(Match match) {
        for (int i = 0; i < match.getTurns().size(); ) {
            turns.add(new TurnModel(match.getTurns().get(i)));

            if (match.getTurns().get(i).getTurnEnd() != TurnEnd.GAME_WON) {
                i += 1; // skip the next turn because it's the ghost
            }

            i += 1; // increment loop
        }
    }

    public Match getMatch() {
        Match match = new Match.Builder(playerName, opponentName)
                .setPlayerTurn(PlayerTurn.values()[playerTurn])
                .setBreakType(BreakType.values()[breakType])
                .setDate(date)
                .setPlayerRanks(playerRank, opponentRank)
                .setLocation(location)
                .setNotes(notes)
                .setDetails(DatabaseAdapter.decodeEnumSet(details))
                .build(GameType.values()[gameType]);

        for (TurnModel turnModel : turns) {
            match.addTurn(turnModel.createTurn());
        }

        return match;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playerName);
        dest.writeString(this.opponentName);
        dest.writeInt(this.playerRank);
        dest.writeInt(this.opponentRank);
        dest.writeInt(this.breakType);
        dest.writeInt(this.playerTurn);
        dest.writeInt(this.gameType);
        dest.writeString(this.location);
        dest.writeString(this.notes);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.details);
        dest.writeTypedList(this.turns);
        dest.writeTypedList(this.undoneTurns);
    }
}
