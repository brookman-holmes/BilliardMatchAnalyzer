package com.brookmanholmes.bma.wizard.model;

import android.support.v4.app.Fragment;

/**
 * Created by Brookman Holmes on 1/13/2017.
 */

public interface UpdatesFragment<T extends Fragment> {
    void registerFragment(T fragment);

    void unregisterFragment();

    void updateFragment();
}
