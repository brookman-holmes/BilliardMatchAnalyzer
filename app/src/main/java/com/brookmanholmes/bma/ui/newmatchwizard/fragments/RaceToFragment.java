package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.FoulPage;
import com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
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
    private static final String ARG_COLUMNS = "column_count";
    private static final String ARG_PLAYER_INDEX = "player_index";
    private static final String ARG_OPP_INDEX = "opp_index";

    private PageFragmentCallbacks callbacks;
    private String key;
    private RaceToPage page;

    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.playerRankGrid) GridLayout playerGrid;
    @Bind(R.id.opponentRankGrid) GridLayout opponentGrid;
    RaceController playerController, opponentController;

    public RaceToFragment() {
    }

    public static RaceToFragment create(String key, int lowerBound, int upperBound, int defaultChoice, int columns) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_DEFAULT_CHOICE_KEY, defaultChoice);
        args.putInt(ARG_LOWER_BOUND_KEY, lowerBound);
        args.putInt(ARG_UPPER_BOUND_KEY, upperBound);
        args.putInt(ARG_COLUMNS, columns);

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
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = (RaceToPage) callbacks.onGetPage(key);
        int min = getArguments().getInt(ARG_LOWER_BOUND_KEY);
        int max = getArguments().getInt(ARG_UPPER_BOUND_KEY);
        int choice = getArguments().getInt(ARG_DEFAULT_CHOICE_KEY);
        int columns = getArguments().getInt(ARG_COLUMNS);

        View view = inflater.inflate(R.layout.fragment_race_page, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        playerController = new RaceController(playerGrid, min, max, columns, page.getData().getInt(ARG_PLAYER_INDEX, choice));
        opponentController = new RaceController(opponentGrid, min, max, columns, page.getData().getInt(ARG_OPP_INDEX, choice));
        updatePage();
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

    public void setPlayerNames(String player, String opponent) {
        if (page.getTitle().equals(getString(R.string.ranks))) {
            playerName.setText(getString(R.string.apa_ranks, player));
            opponentName.setText(getString(R.string.apa_ranks, opponent));
        } else {
            playerName.setText(getString(R.string.race_to, player));
            opponentName.setText(getString(R.string.race_to, opponent));
        }
    }

    private void updatePage() {
        page.getData().putInt(ARG_PLAYER_INDEX, playerController.getIndex());
        page.getData().putInt(ARG_OPP_INDEX, opponentController.getIndex());
        page.getData().putInt(RaceToPage.PLAYER_RANK_KEY, playerController.getSelection());
        page.getData().putInt(RaceToPage.OPPONENT_RANK_KEY, opponentController.getSelection());
    }

    private class RaceController implements View.OnClickListener {
        private GridLayout grid;
        private int selection;

        public RaceController(GridLayout grid, int min, int max, int columns, int selection) {
            this.grid = grid;
            grid.setColumnCount(columns);

            for (int i = min; i < max; i++) {
                grid.addView(createTextView(i));
            }

            selectView(selection);
        }

        private TextView createTextView(int num) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_selection, null);
            textView.setHeight((int)ConversionUtils.convertDpToPx(getContext(), 30));
            textView.setWidth((int)ConversionUtils.convertDpToPx(getContext(), 30));
            textView.setText(String.valueOf(num));
            textView.setOnClickListener(this);
            textView.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.white));
            return textView;
        }

        @Override public void onClick(View view) {
            selectView(grid.indexOfChild(view), selection);

            updatePage();
        }

        private void deselectView(int selection) {
            ((TextView)grid.getChildAt(selection)).getBackground().setTint(ContextCompat.getColor(getContext(), R.color.white));
            ((TextView)grid.getChildAt(selection)).setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text));
        }

        private void selectView(int selection, int oldSelection) {
            selectView(selection);
            deselectView(oldSelection);
        }

        private void selectView(int selection) {
            ((TextView)grid.getChildAt(selection)).getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            ((TextView)grid.getChildAt(selection)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            this.selection = selection;
        }

        private int getSelection() {
            return Integer.valueOf(((TextView)grid.getChildAt(selection)).getText().toString());
        }

        private int getIndex() {
            return selection;
        }
    }
}
