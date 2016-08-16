/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brookmanholmes.bma.wizard.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.TextViewCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.bma.wizard.model.Page;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceFragment extends ListFragment {
    protected static final String ARG_KEY = "key";

    protected PageFragmentCallbacks callbacks;
    protected String key;
    protected List<String> choices;
    protected Page page;

    public MultipleChoiceFragment() {
    }

    public static MultipleChoiceFragment create(String key, int titleSize) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(SingleChoiceFragment.ARG_TITLE_SIZE, titleSize);

        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MultipleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = callbacks.onGetPage(key);

        MultipleFixedChoicePage fixedChoicePage = (MultipleFixedChoicePage) page;
        choices = new ArrayList<>();
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            choices.add(fixedChoicePage.getOptionAt(i));
        }
        Collections.sort(choices);
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());

        if (getArguments().getInt(SingleChoiceFragment.ARG_TITLE_SIZE, -1) != -1) {
            TextView title = (TextView) rootView.findViewById(android.R.id.title);
            TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
            title.setPadding(0, 0, 0, 0);
        }

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                android.R.id.text1,
                choices));
        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Pre-select currently selected items.
        new Handler().post(new Runnable() {
            @Override public void run() {
                ArrayList<String> selectedItems = page.getData().getStringArrayList(
                        Page.SIMPLE_DATA_KEY);
                if (selectedItems == null || selectedItems.size() == 0) {
                    return;
                }

                Set<String> selectedSet = new HashSet<>(selectedItems);

                for (int i = 0; i < choices.size(); i++) {
                    if (selectedSet.contains(choices.get(i))) {
                        listView.setItemChecked(i, true);
                    }
                }
            }
        });

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

        Set<String> selectedSet = new HashSet<>(selectedItems);

        for (int i = 0; i < choices.size(); i++) {
            getListView().setItemChecked(i, selectedSet.contains(choices.get(i)));
        }
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getParentFragment();
        } else if (getActivity() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getActivity();
        } else {
            throw new ClassCastException("Activity/ParentFragment must implement PageFragmentCallbacks");
        }


    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        SparseBooleanArray checkedPositions = getListView().getCheckedItemPositions();
        ArrayList<String> selections = new ArrayList<>();
        for (int i = 0; i < checkedPositions.size(); i++) {
            if (checkedPositions.valueAt(i)) {
                selections.add(getListAdapter().getItem(checkedPositions.keyAt(i)).toString());
            }
        }

        page.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, selections);
        page.notifyDataChanged();
    }
}
