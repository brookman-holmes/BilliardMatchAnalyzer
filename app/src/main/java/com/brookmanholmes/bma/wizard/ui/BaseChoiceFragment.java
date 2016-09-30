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
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
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

/**
 * Created by Brookman Holmes on 8/22/2016.
 */
public abstract class BaseChoiceFragment extends ListFragment {
    protected static final String ARG_KEY = "key";
    protected static final String ARG_SORT_PAGE = "sort_page";
    protected static final String ARG_TITLE_SIZE = "title size";
    protected PageFragmentCallbacks callbacks;
    protected String key;
    protected List<String> choices;
    protected Page page;

    @LayoutRes
    protected int layoutRes;
    protected int choiceMode;

    public BaseChoiceFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getParentFragment();
        } else if (getActivity() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getActivity();
        } else {
            throw new ClassCastException("Activity/ParentFragment must implement PageFragmentCallbacks");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupChoices();
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        setTitle(view);

        setListTypeArgs();
        setListView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preSelectItems();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    protected void setupChoices() {
        page = callbacks.onGetPage(key);

        SingleFixedChoicePage fixedChoicePage = (SingleFixedChoicePage) page;
        choices = new ArrayList<>();
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            choices.add(fixedChoicePage.getOptionAt(i));
        }

        if (getArguments().getBoolean(ARG_SORT_PAGE, true)) {
            Collections.sort(choices);
        }
    }

    protected void setTitle(View view) {
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());

        if (getArguments().getInt(SingleChoiceFragment.ARG_TITLE_SIZE, -1) != -1) {
            TextView title = (TextView) view.findViewById(android.R.id.title);
            TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
            title.setPadding(0, 0, 0, 0);
        }
    }

    protected void setListView(View view) {
        final ListView listView = (ListView) view.findViewById(android.R.id.list);
        setAdapter();
        listView.setDividerHeight(0);
        listView.setChoiceMode(choiceMode);
    }

    protected void setAdapter() {
        setListAdapter(new ArrayAdapter<>(getContext(),
                layoutRes,
                android.R.id.text1,
                choices));
    }

    protected abstract void preSelectItems();

    protected abstract void setListTypeArgs();
}
