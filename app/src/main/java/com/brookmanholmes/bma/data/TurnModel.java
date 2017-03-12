package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brookman Holmes on 2/21/2017.
 */

public class TurnModel {
    private static GameType[] gameTypes = GameType.values();
    private static BallStatus[] ballStatus = BallStatus.values();
    private static AdvStats.HowType[] howTypes = AdvStats.HowType.values();
    private static AdvStats.Angle[] angles = AdvStats.Angle.values();
    private static AdvStats.WhyType[] whyType = AdvStats.WhyType.values();
    private static TurnEnd[] turnEnds = TurnEnd.values();
    private static AdvStats.ShotType[] shotTypes = AdvStats.ShotType.values();
    private static AdvStats.SubType[] subTypes = AdvStats.SubType.values();

    public int turnEnd;
    public int gameType;
    public String tableStatus;
    public boolean isFoul, isSeriousFoul;
    public AdvStatsModel advStats;
    public String playerId;

    public TurnModel() {

    }

    public TurnModel(ITurn turn) {
        turnEnd = turn.getTurnEnd().ordinal();
        gameType = turn.getGameType().ordinal();
        advStats = new AdvStatsModel(turn.getAdvStats());
        tableStatus = tableStatusToString(turn);
        isFoul = turn.isFoul();
        isSeriousFoul = turn.isSeriousFoul();
        playerId = turn.getAdvStats().getPlayer();
    }

    private static String tableStatusToString(ITableStatus table) {
        String tableStatus = table.getGameType().ordinal() + ",";
        for (int i = 1; i <= table.size(); i++) {
            tableStatus += table.getBallStatus(i).ordinal();

            if (i != table.size())
                tableStatus += ",";
        }

        return tableStatus;
    }

    private static ITableStatus stringToTableStatus(String tableInStringForm) {
        String[] ballStatuses = StringUtils.splitByWholeSeparator(tableInStringForm, ",");
        TableStatus table = TableStatus.newTable(gameTypes[Integer.valueOf(ballStatuses[0])]);

        for (int i = 1; i < ballStatuses.length; i++) {
            table.setBallTo(ballStatus[Integer.valueOf(ballStatuses[i])], i);
        }

        return table;
    }

    public static ITurn getTurn(TurnModel model) {
        Collection<AdvStats.Angle> angles = new ArrayList<>();
        Collection<AdvStats.HowType> howTypes = new ArrayList<>();
        Collection<AdvStats.WhyType> whyTypes = new ArrayList<>();

        for (int i : AdvStatsModel.getOrdinalsFromString(model.advStats.angles)) {
            angles.add(TurnModel.angles[i]);
        }
        for (int i : AdvStatsModel.getOrdinalsFromString(model.advStats.whyTypes)) {
            whyTypes.add(TurnModel.whyType[i]);
        }
        for (int i : AdvStatsModel.getOrdinalsFromString(model.advStats.howTypes)) {
            howTypes.add(TurnModel.howTypes[i]);
        }

        AdvStats advStats = new AdvStats.Builder(model.playerId)
                .cbDistance(model.advStats.cbToOb)
                .obDistance(model.advStats.obToPocket)
                .speed(model.advStats.speed)
                .shotType(shotTypes[model.advStats.shotType])
                .subType(subTypes[model.advStats.subType])
                .use(model.advStats.use)
                .startingPosition(model.advStats.startingPosition)
                .cueing(model.advStats.cueX, model.advStats.cueY)
                .angle(angles)
                .howTypes(howTypes)
                .whyTypes(whyTypes)
                .build();

        return new Turn(
                turnEnds[model.turnEnd],
                stringToTableStatus(model.tableStatus),
                model.isFoul,
                model.isSeriousFoul,
                advStats
        );
    }

    @Override
    public String toString() {
        return "TurnModel{" +
                ", turnEnd=" + turnEnd +
                ", gameType=" + gameType +
                ", tableStatus='" + tableStatus + '\'' +
                ", isFoul=" + isFoul +
                ", isSeriousFoul=" + isSeriousFoul +
                ", advStats=" + advStats +
                ", playerId='" + playerId + '\'' +
                '}';
    }

}
