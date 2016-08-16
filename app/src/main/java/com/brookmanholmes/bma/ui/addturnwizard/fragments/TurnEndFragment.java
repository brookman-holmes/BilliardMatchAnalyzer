package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.TurnEndPage;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndFragment extends ListFragment {
    private static final String ARG_KEY = "key";
    private static final String ARG_OPTIONS_KEY = "options";
    private static final String ARG_SELECTION_KEY = "selection";
    ListView listView;
    private PageFragmentCallbacks callbacks;
    private String key;
    private TurnEndPage page;
    private CustomAdapter adapter;

    public TurnEndFragment() {
    }

    public static TurnEndFragment create(String key, ArrayList<String> options, String selection) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putStringArrayList(ARG_OPTIONS_KEY, options);
        args.putString(ARG_SELECTION_KEY, selection);
        TurnEndFragment fragment = new TurnEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getArguments().getString(ARG_KEY);

        adapter = new CustomAdapter(getContext(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                new ArrayList<String>());
    }

    private void populateChoicesList(List<String> options) {
        adapter.clear();
        for (String option : options) {
            TurnEnd ending = TurnEnd.valueOf(option);
            if (ending == TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN)
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending), page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)));
            else if (ending == TurnEnd.OPPONENT_BREAKS_AGAIN)
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending), page.getData().getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY)));
            else
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending)));
        }

        adapter.sort();
        adapter.notifyDataSetChanged();
    }

    @Override public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = (TurnEndPage) callbacks.onGetPage(key);
        List<String> options = getArguments().getStringArrayList(ARG_OPTIONS_KEY);
        if (options == null)
            throw new IllegalArgumentException("Must pass in a string of options");
        else {
            populateChoicesList(options);
        }
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        TextView title = (TextView) rootView.findViewById(android.R.id.title);
        TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
        title.setPadding(0, 0, 0, 0);

        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());
        listView = (ListView) rootView.findViewById(android.R.id.list);

        setListAdapter(adapter);

        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(adapter.getPosition(getArguments().getString(ARG_SELECTION_KEY)), true);

        return rootView;
    }

    @Override public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        updatePage(position);
    }

    public void updateOptions(List<TurnEnd> options, TurnEnd defaultChecked) {
        repopulateChoicesList(options);

        if (adapter.contains(getTurnEndFromPage())) {
            listView.setItemChecked(adapter.indexOf(getTurnEndFromPage()), true);
        } else {
            listView.setItemChecked(adapter.indexOf(getDefaultCheck(defaultChecked)), true);
            updatePage(listView.getCheckedItemPosition());
        }
    }

    private void repopulateChoicesList(List<TurnEnd> options) {
        adapter.clear();
        for (TurnEnd ending : options) {
            if (ending == TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN)
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending), page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)));
            else if (ending == TurnEnd.OPPONENT_BREAKS_AGAIN)
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending), page.getData().getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY)));
            else
                adapter.add(getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending)));
        }
        adapter.sort();
        adapter.notifyDataSetChanged();
    }

    private String getDefaultCheck(TurnEnd ending) {
        if (ending == TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN)
            return getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending),
                    page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY));
        else if (ending == TurnEnd.OPPONENT_BREAKS_AGAIN)
            return getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending),
                    page.getData().getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY));
        else
            return getString(MatchDialogHelperUtils.convertTurnEndToStringRes(ending));
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
        List<String> objects;
        public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
            this.objects = objects;
        }

        private boolean contains(String item) {
            return objects.contains(item);
        }

        private int indexOf(String item) {
            return objects.indexOf(item);
        }

        private void sort() {
            Collections.sort(objects);
        }
    }
}
