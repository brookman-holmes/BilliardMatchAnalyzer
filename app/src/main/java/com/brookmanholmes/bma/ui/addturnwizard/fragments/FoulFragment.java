package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.FoulPage;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;

/**
 * Created by Brookman Holmes on 3/9/2016.
 */
public class FoulFragment extends ListFragment {
    private static final String ARG_KEY = "key";
    ListView listView;
    private PageFragmentCallbacks callbacks;
    private String key;
    private FoulPage page;
    private CustomAdapter adapter;

    public FoulFragment() {
    }

    public static FoulFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FoulFragment fragment = new FoulFragment();
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

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Override public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        page = (FoulPage) callbacks.onGetPage(key);

        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());
        TextView title = (TextView) rootView.findViewById(android.R.id.title);
        TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
        title.setPadding(0, 0, 0, 0);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        adapter = new CustomAdapter(getActivity(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                new ArrayList<String>());
        setListAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return rootView;
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
        updatePage(position);
    }

    public void updateOptions(TurnEndOptions turnEndOptions) {
        String defaultChecked;
        adapter.clear();
        if (turnEndOptions.lostGame) {
            if (GameType.valueOf(page.getData().getString(GAME_TYPE_KEY)) == GameType.BCA_EIGHT_BALL) {
                adapter.add(getString(R.string.foul_lost_game));
                defaultChecked = getString(R.string.foul_lost_game);
            } else {
                adapter.add(getString(R.string.foul_lost_game));
                adapter.add(getString(R.string.yes));
                defaultChecked = getString(R.string.yes);
            }

            if (turnEndOptions.possibleEndings.contains(TurnEnd.SAFETY))
                adapter.add(getString(R.string.no));
        } else if (turnEndOptions.foul) {
            adapter.add(getString(R.string.yes));
            defaultChecked = getString(R.string.yes);
        } else {
            adapter.add(getString(R.string.yes));
            adapter.add(getString(R.string.no));
            defaultChecked = getString(R.string.no);
        }

        adapter.notifyDataSetChanged();

        if (adapter.contains(getFoulFromPage()))
            listView.setItemChecked(adapter.getPosition(getFoulFromPage()), true);
        else {
            listView.setItemChecked(adapter.getPosition(defaultChecked), true);
            updatePage(listView.getCheckedItemPosition());
        }
    }

    private void updatePage(int position) {
        if (!getFoulFromPage().equals(adapter.getItem(position))) {
            page.getData().putString(Page.SIMPLE_DATA_KEY, adapter.getItem(position));
            page.notifyDataChanged();
        }
    }

    private String getFoulFromPage() {
        return page.getData().getString(Page.SIMPLE_DATA_KEY, "");
    }

    private static class CustomAdapter extends ArrayAdapter<String> {
        List<String> objects;
        public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
            this.objects = objects;
        }

        boolean contains(String item) {
            return objects.contains(item);
        }
    }
}
