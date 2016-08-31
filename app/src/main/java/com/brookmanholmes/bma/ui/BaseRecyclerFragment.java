package com.brookmanholmes.bma.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.bma.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/31/2016.
 */
public abstract class BaseRecyclerFragment extends BaseFragment {
    protected RecyclerView.LayoutManager layoutManager;
    @Bind(R.id.scrollView) protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        layoutManager = getLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override public void onDestroyView() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        layoutManager = null;
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
