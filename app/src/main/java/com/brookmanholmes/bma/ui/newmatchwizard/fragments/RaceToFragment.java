package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.FoulPage;
import com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class RaceToFragment extends Fragment {
    private static final String ARG_KEY = "key";
    private static final String ARG_LOWER_BOUND_KEY = "lower_bound";
    private static final String ARG_UPPER_BOUND_KEY = "upper_bound";
    private static final String ARG_DEFAULT_CHOICE_KEY = "default_choice";
    private PageFragmentCallbacks callbacks;
    private String key;
    private RaceToPage page;

    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.playerRankSpinner) Spinner playerRank;
    @Bind(R.id.opponentRankSpinner) Spinner opponentRank;
    ArrayAdapter<String> playerAdapter, opponentAdapter;
    public RaceToFragment() {
    }

    public static RaceToFragment create(String key, int lowerBound, int upperBound, int defaultChoice) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_DEFAULT_CHOICE_KEY, defaultChoice);
        args.putInt(ARG_LOWER_BOUND_KEY, lowerBound);
        args.putInt(ARG_UPPER_BOUND_KEY, upperBound);

        RaceToFragment fragment = new RaceToFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getActivity();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);

        playerAdapter = createAdapter();
        opponentAdapter = createAdapter();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = (RaceToPage) callbacks.onGetPage(key);

        View view = inflater.inflate(R.layout.fragment_race_page, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        playerRank.setAdapter(playerAdapter);
        opponentRank.setAdapter(opponentAdapter);

        playerRank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                page.getData().putInt(RaceToPage.PLAYER_RANK_KEY, Integer.valueOf((String)playerRank.getSelectedItem()));
                page.notifyDataChanged();
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        opponentRank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                page.getData().putInt(RaceToPage.OPPONENT_RANK_KEY, Integer.valueOf((String)opponentRank.getSelectedItem()));
                page.notifyDataChanged();
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        playerRank.setSelection(playerAdapter.getPosition(String.valueOf(getArguments().getInt(ARG_DEFAULT_CHOICE_KEY))));
        opponentRank.setSelection(opponentAdapter.getPosition(String.valueOf(getArguments().getInt(ARG_DEFAULT_CHOICE_KEY))));
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override public void onPause() {
        page.unregisterListener();
        super.onPause();
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

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private ArrayAdapter<String> createAdapter() {
        int size = getArguments().getInt(ARG_UPPER_BOUND_KEY) - getArguments().getInt(ARG_LOWER_BOUND_KEY);
        String[] data = new String[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = String.valueOf(getArguments().getInt(ARG_LOWER_BOUND_KEY) + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    public void setPlayerNames(String player, String opponent) {
        if (page.getTitle().equals(getString(R.string.ranks))) {
            playerName.setText(getString(R.string.apa_ranks, player));
            opponentName.setText(getString(R.string.apa_ranks, opponent));
        } else {
            playerName.setText(getString(R.string.race_to, player));
            opponentName.setText(getString(R.string.race_to, opponent));
        }
    }

}
