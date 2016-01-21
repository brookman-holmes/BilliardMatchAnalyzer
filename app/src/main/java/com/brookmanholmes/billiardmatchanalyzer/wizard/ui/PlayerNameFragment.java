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

package com.brookmanholmes.billiardmatchanalyzer.wizard.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PlayerNamePage;

import java.util.List;

public class PlayerNameFragment extends Fragment {
    private static final String ARG_KEY = "key";
    final long DELAY = 200;
    private long lastKeyPress;
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private PlayerNamePage mPage;
    private AutoCompleteTextView playerName, opponentName;
    private List<String> names;

    public PlayerNameFragment() {
    }

    public static PlayerNameFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        PlayerNameFragment fragment = new PlayerNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (PlayerNamePage) mCallbacks.onGetPage(mKey);
        DatabaseAdapter database = new DatabaseAdapter(getContext());
        database.open();

        names = database.getNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_names, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.select_dialog_item, names);

        playerName = ((AutoCompleteTextView) rootView.findViewById(R.id.playerName));
        playerName.setAdapter(autoCompleteAdapter);
        playerName.setText(mPage.getData().getString(PlayerNamePage.PLAYER_NAME_KEY));

        opponentName = ((AutoCompleteTextView) rootView.findViewById(R.id.opponentName));
        opponentName.setAdapter(autoCompleteAdapter);
        opponentName.setText(mPage.getData().getString(PlayerNamePage.OPPONENT_NAME_KEY));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerName.addTextChangedListener(textWatcher(PlayerNamePage.PLAYER_NAME_KEY));
        opponentName.addTextChangedListener(textWatcher(PlayerNamePage.OPPONENT_NAME_KEY));
    }

    private TextWatcher textWatcher(final String key) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                // // TODO: 1/7/2016 add in a debounce here to prevent multiple firings for no reason
                if (key.equals(PlayerNamePage.PLAYER_NAME_KEY)) {
                    if (TextUtils.equals(editable.toString(), opponentName.getText().toString())) {
                        playerName.setError("Players cannot have the same name");
                    }
                } else {
                    if (TextUtils.equals(editable.toString(), playerName.getText().toString())) {
                        opponentName.setError("Players cannot have the same name");
                    }
                }


                mPage.getData().putString(key, editable.toString());
                mPage.notifyDataChanged();
            }
        };
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (playerName != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
