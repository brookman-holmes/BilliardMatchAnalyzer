package com.brookmanholmes.bma.ui.stats;

import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
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
    private final static int GRAPH = 0;
    private final static int FILTER = 4;
    private final static int TITLE = 5;
    private final static int LINE_ITEM = 7;

    private List<ViewHolderDelegate> delegates = new ArrayList<>();
    private List<AdvStats> stats;

    private ViewHolderDelegate title = new TitleHolderDelegate();
    private ViewHolderDelegate miscues = new MiscueHolderDelegate();
    private ViewHolderDelegate aim = new GraphHolderDelegate(R.string.title_aim, R.string.title_aim_left, R.string.title_aim_right, AdvStats.HowType.AIM_LEFT, AdvStats.HowType.AIM_RIGHT);
    private ViewHolderDelegate cut = new GraphHolderDelegate(R.string.title_cut, R.string.title_cut_over, R.string.title_cut_under, AdvStats.HowType.THIN, AdvStats.HowType.THICK);
    private ViewHolderDelegate bank = new GraphHolderDelegate(R.string.miss_bank, R.string.title_short, R.string.title_long, AdvStats.HowType.BANK_SHORT, AdvStats.HowType.BANK_LONG);
    private ViewHolderDelegate kick = new GraphHolderDelegate(R.string.miss_kick, R.string.title_short, R.string.title_long, AdvStats.HowType.KICK_SHORT, AdvStats.HowType.KICK_LONG);


    AdvStatsRecyclerAdapter(List<AdvStats> stats, List<StatLineItem> items) {
        this.stats = stats;

        delegates.add(title);
        delegates.add(miscues);
        delegates.add(aim);
        delegates.add(cut);
        delegates.add(bank);
        delegates.add(kick);

        int count = 0;
        for (StatLineItem item : items) {
            delegates.add(new LineItemDelegate(item, count++));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createViewByViewType(parent, viewType);

        switch (viewType) {
            case GRAPH:
                return new GraphViewHolder(view);
            case TITLE:
                return new TitleViewHolder(view);
            case LINE_ITEM:
                return new LineItemViewHolder(view);
            case FILTER:
                return new FilterViewHolder(view, stats, null);
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    private View createViewByViewType(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GRAPH:
                return inflateView(parent, R.layout.graph_item);
            case TITLE:
                return inflateView(parent, R.layout.container_adv_stat_title);
            case LINE_ITEM:
                return inflateView(parent, R.layout.container_adv_stat_row);
            case FILTER:
                return inflateView(parent, R.layout.container_adv_shot_filter);
            default:
                throw new IllegalArgumentException("No such view type: " + viewType);
        }
    }

    private View inflateView(ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegates.get(position).bind(holder, stats);
    }

    @Override
    public int getItemCount() {
        return delegates.size();
    }

    @Override
    public int getItemViewType(int position) {
        return delegates.get(position).getViewType();
    }


    public void update(List<AdvStats> stats, List<StatLineItem> items) {
        this.stats = stats;

        List<ViewHolderDelegate> oldList = new ArrayList<>(delegates);
        delegates = new ArrayList<>();
        delegates.add(title);
        delegates.add(miscues);
        delegates.add(aim);
        delegates.add(cut);
        delegates.add(bank);
        delegates.add(kick);

        int count = 0;
        for (StatLineItem item : items) {
            delegates.add(new LineItemDelegate(item, count++));
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new AdvStatsDiffCallback(oldList, delegates)
        );
        diffResult.dispatchUpdatesTo(this);
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

        GraphViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(@StringRes int title, @StringRes int leftLabel, @StringRes int rightLabel, List<AdvStats> stats, AdvStats.HowType left, AdvStats.HowType right) {
            TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            this.title.setText(title);
            this.leftLabel.setText(leftLabel);
            this.rightLabel.setText(rightLabel);
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

        public void bind(StatLineItem statLineItem, @ColorRes int color) {
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
            count.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
            pct.setTextColor(ContextCompat.getColor(itemView.getContext(), color));

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

        public void bind(@StringRes int res, int size) {
            this.title.setText(itemView.getContext().getString(res, size));
        }
    }

    private abstract static class ViewHolderDelegate {
        int viewType;

        private ViewHolderDelegate(int viewType) {
            this.viewType = viewType;
        }

        private int getViewType() {
            return viewType;
        }

        abstract void bind(RecyclerView.ViewHolder holder, List<AdvStats> stats);
    }

    private static class TitleHolderDelegate extends ViewHolderDelegate {
        @StringRes
        int title = R.string.title_shooting_errors;
        int size = 0;

        private TitleHolderDelegate() {
            super(TITLE);
        }

        @Override
        void bind(RecyclerView.ViewHolder holder, List<AdvStats> stats) {
            if (holder instanceof TitleViewHolder) {
                size = stats.size();
                ((TitleViewHolder) holder).bind(title, size);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TitleHolderDelegate that = (TitleHolderDelegate) o;

            if (title != that.title) return false;
            return size == that.size;

        }

        @Override
        public int hashCode() {
            int result = title;
            result = 31 * result + size;
            return result;
        }
    }

    private static class MiscueHolderDelegate extends ViewHolderDelegate {
        @StringRes
        int title = R.string.title_miscues;
        int size = 0;

        private MiscueHolderDelegate() {
            super(TITLE);
        }

        @Override
        void bind(RecyclerView.ViewHolder holder, List<AdvStats> stats) {
            if (holder instanceof TitleViewHolder) {
                int size = StatsUtils.getMiscues(stats);
                ((TitleViewHolder) holder).bind(title, size);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MiscueHolderDelegate that = (MiscueHolderDelegate) o;

            if (title != that.title) return false;
            return size == that.size;

        }

        @Override
        public int hashCode() {
            int result = title;
            result = 31 * result + size;
            return result;
        }
    }

    private static class GraphHolderDelegate extends ViewHolderDelegate {
        private
        @StringRes
        int title, leftLabel, rightLabel;
        private AdvStats.HowType left, right;
        private List<AdvStats> stats;

        private GraphHolderDelegate(int title, int leftLabel, int rightLabel,
                                    AdvStats.HowType left, AdvStats.HowType right) {
            super(GRAPH);
            this.title = title;
            this.leftLabel = leftLabel;
            this.rightLabel = rightLabel;
            this.left = left;
            this.right = right;
        }

        @Override
        void bind(RecyclerView.ViewHolder holder, List<AdvStats> stats) {
            if (holder instanceof GraphViewHolder) {
                this.stats = stats;
                ((GraphViewHolder) holder).bind(title, leftLabel, rightLabel, stats, left, right);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GraphHolderDelegate that = (GraphHolderDelegate) o;

            if (title != that.title) return false;
            if (leftLabel != that.leftLabel) return false;
            if (rightLabel != that.rightLabel) return false;
            if (left != that.left) return false;
            if (right != that.right) return false;
            return stats.equals(that.stats);

        }

        @Override
        public int hashCode() {
            int result = title;
            result = 31 * result + leftLabel;
            result = 31 * result + rightLabel;
            result = 31 * result + left.hashCode();
            result = 31 * result + right.hashCode();
            result = 31 * result + stats.hashCode();
            return result;
        }
    }

    private static class LineItemDelegate extends ViewHolderDelegate {
        StatLineItem item;
        @ColorRes
        int color;

        private LineItemDelegate(StatLineItem item, int position) {
            super(LINE_ITEM);

            this.item = item;

            color = position % 2 == 0 ? R.color.primary_text : R.color.secondary_text;
        }

        @Override
        void bind(RecyclerView.ViewHolder holder, List<AdvStats> stats) {
            if (holder instanceof LineItemViewHolder) {
                ((LineItemViewHolder) holder).bind(item, color);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LineItemDelegate that = (LineItemDelegate) o;

            if (color != that.color) return false;
            return item.equals(that.item);

        }

        @Override
        public int hashCode() {
            int result = item.hashCode();
            result = 31 * result + color;
            return result;
        }
    }

    private static class AdvStatsDiffCallback extends DiffUtil.Callback {
        private List<ViewHolderDelegate> oldList;
        private List<ViewHolderDelegate> newList;

        AdvStatsDiffCallback(List<ViewHolderDelegate> oldList, List<ViewHolderDelegate> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getViewType() == newList.get(newItemPosition).getViewType();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
