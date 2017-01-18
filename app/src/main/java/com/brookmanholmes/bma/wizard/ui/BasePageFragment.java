package com.brookmanholmes.bma.wizard.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.wizard.model.Page;

/**
 * Created by Brookman Holmes on 1/13/2017.
 */

public abstract class BasePageFragment<T extends Page> extends Fragment {
    private static final String ARG_KEY = "key";
    protected PageFragmentCallbacks callbacks;
    protected T page;
    protected String key;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getParentFragment();
        } else if (getActivity() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getActivity();
        } else {
            throw new ClassCastException("Activity/ParentFragment must implement PageFragmentCallbacks");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
}
