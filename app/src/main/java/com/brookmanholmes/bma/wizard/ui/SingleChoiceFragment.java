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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleChoiceFragment extends ListFragment {
    static final String ARG_TITLE_SIZE = "title size";
    private static final String ARG_KEY = "key";
    protected PageFragmentCallbacks callbacks;
    protected List<String> choices;
    protected String key;
    protected Page page;

    public SingleChoiceFragment() {
    }

    public static SingleChoiceFragment create(String key, int titleSize) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_TITLE_SIZE, titleSize);

        SingleChoiceFragment fragment = new SingleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SingleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        SingleChoiceFragment fragment = new SingleChoiceFragment();
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

        SingleFixedChoicePage fixedChoicePage = (SingleFixedChoicePage) page;
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
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                choices));
        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Pre-select currently selected item.
        new Handler().post(new Runnable() {
            @Override public void run() {
                String selection = page.getData().getString(Page.SIMPLE_DATA_KEY);
                for (int i = 0; i < choices.size(); i++) {
                    if (choices.get(i).equals(selection)) {
                        listView.setItemChecked(i, true);
                        break;
                    }
                }
            }
        });

        return rootView;
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
        page.getData().putString(Page.SIMPLE_DATA_KEY,
                getListAdapter().getItem(position).toString());
        page.notifyDataChanged();
    }
}