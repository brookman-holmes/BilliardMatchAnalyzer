package com.brookmanholmes.bma.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    public final TableStatus tableStatus;
    public final AdvStats.Builder advStats = new AdvStats.Builder();
    public TurnEnd turnEnd;
    public boolean foul;
    public boolean lostGame;

    public TurnBuilder(GameType gameType) {
        this.tableStatus = TableStatus.newTable(gameType);
    }

    public TurnBuilder(GameType gameType, List<Integer> ballsOnTable) {
        this.tableStatus = TableStatus.newTable(gameType, ballsOnTable);
    }

    public String advData() {
        return advStats.toString();
    }

    @Override public String toString() {
        return "TurnBuilder{" +
                "tableStatus=" + tableStatus.toString() +
                ", turnEnd=" + (turnEnd == null ? "null" : turnEnd) +
                ", lostGame=" + lostGame +
                ", foul=" + foul +
                '}';
    }
}
