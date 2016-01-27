package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectTurnEndDialog extends Fragment {
    static final String ARG_KEY = "key";
    @Bind(R.id.title)
    TextView title;

    public static SelectTurnEndDialog create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString("title", "How Brookman's turn ended");

        SelectTurnEndDialog fragment = new SelectTurnEndDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_turn_end_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText(getArguments().getString("title", "--"));
        return view;
    }
}
