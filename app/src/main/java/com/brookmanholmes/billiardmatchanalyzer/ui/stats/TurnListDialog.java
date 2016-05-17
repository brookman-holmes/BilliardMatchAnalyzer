package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 4/28/2016.
 */
public class TurnListDialog extends DialogFragment implements
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private ExpandableTurnListAdapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewExpandableItemManager itemManager;

    public TurnListDialog() {
    }

    public static TurnListDialog create(long matchId) {
        TurnListDialog frag = new TurnListDialog();
        Bundle args = new Bundle();
        args.putLong(AdvStatsDialog.ARG_MATCH_ID, matchId);
        frag.setArguments(args);

        return frag;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseAdapter db = new DatabaseAdapter(getContext());

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID);

        adapter = new ExpandableTurnListAdapter(getContext(), db.getMatch(matchId), db.getAdvMatchTurns(matchId));
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_adv_stats_list, null, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getContext());
        final Parcelable eimSavedState = (savedInstanceState != null) ?
                savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;

        itemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        itemManager.setOnGroupCollapseListener(this);
        itemManager.setOnGroupExpandListener(this);
        wrappedAdapter = itemManager.createWrappedAdapter(adapter);

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        recyclerView.setAdapter(wrappedAdapter);
        recyclerView.setItemAnimator(animator);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(
                ContextCompat.getDrawable(getContext(), R.drawable.line_divider), false));
        itemManager.attachRecyclerView(recyclerView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setView(view);
        return builder.create();
    }

    @Override public void onDestroyView() {
        if (itemManager != null) {
            itemManager.release();
            itemManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        layoutManager = null;

        super.onDestroyView();
    }

    @Override public void onGroupExpand(int groupPosition, boolean fromUser) {
        for (int groupToCollapse = 0; groupToCollapse < itemManager.getGroupCount(); groupToCollapse++)
            if (groupToCollapse != groupPosition) {
                itemManager.collapseGroup(groupToCollapse);
            }

        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    @Override public void onGroupCollapse(int groupPosition, boolean fromUser) {
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = (int) (getActivity().getResources().getDisplayMetrics().density * 64);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        itemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }
}