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

import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;
import com.brookmanholmes.bma.wizard.model.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MultipleChoiceFragment extends BaseChoiceFragment {
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

    public static MultipleChoiceFragment create(String key, boolean sortPage) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putBoolean(ARG_SORT_PAGE, sortPage);

        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override protected void preSelectItems() {
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
                        getListView().setItemChecked(i, true);
                    }
                }
            }
        });
    }

    @Override protected void setListTypeArgs() {
        layoutRes = android.R.layout.simple_list_item_multiple_choice;
        choiceMode = ListView.CHOICE_MODE_MULTIPLE;
    }
}