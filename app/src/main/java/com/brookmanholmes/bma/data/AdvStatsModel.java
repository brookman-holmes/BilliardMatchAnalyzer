package com.brookmanholmes.bma.data;

import android.text.TextUtils;

import com.brookmanholmes.billiards.turn.AdvStats;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Created by Brookman Holmes on 3/3/2017.
 */
public class AdvStatsModel {
    public int shotType;
    public int subType;
    public String howTypes;
    public String whyTypes;
    public String angles;
    public float cbToOb;
    public float obToPocket;
    public int speed;
    public int cueX, cueY;
    public String startingPosition;
    public boolean use;

    public AdvStatsModel() {

    }

    public AdvStatsModel(AdvStats advStats) {
        howTypes = enumListToString(advStats.getHowTypes());
        whyTypes = enumListToString(advStats.getWhyTypes());
        angles = enumListToString(advStats.getAngles());
        shotType = advStats.getShotType().ordinal();
        subType = advStats.getShotSubtype().ordinal();
        cbToOb = advStats.getCbToOb();
        obToPocket = advStats.getObToPocket();
        speed = advStats.getSpeed();
        cueX = advStats.getCueX();
        cueY = advStats.getCueY();
        startingPosition = advStats.getStartingPosition();
        use = advStats.use();
    }

    private static <T extends Enum<T>> String enumListToString(Collection<T> collection) {
        String result = "";

        int size = 0;
        for (T item : collection) {
            result += item.ordinal();
            size += 1;
            if (size != collection.size())
                result += ",";
        }

        return result;
    }

    public static int[] getOrdinalsFromString(String string) {
        String[] strings = StringUtils.splitByWholeSeparator(string, ",");
        int[] result = new int[strings.length];

        for (int i = 0; i < strings.length; i++)
            if (!TextUtils.isEmpty(strings[i]))
                result[i] = Integer.valueOf(strings[i]);

        return result;
    }

    @Override
    public String toString() {
        return "AdvStatsModel{" +
                "shotType=" + shotType +
                ", subType=" + subType +
                ", howTypes=" + howTypes +
                ", whyTypes=" + whyTypes +
                ", angles=" + angles +
                ", cbToOb=" + cbToOb +
                ", obToPocket=" + obToPocket +
                ", speed=" + speed +
                ", cueX=" + cueX +
                ", cueY=" + cueY +
                ", startingPosition='" + startingPosition + '\'' +
                ", use=" + use +
                '}';
    }
}
