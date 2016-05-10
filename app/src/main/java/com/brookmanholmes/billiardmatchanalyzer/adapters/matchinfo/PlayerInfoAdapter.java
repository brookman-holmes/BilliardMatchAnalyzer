package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.MatchOverviewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.SafetiesHolder;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.vh.ShootingPctHolder;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 5/7/2016.
 */
public class PlayerInfoAdapter extends RecyclerView.Adapter<BaseViewHolder<CompPlayer>> {
    public static final int ITEM_MATCH_OVERVIEW = 1;
    public static final int ITEM_SHOOTING_PCT = 2;
    public static final int ITEM_SAFETIES = 3;
    public static final int ITEM_BREAKS = 4;
    public static final int ITEM_RUN_OUTS = 5;
    public static final int ITEM_GRAPH = 0;

    List<AbstractPlayer> players = new ArrayList<>(), opponents = new ArrayList<>();
    String playerName, opponentName;
    Match.StatsDetail detail;

    public PlayerInfoAdapter(List<Pair<AbstractPlayer, AbstractPlayer>> pairs, String playerName, String opponentName) {
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

    @Override public BaseViewHolder<CompPlayer> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
        view.setTag(viewType);
        return getMatchInfoHolderByViewType(view, viewType);
    }

    @Override public void onBindViewHolder(BaseViewHolder<CompPlayer> holder, int position) {
        holder.bind(getPlayer(), getOpponent());

        if (holder instanceof GraphViewHolder) {
            ((GraphViewHolder) holder).bind(players, opponents);
        }
    }

    public void updatePlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
        splitPlayers(pairs);
        notifyDataSetChanged();
    }

    private CompPlayer getPlayer() {
        return getPlayerFromList(players, new CompPlayer(playerName));
    }

    private CompPlayer getOpponent() {
        return getPlayerFromList(opponents, new CompPlayer(opponentName));
    }

    private CompPlayer getPlayerFromList(List<? extends AbstractPlayer> players, CompPlayer newPlayer) {
        for (AbstractPlayer player : players) {
            newPlayer.addPlayerStats(player);
        }

        return newPlayer;
    }

    @LayoutRes int getLayoutResource(int viewType) {
        switch (viewType) {
            case ITEM_RUN_OUTS:
                return R.layout.card_run_outs;
            case ITEM_BREAKS:
                return R.layout.card_breaks;
            case ITEM_MATCH_OVERVIEW:
                return R.layout.card_match_overview;
            case ITEM_SAFETIES:
                return R.layout.card_safeties;
            case ITEM_SHOOTING_PCT:
                return R.layout.card_shooting_pct;
            case ITEM_GRAPH:
                return R.layout.card_graph;
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    BaseViewHolder<CompPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
        switch (viewType) {
            case ITEM_MATCH_OVERVIEW:
                return new MatchOverviewHolder<>(view, detail);
            case ITEM_SHOOTING_PCT:
                return new ShootingPctHolder<>(view, detail);
            case ITEM_BREAKS:
                return new BreaksWithWinsHolder<>(view, 10, detail);
            case ITEM_RUN_OUTS:
                return new RunOutsWithEarlyWinsHolder<>(view, detail);
            case ITEM_SAFETIES:
                return new SafetiesHolder<>(view, detail);
            case ITEM_GRAPH:
                return new GraphViewHolder(view);
            default:
                throw new IllegalArgumentException("No such view type");
        }
    }

    @Override public int getItemCount() {
        return 6;
    }

    @Override public int getItemViewType(int position) {
        return position;
    }

    static class GraphViewHolder extends BaseViewHolder<CompPlayer> {
        @Bind(R.id.graph) LineChart chart;

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

        @Override public void bind(CompPlayer player, CompPlayer opponent) {

        }

        public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
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
}
