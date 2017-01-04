package com.brookmanholmes.bma.ui.matchinfo;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.brookmanholmes.bma.ui.view.HeatGraph;
import com.brookmanholmes.bma.ui.view.HorizontalBarView;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.billiards.turn.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;
import static com.brookmanholmes.billiards.turn.TurnEnd.SAFETY_ERROR;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

class MinimalTurnViewHolder extends BaseViewHolder {
    private static final String TAG = "MinTurnVH";
    @Bind(R.id.turn)
    TextView turnString;
    @Bind(R.id.heatGraph)
    HeatGraph heatGraph;
    @Bind(R.id.speedGraph)
    HorizontalBarView shotSpeed;
    @Bind(R.id.cbGraph)
    HorizontalBarView cbDistance;
    @Bind(R.id.obGraph)
    HorizontalBarView obDistance;
    @Bind(R.id.shotType)
    TextView shotType;
    @Bind(R.id.angle)
    TextView angle;
    @Bind(R.id.divider)
    ViewGroup divider;
    @Bind(R.id.howMissDescription)
    TextView howMissDescription;

    private EnumSet<Match.StatsDetail> dataCollection;

    MinimalTurnViewHolder(View itemView, Match match) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        dataCollection = EnumSet.copyOf(match.getDetails());
    }

    void bind(ITurn turn, PlayerTurn playerTurn, AbstractPlayer player) {
        String color = (playerTurn == PlayerTurn.PLAYER ? "#2196F3" : "#FF3D00");
        TurnStringAdapter turnStringAdapter = new TurnStringAdapter(itemView.getContext(), turn, player, color);

        turnString.setText(turnStringAdapter.getTurnString());

        if (turn.getAdvStats() != null) {
            setAdvData(turn, playerTurn);
            if (turn.getTurnEnd() != GAME_WON)
                divider.setVisibility(View.VISIBLE);
            else divider.setVisibility(View.GONE);
        } else {
            heatGraph.setVisibility(View.GONE);
            shotSpeed.setVisibility(View.GONE);
            angle.setVisibility(View.GONE);
            obDistance.setVisibility(View.GONE);
            cbDistance.setVisibility(View.GONE);
            shotType.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            howMissDescription.setVisibility(View.GONE);
        }
    }

    private String formatDistance(float distance) {
        if (Float.compare(distance, 0) == 0)
            return "less than .5'";
        else return new DecimalFormat("#.#''").format(distance);
    }

    private void setAdvData(ITurn turn, PlayerTurn playerTurn) {
        if (isCueing(playerTurn) && turn.getAdvStats().isCueingValid() && turn.getTurnEnd() == MISS) {
            heatGraph.setVisibility(View.VISIBLE);
            heatGraph.setData(new Point(turn.getAdvStats().getCueX(), turn.getAdvStats().getCueY()));
        } else {
            heatGraph.setVisibility(View.GONE);
        }

        if (isSpeed(playerTurn) && turn.getAdvStats().getSpeed() > 0 && turn.getTurnEnd() == MISS) {
            shotSpeed.setText(String.format(Locale.getDefault(), "Speed: %1$d/10", turn.getAdvStats().getSpeed()));
            shotSpeed.setPercentage(turn.getAdvStats().getSpeed() * 10);
        } else {
            shotSpeed.setVisibility(View.GONE);
        }

        if (turn.getAdvStats() != null && turn.getAdvStats().getAngles().size() > 0) {
            angle.setVisibility(View.VISIBLE);
            String title = turn.getAdvStats().getAngles().size() == 1 ? "Angle: %1$s" : "Angles: %1$s";
            angle.setText(String.format(title,
                    StringUtils.join(getAngleStringList(turn.getAdvStats().getAngles()), ", ").toLowerCase()));
        } else {
            angle.setVisibility(View.GONE);
        }

        if (isHowMiss(playerTurn) && (turn.getTurnEnd() == MISS || turn.getTurnEnd() == SAFETY_ERROR)) {
            String title = "How the shot was missed: %1$s";
            howMissDescription.setText(String.format(Locale.getDefault(), title,
                    StringUtils.join(getHowTypeStringList(turn.getAdvStats().getHowTypes()), ", ")));
            howMissDescription.setVisibility(View.VISIBLE);
        } else {
            howMissDescription.setVisibility(View.GONE);
        }

        if (isDistances(playerTurn) && turn.getAdvStats().getCbToOb() + turn.getAdvStats().getObToPocket() >= 0 && turn.getTurnEnd() == MISS) {
            obDistance.setVisibility(View.VISIBLE);
            cbDistance.setVisibility(View.VISIBLE);

            obDistance.setText(String.format(Locale.getDefault(), "OB->Pocket %1$s", formatDistance(turn.getAdvStats().getObToPocket())));
            cbDistance.setText(String.format(Locale.getDefault(), "OB->CB %1$s", formatDistance(turn.getAdvStats().getCbToOb())));
            obDistance.setPercentage(turn.getAdvStats().getObToPocket() / 9.5f * 100);
            cbDistance.setPercentage(turn.getAdvStats().getCbToOb() / 9.5f * 100);
        } else {
            obDistance.setVisibility(View.GONE);
            cbDistance.setVisibility(View.GONE);
        }

        if (isShotType(playerTurn) && turn.getTurnEnd() == MISS) {
            shotType.setVisibility(View.VISIBLE);
            String type;
            if (turn.getAdvStats().getShotType() == AdvStats.ShotType.CUT)
                type = itemView.getContext().getString(MatchDialogHelperUtils.convertSubTypeToStringRes(turn.getAdvStats().getShotSubtype()));
            else
                type = itemView.getContext().getString(MatchDialogHelperUtils.convertShotTypeToStringRes(turn.getAdvStats().getShotType()));
            shotType.setText(String.format("Shot type: %1$s", type));
        } else {
            shotType.setVisibility(View.GONE);
        }

        if (isSafeties(playerTurn) && turn.getTurnEnd() == TurnEnd.SAFETY) {
            shotType.setVisibility(View.VISIBLE);
            shotType.setText(MatchDialogHelperUtils.convertSubTypeToStringRes(turn.getAdvStats().getShotSubtype()));
        } else if (!(isShotType(playerTurn) && turn.getTurnEnd() == MISS)) {
            shotType.setVisibility(View.GONE);
        }
    }

    private boolean isShotType(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SHOT_TYPE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SHOT_TYPE_OPPONENT);
        }
    }

    private boolean isCueing(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.CUEING_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.CUEING_OPPONENT);
        }
    }

    private boolean isHowMiss(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.HOW_MISS_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.HOW_MISS_OPPONENT);
        }
    }

    private boolean isSafeties(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SAFETIES_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SAFETIES_OPPONENT);
        }
    }

    private boolean isSpeed(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SPEED_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SPEED_OPPONENT);
        }
    }

    private boolean isDistances(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.BALL_DISTANCES_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.BALL_DISTANCES_OPPONENT);
        }
    }

    private boolean isAngle(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.ANGLE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.ANGLE_OPPONENT);
        }
    }

    private boolean isSimpleAngle(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.ANGLE_SIMPLE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.ANGLE_SIMPLE_OPPONENT);
        }
    }

    private List<String> getAngleStringList(List<AdvStats.Angle> angles) {
        List<String> strings = new ArrayList<>();
        for (AdvStats.Angle angle : angles) {
            strings.add(itemView.getContext().getString(MatchDialogHelperUtils.convertAngleToStringRes(angle)));
        }

        return strings;
    }

    private List<String> getHowTypeStringList(List<AdvStats.HowType> howTypes) {
        List<String> strings = new ArrayList<>();
        for (AdvStats.HowType howType : howTypes) {
            strings.add(itemView.getContext().getString(MatchDialogHelperUtils.convertHowToStringRes(howType)));
        }

        return strings;
    }
}
