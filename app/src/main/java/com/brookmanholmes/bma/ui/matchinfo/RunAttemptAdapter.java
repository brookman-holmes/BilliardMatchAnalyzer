package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.databinding.CardGhostInfoBinding;
import com.brookmanholmes.bma.databinding.CardHighRunAttemptInfoBinding;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

class RunAttemptAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int RUNS_VIEW = 0;
    private static final int RUNS_GRAPH_VIEW = 1;
    private static final int TURN_VIEW = 2;
    private static final int TURN_VIEW_HEADER = 3;
    private static final int FOOTER = 4;
    private static final String TAG = "MinTurnAdapter";

    private Match match;
    private RunAttemptActivity activity;
    private List<Player> previousPlayers = new ArrayList<>();

    RunAttemptAdapter(RunAttemptActivity activity, Match match, List<Player> previousPlayers) {
        this.match = match;
        this.activity = activity;
        this.previousPlayers.addAll(previousPlayers);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TURN_VIEW)
            return new MinimalTurnViewHolder(inflater.inflate(R.layout.row_turn_min, parent, false), match);
        else if (viewType == RUNS_VIEW)
            return new RunViewHolder(inflater.inflate(getRunAttemptCardLayout(), parent, false), match);
        else if (viewType == TURN_VIEW_HEADER)
            return new TurnHeaderHolder(inflater.inflate(R.layout.layout_turn_header, parent, false));
        else if (viewType == RUNS_GRAPH_VIEW)
            return new RunGraphViewHolder(inflater.inflate(R.layout.card_high_run_attempt_graph, parent, false), match, activity);
        else if (viewType == FOOTER)
            return new TurnHeaderHolder(inflater.inflate(R.layout.footer, parent, false));
        else throw new IllegalArgumentException("Invalid viewType: " + viewType);
    }

    @LayoutRes
    private int getRunAttemptCardLayout() {
        if (match.getGameStatus().gameType.isStraightPool())
            return R.layout.card_high_run_attempt_info;
        else return R.layout.card_ghost_info;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof MinimalTurnViewHolder) {
            int realPosition = position - 3;

            Player player = match.getPlayer(0, realPosition);

            ((MinimalTurnViewHolder) holder).bind(match.getTurns().get(realPosition), PlayerTurn.PLAYER, player);
            ((MinimalTurnViewHolder) holder).showDivider(realPosition != 0, match.getGameStatus(realPosition));
        } else if (holder instanceof RunViewHolder) {
            ((RunViewHolder) holder).bind(match, previousPlayers);
        } else if (holder instanceof RunGraphViewHolder) {
            ((RunGraphViewHolder) holder).bind(match, previousPlayers);
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
        private RunAttemptBinder runAttemptBinder;

        RunViewHolder(View itemView, Match match) {
            super(itemView);
            if (match.getGameStatus().gameType.isStraightPool()) {
                CardHighRunAttemptInfoBinding binder = DataBindingUtil.bind(itemView);
                runAttemptBinder = new HighRunAttemptBinder(match.getPlayer());
                binder.setRunAttempt(runAttemptBinder);
            } else {
                CardGhostInfoBinding binder = DataBindingUtil.bind(itemView);
                runAttemptBinder = new GhostAttemptBinder(match.getPlayer());
                binder.setGhostAttempt(runAttemptBinder);
            }
        }

        private void bind(Match match, Collection<Player> previousPlayers) {
            runAttemptBinder.update(match.getPlayer(), previousPlayers);
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
        RunAttemptActivity activity;

        RunGraphViewHolder(View itemView, Match match, RunAttemptActivity activity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.match = match;
            this.activity = activity;

            EnumSet<Match.StatsDetail> details = EnumSet.copyOf(match.getDetails());
            details.retainAll(Match.StatsDetail.getPlayerStatsTracked());
            buttonShowAdvShot.setVisibility(details.size() > 0 ? View.VISIBLE : View.GONE);
        }

        private void bind(Match match, Collection<Player> players) {
            this.match = match;
            if (match.getGameStatus().gameType.isStraightPool())
                setStraightPoolStats(players);
            else
                setRegularStats(players);
            chart.setLineChartData(getData());
        }

        private void setRegularStats(Collection<Player> players) {
            int attempts = match.getPlayer().getShootingTurns();
            int shootingBallsMade = match.getPlayer().getShootingBallsMade();
            int breakBallsMade = match.getPlayer().getBreakBallsMade();

            for (Player player : players) {
                shootingBallsMade += player.getShootingBallsMade();
                breakBallsMade += player.getBreakBallsMade();
                attempts += player.getShootingTurns();
            }

            averageRun = divide(shootingBallsMade, attempts) + divide(breakBallsMade, attempts);
        }

        private float divide(int top, int bottom) {
            if (bottom == 0)
                return 0f;
            else
                return (float) top / (float) bottom;
        }

        private void setStraightPoolStats(Collection<Player> players) {
            List<Integer> runLengths = new ArrayList<>();
            for (Player player : players) {
                runLengths.add(player.getShootingBallsMade());
            }
            double[] combinedRuns = ArrayUtils.addAll(convertListToDoubleArray(runLengths),
                    convertListToDoubleArray(match.getPlayer().getRunLengths()));

            highRun = (int) StatUtils.max(combinedRuns);
            averageRun = (float) StatUtils.mean(combinedRuns);
        }

        private double[] convertListToDoubleArray(Collection<Integer> runLengths) {
            double[] result = new double[runLengths.size()];
            int count = 0;
            for (Integer i : runLengths) {
                result[count++] = i;
            }
            return result;
        }

        private LineChartData getData() {
            List<PointValue> values = new ArrayList<>();
            int count = 0;
            for (ITurn turn : match.getPlayer().getTurns()) {
                values.add(new PointValue(count++, turn.getShootingBallsMade() + turn.getBreakBallsMade()));
            }

            Line line = new Line(values)
                    .setPointRadius(itemView.getContext().getResources().getInteger(R.integer.graph_point_size) + 1)
                    .setColor(getColor(R.color.chart2))
                    .setHasLabels(true)
                    .setFormatter(new SimpleLineChartValueFormatter(0))
                    .setStrokeWidth(itemView.getContext().getResources().getInteger(R.integer.graph_line_size))
                    .setHasLabelsOnlyForSelected(true)
                    .setFilled(true)
                    .setAreaTransparency(96);

            Line dummyLine = new Line(Arrays.asList(new PointValue(0, 0), new PointValue(0, getUpperLimit(highRun))))
                    .setColor(getColor(android.R.color.transparent))
                    .setHasPoints(false);

            Line averageLine = new Line(Arrays.asList(new PointValue(0, averageRun),
                    new PointValue(match.getPlayer().getRunLengths().size(), averageRun)))
                    .setColor(getColor(R.color.chart1))
                    .setHasPoints(false)
                    .setHasLabels(false)
                    .setStrokeWidth(1);
            List<Line> lines = new ArrayList<>(Arrays.asList(averageLine, line, dummyLine));
            if (!match.getGameStatus().gameType.isStraightPool()) {
                List<PointValue> winValues = new ArrayList<>();
                int wins = 0;
                for (ITurn turn : match.getPlayer().getTurns()) {
                    if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                        winValues.add(new PointValue(wins, turn.getShootingBallsMade() + turn.getBreakBallsMade()));
                    }
                    wins++;
                }

                List<PointValue> lossValues = new ArrayList<>();
                int consecutiveMisses = 0;
                for (int i = 0; i < match.getPlayer().getTurns().size(); i++) {
                    TurnEnd turnEnd = match.getPlayer().getTurns().get(i).getTurnEnd();
                    if (turnEnd == TurnEnd.MISS)
                        consecutiveMisses++;
                    else if (turnEnd == TurnEnd.GAME_WON)
                        consecutiveMisses = 0;

                    if (consecutiveMisses >= match.getInitialGameStatus().maxAttemptsPerGame) {
                        consecutiveMisses = 0;
                        ITurn turn = match.getPlayer().getTurns().get(i);
                        lossValues.add(new PointValue(i, turn.getShootingBallsMade() + turn.getBreakBallsMade()));
                    }
                }


                lines.add(new Line(winValues)
                        .setColor(getColor(R.color.chart3))
                        .setHasLines(false)
                        .setHasPoints(true)
                        .setPointRadius(itemView.getContext().getResources().getInteger(R.integer.graph_point_size) - 1)
                        .setHasLabelsOnlyForSelected(false)
                        .setHasLabels(false));
                lines.add(new Line(lossValues)
                        .setColor(getColor(R.color.chart1))
                        .setHasLines(false)
                        .setHasPoints(true)
                        .setPointRadius(itemView.getContext().getResources().getInteger(R.integer.graph_point_size) - 1)
                        .setHasLabelsOnlyForSelected(false)
                        .setHasLabels(false));
            }

            LineChartData data = new LineChartData(lines);

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

        private float getUpperLimit(int highRun) {
            if (match.getGameStatus().gameType.isStraightPool())
                return highRun + 10.5f;
            else
                return match.getGameStatus().gameType.getMaxBalls() + 2;
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
