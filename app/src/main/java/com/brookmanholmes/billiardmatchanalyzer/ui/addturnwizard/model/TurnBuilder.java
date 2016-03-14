package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    public TableStatus tableStatus;
    public TurnEnd turnEnd;
    public boolean scratch = false;
    public boolean lostGame = false;
    public String shotType = "";
    public String shotSubType = "";
    public List<String> angleType = new ArrayList<>();
    public List<String> howTypes = new ArrayList<>();
    public List<String> whyTypes = new ArrayList<>();

    public TurnBuilder(GameType gameType) {
        this.tableStatus = TableStatus.newTable(gameType);
    }

    public TurnBuilder(GameType gameType, List<Integer> ballsOnTable) {
        this.tableStatus = TableStatus.newTable(gameType, ballsOnTable);
    }

    public String advData() {
        return "shotType = " + shotType +
                "\nshotSubType = " + shotSubType +
                "\nangleType = " + angleType.toString() +
                "\nhowTypes = " + howTypes.toString() +
                "\nwhyTypes = " + whyTypes.toString();
    }

    @Override
    public String toString() {
        return "TurnBuilder{" +
                "tableStatus=" + tableStatus.toString() +
                ", turnEnd=" + (turnEnd == null ? "null" : turnEnd.toString()) +
                ", foul=" + scratch +
                '}';
    }
}
