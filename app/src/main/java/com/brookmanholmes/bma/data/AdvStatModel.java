package com.brookmanholmes.bma.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

/**
 * Created by Brookman Holmes on 11/16/2016.
 */
class AdvStatModel implements Parcelable {
    public static final Parcelable.Creator<AdvStatModel> CREATOR = new Parcelable.Creator<AdvStatModel>() {
        @Override
        public AdvStatModel createFromParcel(Parcel source) {
            return new AdvStatModel(source);
        }

        @Override
        public AdvStatModel[] newArray(int size) {
            return new AdvStatModel[size];
        }
    };
    private final String player;
    private final int shotType;
    private final int shotSubtype;
    private final float cbToOb;
    private final float obToPocket;
    private final int speed;
    private final int cueX;
    private final int cueY;
    private final String startingPosition;
    private final boolean use;
    private int howTypes;
    private int whyTypes;
    private int angles;

    AdvStatModel(AdvStats stat) {
        player = stat.getPlayer();
        shotType = stat.getShotType().ordinal();
        shotSubtype = stat.getShotSubtype().ordinal();
        howTypes = encode(EnumSet.copyOf(stat.getHowTypes()));
        whyTypes = encode(EnumSet.copyOf(stat.getWhyTypes()));
        angles = encode(EnumSet.copyOf(stat.getAngles()));
        cbToOb = stat.getCbToOb();
        obToPocket = stat.getObToPocket();
        speed = stat.getSpeed();
        cueX = stat.getCueX();
        cueY = stat.getCueY();
        startingPosition = stat.getStartingPosition();
        use = stat.use();
    }


    protected AdvStatModel(Parcel in) {
        this.player = in.readString();
        this.shotType = in.readInt();
        this.shotSubtype = in.readInt();
        this.howTypes = in.readInt();
        this.whyTypes = in.readInt();
        this.angles = in.readInt();
        this.cbToOb = in.readFloat();
        this.obToPocket = in.readFloat();
        this.speed = in.readInt();
        this.cueX = in.readInt();
        this.cueY = in.readInt();
        this.startingPosition = in.readString();
        this.use = in.readByte() != 0;
    }

    // From finnw's answer on SO see: stackoverflow.com/questions/2199399/storing-enumset-in-a-database
    public static <E extends Enum<E>> int encode(EnumSet<E> set) {
        int ret = 0;

        for (E val : set) {
            ret |= 1 << val.ordinal();
        }

        return ret;
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

    AdvStats createAdvStat() {
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
                .use(use).build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.player);
        dest.writeInt(this.shotType);
        dest.writeInt(this.shotSubtype);
        dest.writeInt(this.howTypes);
        dest.writeInt(this.whyTypes);
        dest.writeInt(this.angles);
        dest.writeFloat(this.cbToOb);
        dest.writeFloat(this.obToPocket);
        dest.writeInt(this.speed);
        dest.writeInt(this.cueX);
        dest.writeInt(this.cueY);
        dest.writeString(this.startingPosition);
        dest.writeByte(this.use ? (byte) 1 : (byte) 0);
    }
}
