package com.brookmanholmes.bma.ui.profile;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseRecyclerFragment;
import com.brookmanholmes.bma.ui.matchinfo.PlayersListener;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoGraphicFragment extends BaseRecyclerFragment<PlayerInfoGraphicFragment.PlayerInfoGraphicAdapter>
        implements PlayersListener {
    private static final String TAG = "PIGF";
    DecimalFormat df = new DecimalFormat("#.###");

    public PlayerInfoGraphicFragment() {
    }

    private static String convertFloatToPercent(float val) {
        if (Float.isNaN(val))
            return String.format(Locale.getDefault(), "%.0f%%", 0f);
        else
            return String.format(Locale.getDefault(), "%.0f%%", val * 100);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df.setRoundingMode(RoundingMode.FLOOR);
        adapter = new PlayerInfoGraphicAdapter(new ArrayList<Player>(), new ArrayList<Player>());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addStatListener(this);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeStatListener(this);
        }
        super.onDestroy();
    }

    @Override
    public void updatePlayers(List<Player> players, List<Player> opponents) {
        adapter.updatePlayers(players, opponents);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
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
    static class PlayerInfoGraphicAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        static final int ITEM_GRAPH = 0;
        static final int ITEM_SAFETY_INFO = 1;
        static final int ITEM_SAFETY_GRAPH = 2;
        static final int ITEM_BREAK_GRAPH = 3;
        static final int ITEM_BREAK_INFO = 4;
        static final int ITEM_FOOTER = 5;

        final List<Player> players;
        final List<Player> opponents;

        PlayerInfoGraphicAdapter(List<Player> players, List<Player> opponents) {
            this.players = new ArrayList<>(players);
            this.opponents = new ArrayList<>(opponents);
        }

        private static Player getPlayerFromList(List<Player> players) {
            if (players.size() > 0) {
                Player player = new Player(players.get(0).getId(), players.get(0).getName(), GameType.ALL);
                for (Player abstractPlayer : players) {
                    player.addPlayerStats(abstractPlayer);
                }

                return player;
            } else return new Player("", "", GameType.ALL);
        }

        private static float roundNumber(int top, int bottom) {
            BigDecimal decimal = new BigDecimal((float) top / (float) bottom).round(new MathContext(4, RoundingMode.FLOOR));
            return decimal.floatValue();
        }

        private void updatePlayers(List<Player> players, List<Player> opponents) {
            this.players.clear();
            this.opponents.clear();
            this.players.addAll(players);
            this.opponents.addAll(opponents);
            notifyItemRangeChanged(0, getItemCount());
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
            return getViewHolderByViewType(view, viewType);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
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
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @LayoutRes
        int getLayoutResource(int viewType) {
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
                case ITEM_FOOTER:
                    return R.layout.footer;
                default:
                    throw new IllegalArgumentException("No such view type: " + viewType);
            }
        }

        BaseViewHolder getViewHolderByViewType(View view, int viewType) {
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
                case ITEM_FOOTER:
                    return new FooterViewHolder(view);
                default:
                    throw new IllegalArgumentException("No such view type: " + viewType);
            }
        }

        static class GraphViewHolder extends BaseViewHolder {
            private static final String TAG = "GraphViewHolder";
            @Bind(R.id.graph)
            LineChartView chart;

            GraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(List<Player> players) {
                List<PointValue> tsp = new ArrayList<>();
                List<PointValue> shooting = new ArrayList<>();
                List<PointValue> safeties = new ArrayList<>();
                List<PointValue> breaking = new ArrayList<>();
                List<AxisValue> xAxisValues = new ArrayList<>();

                float count = 0;
                for (Player player : players) {
                    if (player.getShootingAttempts() + player.getSafetyAttempts() + player.getBreakAttempts() > 0) {
                        tsp.add(new PointValue(count, (float) player.getTrueShootingPct()));
                        xAxisValues.add(new AxisValue(count, DateFormat.getDateInstance(DateFormat.SHORT).format(player.getMatchDate()).toCharArray()));
                        if (player.getShootingAttempts() > 0)
                            shooting.add(new PointValue(count, (float) player.getShootingPct()));
                        if (player.getSafetyAttempts() > 0)
                            safeties.add(new PointValue(count, (float) player.getSafetyPct()));
                        if (player.getBreakAttempts() > 0)
                            breaking.add(new PointValue(count, (float) player.getBreakPct()));
                    }

                    count += 1;
                }

                LineChartData data = new LineChartData(getLines(tsp, shooting, safeties, breaking, getDummyLine((int) count)));

                List<AxisValue> yAxisValues = new ArrayList<>();
                for (int i = 0; i <= 10; i++) {
                    yAxisValues.add(new AxisValue(i * .1f));
                }
                Axis xAxis = new Axis(xAxisValues);
                xAxis.setHasTiltedLabels(false)
                        .setHasSeparationLine(true);

                Axis yAxis = new Axis(yAxisValues);
                yAxis.setHasSeparationLine(true)
                        .setTextSize(16)
                        .setFormatter(new SimpleAxisValueFormatter(3))
                        .setInside(true)
                        .setHasLines(true);

                data.setAxisYRight(yAxis);
                data.setAxisXBottom(xAxis);
                data.setValueLabelTextSize(24);

                chart.setLineChartData(data);
                if (chart != null) {
                    final Viewport viewPort = new Viewport();
                    viewPort.set(-1000f, 1.1f, xAxisValues.size() + 1f, -.5f);
                    chart.setCurrentViewport(viewPort);
                    chart.setMaximumViewport(viewPort);
                }
            }

            private Line getLine(List<PointValue> values, @ColorRes int color) {
                return new Line(values)
                        .setPointRadius(itemView.getContext().getResources().getInteger(R.integer.graph_point_size))
                        .setColor(getColor(color))
                        .setPointColor(getColor(color))
                        .setHasLabels(false)
                        .setStrokeWidth(itemView.getContext().getResources().getInteger(R.integer.graph_line_size))
                        .setHasPoints(true)
                        .setHasLabelsOnlyForSelected(true)
                        .setFormatter(new SimpleLineChartValueFormatter(3));
            }

            private Line getDummyLine(int count) {
                return new Line(Arrays.asList(new PointValue(0, 0), new PointValue(count, 1.1f)))
                        .setColor(getColor(android.R.color.transparent))
                        .setHasPoints(false);
            }

            private List<Line> getLines(List<PointValue> tsp, List<PointValue> shooting, List<PointValue> safeties, List<PointValue> breaking, Line line) {
                return Arrays.asList(getLine(shooting, R.color.chart1),
                        getLine(safeties, R.color.chart3),
                        getLine(breaking, R.color.chart2),
                        getLine(tsp, R.color.chart),
                        line);
            }

            @ColorInt
            private int getColor(@ColorRes int color, int alpha) {
                return ColorUtils.setAlphaComponent(getColor(color), alpha);
            }
        }

        abstract static class TwoItemHolder extends BaseViewHolder {
            @Bind(R.id.item1)
            TextView item1;
            @Bind(R.id.item1Desc)
            TextView item1Desc;
            @Bind(R.id.item2)
            TextView item2;
            @Bind(R.id.item2Desc)
            TextView item2Desc;

            TwoItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                setItemDesc();
            }

            public abstract void bind(List<Player> players, List<Player> opponents);

            abstract void setItemDesc();
        }

        static class SafetyTwoItemHolder extends TwoItemHolder {
            SafetyTwoItemHolder(View itemView) {
                super(itemView);
            }

            @Override
            void setItemDesc() {
                item1Desc.setText(R.string.title_forced_opponent_foul);
                item2Desc.setText(R.string.title_effect_safety_rate);
            }

            @Override
            public void bind(List<Player> players, List<Player> opponents) {
                Player player = getPlayerFromList(players);
                Player opponent = getPlayerFromList(opponents);

                float safetyPct = (float) (player.getSafetySuccesses() - opponent.getSafetyEscapes() - opponent.getSafetyReturns()) / (float) player.getSafetyAttempts();
                int ballInHands = getForcedErrors(opponents);

                item1.setText(String.format(Locale.getDefault(), "%1$d", ballInHands));
                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(safetyPct)));
            }

            int getForcedErrors(List<Player> opponents) {
                int ballInHands = 0;

                for (Player opp : opponents) {
                    ballInHands += opp.getSafetyForcedErrors();
                }

                return ballInHands;
            }
        }

        static class BreakTwoItemHolder extends TwoItemHolder {
            BreakTwoItemHolder(View itemView) {
                super(itemView);
            }

            @Override
            void setItemDesc() {
                item1Desc.setText(R.string.title_break_runs);
                item2Desc.setText(R.string.title_break_run_pct);
            }

            @Override
            public void bind(List<Player> players, List<Player> opponents) {
                float breakRunPct = (float) getPlayerFromList(players).getBreakAndRuns() / (float) getPlayerFromList(players).getBreakAttempts();
                item1.setText(String.format(Locale.getDefault(), "%1$d", getPlayerFromList(players).getBreakAndRuns()));
                item2.setText(String.format(Locale.getDefault(), "%1$s", convertFloatToPercent(breakRunPct)));
            }
        }

        static class BreaksGraphViewHolder extends BaseViewHolder {
            private static final String TAG = "BreaksGraphVH";
            final int color1;
            final int color2;
            final int color3;
            final int color4;
            final int grey;
            @Bind(R.id.decoView)
            DecoView decoView;
            @Bind(R.id.title1)
            TextView successfulBreak;
            @Bind(R.id.title2)
            TextView continuationBreak;
            @Bind(R.id.title3)
            TextView foulBreak;
            @Bind(R.id.title4)
            TextView winBreak;

            BreaksGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                float width = getDimension(R.dimen.decoview_inset);

                color1 = getColor(R.color.chart3);
                color2 = getColor(R.color.chart2);
                color3 = getColor(R.color.chart1);
                color4 = getColor(R.color.chart);
                grey = getColor(R.color.chart4);
                decoView.deleteAll();
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(width)
                        .setCapRounded(false)
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(width)
                        .setCapRounded(false)
                        .setInset(new PointF(width, width))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(width)
                        .setCapRounded(false)
                        .setInset(new PointF(width * 2, width * 2))
                        .build());
                decoView.addSeries(new SeriesItem.Builder(grey)
                        .setRange(0, 1, 1)
                        .setLineWidth(width)
                        .setCapRounded(false)
                        .setInset(new PointF(width * 3, width * 3))
                        .build());

                decoView.configureAngles(300, 0);
            }

            void bind(Player player) {
                int attempts = player.getBreakAttempts();

                float continuation = attempts != 0 ? roundNumber(player.getBreakContinuations(), attempts) : 0;
                float successes = attempts != 0 ? roundNumber(player.getBreakSuccesses(), attempts) : 0;
                float wins = attempts != 0 ? roundNumber(player.getWinsOnBreak(), attempts) : 0;
                float fouls = attempts != 0 ? roundNumber(player.getBreakFouls(), attempts) : 0;

                float width = getDimension(R.dimen.decoview_inset);

                if (player.getBreakSuccesses() > 0)
                    decoView.addSeries(new SeriesItem.Builder(color1)
                            .setRange(0, 1, successes)
                            .setInset(new PointF(0, 0))
                            .setLineWidth(width)
                            .setCapRounded(false)
                            .build());
                if (player.getBreakContinuations() > 0)
                    decoView.addSeries(new SeriesItem.Builder(color2)
                            .setRange(0, 1, continuation)
                            .setInset(new PointF(width, width))
                            .setLineWidth(width)
                            .setCapRounded(false)
                            .build());
                if (player.getWinsOnBreak() > 0)
                    decoView.addSeries(new SeriesItem.Builder(color4)
                            .setRange(0, 1, wins)
                            .setInset(new PointF(width * 2, width * 2))
                            .setLineWidth(width)
                            .setCapRounded(false)
                            .build());
                if (player.getBreakFouls() > 0)
                    decoView.addSeries(new SeriesItem.Builder(color3)
                            .setRange(0, 1, fouls)
                            .setInset(new PointF(width * 3, width * 3))
                            .setLineWidth(width)
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

            private int getDimension(@DimenRes int dimen) {
                return itemView.getContext().getResources().getDimensionPixelSize(dimen);
            }
        }

        static class SafetyGraphViewHolder extends BaseViewHolder {
            final int color1;
            final int color2;
            final int color3;
            @Bind(R.id.decoView)
            DecoView decoView;
            @Bind(R.id.title)
            TextView title;
            @Bind(R.id.title1)
            TextView safetyReturns;
            @Bind(R.id.title2)
            TextView safetyEscapes;
            @Bind(R.id.title3)
            TextView misses;
            @Bind(R.id.title4)
            View notValid;

            SafetyGraphViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                decoView.deleteAll();
                title.setText(R.string.title_after_opponent_safety);
                notValid.setVisibility(View.GONE);
                color1 = getColor(R.color.chart3);
                color2 = getColor(R.color.chart2);
                color3 = getColor(R.color.chart1);
            }

            public void bind(List<Player> players, List<Player> opponents) {
                Player player = getPlayerFromList(players);
                Player opponent = getPlayerFromList(opponents);
                int total = opponent.getSafetySuccesses();

                float width = getDimension(R.dimen.decoview_inset) * 4;

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
                            .setLineWidth(width)
                            .build());
                    decoView.addSeries(new SeriesItem.Builder(color1)
                            .setRange(0, 1, returns + escapes)
                            .setCapRounded(false)
                            .setLineWidth(width)
                            .build());
                    decoView.addSeries(new SeriesItem.Builder(color2)
                            .setRange(0, 1, escapes)
                            .setCapRounded(false)
                            .setLineWidth(width)
                            .build());
                } else {
                    decoView.addSeries(new SeriesItem.Builder(Color.parseColor("#F5F5F5"))
                            .setRange(0, 1, 1)
                            .setCapRounded(false)
                            .setLineWidth(width)
                            .build());
                }

                int missed = opponent.getSafetySuccesses() - player.getSafetyEscapes() - player.getSafetyReturns();
                misses.setText(itemView.getContext().getString(R.string.legend_missed_shot, missed, opponent.getSafetySuccesses(), convertFloatToPercent((float) missed / (float) opponent.getSafetySuccesses())));
                safetyReturns.setText(itemView.getContext().getString(R.string.legend_got_safe, player.getSafetyReturns(), opponent.getSafetySuccesses(), convertFloatToPercent((float) player.getSafetyReturns() / (float) opponent.getSafetySuccesses())));
                safetyEscapes.setText(itemView.getContext().getString(R.string.legend_made_ball, player.getSafetyEscapes(), opponent.getSafetySuccesses(), convertFloatToPercent((float) player.getSafetyEscapes() / (float) opponent.getSafetySuccesses())));
            }

            private int getDimension(@DimenRes int dimen) {
                return itemView.getContext().getResources().getDimensionPixelSize(dimen);
            }
        }

        static class FooterViewHolder extends BaseViewHolder {
            FooterViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
