package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.TextViewCompat;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.TurnEndPage;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndFragment extends ListFragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "TurnEndFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_OPTIONS_KEY = "options";
    private static final String ARG_SELECTION_KEY = "selection";
    private ListView listView;
    private PageFragmentCallbacks callbacks;
    private String key;
    private TurnEndPage page;
    private CustomAdapter adapter;
    private LinearLayout foulLayout;
    private RadioGroup foulGroup;

    public TurnEndFragment() {
    }

    public static TurnEndFragment create(String key, ArrayList<String> options, String selection) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putStringArrayList(ARG_OPTIONS_KEY, options);
        args.putString(ARG_SELECTION_KEY, selection);
        TurnEndFragment fragment = new TurnEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getArguments().getString(ARG_KEY);

        adapter = new CustomAdapter(getContext(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                new ArrayList<String>());

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = (TurnEndPage) callbacks.onGetPage(key);
        List<String> options = getArguments().getStringArrayList(ARG_OPTIONS_KEY);
        if (options == null)
            throw new IllegalArgumentException("Must pass in a string of options");
        else {
            populateChoicesList(options);
        }
        View rootView = inflater.inflate(R.layout.fragment_page_turn_end, container, false);
        TextView title = (TextView) rootView.findViewById(android.R.id.title);
        TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
        title.setPadding(0, 0, 0, 0);
        foulLayout = (LinearLayout) rootView.findViewById(R.id.foulLayout);
        foulGroup = (RadioGroup) rootView.findViewById(R.id.foulGroup);
        foulGroup.setOnCheckedChangeListener(this);
        foulGroup.check(R.id.no);
        ((TextView)rootView.findViewById(R.id.subTitle)).setText(getString(R.string.title_foul, page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)));
        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());
        listView = (ListView) rootView.findViewById(android.R.id.list);

        setListAdapter(adapter);

        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(adapter.getPosition(getArguments().getString(ARG_SELECTION_KEY)), true);

        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
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

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        String selection = adapter.getItem(position);
        updateFoulLayout(selection);
        updatePage(position);
    }

    @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.yes)
            page.getData().putString(TurnEndPage.FOUL_KEY, getString(R.string.yes));
        else if (checkedId == R.id.no)
            page.getData().putString(TurnEndPage.FOUL_KEY, getString(R.string.no));
        else if (checkedId == R.id.lostGame)
            page.getData().putString(TurnEndPage.FOUL_KEY, getString(R.string.foul_lost_game));
    }

    private void populateChoicesList(List<String> options) {
        adapter.clear();
        for (String option : options) {
            TurnEnd ending = TurnEnd.valueOf(option);
            adapter.add(getString(ending));
        }

        adapter.sort();
        adapter.notifyDataSetChanged();
    }

    private void updateFoulLayout(String selection) {
        TransitionManager.beginDelayedTransition(foulLayout);
        if (page.isFoulPossible(selection)) {
            foulLayout.setVisibility(View.VISIBLE);
        } else {
            foulLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateFoulChoices(TurnEndOptions options) {
        if (options.lostGame) {
            if (GameType.valueOf(page.getData().getString(GAME_TYPE_KEY)) == GameType.BCA_EIGHT_BALL ||
                    GameType.valueOf(page.getData().getString(GAME_TYPE_KEY)) == GameType.APA_EIGHT_BALL) {
                foulGroup.findViewById(R.id.lostGame).setVisibility(View.VISIBLE);
                foulGroup.findViewById(R.id.no).setVisibility(View.GONE);
                foulGroup.findViewById(R.id.yes).setVisibility(View.GONE);
            } else {
                foulGroup.findViewById(R.id.lostGame).setVisibility(View.GONE);
                foulGroup.findViewById(R.id.no).setVisibility(View.GONE);
                foulGroup.findViewById(R.id.yes).setVisibility(View.VISIBLE);
            }

            if (options.possibleEndings.contains(TurnEnd.SAFETY))
                foulGroup.findViewById(R.id.no).setVisibility(View.VISIBLE);

        } else if (options.foul) {
            foulGroup.findViewById(R.id.lostGame).setVisibility(View.GONE);
            foulGroup.findViewById(R.id.no).setVisibility(View.GONE);
            foulGroup.findViewById(R.id.yes).setVisibility(View.VISIBLE);
        } else {
            foulGroup.findViewById(R.id.lostGame).setVisibility(View.GONE);
            foulGroup.findViewById(R.id.no).setVisibility(View.VISIBLE);
            foulGroup.findViewById(R.id.yes).setVisibility(View.VISIBLE);
        }

        updateFoulChoice();
    }

    private void updateFoulChoice() {
        boolean no = foulGroup.findViewById(R.id.no).getVisibility() == View.VISIBLE;
        boolean yes = foulGroup.findViewById(R.id.yes).getVisibility() == View.VISIBLE;
        boolean lostGame = foulGroup.findViewById(R.id.lostGame).getVisibility() == View.VISIBLE;

        if (!no && !yes) // if the only option is lost game check it
            foulGroup.check(R.id.lostGame);
        else if (!no && !lostGame) // if the only option is foul check it
            foulGroup.check(R.id.yes);
        else {
            if (getFoulFromPage().equals(getString(R.string.no)))
                foulGroup.check(R.id.no);
            else if (getFoulFromPage().equals(getString(R.string.yes)))
                foulGroup.check(R.id.yes);
            else
                foulGroup.check(R.id.lostGame);
        }
    }

    public void updateOptions(TurnEndOptions options) {
        repopulateChoicesList(options.possibleEndings);
        updateFoulChoices(options);

        if (adapter.contains(getTurnEndFromPage())) {
            listView.setItemChecked(adapter.indexOf(getTurnEndFromPage()), true);
        } else {
            listView.setItemChecked(adapter.indexOf(getString(options.defaultCheck)), true);
            updatePage(listView.getCheckedItemPosition());
        }

        updateFoulLayout(getTurnEndFromPage());
    }

    private void repopulateChoicesList(List<TurnEnd> options) {
        adapter.clear();
        for (TurnEnd ending : options) {
                adapter.add(getString(ending));
        }
        adapter.sort();
        adapter.notifyDataSetChanged();
    }

    private String getString(TurnEnd ending) {
        return MatchDialogHelperUtils.convertTurnEndToStringRes(getContext(),
                ending,
                page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY),
                page.getData().getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY));
    }

    private void updatePage(int position) {
        if (!page.getData().getString(Page.SIMPLE_DATA_KEY, "").equals(adapter.getItem(position))) {
            page.getData().putString(Page.SIMPLE_DATA_KEY, adapter.getItem(position));
            page.notifyDataChanged();
        }
    }

    private String getTurnEndFromPage() {
        return page.getData().getString(Page.SIMPLE_DATA_KEY, "");
    }

    private String getFoulFromPage() {
        return page.getData().getString(TurnEndPage.FOUL_KEY, getString(R.string.no));
    }

    private static class CustomAdapter extends ArrayAdapter<String> {
        final List<String> objects;
        public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
            this.objects = objects;
        }

        private boolean contains(String item) {
            return objects.contains(item);
        }

        private int indexOf(String item) {
            return objects.indexOf(item);
        }

        private void sort() {
            Collections.sort(objects);
        }
    }
}
