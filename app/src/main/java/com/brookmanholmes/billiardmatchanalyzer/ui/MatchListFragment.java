package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.ListItemClickListener;
import com.brookmanholmes.billiardmatchanalyzer.adapters.MatchListRecyclerAdapter;
import com.brookmanholmes.billiardmatchanalyzer.adapters.SimpleDividerItemDecoration;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.MatchListLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ListItemClickListener {
    private static int LOADER_ID = 100;

    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    private MatchListRecyclerAdapter adapter;
    private DatabaseAdapter database;

    public MatchListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view2, null);
        ButterKnife.bind(this, view);

        adapter = new MatchListRecyclerAdapter(this);

        database = new DatabaseAdapter(getActivity());
        database.open();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, getArguments(), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Loader methods
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        return new MatchListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onSelectMatch(long id) {
        Intent intent = new Intent(getActivity(), MatchInfoActivity.class);
        intent.putExtra(BaseActivity.ARG_MATCH_ID, id);
        startActivity(intent);
    }

    @Override
    public void onLongSelectMatch(long id) {
        Log.i("MatchListFragment", "Long selected match: " + id);
        // // TODO: 1/16/2016 delete match here
    }
}
