package com.brookmanholmes.billiards.player;

import java.util.List;

/**
 * Created by Brookman Holmes on 12/16/2016.
 */
public interface IStraightPool {
    int getHighRun();

    List<Integer> getRunLengths();

    double getAverageRunLength();

    double getMedianRunLength();
}
