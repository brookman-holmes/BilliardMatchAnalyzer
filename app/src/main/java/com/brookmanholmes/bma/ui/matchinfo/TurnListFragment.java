package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 4/28/2016.
 */
public class TurnListFragment extends BaseFragment implements
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener,
        MatchInfoListener {
    private static final String TAG = "TurnListFragment";
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private static final String ARG_MATCH_ID = "match id";

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    private ExpandableTurnListAdapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerViewExpandableItemManager itemManager;

    public TurnListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Parcelable eimSavedState = (savedInstanceState != null) ?
                savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        itemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        adapter = new ExpandableTurnListAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        itemManager.setOnGroupCollapseListener(this);
        itemManager.setOnGroupExpandListener(this);
        wrappedAdapter = itemManager.createWrappedAdapter(adapter);
    }

    @Override
    public void update(Match match) {
        adapter.updateMatch(match);
        // after update scroll to the end of the data
        adapter.notifyItemRangeChanged(0, adapter.getGroupCount());
        itemManager.expandGroup(adapter.getGroupCount() >= 2 ? adapter.getGroupCount() - 2 : 0);

        layoutManager.scrollToPositionWithOffset(adapter.getGroupCount() + 2, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() instanceof MatchInfoActivity) {
            ((MatchInfoActivity) getActivity()).registerFragment(this);
        }
        recyclerView.setPadding(0, 0, 0, 0);

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        recyclerView.setAdapter(wrappedAdapter);
        recyclerView.setItemAnimator(animator);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        itemManager.attachRecyclerView(recyclerView);

        // fix this here
        if (adapter.getGroupCount() > 1) {
            itemManager.expandGroup(adapter.getGroupCount() - 2);
            itemManager.scrollToGroup(adapter.getGroupCount() - 2, 0);
            adapter.notifyItemRangeChanged(0, adapter.getGroupCount());
        }
        return view;
    }

    @Override
    public void onDestroyView() {
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

    @Override
    public void onDetach() {
        ((MatchInfoActivity) getActivity()).removeFragment(this);
        super.onDetach();
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        for (int groupToCollapse = 0; groupToCollapse < itemManager.getGroupCount(); groupToCollapse++)
            if (groupToCollapse != groupPosition) {
                itemManager.collapseGroup(groupToCollapse);
            }

        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = (int) (getActivity().getResources().getDisplayMetrics().density * 64);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        itemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }
}
