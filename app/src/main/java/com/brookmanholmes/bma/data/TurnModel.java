package com.brookmanholmes.bma.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

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
    private static final String TAG = "TurnModel";
    private final int turnEnd;
    private final boolean foul;
    private final boolean seriousFoul;
    private final int gameType;
    private int[] ballStatuses;
    private String player = "";
    private int shotType = AdvStats.ShotType.NONE.ordinal();
    private int shotSubtype = AdvStats.SubType.NONE.ordinal();
    private float cbToOb = -1;
    private float obToPocket = -1;
    private int speed = -1;
    private int cueX = -200;
    private int cueY = -200;
    private String startingPosition = "open";
    private boolean use = false;
    private int howTypes = 0;
    private int whyTypes = 0;
    private int angles = 0;

    TurnModel(ITurn turn) {
        if (turn.getAdvStats() != null) {
            player = turn.getAdvStats().getPlayer();
            shotType = turn.getAdvStats().getShotType().ordinal();
            shotSubtype = turn.getAdvStats().getShotSubtype().ordinal();

            if (turn.getAdvStats().getHowTypes().size() > 0)
                howTypes = encode(EnumSet.copyOf(turn.getAdvStats().getHowTypes()));

            if (turn.getAdvStats().getWhyTypes().size() > 0)
                whyTypes = encode(EnumSet.copyOf(turn.getAdvStats().getWhyTypes()));

            if (turn.getAdvStats().getAngles().size() > 0)
                angles = encode(EnumSet.copyOf(turn.getAdvStats().getAngles()));

            cbToOb = turn.getAdvStats().getCbToOb();
            obToPocket = turn.getAdvStats().getObToPocket();
            speed = turn.getAdvStats().getSpeed();
            cueX = turn.getAdvStats().getCueX();
            cueY = turn.getAdvStats().getCueY();
            startingPosition = turn.getAdvStats().getStartingPosition();
            use = true;
        }

        turnEnd = turn.getTurnEnd().ordinal();
        foul = turn.isFoul();
        seriousFoul = turn.isSeriousFoul();
        gameType = turn.getGameType().ordinal();

        ballStatuses = new int[turn.size()];
        for (int i = 1; i <= turn.size(); i++) {
            ballStatuses[i - 1] = turn.getBallStatus(i).ordinal();
        }
    }

    protected TurnModel(Parcel in) {
        this.turnEnd = in.readInt();
        this.foul = in.readInt() == 1;
        this.seriousFoul = in.readInt() == 1;
        this.gameType = in.readInt();
        this.ballStatuses = in.createIntArray();
        this.player = in.readString();
        this.shotType = in.readInt();
        this.shotSubtype = in.readInt();
        this.cbToOb = in.readFloat();
        this.obToPocket = in.readFloat();
        this.speed = in.readInt();
        this.cueX = in.readInt();
        this.cueY = in.readInt();
        this.startingPosition = in.readString();
        this.use = in.readInt() == 1;
        this.howTypes = in.readInt();
        this.whyTypes = in.readInt();
        this.angles = in.readInt();
    }

    // From finnw's answer on SO see: stackoverflow.com/questions/2199399/storing-enumset-in-a-database
    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> EnumSet<E> decode(int code,
                                                         Class<E> enumType) {
        try {
            E[] values = (E[]) enumType.getMethod("values").invoke(null);
            EnumSet<E> result = EnumSet.noneOf(enumType);
            while (code != 0) {
                int ordinal = Integer.numberOfTrailingZeros(code);
                code ^= Integer.lowestOneBit(code);
                result.add(values[ordinal]);
            }
            return result;
        } catch (IllegalAccessException ex) {
            // Shouldn't happen
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            // Probably a NullPointerException, caused by calling this method
            // from within E's initializer.
            throw (RuntimeException) ex.getCause();
        } catch (NoSuchMethodException ex) {
            // Shouldn't happen
            throw new RuntimeException(ex);
        }
    }

    // From finnw's answer on SO see: stackoverflow.com/questions/2199399/storing-enumset-in-a-database
    private static <E extends Enum<E>> int encode(EnumSet<E> set) {
        int ret = 0;

        for (E val : set) {
            ret |= 1 << val.ordinal();
        }

        return ret;
    }

    ITurn createTurn() {
        TableStatus tableStatus = TableStatus.newTable(GameType.values()[gameType]);
        for (int i = 1; i <= tableStatus.size(); i++) {
            tableStatus.setBallTo(BallStatus.values()[ballStatuses[i - 1]], i);
        }

        AdvStats stat = createAdvStat();

        return new Turn(TurnEnd.values()[turnEnd], tableStatus, foul, seriousFoul, use ? createAdvStat() : null);
    }

    private AdvStats createAdvStat() {
        return new AdvStats.Builder(player)
                .shotType(AdvStats.ShotType.values()[shotType])
                .subType(AdvStats.SubType.values()[shotSubtype])
                .howTypes(decode(howTypes, AdvStats.HowType.class))
                .whyTypes(decode(whyTypes, AdvStats.WhyType.class))
                .angle(decode(angles, AdvStats.Angle.class))
                .cbDistance(cbToOb)
                .obDistance(obToPocket)
                .speed(speed)
                .cueing(cueX, cueY)
                .startingPosition(startingPosition)
                .use(use)
                .build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.turnEnd);
        dest.writeInt(this.foul ? 1 : 0);
        dest.writeInt(this.seriousFoul ? 1 : 0);
        dest.writeInt(this.gameType);
        dest.writeIntArray(this.ballStatuses);
        dest.writeString(this.player);
        dest.writeInt(this.shotType);
        dest.writeInt(this.shotSubtype);
        dest.writeFloat(this.cbToOb);
        dest.writeFloat(this.obToPocket);
        dest.writeInt(this.speed);
        dest.writeInt(this.cueX);
        dest.writeInt(this.cueY);
        dest.writeString(this.startingPosition);
        dest.writeInt(this.use ? 1 : 0);
        dest.writeInt(this.howTypes);
        dest.writeInt(this.whyTypes);
        dest.writeInt(this.angles);
    }
}
