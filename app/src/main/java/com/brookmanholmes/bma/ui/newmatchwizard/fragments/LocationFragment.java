package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.newmatchwizard.model.LocationPage;
import com.brookmanholmes.bma.wizard.ui.BasePageFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by Brookman Holmes on 3/7/2017.
 */

public class LocationFragment extends BasePageFragment<LocationPage> {
    @Bind(R.id.location)
    MaterialEditText location;
    @Bind(R.id.notes)
    MaterialEditText notes;

    public static LocationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = (LocationPage) callbacks.onGetPage(key);
        View view = inflater.inflate(R.layout.fragment_page_location, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        return view;
    }

    @OnTextChanged(R.id.location)
    void onLocationChanged() {
        page.getData().putString("location", location.getText().toString());
        page.notifyDataChanged();
    }

    @OnTextChanged(R.id.notes)
    void onNotesChanged() {
        page.getData().putString("notes", notes.getText().toString());
        page.notifyDataChanged();
    }
}
