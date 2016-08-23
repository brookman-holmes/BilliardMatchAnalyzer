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

public class SingleChoiceFragment extends BaseChoiceFragment {
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

    public static SingleChoiceFragment create(String key, int titleSize, boolean sortPage) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_TITLE_SIZE, titleSize);
        args.putBoolean(ARG_SORT_PAGE, sortPage);

        SingleChoiceFragment fragment = new SingleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SingleChoiceFragment create(String key, boolean sortPage) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putBoolean(ARG_SORT_PAGE, sortPage);

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

    @Override protected void preSelectItems() {
        // Pre-select currently selected item.
        new Handler().post(new Runnable() {
            @Override public void run() {
                String selection = page.getData().getString(Page.SIMPLE_DATA_KEY);
                for (int i = 0; i < choices.size(); i++) {
                    if (choices.get(i).equals(selection)) {
                        getListView().setItemChecked(i, true);
                        break;
                    }
                }
            }
        });
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        page.getData().putString(Page.SIMPLE_DATA_KEY,
                getListAdapter().getItem(position).toString());
        page.notifyDataChanged();
    }

    @Override protected void setListTypeArgs() {
        layoutRes = android.R.layout.simple_list_item_single_choice;
        choiceMode = ListView.CHOICE_MODE_SINGLE;
    }
}
