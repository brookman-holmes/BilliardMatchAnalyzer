package com.brookmanholmes.bma.ui.matchinfo;

import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.brookmanholmes.bma.ui.view.HeatGraph;
import com.brookmanholmes.bma.ui.view.HorizontalBarView;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.billiards.turn.TurnEnd.MISS;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

class MinimalTurnViewHolder extends BaseViewHolder {
    private static final String TAG = "MinTurnVH";
    @Bind(R.id.divider)
    View divider;
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
    @Bind(R.id.howMissDescription)
    TextView howMissDescription;

    private EnumSet<Match.StatsDetail> dataCollection;

    MinimalTurnViewHolder(View itemView, Match match) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        dataCollection = EnumSet.copyOf(match.getDetails());
    }

    void bind(ITurn turn, PlayerTurn playerTurn, Player player) {
        String color = ConversionUtils.getColorString(getColor(R.color.colorAccent));
        TurnStringAdapter turnStringAdapter = new TurnStringAdapter(itemView.getContext(), turn, player.getName(), color);
        turnString.setText(turnStringAdapter.getTurnString());

        if (turn.getAdvStats() != null) {
            setAdvData(turn, playerTurn);
        } else {
            heatGraph.setVisibility(View.GONE);
            shotSpeed.setVisibility(View.GONE);
            angle.setVisibility(View.GONE);
            obDistance.setVisibility(View.GONE);
            cbDistance.setVisibility(View.GONE);
            shotType.setVisibility(View.GONE);
            howMissDescription.setVisibility(View.GONE);
        }
    }

    void showDivider(boolean showDivider, GameStatus gameStatus) {
        divider.setVisibility((showDivider && !gameStatus.newGame) ? View.VISIBLE : View.GONE);

        boolean scrunchTopMargin = !gameStatus.newGame || gameStatus.gameType.isStraightPool();

        CardView.LayoutParams params = new CardView.LayoutParams(itemView.getLayoutParams());
        params.topMargin = (int) ConversionUtils.convertDpToPx(itemView.getContext(), scrunchTopMargin ? -6 : 8);
        params.bottomMargin = (int) ConversionUtils.convertDpToPx(itemView.getContext(), 4);
        params.leftMargin = (int) ConversionUtils.convertDpToPx(itemView.getContext(), 4);
        params.rightMargin = (int) ConversionUtils.convertDpToPx(itemView.getContext(), 4);
        itemView.setLayoutParams(params);
    }

    private String formatDistance(float distance) {
        if (Float.compare(distance, 0) == 0)
            return "less than .5'";
        else return new DecimalFormat("#.#''").format(distance);
    }

    private void setAdvData(ITurn turn, PlayerTurn playerTurn) {
        if (turn.getAdvStats().isCueingValid()) {
            heatGraph.setVisibility(View.VISIBLE);
            heatGraph.setData(new Point(turn.getAdvStats().getCueX(), turn.getAdvStats().getCueY()));
        } else {
            heatGraph.setVisibility(View.GONE);
        }

        if (turn.getAdvStats().getSpeed() > 0) {
            shotSpeed.setVisibility(View.VISIBLE);
            shotSpeed.setText(String.format(Locale.getDefault(), "Speed: %1$d/10", turn.getAdvStats().getSpeed()));
            shotSpeed.setPercentage(turn.getAdvStats().getSpeed() * 10);
        } else {
            shotSpeed.setVisibility(View.GONE);
        }

        if (turn.getAdvStats().getAngles().size() > 0) {
            angle.setVisibility(View.VISIBLE);
            String title = turn.getAdvStats().getAngles().size() == 1 ? "Angle:<br>%1$s" : "Angles:<br>%1$s";
            angle.setText(Html.fromHtml(String.format(title,
                    StringUtils.join(getAngleStringList(turn.getAdvStats().getAngles()), "<br>"))));
        } else {
            angle.setVisibility(View.GONE);
        }

        if (turn.getAdvStats().getHowTypes().size() > 0) {
            String title = "How the shot was missed:<br>%1$s";
            howMissDescription.setText(Html.fromHtml(String.format(Locale.getDefault(), title,
                    StringUtils.join(getHowTypeStringList(turn.getAdvStats().getHowTypes()), "<br>"))));
            howMissDescription.setVisibility(View.VISIBLE);
        } else {
            howMissDescription.setVisibility(View.GONE);
        }

        if (turn.getAdvStats().getCbToOb() >= 0 && turn.getAdvStats().getObToPocket() >= 0) {
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
            type = setStringStyle(type);
            shotType.setText(Html.fromHtml(String.format("Shot type:<br>%1$s", type)));
        } else {
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

    private List<String> getAngleStringList(List<AdvStats.Angle> angles) {
        List<String> strings = new ArrayList<>();
        for (AdvStats.Angle angle : angles) {
            strings.add(setStringStyle(itemView.getContext().getString(MatchDialogHelperUtils.convertAngleToStringRes(angle))));
        }

        return strings;
    }

    private List<String> getHowTypeStringList(List<AdvStats.HowType> howTypes) {
        List<String> strings = new ArrayList<>();
        for (AdvStats.HowType howType : howTypes) {
            strings.add(setStringStyle(itemView.getContext().getString(MatchDialogHelperUtils.convertHowToStringRes(howType))));
        }

        return strings;
    }

    private String setStringStyle(String string) {
        String result = "<b><font color='" + ConversionUtils.getColorString(getColor(R.color.colorPrimary)) + "'>";
        result += string;
        result += "</font></b>";

        return result;
    }
}
