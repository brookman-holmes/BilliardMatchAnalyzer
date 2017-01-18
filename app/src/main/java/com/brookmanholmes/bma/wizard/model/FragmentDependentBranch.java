package com.brookmanholmes.bma.wizard.model;

import android.support.v4.app.Fragment;

/**
 * Created by Brookman Holmes on 1/13/2017.
 */

public abstract class FragmentDependentBranch<T extends Fragment> extends BranchPage implements UpdatesFragment<T> {
    protected T fragment;

    protected FragmentDependentBranch(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public void registerFragment(T fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    @Override
    public void unregisterFragment() {
        fragment = null;
    }
}
