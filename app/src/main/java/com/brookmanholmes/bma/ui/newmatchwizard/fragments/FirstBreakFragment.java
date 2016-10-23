package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.ui.SingleChoiceFragment;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 10/22/2016.
 */

public class FirstBreakFragment extends SingleChoiceFragment {
    private static final String TAG = "FirstBreakFragment";

    public FirstBreakFragment() {
    }

    public static FirstBreakFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FirstBreakFragment fragment = new FirstBreakFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_first_break_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.randomizeSelectionButton)
    public void randomSelection() {
        final Handler handler = new Handler();
        final int wait = 100;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectItem(0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectItem(1);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectItem(0);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectItem(1);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                selectItem(0);
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (new Random().nextBoolean()) {
                                                            selectItem(0);
                                                        } else {
                                                            selectItem(1);
                                                        }
                                                    }
                                                }, wait);
                                            }
                                        }, wait);
                                    }
                                }, wait);
                            }
                        }, wait);
                    }
                }, wait);
            }
        }, wait);


    }

    private void selectItem(final int item) {
        getListView().performItemClick(getListView().getChildAt(item), item, getListView().getItemIdAtPosition(item));
    }


}
