package com.brookmanholmes.bma.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 11/16/2016.
 */
class TurnModel implements Parcelable {
    public static final Creator<TurnModel> CREATOR = new Creator<TurnModel>() {
        @Override
        public TurnModel createFromParcel(Parcel source) {
            return new TurnModel(source);
        }

        @Override
        public TurnModel[] newArray(int size) {
            return new TurnModel[size];
        }
    };
    private final int turnEnd;
    private final boolean foul;
    private final boolean lostGame;
    private final int gameType;
    private int[] ballStatuses;
    private AdvStatModel advStat;

    TurnModel(ITurn turn) {
        if (turn.getAdvStats() != null)
            advStat = new AdvStatModel(turn.getAdvStats());

        turnEnd = turn.getTurnEnd().ordinal();
        foul = turn.isFoul();
        lostGame = turn.isSeriousFoul();
        gameType = turn.getGameType().ordinal();

        ballStatuses = new int[turn.size()];
        for (int i = 1; i <= turn.size(); i++) {
            ballStatuses[i - 1] = turn.getBallStatus(i).ordinal();
        }
    }

    protected TurnModel(Parcel in) {
        this.ballStatuses = in.createIntArray();
        this.turnEnd = in.readInt();
        this.foul = in.readByte() != 0;
        this.lostGame = in.readByte() != 0;
        this.advStat = in.readParcelable(AdvStatModel.class.getClassLoader());
        this.gameType = in.readInt();
    }

    ITurn createTurn() {
        TableStatus tableStatus = TableStatus.newTable(GameType.values()[gameType]);
        for (int i = 1; i <= tableStatus.size(); i++) {
            tableStatus.setBallTo(BallStatus.values()[ballStatuses[i - 1]], i);
        }
        if (advStat != null) {
            return new Turn(TurnEnd.values()[turnEnd], tableStatus, foul, lostGame, advStat.createAdvStat());
        } else {
            return new Turn(TurnEnd.values()[turnEnd], tableStatus, foul, lostGame, null);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.ballStatuses);
        dest.writeInt(this.turnEnd);
        dest.writeByte(this.foul ? (byte) 1 : (byte) 0);
        dest.writeByte(this.lostGame ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.advStat, flags);
        dest.writeInt(this.gameType);
    }
}
