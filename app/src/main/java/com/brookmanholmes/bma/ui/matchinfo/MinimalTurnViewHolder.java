package com.brookmanholmes.bma.ui.matchinfo;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.HeatGraph;
import com.brookmanholmes.bma.ui.view.HowMissLayout;
import com.brookmanholmes.bma.ui.view.MiniBarGraphLayout;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

class MinimalTurnViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MinTurnVH";
    @Bind(R.id.turn)
    TextView turnString;
    @Bind(R.id.heatGraph)
    HeatGraph heatGraph;
    @Bind(R.id.aim)
    HowMissLayout aim;
    @Bind(R.id.speed)
    HowMissLayout speed;
    @Bind(R.id.cut)
    HowMissLayout cut;
    @Bind(R.id.bank)
    HowMissLayout bank;
    @Bind(R.id.kick)
    HowMissLayout kick;
    @Bind(R.id.masse)
    HowMissLayout masse;
    @Bind(R.id.speedGraph)
    MiniBarGraphLayout shotSpeed;
    @Bind(R.id.cbGraph)
    MiniBarGraphLayout cbDistance;
    @Bind(R.id.obGraph)
    MiniBarGraphLayout obDistance;

    private EnumSet<Match.StatsDetail> dataCollection;

    MinimalTurnViewHolder(View itemView, Match match) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        dataCollection = EnumSet.copyOf(match.getDetails());

        final IAxisValueFormatter valueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (Float.compare(value, 0) == 0)
                    return "less than .5'";
                else
                    return new DecimalFormat("#.#").format(value) + "'";
            }
        };
        obDistance.setValueFormatter(valueFormatter);
        cbDistance.setValueFormatter(valueFormatter);
    }

    void bind(ITurn turn, PlayerTurn playerTurn, AbstractPlayer player) {
        String color = (playerTurn == PlayerTurn.PLAYER ? "#2196F3" : "#FF3D00");
        TurnStringAdapter turnStringAdapter = new TurnStringAdapter(itemView.getContext(), turn, player, color);

        turnString.setText(turnStringAdapter.getTurnString());

        setAdvData(turn, playerTurn);
    }

    private void setAdvData(ITurn turn, PlayerTurn playerTurn) {
        if (isCueing(playerTurn) && turn.getAdvStats().isCueingValid()) {
            heatGraph.setVisibility(View.VISIBLE);
            heatGraph.setData(new Point(turn.getAdvStats().getCueX(), turn.getAdvStats().getCueY()));
        } else {
            heatGraph.setVisibility(View.GONE);
        }

        if (isSpeed(playerTurn) && turn.getAdvStats().getSpeed() > 0) {
            shotSpeed.setBarWeight(turn.getAdvStats().getSpeed());
            Log.i(TAG, "SPEED: " + turn.getAdvStats().getSpeed());
        } else {
            shotSpeed.setVisibility(View.GONE);
        }

        if (isAngle(playerTurn) || isSimpleAngle(playerTurn)) {

        } else {

        }

        if (isDistances(playerTurn) && turn.getAdvStats().getCbToOb() + turn.getAdvStats().getObToPocket() >= 0) {
            obDistance.setVisibility(View.VISIBLE);
            cbDistance.setVisibility(View.VISIBLE);

            obDistance.setBarWeight(turn.getAdvStats().getObToPocket());
            cbDistance.setBarWeight(turn.getAdvStats().getCbToOb());
            Log.i(TAG, "CB DISTANCE: " + turn.getAdvStats().getCbToOb());
            Log.i(TAG, "OB DISTANCE: " + turn.getAdvStats().getObToPocket());
        } else {
            obDistance.setVisibility(View.GONE);
            cbDistance.setVisibility(View.GONE);
        }

        if (isHowMiss(playerTurn)) {
            aim.setVisibility(View.VISIBLE);
            speed.setVisibility(View.VISIBLE);
            cut.setVisibility(View.VISIBLE);
            kick.setVisibility(View.VISIBLE);
            bank.setVisibility(View.VISIBLE);
            masse.setVisibility(View.VISIBLE);

            List<AdvStats.HowType> how = turn.getAdvStats().getHowTypes();

            aim.setHowMiss(how.contains(AdvStats.HowType.AIM_LEFT), how.contains(AdvStats.HowType.AIM_RIGHT));
            speed.setHowMiss(how.contains(AdvStats.HowType.TOO_SOFT), how.contains(AdvStats.HowType.TOO_HARD));
            cut.setHowMiss(how.contains(AdvStats.HowType.THIN), how.contains(AdvStats.HowType.THICK));
            kick.setHowMiss(how.contains(AdvStats.HowType.KICK_SHORT), how.contains(AdvStats.HowType.KICK_LONG));
            bank.setHowMiss(how.contains(AdvStats.HowType.BANK_SHORT), how.contains(AdvStats.HowType.BANK_LONG));
            masse.setHowMiss(how.contains(AdvStats.HowType.CURVE_EARLY), how.contains(AdvStats.HowType.CURVE_LATE));
        } else {
            aim.setVisibility(View.GONE);
            speed.setVisibility(View.GONE);
            cut.setVisibility(View.GONE);
            kick.setVisibility(View.GONE);
            bank.setVisibility(View.GONE);
            masse.setVisibility(View.GONE);
        }

        if (isSafeties(playerTurn)) {
            // TODO: 12/14/2016 probably have to do something special here
        } else {

        }

        if (isShotType(playerTurn)) {

        } else {

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

    private String formatDistance(float dist) {
        if (Float.compare(dist, 0) == 0)
            return "less than .5'";
        else return dist + "'";
    }
}
