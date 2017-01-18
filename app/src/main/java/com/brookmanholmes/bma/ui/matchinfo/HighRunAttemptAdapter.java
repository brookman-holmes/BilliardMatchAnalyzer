package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IStraightPool;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.databinding.CardHighRunAttemptInfoBinding;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by Brookman Holmes on 12/20/2016.
 */

class HighRunAttemptAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int RUNS_VIEW = 0;
    private static final int RUNS_GRAPH_VIEW = 1;
    private static final int TURN_VIEW = 2;
    private static final int TURN_VIEW_HEADER = 3;
    private static final int FOOTER = 4;
    private static final String TAG = "MinTurnAdapter";

    private Match match;
    private HighRunAttemptActivity activity;

    HighRunAttemptAdapter(HighRunAttemptActivity activity, Match match) {
        this.match = match;
        this.activity = activity;
        setHasStableIds(true);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TURN_VIEW)
            return new MinimalTurnViewHolder(inflater.inflate(R.layout.row_turn_min, parent, false), match);
        else if (viewType == RUNS_VIEW)
            return new RunViewHolder(inflater.inflate(R.layout.card_high_run_attempt_info, parent, false), match);
        else if (viewType == TURN_VIEW_HEADER)
            return new TurnHeaderHolder(inflater.inflate(R.layout.layout_turn_header, parent, false));
        else if (viewType == RUNS_GRAPH_VIEW)
            return new RunGraphViewHolder(inflater.inflate(R.layout.card_high_run_attempt_graph, parent, false), match, activity);
        else if (viewType == FOOTER)
            return new TurnHeaderHolder(inflater.inflate(R.layout.footer, parent, false));
        else throw new IllegalArgumentException("Invalid viewType: " + viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof MinimalTurnViewHolder) {
            int realPosition = position - 3;

            PlayerTurn turn = match.getGameStatus(realPosition).turn;
            AbstractPlayer player = turn == PlayerTurn.PLAYER ?
                    match.getPlayer(0, realPosition) : match.getOpponent(0, realPosition);

            ((MinimalTurnViewHolder) holder).bind(match.getTurns().get(realPosition), turn, player);
            if (realPosition == 0)
                ((MinimalTurnViewHolder) holder).showDivider(false);
        } else if (holder instanceof RunViewHolder) {
            ((RunViewHolder) holder).bind(match);
        } else if (holder instanceof RunGraphViewHolder) {
            ((RunGraphViewHolder) holder).bind(match, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return match.getTurnCount() == 0 ? 4 : match.getTurnCount() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount())
            return FOOTER;
        switch (position) {
            case 0:
                return RUNS_VIEW;
            case 1:
                return RUNS_GRAPH_VIEW;
            case 2:
                return TURN_VIEW_HEADER;
            default:
                return TURN_VIEW;
        }
    }

    public void update(Match match) {
        this.match = match;
        notifyDataSetChanged(); // lazy!
    }

    private static class RunViewHolder extends BaseViewHolder {
        private HighRunAttemptBinder runAttemptBinder;

        RunViewHolder(View itemView, Match match) {
            super(itemView);
            CardHighRunAttemptInfoBinding binder = DataBindingUtil.bind(itemView);
            List<AbstractPlayer> players = new DatabaseAdapter(itemView.getContext()).getPlayer(match.getPlayer().getName(), match.getGameStatus().gameType, match.getMatchId());
            runAttemptBinder = new HighRunAttemptBinder((StraightPoolPlayer) match.getPlayer(), getRunLengths(players));
            binder.setRunAttempt(runAttemptBinder);
        }

        private void bind(Match match) {
            runAttemptBinder.update((StraightPoolPlayer) match.getPlayer());
        }

        private List<Integer> getRunLengths(List<AbstractPlayer> players) {
            List<Integer> result = new ArrayList<>();

            for (AbstractPlayer player : players) {
                if (player instanceof StraightPoolPlayer)
                    result.addAll(((StraightPoolPlayer) player).getRunLengths());
            }

            return result;
        }
    }

    static class RunGraphViewHolder extends BaseViewHolder {
        @Bind(R.id.chart)
        LineChartView chart;
        @Bind(R.id.button)
        Button buttonShowAdvShot;

        Match match;
        int highRun = 0;
        float averageRun = 0f;
        HighRunAttemptActivity activity;

        RunGraphViewHolder(View itemView, Match match, HighRunAttemptActivity activity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.match = match;
            this.activity = activity;
            EnumSet<Match.StatsDetail> details = EnumSet.copyOf(match.getDetails());
            details.retainAll(Match.StatsDetail.getPlayerStatsTracked());

            buttonShowAdvShot.setVisibility(details.size() > 0 ? View.VISIBLE : View.GONE);
        }

        private void bind(Match match, int highRun, float averageRun) {
            this.match = match;
            chart.setLineChartData(getData());
            this.highRun = highRun;
            this.averageRun = averageRun;
        }

        private LineChartData getData() {
            List<PointValue> values = new ArrayList<>();
            List<Integer> runLengths = ((IStraightPool) match.getPlayer()).getRunLengths();
            int count = 0;
            for (Integer integer : runLengths) {
                values.add(new PointValue(count++, integer));
            }

            Line line = new Line(values)
                    .setPointRadius(4)
                    .setColor(getColor(R.color.chart2))
                    .setHasLabels(true)
                    .setFormatter(new SimpleLineChartValueFormatter(0))
                    .setStrokeWidth(2)
                    .setCubic(true)
                    .setHasLabelsOnlyForSelected(true)
                    .setFilled(true)
                    .setAreaTransparency(96);
            float upperLimit = 15.05f;
            if (highRun > upperLimit)
                upperLimit = (float) highRun + 1.05f;

            Line dummyLine = new Line(Arrays.asList(new PointValue(0, 0), new PointValue(0, upperLimit)))
                    .setColor(getColor(android.R.color.transparent))
                    .setHasPoints(false);
            Line averageLine = new Line(Arrays.asList(new PointValue(0, averageRun),
                    new PointValue(runLengths.size(), averageRun)))
                    .setColor(getColor(R.color.chart1))
                    .setHasPoints(false)
                    .setHasLabels(false)
                    .setStrokeWidth(1);

            LineChartData data = new LineChartData(Arrays.asList(averageLine, line, dummyLine));
            Axis axis = new Axis();
            axis.setHasSeparationLine(true)
                    .setFormatter(new SimpleAxisValueFormatter())
                    .setTextColor(getColor(R.color.secondary_text))
                    .setHasLines(true);
            data.setAxisYLeft(axis);
            data.setValueLabelBackgroundColor(getColor(R.color.chart2));
            data.setValueLabelsTextColor(getColor(R.color.white));
            data.setValueLabelTextSize(16);
            return data;
        }

        @OnClick(R.id.button)
        public void showAdvShotData() {
            activity.showAdvShotData();
        }
    }

    private static class TurnHeaderHolder extends BaseViewHolder {
        TurnHeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
