package com.brookmanholmes.bma.ui.profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseRecyclerFragment;
import com.brookmanholmes.bma.ui.stats.Filterable;
import com.brookmanholmes.bma.ui.stats.StatFilter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoGraphicFragment extends BaseRecyclerFragment implements Filterable {
    private static final String ARG_PLAYER = "arg player";
    DecimalFormat df = new DecimalFormat("#.###");
    private DatabaseAdapter database;
    private String player;
    private UpdatePlayersAsync task;

    public PlayerInfoGraphicFragment() {}

    public static PlayerInfoGraphicFragment create(String player) {
        PlayerInfoGraphicFragment fragment = new PlayerInfoGraphicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER, player);

        fragment.setArguments(args);
        return fragment;
    }

    private static String convertFloatToPercent(float val) {
        if (Float.isNaN(val))
            return String.format(Locale.getDefault(), "%.0f%%", 0f);
        else
            return String.format(Locale.getDefault(), "%.0f%%", val * 100);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df.setRoundingMode(RoundingMode.FLOOR);
        database = new DatabaseAdapter(getContext());
        player = getArguments().getString(ARG_PLAYER);

        adapter = new PlayerInfoGraphicAdapter(new ArrayList<Pair<AbstractPlayer, AbstractPlayer>>(), player, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onDestroy() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        if (task != null)
            task.cancel(true);
        super.onDestroy();
    }

    @Override public void setFilter(StatFilter filter) {
        if (task == null) {
            task = new UpdatePlayersAsync();
            task.execute(filter);
        }

        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            task.cancel(true);
            task = new UpdatePlayersAsync();
            task.execute(filter);
        }
    }

    @Override protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
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
        return layoutManager;
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

        final List<AbstractPlayer> players = new ArrayList<>();
        final List<AbstractPlayer> opponents = new ArrayList<>();
        final String playerName;
        final String opponentName;

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

        private static float roundNumber(int top, int bottom) {
            BigDecimal decimal = new BigDecimal((float) top / (float) bottom).round(new MathContext(4, RoundingMode.HALF_EVEN));
            return decimal.floatValue();
        }

        private void splitPlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            for (Pair<AbstractPlayer, AbstractPlayer> pair : pairs) {
                players.add(pair.getLeft());
                opponents.add(pair.getRight());
            }
        }

        private void updatePlayers(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            players.clear();
            opponents.clear();
            splitPlayers(pairs);
            notifyItemRangeChanged(0, getItemCount());
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
                    return R.layout.card_deco_view;
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
                chart.getLegend().setEnabled(false);

                chart.setDrawGridBackground(false);
            }

            public void bind(List<AbstractPlayer> players) {
                Context context = itemView.getContext();
                List<String> xVals = new ArrayList<>();
                for (AbstractPlayer player : players) {
                    xVals.add(DateFormat.getDateInstance(DateFormat.SHORT).format(player.getMatchDate()));
                }
                LineData data = new LineData(xVals);
                LineDataSet tsp = getDataSet(getTspDataSet(players), context.getString(R.string.title_tsp), getColor(R.color.chart));
                LineDataSet shootingP = getDataSet(getShootingDataSet(players), context.getString(R.string.title_shooting_pct), getColor(R.color.chart1));
                LineDataSet breakingP = getDataSet(getBreakingDataSet(players), context.getString(R.string.title_break_pct), getColor(R.color.chart2));
                LineDataSet safetyP = getDataSet(getSafetyDataSet(players), context.getString(R.string.title_safety_pct), getColor(R.color.chart3));

                data.addDataSet(tsp);
                data.addDataSet(shootingP);
                data.addDataSet(breakingP);
                data.addDataSet(safetyP);


                chart.setData(data);
                chart.animateX(1750);
            }

            private List<Entry> getTspDataSet(List<AbstractPlayer> players) {
                List<Entry> list = new ArrayList<>();

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getShootingAttempts() +
                            players.get(i).getBreakAttempts() +
                            players.get(i).getSafetyAttempts() > 0)
                        list.add(new Entry(Float.valueOf(players.get(i).getTrueShootingPct()), i));
                }

                return list;
            }

            private List<Entry> getShootingDataSet(List<AbstractPlayer> players) {
                List<Entry> list = new ArrayList<>();

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getShootingAttempts() > 0)
                        list.add(new Entry(Float.valueOf(players.get(i).getShootingPct()), i));
                }

                return list;
            }

            private List<Entry> getSafetyDataSet(List<AbstractPlayer> players) {
                List<Entry> list = new ArrayList<>();

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getSafetyAttempts() > 0)
                        list.add(new Entry(Float.valueOf(players.get(i).getSafetyPct()), i));
                }

                return list;
            }

            private List<Entry> getBreakingDataSet(List<AbstractPlayer> players) {
                List<Entry> list = new ArrayList<>();

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getBreakAttempts() > 0)
                        list.add(new Entry(Float.valueOf(players.get(i).getBreakPct()), i));
                }

                return list;
            }

            private LineDataSet getDataSet(List<Entry> entries, String label, int color) {
                LineDataSet dataSet = new LineDataSet(entries, label);
                dataSet.setLineWidth(1.75f);
                dataSet.setCircleRadius(4f);
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
        }

        static class SafetyTwoItemHolder extends TwoItemHolder {
            public SafetyTwoItemHolder(View itemView) {
                super(itemView);
            }

            @Override void setItemDesc() {
                item1Desc.setText(R.string.title_forced_opponent_foul);
                item2Desc.setText(R.string.title_effect_safety_rate);
            }

            @Override
            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                AbstractPlayer player = getPlayerFromList(players);
                AbstractPlayer opponent = getPlayerFromList(opponents);

                float safetyPct = (float) (player.getSafetySuccesses() - opponent.getSafetyEscapes() - opponent.getSafetyReturns()) / (float) player.getSafetyAttempts();
                int ballInHands = getForcedErrors(opponents);

                item1.setText(String.format(Locale.getDefault(), "%1$d", ballInHands));
                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(safetyPct)));
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
                item1Desc.setText(R.string.title_break_runs);
                item2Desc.setText(R.string.title_break_run_pct);
            }

            @Override
            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                float breakRunPct = (float) getPlayerFromList(players).getBreakAndRuns() / (float) getPlayerFromList(players).getBreakAttempts();
                item1.setText(String.format(Locale.getDefault(), "%1$d", getPlayerFromList(players).getBreakAndRuns()));
                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(breakRunPct)));
            }
        }

        static class BreaksGraphViewHolder extends RecyclerView.ViewHolder {
            final int color1;
            final int color2;
            final int color3;
            final int color4;
            final int grey;
            @Bind(R.id.decoView) DecoView decoView;
            @Bind(R.id.title1) TextView successfulBreak;
            @Bind(R.id.title2) TextView continuationBreak;
            @Bind(R.id.title3) TextView foulBreak;
            @Bind(R.id.title4) TextView winBreak;

            public BreaksGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                color1 = ContextCompat.getColor(itemView.getContext(), R.color.chart3);
                color2 = ContextCompat.getColor(itemView.getContext(), R.color.chart2);
                color3 = ContextCompat.getColor(itemView.getContext(), R.color.chart1);
                color4 = ContextCompat.getColor(itemView.getContext(), R.color.chart);
                grey = ContextCompat.getColor(itemView.getContext(), R.color.chart4);
                decoView.deleteAll();
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(32, 32))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(64, 64))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .setInset(new PointF(96, 96))
                        .build());

                decoView.configureAngles(300, 0);
            }

            void bind(CompPlayer player) {
                int attempts = player.getBreakAttempts();

                float continuation = attempts != 0 ? roundNumber(player.getBreakContinuations(), attempts) : 0;
                float successes = attempts != 0 ? roundNumber(player.getBreakSuccesses(), attempts) : 0;
                float wins = attempts != 0 ? roundNumber(player.getWinsOnBreak(), attempts) : 0;
                float fouls = attempts != 0 ? roundNumber(player.getBreakFouls(), attempts) : 0;

                decoView.addSeries(new SeriesItem.Builder(color1)
                        .setRange(0, 1, successes)
                        .setInset(new PointF(0, 0))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color2)
                        .setRange(0, 1, continuation)
                        .setInset(new PointF(32, 32))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color4)
                        .setRange(0, 1, wins)
                        .setInset(new PointF(64, 64))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(color3)
                        .setRange(0, 1, fouls)
                        .setInset(new PointF(96, 96))
                        .setLineWidth(32f)
                        .setCapRounded(false)
                        .build());

                successfulBreak.setText(getString(R.string.successful_breaks,
                        player.getBreakSuccesses(),
                        player.getBreakAttempts(),
                        convertFloatToPercent((float) player.getBreakSuccesses() / (float) player.getBreakAttempts())));
                continuationBreak.setText(getString(R.string.continuation_after_the_break,
                        player.getBreakContinuations(),
                        player.getBreakAttempts(),
                        convertFloatToPercent((float) player.getBreakContinuations() / (float) player.getBreakAttempts())));
                winBreak.setText(getString(R.string._8_9_on_the_break,
                        player.getWinsOnBreak(),
                        player.getBreakAttempts(),
                        convertFloatToPercent((float) player.getWinsOnBreak() / (float) player.getBreakAttempts())));
                foulBreak.setText(getString(R.string.fouls_on_the_break,
                        player.getBreakFouls(),
                        player.getBreakAttempts(),
                        convertFloatToPercent((float) player.getBreakFouls() / (float) player.getBreakAttempts())));
            }

            private String getString(@StringRes int res, Object... formatArgs) {
                return itemView.getContext().getString(res, formatArgs);
            }
        }

        static class SafetyGraphViewHolder extends RecyclerView.ViewHolder {
            final int color1;
            final int color2;
            final int color3;
            @Bind(R.id.decoView) DecoView decoView;
            @Bind(R.id.title) TextView title;
            @Bind(R.id.title1) TextView safetyReturns;
            @Bind(R.id.title2) TextView safetyEscapes;
            @Bind(R.id.title3) TextView misses;
            @Bind(R.id.toDisappear) ViewGroup notValid;
            public SafetyGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                decoView.deleteAll();
                title.setText(R.string.title_after_opponent_safety);
                notValid.setVisibility(View.GONE);
                color1 = ContextCompat.getColor(itemView.getContext(), R.color.chart3);
                color2 = ContextCompat.getColor(itemView.getContext(), R.color.chart2);
                color3 = ContextCompat.getColor(itemView.getContext(), R.color.chart1);
            }

            // TODO: 8/26/2016 make sure that numbers are within range
            public void bind(List<AbstractPlayer> players, List<AbstractPlayer> opponents) {
                CompPlayer player = getPlayerFromList(players);
                CompPlayer opponent = getPlayerFromList(opponents);
                int total = opponent.getSafetySuccesses();

                if (opponent.getSafetySuccesses() > 0) {
                    float returns = roundNumber(player.getSafetyReturns(), total);
                    float escapes = roundNumber(player.getSafetyEscapes(), total);

                    if (returns < 0)
                        returns = 0;
                    if (escapes < 0)
                        escapes = 0;

                    decoView.addSeries(new SeriesItem.Builder(color3)
                            .setRange(0, 1, 1)
                            .setCapRounded(false)
                            .setLineWidth(128f)
                            .build());
                    decoView.addSeries(new SeriesItem.Builder(color1)
                            .setRange(0, 1, returns + escapes)
                            .setCapRounded(false)
                            .setLineWidth(128f)
                            .build());
                    decoView.addSeries(new SeriesItem.Builder(color2)
                            .setRange(0, 1, escapes)
                            .setCapRounded(false)
                            .setLineWidth(128f)
                            .build());
                } else {
                    decoView.addSeries(new SeriesItem.Builder(Color.parseColor("#F5F5F5"))
                            .setRange(0, 1, 1)
                            .setCapRounded(false)
                            .setLineWidth(128f)
                            .build());
                }

                int missed = opponent.getSafetySuccesses() - player.getSafetyEscapes() - player.getSafetyReturns();
                misses.setText(itemView.getContext().getString(R.string.legend_missed_shot, missed, opponent.getSafetySuccesses(), convertFloatToPercent((float) missed / (float) opponent.getSafetySuccesses())));
                safetyReturns.setText(itemView.getContext().getString(R.string.legend_got_safe, player.getSafetyReturns(), opponent.getSafetySuccesses(), convertFloatToPercent((float) player.getSafetyReturns() / (float) opponent.getSafetySuccesses())));
                safetyEscapes.setText(itemView.getContext().getString(R.string.legend_made_ball, player.getSafetyEscapes(), opponent.getSafetySuccesses(), convertFloatToPercent((float) player.getSafetyEscapes() / (float) opponent.getSafetySuccesses())));
            }

        }
    }

    private class UpdatePlayersAsync extends AsyncTask<StatFilter, Void, List<Pair<AbstractPlayer, AbstractPlayer>>> {
        @Override
        protected List<Pair<AbstractPlayer, AbstractPlayer>> doInBackground(StatFilter... filter) {
            List<Pair<AbstractPlayer, AbstractPlayer>> players = database.getPlayerPairs(player);
            List<Pair<AbstractPlayer, AbstractPlayer>> filteredPlayers = new ArrayList<>();

            for (Pair<AbstractPlayer, AbstractPlayer> pair : players) {
                if (filter[0].isPlayerQualified(pair.getRight()))
                    filteredPlayers.add(pair);
            }

            return filteredPlayers;
        }

        @Override protected void onPostExecute(List<Pair<AbstractPlayer, AbstractPlayer>> pairs) {
            ((PlayerInfoGraphicAdapter) adapter).updatePlayers(pairs);
        }
    }
}
