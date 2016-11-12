package com.brookmanholmes.bma.ui.stats;

import com.brookmanholmes.billiards.turn.AdvStats;

/**
 * Created by Brookman Holmes on 9/19/2016.
 */
interface FilterListener {
    void updateShotType(String shotType);

    void updateSubType(String subType);

    void updateAngle(String angle);

    boolean isShotType(AdvStats stat);

    boolean isSubType(AdvStats stat);
}
