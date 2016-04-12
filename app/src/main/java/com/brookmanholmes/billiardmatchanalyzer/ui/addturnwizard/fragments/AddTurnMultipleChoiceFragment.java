package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.MultipleChoiceFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class AddTurnMultipleChoiceFragment extends MultipleChoiceFragment {
    public static AddTurnMultipleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AddTurnMultipleChoiceFragment fragment = new AddTurnMultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_turn_page, container, false);
        ((TextView) rootView.findViewById(R.id.title)).setText(page.getTitle());

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                android.R.id.text1,
                choices));
        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        // retarded work around for weird bug where items get selected randomly?
        // Pre-select currently selected items.
        ArrayList<String> selectedItems = page.getData().getStringArrayList(
                Page.SIMPLE_DATA_KEY);
        if (selectedItems == null || selectedItems.size() == 0) {
            for (int i = 0; i < choices.size(); i++) {
                getListView().setItemChecked(i, false);
            }

            return;
        }

        Set<String> selectedSet = new HashSet<String>(selectedItems);

        for (int i = 0; i < choices.size(); i++) {
            getListView().setItemChecked(i, selectedSet.contains(choices.get(i)));
        }
    }
}
