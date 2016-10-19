package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage.OPPONENT_RANK_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage.PLAYER_RANK_KEY;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class RaceToFragment extends BaseFragment {
    private static final String TAG = "RaceToFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_LOWER_BOUND_KEY = "lower_bound";
    private static final String ARG_UPPER_BOUND_KEY = "upper_bound";
    private static final String ARG_DEFAULT_CHOICE_KEY = "default_choice";
    private static final String ARG_COLUMNS = "column_count";
    private static final String ARG_PLAYER_INDEX = "player_index";
    private static final String ARG_OPP_INDEX = "opp_index";
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.playerRankGrid)
    GridLayout playerGrid;
    @Bind(R.id.opponentRankGrid)
    GridLayout opponentGrid;
    RaceController playerController, opponentController;
    private PageFragmentCallbacks callbacks;
    private String key;
    private RaceToPage page;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getActivity();
        } else {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
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
        page = (RaceToPage) callbacks.onGetPage(key);
        int min = getArguments().getInt(ARG_LOWER_BOUND_KEY);
        int max = getArguments().getInt(ARG_UPPER_BOUND_KEY);
        int choice = getArguments().getInt(ARG_DEFAULT_CHOICE_KEY);
        int columns = getArguments().getInt(ARG_COLUMNS);

        View view = inflater.inflate(R.layout.fragment_race_page, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        setPlayerNames(page.getPlayerName(), page.getOpponentName());

        playerController = new RaceController(playerGrid, min, max, columns, page.getData().getInt(ARG_PLAYER_INDEX, choice));
        opponentController = new RaceController(opponentGrid, min, max, columns, page.getData().getInt(ARG_OPP_INDEX, choice));
        updatePage();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override
    public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override
    public void onDetach() {
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
        page.getData().putInt(PLAYER_RANK_KEY, playerController.getSelection());
        page.getData().putInt(OPPONENT_RANK_KEY, opponentController.getSelection());
        page.notifyDataChanged();
    }

    private class RaceController implements View.OnClickListener {
        private GridLayout grid;
        private int selection;
        private Drawable selectedBackground;
        private Drawable unselectedBackground;

        RaceController(GridLayout grid, int min, int max, int columns, int selection) {
            this.grid = grid;
            unselectedBackground = ContextCompat.getDrawable(grid.getContext(), R.drawable.white_circle);
            selectedBackground = ContextCompat.getDrawable(grid.getContext(), R.drawable.blue_circle);

            grid.setColumnCount(columns);

            for (int i = min; i < max; i++) {
                grid.addView(createTextView(i));
            }

            selectView(selection);
        }

        private TextView createTextView(int num) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_selection, null);
            textView.setHeight((int) ConversionUtils.convertDpToPx(getContext(), 30));
            textView.setWidth((int) ConversionUtils.convertDpToPx(getContext(), 30));
            textView.setText(String.valueOf(num));
            textView.setOnClickListener(this);
            textView.setBackground(unselectedBackground);
            return textView;
        }

        @Override
        public void onClick(View view) {
            selectView(grid.indexOfChild(view), selection);

            updatePage();
        }

        private void selectView(int selection, int oldSelection) {
            selectView(selection);
            deselectView(oldSelection);
        }

        private void deselectView(int selection) {
            grid.getChildAt(selection).setBackground(unselectedBackground);
            ((TextView) grid.getChildAt(selection)).setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text));
        }

        private void selectView(int selection) {
            grid.getChildAt(selection).setBackground(selectedBackground);
            ((TextView) grid.getChildAt(selection)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            this.selection = selection;
        }

        private int getSelection() {
            return Integer.valueOf(((TextView) grid.getChildAt(selection)).getText().toString());
        }

        private int getIndex() {
            return selection;
        }
    }
}
