package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.match.Match;

import java.util.List;
import java.util.Map;

/**
 * Created by Brookman Holmes on 3/5/2017.
 */

public interface MatchListener {
    void update(List<Match> matches, Map<String, List<String>> turnIds);
}
