package com.brookmanholmes.bma.ui.stats;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 9/19/2016.
 */

class AdvStatsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int GRAPH_CUT = 0;
    private final static int GRAPH_AIM = 1;
    private final static int GRAPH_BANK = 2;
    private final static int GRAPH_KICK = 3;
    private final static int FILTER = 4;
    private final static int TITLE = 5;
    private final static int MISCUES = 6;
    private final static int LINE_ITEM = 7;

    List<AdvStats> stats;
    List<StatLineItem> items;
    Context context;
    FilterListener listener;
    private String shotType, subType, angle;

    AdvStatsRecyclerAdapter(Context context, List<AdvStats> stats, List<StatLineItem> items, FilterListener listener) {
        this.stats = stats;
        this.items = items;
        this.listener = listener;
        this.context = context;

        shotType = context.getString(R.string.all);
        subType = context.getString(R.string.all);
        angle = context.getString(R.string.all);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getViewByViewType(parent, viewType);
        switch (viewType) {
            case GRAPH_AIM:
                return new GraphViewHolder(view, R.string.title_aim, R.string.title_aim_left, R.string.title_aim_right);
            case GRAPH_CUT:
                return new GraphViewHolder(view, R.string.title_cut, R.string.title_cut_over, R.string.title_cut_under);
            case GRAPH_BANK:
                return new GraphViewHolder(view, R.string.miss_bank, R.string.title_short, R.string.title_long);
            case GRAPH_KICK:
                return new GraphViewHolder(view, R.string.miss_kick, R.string.title_short, R.string.title_long);
            case FILTER:
                return new FilterViewHolder(view, stats, listener);
            case LINE_ITEM:
                return new TitleViewHolder(view);
            case TITLE:
                return new TitleViewHolder(view);
            case MISCUES:
                return new MiscuesViewHolder(view);
            default:
                throw new IllegalArgumentException("No viewType: " + viewType);
        }
    }

    private View getViewByViewType(ViewGroup parent, int viewType) {
        if (viewType >= 0 && viewType <= 3)
            return inflateView(parent, R.layout.graph_item);
        else if (viewType == FILTER)
            return inflateView(parent, R.layout.container_adv_shot_filter);
        else if (viewType == LINE_ITEM)
            return inflateView(parent, R.layout.container_adv_stat_row);
        else if (viewType == MISCUES)
            return inflateView(parent, R.layout.container_miscues);
        else if (viewType == TITLE)
            return inflateView(parent, R.layout.container_adv_stat_title);
        else throw new IllegalArgumentException("No viewType: " + viewType);
    }

    private View inflateView(ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case GRAPH_AIM:
                ((GraphViewHolder) holder).bind(stats, AdvStats.HowType.AIM_LEFT, AdvStats.HowType.AIM_RIGHT);
                break;
            case GRAPH_CUT:
                ((GraphViewHolder) holder).bind(stats, AdvStats.HowType.THIN, AdvStats.HowType.THICK);
                break;
            case GRAPH_BANK:
                ((GraphViewHolder) holder).bind(stats, AdvStats.HowType.BANK_SHORT, AdvStats.HowType.BANK_LONG);
                break;
            case GRAPH_KICK:
                ((GraphViewHolder) holder).bind(stats, AdvStats.HowType.KICK_SHORT, AdvStats.HowType.KICK_LONG);
                break;
            case FILTER:
                ((FilterViewHolder) holder).bind(stats);
                break;
            case LINE_ITEM:
                @ColorInt int color = position - getInitialItems() % 2 == 0 ? R.color.primary_text : R.color.secondary_text;
                ((LineItemViewHolder) holder).bind(items.get(position - getInitialItems()), color);
                break;
            case TITLE:
                ((TitleViewHolder) holder).bind(stats.size());
                break;
            case MISCUES:
                ((MiscuesViewHolder) holder).bind(StatsUtils.getMiscues(stats));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getInitialItems() + items.size();
    }

    private int getInitialItems() {
        int staticItems = 3;
        int dynamicItems = 0;

        if (isKickShot())
            dynamicItems++;
        if (isBankShot())
            dynamicItems++;

        return staticItems + dynamicItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FILTER;
        } else if (position == 1) {
            return TITLE;
        } else if (position == 2) {
            return MISCUES;
        } else if (position == 3) {
            return GRAPH_CUT;
        } else if (position == 4) {
            return GRAPH_AIM;
        } else if (position == 5) {
            if (isKickShot())
                return GRAPH_KICK;
            else if (isBankShot())
                return GRAPH_BANK;
            else return LINE_ITEM;
        } else if (position == 6) {
            if (isKickShot() && isBankShot())
                return GRAPH_BANK;
            else return LINE_ITEM;
        } else return LINE_ITEM;
    }

    private boolean isKickShot() {
        return shotType.equals(context.getString(R.string.miss_kick)) || shotType.equals(context.getString(R.string.all));
    }

    private boolean isBankShot() {
        return shotType.equals(context.getString(R.string.miss_bank)) || shotType.equals(context.getString(R.string.all));
    }

    void update(List<AdvStats> stats, List<StatLineItem> items) {
        this.stats = stats;
        this.items = items;

        notifyDataSetChanged();
    }

    static class GraphViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.leftLabel)
        TextView leftLabel;
        @Bind(R.id.rightLabel)
        TextView rightLabel;
        @Bind(R.id.left)
        TextView leftText;
        @Bind(R.id.right)
        TextView rightText;

        GraphViewHolder(View itemView, @StringRes int title, @StringRes int leftLabel, @StringRes int rightLabel) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.title.setText(title);
            this.leftLabel.setText(leftLabel);
            this.rightLabel.setText(rightLabel);
        }

        private void bind(List<AdvStats> stats, AdvStats.HowType left, AdvStats.HowType right) {
            StatsUtils.setLayoutWeights(stats, left, right, leftText, rightText);
        }
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.shotTypeSpinner)
        Spinner shotTypeSpinner;
        @Bind(R.id.shotSubTypeSpinner)
        Spinner shotSubTypeSpinner;
        @Bind(R.id.angleSpinner)
        Spinner angleSpinner;
        FilterListener listener;
        List<AdvStats> stats;

        FilterViewHolder(View itemView, List<AdvStats> stats, FilterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            this.stats = stats;
            shotTypeSpinner.setAdapter(createAdapter(getPossibleShotTypes(stats)));
            shotSubTypeSpinner.setAdapter(createAdapter(getPossibleShotSubTypes(stats)));
            angleSpinner.setAdapter(createAdapter(getPossibleAngles(stats)));
        }

        private ArrayAdapter<String> createAdapter(List<String> data) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item,
                    data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return adapter;
        }

        private List<String> getPossibleShotTypes(List<AdvStats> stats) {
            SortedSet<String> shotTypes = new TreeSet<>();
            for (AdvStats stat : stats) {
                shotTypes.add(getString(MatchDialogHelperUtils.convertShotTypeToStringRes(stat.getShotType())));
            }
            shotTypes.add(getString(R.string.all));

            return new ArrayList<>(shotTypes);
        }

        private List<String> getPossibleShotSubTypes(List<AdvStats> stats) {
            SortedSet<String> shotSubTypes = new TreeSet<>();
            for (AdvStats stat : stats) {
                if (listener.isShotType(stat))
                    shotSubTypes.add(getString(MatchDialogHelperUtils.convertSubTypeToStringRes(stat.getShotSubtype())));
            }
            shotSubTypes.add(getString(R.string.all));
            shotSubTypes.remove(getString(R.string.empty_string));

            return new ArrayList<>(shotSubTypes);
        }

        private List<String> getPossibleAngles(List<AdvStats> stats) {
            SortedSet<String> angles = new TreeSet<>();
            for (AdvStats stat : stats) {
                if ((listener.isShotType(stat)) && (listener.isSubType(stat))) {
                    for (AdvStats.Angle angle : stat.getAngles())
                        angles.add(getString(MatchDialogHelperUtils.convertAngleToStringRes(angle)));
                }
            }

            List<String> list = new ArrayList<>(angles);
            list.add(0, getString(R.string.all));

            return list;
        }

        private String getString(@StringRes int res) {
            return itemView.getContext().getString(res);
        }

        private void setItems(Spinner spinner, List<String> items) {
            getAdapter(spinner).clear();
            getAdapter(spinner).addAll(items);
        }

        private ArrayAdapter<String> getAdapter(Spinner spinner) {
            return (ArrayAdapter<String>) spinner.getAdapter();
        }

        public void bind(List<AdvStats> stats) {
            this.stats = stats;
        }
    }

    static class LineItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.count)
        TextView count;
        @Bind(R.id.pct)
        TextView pct;

        LineItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(StatLineItem statLineItem, @ColorInt int color) {
            title.setTextColor(color);
            count.setTextColor(color);
            pct.setTextColor(color);

            title.setText(statLineItem.getDescription());
            count.setText(String.format(Locale.getDefault(), "%1$d", statLineItem.getCount()));
            pct.setText(statLineItem.getPercentage());
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.shootingErrorTitle)
        TextView title;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int size) {
            this.title.setText(itemView.getContext().getString(R.string.title_shooting_errors, size));
        }
    }

    static class MiscuesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.miscues)
        TextView miscues;

        MiscuesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int miscues) {
            this.miscues.setText(itemView.getContext().getString(R.string.title_miscues, miscues));
        }
    }
}
