package com.brookmanholmes.billiardmatchanalyzer.ui.profile;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoGraphicFragment extends Fragment {
    private static final String ARG_PLAYER = "arg player";
    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private PlayerInfoGraphicAdapter adapter;
    private GridLayoutManager layoutManager;
    private DatabaseAdapter database;

    public PlayerInfoGraphicFragment() {
    }

    public static PlayerInfoGraphicFragment create(String player) {
        PlayerInfoGraphicFragment fragment = new PlayerInfoGraphicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER, player);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseAdapter(getContext());
        String player = getArguments().getString(ARG_PLAYER);

        adapter = new PlayerInfoGraphicAdapter(database.getPlayer(player), player, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case PlayerInfoGraphicAdapter.ITEM_GRAPH:
                        return 3;
                    case PlayerInfoGraphicAdapter.ITEM_SAFETY_GRAPH:
                        return 2;
                    case PlayerInfoGraphicAdapter.ITEM_BREAK_GRAPH:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Created by helios on 5/22/2016.
     */
    static class PlayerInfoGraphicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        static final int ITEM_GRAPH = 0;
        static final int ITEM_SAFETY_INFO = 1;
        static final int ITEM_SAFETY_GRAPH = 2;
        static final int ITEM_BREAK_GRAPH = 3;
        static final int ITEM_BREAK_INFO = 4;

        List<AbstractPlayer> players = new ArrayList<>(), opponents = new ArrayList<>();
        String playerName, opponentName;

        public PlayerInfoGraphicAdapter(List<Pair<AbstractPlayer, AbstractPlayer>> pairs, String playerName, String opponentName) {
            splitPlayers(pairs);
            this.playerName = playerName;
            this.opponentName = opponentName;
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
            switch (getItemViewType(position)) {
                case ITEM_GRAPH:
                    ((GraphViewHolder) holder).bind(players);
                    break;
                case ITEM_SAFETY_INFO:
                    ((TwoItemHolder) holder).bind(players, opponents);
                    break;
                case ITEM_SAFETY_GRAPH:
                    ((SafetyGraphViewHolder) holder).bind(players, opponents);
                    break;
                case ITEM_BREAK_GRAPH:
                    ((BreaksGraphViewHolder) holder).bind(getPlayerFromList(players));
                    break;
                case ITEM_BREAK_INFO:
                    ((TwoItemHolder) holder).bind(players, opponents);
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @LayoutRes int getLayoutResource(int viewType) {
            switch (viewType) {
                case ITEM_GRAPH:
                    return R.layout.card_graph;
                case ITEM_SAFETY_INFO:
                    return R.layout.card_two_item_holder;
                case ITEM_BREAK_GRAPH:
                    return R.layout.card_deco_view;
                case ITEM_BREAK_INFO:
                    return R.layout.card_two_item_holder;
                case ITEM_SAFETY_GRAPH:
                    return R.layout.card_pie_graph;
                default:
                    throw new IllegalArgumentException("No such view type: " + viewType);
            }
        }

        RecyclerView.ViewHolder getViewHolderByViewType(View view, int viewType) {
            switch (viewType) {
                case ITEM_GRAPH:
                    return new GraphViewHolder(view);
                case ITEM_SAFETY_INFO:
                    return new SafetyTwoItemHolder(view);
                case ITEM_BREAK_GRAPH:
                    return new BreaksGraphViewHolder(view);
                case ITEM_SAFETY_GRAPH:
                    return new SafetyGraphViewHolder(view);
                case ITEM_BREAK_INFO:
                    return new BreakTwoItemHolder(view);
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

        abstract static class TwoItemHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.item1) TextView item1;
            @Bind(R.id.item1Desc) TextView item1Desc;
            @Bind(R.id.item2) TextView item2;
            @Bind(R.id.item2Desc) TextView item2Desc;

            public TwoItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                setItemDesc();
            }

            public abstract void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents);

            abstract void setItemDesc();

            String convertFloatToPercent(float val) {
                return String.format(Locale.getDefault(), "%.0f%%", val * 100);
            }
        }

        static class SafetyTwoItemHolder extends TwoItemHolder {
            public SafetyTwoItemHolder(View itemView) {
                super(itemView);
            }

            @Override void setItemDesc() {
                item1Desc.setText("Forced opponent\nto foul");
                item2Desc.setText("Safety success rate");
            }

            @Override
            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                float safetyPct = (float) getPlayerFromList(players).getSafetySuccesses() / (float) getPlayerFromList(players).getSafetyAttempts();
                int ballInHands = getForcedErrors(opponents);

                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(safetyPct)));
                item1.setText(String.format(Locale.getDefault(), "%1$d", ballInHands));
            }

            int getForcedErrors(List<AbstractPlayer> opponents) {
                int ballInHands = 0;

                for (AbstractPlayer opp : opponents) {
                    ballInHands += opp.getSafetyForcedErrors();
                }

                return ballInHands;
            }
        }

        static class BreakTwoItemHolder extends TwoItemHolder {
            public BreakTwoItemHolder(View itemView) {
                super(itemView);
            }

            @Override void setItemDesc() {
                item1Desc.setText("Break and runs");
                item2Desc.setText("Break and run %");
            }

            @Override
            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                float breakRunPct = (float) getPlayerFromList(players).getRunOuts() / (float) getPlayerFromList(players).getBreakAttempts();
                item1.setText(String.format(Locale.getDefault(), "%1$d", getPlayerFromList(players).getRunOuts()));
                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(breakRunPct)));
            }
        }

        static class BreaksGraphViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.decoView) DecoView decoView;
            @Bind(R.id.successful_breaks) TextView successfulBreak;
            @Bind(R.id.continuation_break) TextView continuationBreak;
            @Bind(R.id.foul_break) TextView foulBreak;
            @Bind(R.id.win_break) TextView winBreak;

            int color1, color2, color3, color4;

            public BreaksGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                int grey = Color.parseColor("#F5F5F5");
                color1 = ContextCompat.getColor(itemView.getContext(), R.color.chart3);
                color2 = ContextCompat.getColor(itemView.getContext(), R.color.chart2);
                color3 = ContextCompat.getColor(itemView.getContext(), R.color.chart1);
                color4 = ContextCompat.getColor(itemView.getContext(), R.color.chart);

                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 100, 100)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 100, 100)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(32, 32))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 100, 100)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(64, 64))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 100, 100)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(96, 96))
                        .build());
                decoView.configureAngles(300, 0);
            }

            void bind(CompPlayer player) {
                float attempts = player.getBreakAttempts();

                float continuation = (float) player.getBreakContinuations() / attempts;
                float successes = (float) player.getBreakSuccesses() / attempts;
                float wins = (float) player.getWinsOnBreak() / attempts;
                float fouls = (float) player.getBreakFouls() / attempts;

                decoView.addSeries(new SeriesItem.Builder(color1)
                        .setRange(0, 100, successes * 100)
                        .setInset(new PointF(0, 0))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color2)
                        .setRange(0, 100, continuation * 100)
                        .setInset(new PointF(32, 32))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color4)
                        .setRange(0, 100, wins * 100)
                        .setInset(new PointF(64, 64))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color3)
                        .setRange(0, 100, fouls * 100)
                        .setInset(new PointF(96, 96))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());


                successfulBreak.setText(getString(R.string.successful_breaks, player.getBreakSuccesses(), player.getBreakAttempts()));
                continuationBreak.setText(getString(R.string.continuation_after_the_break, player.getBreakContinuations(), player.getBreakAttempts()));
                winBreak.setText(getString(R.string._9_10_on_the_break, player.getWinsOnBreak(), player.getBreakAttempts()));
                foulBreak.setText(getString(R.string.fouls_on_the_break, player.getBreakFouls(), player.getBreakAttempts()));
            }

            private String getString(@StringRes int res, Object... formatArgs) {
                return itemView.getContext().getString(res, formatArgs);
            }
        }

        static class SafetyGraphViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.pieChart) PieChart chart;
            @Bind(R.id.title) TextView title;
            PieData data = new PieData(new String[]{"Made a ball", "Played safe back", "Missed"});
            PieDataSet dataSet;

            public SafetyGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                chart.setDescriptionPosition(0, 0);
                chart.getLegend().setEnabled(false);
                title.setText("Shots after being safetied");

            }

            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                CompPlayer player = getPlayerFromList(players);
                CompPlayer opponent = getPlayerFromList(opponents);
                float total = (float) opponent.getSafetySuccesses();

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(player.getSafetyEscapes() / total, 1));
                entries.add(new Entry(player.getSafetyReturns() / total, 2));
                entries.add(new Entry((total - player.getSafetyEscapes() - player.getSafetyReturns()) / total, 3));

                dataSet = new PieDataSet(entries, "Result after being safetied");
                dataSet.setColors(getColor(R.color.chart, R.color.chart1, R.color.chart2, R.color.chart3));

                dataSet.setValueTextSize(12);
                dataSet.setSliceSpace(3);
                dataSet.setValueFormatter(new MyValueFormatter());
                data.setDataSet(dataSet);
                chart.setData(data);
                chart.invalidate();
            }

            private int[] getColor(@ColorRes int... color) {
                int[] array = new int[color.length];

                for (int i = 0; i < color.length; i++) {
                    array[i] = ContextCompat.getColor(itemView.getContext(), color[i]);
                }
                return array;
            }
        }

        private static class MyValueFormatter implements ValueFormatter {
            public MyValueFormatter() {
            }

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format(Locale.getDefault(), "%.0f%%", value * 100);
            }
        }
    }
}