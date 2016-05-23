package com.brookmanholmes.billiardmatchanalyzer.adapters;

import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.FooterViewHolder;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.billiards.player.controller.PlayerController;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/22/2016.
 */
public class PlayerInfoGraphicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static final int ITEM_GRAPH = 0;
    static final int ITEM_BREAK_RUN_PERCENT = 1;
    static final int ITEM_SAFETY_GRAPH = 2;
    static final int ITEM_CONTINUATION_PERCENT = 3;
    static final int ITEM_BREAK_GRAPH = 4;
    static final int ITEM_FOOTER = 5;

    List<AbstractPlayer> players = new ArrayList<>(), opponents = new ArrayList<>();
    String playerName, opponentName;

    public PlayerInfoGraphicAdapter(List<Pair<AbstractPlayer, AbstractPlayer>> pairs, String playerName, String opponentName) {
        splitPlayers(pairs);
        this.playerName = playerName;
        this.opponentName = opponentName;
    }

    private void splitPlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
        for (Pair<AbstractPlayer, AbstractPlayer> pair : pairs) {
            players.add(pair.getLeft());
            opponents.add(pair.getRight());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        return getViewHolderByViewType(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setFullSpan(true);
        switch (getItemViewType(position)) {
            case ITEM_GRAPH:
                holder.itemView.setLayoutParams(params);
                ((GraphViewHolder) holder).bind(players);
                break;
            case ITEM_BREAK_RUN_PERCENT:
                ((PercentViewHolder)holder).bind(players);
                break;
            case ITEM_CONTINUATION_PERCENT:
                ((PercentViewHolder)holder).bind(players);
                break;
            case ITEM_SAFETY_GRAPH:
                ((SafetyGraphViewHolder)holder).bind(players, opponents);
                break;
            case ITEM_BREAK_GRAPH:
                holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount())
            return ITEM_FOOTER;
        else
            return position;
    }

    @LayoutRes
    int getLayoutResource(int viewType) {
        switch (viewType) {
            case ITEM_GRAPH:
                return R.layout.card_graph;
            case ITEM_BREAK_RUN_PERCENT:
                return R.layout.card_break_run_percent;
            case ITEM_CONTINUATION_PERCENT:
                return R.layout.card_continuation_percent;
            case ITEM_BREAK_GRAPH:
                return R.layout.card_graph;
            case ITEM_SAFETY_GRAPH:
                return R.layout.card_pie_graph;
            case ITEM_FOOTER:
                return R.layout.footer;
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    RecyclerView.ViewHolder getViewHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_GRAPH:
                return new GraphViewHolder(view);
            case ITEM_BREAK_RUN_PERCENT:
                return new BreaksPercentViewHolder(view);
            case ITEM_CONTINUATION_PERCENT:
                return new ContinuationPercentViewHolder(view);
            case ITEM_BREAK_GRAPH:
                return new BreaksGraphViewHolder(view);
            case ITEM_SAFETY_GRAPH:
                return new SafetyGraphViewHolder(view);
            case ITEM_FOOTER:
                return new FooterViewHolder<>(view);
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    static class GraphViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.graph)
        LineChart chart;

        public GraphViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            chart.setDescription("");
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setDrawLabels(true);
            chart.getXAxis().setDrawGridLines(false);
            chart.getAxisRight().setAxisMinValue(0f);
            chart.getAxisRight().setAxisMaxValue(1.1f);
            chart.getAxisLeft().setAxisMinValue(0f);
            chart.getAxisLeft().setAxisMaxValue(1.1f);
            chart.getAxisLeft().setGranularity(.1f);
            chart.getAxisRight().setDrawLabels(false);


            chart.setDrawGridBackground(false);
        }

        public void bind(List<AbstractPlayer> players) {
            List<String> xVals = new ArrayList<>();
            for (AbstractPlayer player : players) {
                xVals.add(DateFormat.getDateInstance(DateFormat.SHORT).format(player.getMatchDate()));
            }
            LineData data = new LineData(xVals);
            LineDataSet tsp = getDataSet(getTspDataSet(players), "True Shooting %", getColor(R.color.chart));
            LineDataSet shootingP = getDataSet(getShootingDataSet(players), "Shooting %", getColor(R.color.chart1));
            LineDataSet breakingP = getDataSet(getBreakingDataSet(players), "Breaking %", getColor(R.color.chart2));
            LineDataSet safetyP = getDataSet(getSafetyDataSet(players), "Safeties %", getColor(R.color.chart3));

            data.addDataSet(tsp);
            data.addDataSet(shootingP);
            data.addDataSet(breakingP);
            data.addDataSet(safetyP);


            chart.setData(data);
            chart.invalidate();
            chart.animateX(750);
        }

        private List<Entry> getTspDataSet(List<AbstractPlayer> players) {
            List<Entry> list = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                list.add(new Entry(Float.valueOf(players.get(i).getTrueShootingPct()), i));
            }

            return list;
        }

        private List<Entry> getShootingDataSet(List<AbstractPlayer> players) {
            List<Entry> list = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                list.add(new Entry(Float.valueOf(players.get(i).getShootingPct()), i));
            }

            return list;
        }

        private List<Entry> getSafetyDataSet(List<AbstractPlayer> players) {
            List<Entry> list = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                list.add(new Entry(Float.valueOf(players.get(i).getSafetyPct()), i));
            }

            return list;
        }

        private List<Entry> getBreakingDataSet(List<AbstractPlayer> players) {
            List<Entry> list = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                list.add(new Entry(Float.valueOf(players.get(i).getBreakPct()), i));
            }

            return list;
        }

        private LineDataSet getDataSet(List<Entry> entries, String label, int color) {
            LineDataSet dataSet = new LineDataSet(entries, label);

            dataSet.setLineWidth(1.75f);
            dataSet.setCircleRadius(5f);
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(true);
            dataSet.setDrawCircleHole(true);
            dataSet.setCircleColor(color);
            dataSet.setColor(color);
            return dataSet;
        }

        private int getColor(@ColorRes int color) {
            return ContextCompat.getColor(itemView.getContext(), color);
        }
    }

    static abstract class PercentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.percent) TextView percent;
        public PercentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(List<AbstractPlayer> players) {
            float val = getValFromPlayer(players);
            percent.setText(convertFloatToPercent(val));
        }

        abstract float getValFromPlayer(List<AbstractPlayer> player);

        String convertFloatToPercent(float val) {
            return String.format(Locale.getDefault(), "%.0f%%", val * 100);
        }
    }

    static class BreaksPercentViewHolder extends PercentViewHolder {
        public BreaksPercentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        float getValFromPlayer(List<AbstractPlayer> players) {
            float runOuts = 0, breakAttempts = 0;

            for (AbstractPlayer player : players) {
                runOuts += player.getRunOuts();
                breakAttempts += player.getBreakAttempts();
            }
            return runOuts / breakAttempts;
        }
    }

    static class ContinuationPercentViewHolder extends PercentViewHolder {
        public ContinuationPercentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        float getValFromPlayer(List<AbstractPlayer> players) {
            float continuations = 0, breakAttempts = 0;

            for (AbstractPlayer player : players) {
                continuations += player.getBreakContinuations();
                breakAttempts += player.getBreakAttempts();
            }
            return continuations /breakAttempts;
        }
    }

    static class BreaksGraphViewHolder extends RecyclerView.ViewHolder {
        public BreaksGraphViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class SafetyGraphViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pieChart) PieChart chart;
        @Bind(R.id.title) TextView title;
        PieData data = new PieData(new String[] {"Escaped", "Returned", "Left open"});
        PieDataSet dataSet;
        public SafetyGraphViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chart.setDescriptionPosition(0,0);
            chart.getLegend().setEnabled(false);
            title.setText("Shots after being safetied");

        }

        public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
            CompPlayer player = getPlayerFromList(players);
            CompPlayer opponent = getPlayerFromList(opponents);

            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(3f / 13f, 1));
            entries.add(new Entry(5f / 13f, 2));
            entries.add(new Entry(5f / 13f, 3));

            dataSet = new PieDataSet(entries, "Result of being safetied");
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

            dataSet.setValueTextSize(12);
            dataSet.setSliceSpace(3);
            dataSet.setValueFormatter(new MyValueFormatter());
            data.setDataSet(dataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    private static class MyValueFormatter implements ValueFormatter {
        public MyValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return String.format(Locale.getDefault(), "%.0f%%", value * 100);        }
    }

    private static CompPlayer getPlayerFromList(List<AbstractPlayer> players) {
        if (players.size() > 0) {
            CompPlayer player = new CompPlayer(players.get(0).getName());
            for (AbstractPlayer abstractPlayer : players) {
                player.addPlayerStats(abstractPlayer);
            }

            return player;
        } else return new CompPlayer("");
    }
}
