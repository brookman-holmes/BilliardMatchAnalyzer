package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnEndPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndFragment extends ListFragment {
    private static final String ARG_KEY = "key";
    ListView listView;
    private PageFragmentCallbacks callbacks;
    private String key;
    private TurnEndPage page;
    private CustomAdapter adapter;

    public TurnEndFragment() {
    }

    public static TurnEndFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        TurnEndFragment fragment = new TurnEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
        page = (TurnEndPage) callbacks.onGetPage(key);
    }

    @Override
    public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_turn_page, container, false);
        ((TextView) rootView.findViewById(R.id.title)).setText(page.getTitle());
        listView = (ListView) rootView.findViewById(android.R.id.list);
        adapter = new CustomAdapter(getActivity(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                new ArrayList<String>());
        setListAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return rootView;
    }

    @Override
    public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        updatePage(position);
    }

    public void updateOptions(List<String> options, String defaultChecked) {
        adapter.clear();
        adapter.addAll(options);
        adapter.notifyDataSetChanged();


        if (options.contains(getTurnEndFromPage())) {
            listView.setItemChecked(options.indexOf(getTurnEndFromPage()), true);
        } else {
            listView.setItemChecked(options.indexOf(defaultChecked), true);
            updatePage(listView.getCheckedItemPosition());
        }
    }

    private void updatePage(int position) {
        if (!page.getData().getString(Page.SIMPLE_DATA_KEY, "").equals(adapter.getItem(position))) {
            page.getData().putString(Page.SIMPLE_DATA_KEY, adapter.getItem(position));
            page.notifyDataChanged();
        }
    }

    private String getTurnEndFromPage() {
        return page.getData().getString(Page.SIMPLE_DATA_KEY, "");
    }

    private static class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }
    }
}
