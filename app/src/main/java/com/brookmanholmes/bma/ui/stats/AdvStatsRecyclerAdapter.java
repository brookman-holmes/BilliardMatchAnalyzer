package com.brookmanholmes.bma.ui.stats;

import android.support.annotation.StringRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 9/1/2016.
 */
public class AdvStatsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TITLE = 0;
    private static final int FILTER = 1;
    private static final int MISCUES = 2;
    private static final int GRAPH = 3;
    private static final int WHY = 4;

    List<StatLineItem> lineItems;
    List<AdvStats> advStats;

    public AdvStatsRecyclerAdapter(List<AdvStats> stats) {
        advStats = stats;
        lineItems = StatsUtils.getStats(stats);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.container_adv_stat_title, parent, false));
            case FILTER:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.container_adv_shot_filter, parent, false));
            case MISCUES:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.container_miscues, parent, false));
            case GRAPH:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.graph_item, parent, false));
            default:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.container_adv_stat_row, parent, false));
        }
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0)
            ((TitleViewHolder) holder).bind(advStats.size());
        else if (position == 1)
            ((FilterViewHolder) holder).bind();
        else if (position == 2)
            ((MiscuesViewHolder) holder).bind(StatsUtils.getMiscues(holder.itemView.getContext(), advStats));
        else if (position == 3) {
            Pair<Integer, Integer> weights = StatsUtils.getHowCutErrors(holder.itemView.getContext(), advStats);
            ((BarGraphViewHolder) holder).bind(weights.first, weights.second, R.string.title_cut,
                    R.string.title_cut_under, R.string.title_cut_over);
        } else if (position == 4) {
            Pair<Integer, Integer> weights = StatsUtils.getHowAimErrors(holder.itemView.getContext(), advStats);
            ((BarGraphViewHolder) holder).bind(weights.first, weights.second, R.string.title_aim,
                    R.string.title_aim_left, R.string.title_aim_right);
        } else if (position == 5) {
            Pair<Integer, Integer> weights = StatsUtils.getHowBankErrors(holder.itemView.getContext(), advStats);
            ((BarGraphViewHolder) holder).bind(weights.first, weights.second, R.string.title_bank_error,
                    R.string.title_short, R.string.title_long);
        } else if (position == 6) {
            Pair<Integer, Integer> weights = StatsUtils.getHowCutErrors(holder.itemView.getContext(), advStats);
            ((BarGraphViewHolder) holder).bind(weights.first, weights.second, R.string.title_kick_error,
                    R.string.title_short, R.string.title_long);
        } else {
            ((WhyMissViewHolder) holder).bind(lineItems.get(position - 7));
        }

    }

    @Override public int getItemCount() {
        return 7 + lineItems.size();
    }

    @Override public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TITLE;
            case 1:
                return FILTER;
            case 2:
                return MISCUES;
            case 3:
                return GRAPH;
            case 4:
                return GRAPH;
            case 5:
                return GRAPH;
            case 6:
                return GRAPH;
            default:
                return WHY;
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int count) {
            title.setText(itemView.getContext().getString(R.string.title_shooting_errors, count));
        }
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        public FilterViewHolder(View itemView) {
            super(itemView);
        }

        public void bind() {

        }
    }

    static class MiscuesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.miscues) TextView miscues;

        public MiscuesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int count) {
            miscues.setText(itemView.getContext().getString(R.string.title_miscues, count));
        }
    }

    static class BarGraphViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.leftText) TextView leftText;
        @Bind(R.id.rightText) TextView rightText;
        @Bind(R.id.left) TextView left;
        @Bind(R.id.right) TextView right;

        public BarGraphViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(float leftWeight, float rightWeight, @StringRes int title,
                         @StringRes int leftText, @StringRes int rightText) {
            this.title.setText(title);
            this.leftText.setText(leftText);
            this.rightText.setText(rightText);

            int first = (int) leftWeight, second = (int) rightWeight;
            if (leftWeight == 0)
                leftWeight = .1f;
            else
                leftWeight = leftWeight / (leftWeight + rightWeight);

            if (rightWeight == 0)
                rightWeight = .1f;
            else
                rightWeight = rightWeight / (leftWeight + rightWeight);

            left.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, leftWeight));
            right.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, rightWeight));

            left.setText(Integer.toString(first));
            right.setText(Integer.toString(second));
        }
    }

    static class WhyMissViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.count) TextView count;
        @Bind(R.id.pct) TextView pct;

        public WhyMissViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(StatLineItem item) {
            title.setText(item.getDescription());
            count.setText(Integer.toString(item.getCount()));
            pct.setText(item.getPercentage());
        }
    }
}
