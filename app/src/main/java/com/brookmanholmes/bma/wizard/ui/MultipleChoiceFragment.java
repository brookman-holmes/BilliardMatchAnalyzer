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
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceFragment extends BaseChoiceFragment {
    ArrayAdapter<String> adapter;

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

    @Override
    public void onResume() {
        super.onResume();
        // retarded work around for weird bug where items get selected randomly?
        // Pre-select currently selected items.
        ArrayList<String> selectedItems = page.getData().getStringArrayList(Page.SIMPLE_DATA_KEY);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // deselect items that conflict with this selection
        for (String item : getOpposingItems(adapter.getItem(position))) {
            int opposingItemPosition = adapter.getPosition(item);
            if (opposingItemPosition != -1)
                getListView().setItemChecked(opposingItemPosition, false);
        }

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

    @Override
    protected void preSelectItems() {
        // Pre-select currently selected items.
        ArrayList<String> selectedItems = page.getData().getStringArrayList(Page.SIMPLE_DATA_KEY);
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

    @Override
    protected void setListTypeArgs() {
        layoutRes = android.R.layout.simple_list_item_multiple_choice;
        choiceMode = ListView.CHOICE_MODE_MULTIPLE;
    }

    @Override
    protected void setAdapter() {
        adapter = new ArrayAdapter<>(
                getContext(),
                layoutRes,
                android.R.id.text1,
                choices);
        setListAdapter(adapter);
    }

    private List<String> getOpposingItems(String item) {
        List<String> list = new ArrayList<>();
        if (item.equals(getString(R.string.too_hard))) {
            list.add(getString(R.string.too_soft));
        } else if (item.equals(getString(R.string.too_soft))) {
            list.add(getString(R.string.too_hard));
        } else if (item.equals(getString(R.string.aim_to_left))) {
            list.add(getString(R.string.aim_to_right));
        } else if (item.equals(getString(R.string.aim_to_right))) {
            list.add(getString(R.string.aim_to_left));
        } else if (item.equals(getString(R.string.thick_hit))) {
            list.add(getString(R.string.thin_hit));
        } else if (item.equals(getString(R.string.thin_hit))) {
            list.add(getString(R.string.thick_hit));
        } else if (item.equals(getString(R.string.bank_short))) {
            list.add(getString(R.string.bank_long));
        } else if (item.equals(getString(R.string.bank_long))) {
            list.add(getString(R.string.bank_short));
        } else if (item.equals(getString(R.string.kick_short))) {
            list.add(getString(R.string.kick_long));
        } else if (item.equals(getString(R.string.kick_long))) {
            list.add(getString(R.string.kick_short));
        } else if (item.equals(getString(R.string.curve_early))) {
            list.add(getString(R.string.curve_late));
        } else if (item.equals(getString(R.string.curve_late))) {
            list.add(getString(R.string.curve_early));
        } else if (item.equals(getString(R.string.why_slow))) {
            list.add(getString(R.string.why_fast));
        } else if (item.equals(getString(R.string.why_fast))) {
            list.add(getString(R.string.why_slow));
        } else if (item.equals(getString(R.string.why_too_much_draw))) {
            list.add(getString(R.string.why_too_much_follow));
            list.add(getString(R.string.why_too_little_follow));
            list.add(getString(R.string.why_too_little_draw));
        } else if (item.equals(getString(R.string.why_too_little_draw))) {
            list.add(getString(R.string.why_too_much_follow));
            list.add(getString(R.string.why_too_much_draw));
            list.add(getString(R.string.why_too_little_follow));
        } else if (item.equals(getString(R.string.why_too_much_follow))) {
            list.add(getString(R.string.why_too_much_draw));
            list.add(getString(R.string.why_too_little_draw));
            list.add(getString(R.string.why_too_little_follow));
        } else if (item.equals(getString(R.string.why_too_little_follow))) {
            list.add(getString(R.string.why_too_much_draw));
            list.add(getString(R.string.why_too_little_draw));
            list.add(getString(R.string.why_too_much_follow));
        } else if (item.equals(getString(R.string.why_too_much_inside))) {
            list.add(getString(R.string.why_too_much_outside));
            list.add(getString(R.string.why_too_little_outside));
            list.add(getString(R.string.why_too_little_inside));
        } else if (item.equals(getString(R.string.why_too_little_inside))) {
            list.add(getString(R.string.why_too_much_outside));
            list.add(getString(R.string.why_too_little_outside));
            list.add(getString(R.string.why_too_much_inside));
        } else if (item.equals(getString(R.string.why_too_much_outside))) {
            list.add(getString(R.string.why_too_much_inside));
            list.add(getString(R.string.why_too_little_inside));
            list.add(getString(R.string.why_too_little_outside));
        } else if (item.equals(getString(R.string.why_too_little_outside))) {
            list.add(getString(R.string.why_too_much_inside));
            list.add(getString(R.string.why_too_much_outside));
            list.add(getString(R.string.why_too_little_inside));
        }
        return list;
    }
}