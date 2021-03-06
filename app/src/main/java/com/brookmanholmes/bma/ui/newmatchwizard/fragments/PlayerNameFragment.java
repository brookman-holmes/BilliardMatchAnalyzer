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

package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerNameFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "PlayerNameFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    @Bind(R.id.playerName)
    MaterialAutoCompleteTextView playerName;
    @Bind(R.id.opponentName)
    MaterialAutoCompleteTextView opponentName;
    @Bind(R.id.location)
    MaterialEditText location;
    @Bind(R.id.extra)
    MaterialEditText extra;
    @Bind(R.id.cbGhost)
    CheckBox playTheGhost;

    private PageFragmentCallbacks callbacks;
    private String key;
    private PlayerNamePage page;
    private List<String> names;

    public PlayerNameFragment() {
    }

    public static PlayerNameFragment create(String key, @Nullable String playerName) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        if (playerName != null) {
            args.putString(ARG_PLAYER_NAME, playerName);
        }

        PlayerNameFragment fragment = new PlayerNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);

        DatabaseAdapter database = new DatabaseAdapter(getContext());

        names = database.getPlayerNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = (PlayerNamePage) callbacks.onGetPage(key);
        View rootView = inflater.inflate(R.layout.fragment_player_names, container, false);
        ButterKnife.bind(this, rootView);

        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.select_dialog_item, names);

        playerName.setAdapter(autoCompleteAdapter);
        playerName.setText(page.getData().getString(PlayerNamePage.PLAYER_NAME_KEY, getArguments().getString(ARG_PLAYER_NAME, "")));

        opponentName.setAdapter(autoCompleteAdapter);
        opponentName.setText(page.getData().getString(PlayerNamePage.OPPONENT_NAME_KEY));

        playTheGhost.setOnCheckedChangeListener(this);

        playTheGhost.setChecked(Boolean.parseBoolean(page.getData().getString(PlayerNamePage.SIMPLE_DATA_KEY)));

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        page.setPlayTheGhost(isChecked);
        opponentName.setEnabled(!isChecked);

        if (isChecked)
            opponentName.setText(R.string.ghost_name);
        else
            opponentName.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerName.addTextChangedListener(textWatcher(PlayerNamePage.PLAYER_NAME_KEY));
        opponentName.addTextChangedListener(textWatcher(PlayerNamePage.OPPONENT_NAME_KEY));
        location.addTextChangedListener(textWatcher(PlayerNamePage.LOCATION_KEY));
        extra.addTextChangedListener(textWatcher(PlayerNamePage.EXTRA_INFO_KEY));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!getArguments().getString(ARG_PLAYER_NAME, "").isEmpty()) {
            opponentName.setFocusableInTouchMode(true);
            opponentName.requestFocus();
        } else {
            playerName.setFocusableInTouchMode(true);
            playerName.requestFocus();
        }
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
                if (key.equals(PlayerNamePage.PLAYER_NAME_KEY)) {
                    if (TextUtils.equals(editable.toString(), opponentName.getText().toString())) {
                        playerName.setError(getString(R.string.error_same_name));
                    }
                } else if (key.equals(PlayerNamePage.OPPONENT_NAME_KEY)) {
                    if (TextUtils.equals(editable.toString(), playerName.getText().toString())) {
                        opponentName.setError(getString(R.string.error_same_name));
                    }
                }

                page.getData().putString(key, editable.toString());
                page.notifyDataChanged();
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
                if (getView() != null)
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
