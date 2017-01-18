package com.brookmanholmes.bma.wizard.ui;

import com.brookmanholmes.bma.wizard.model.FragmentDependentPage;

/**
 * Created by Brookman Holmes on 1/13/2017.
 */

public abstract class BaseFragmentDependentPageFragment<T extends FragmentDependentPage>
        extends BasePageFragment<T> {

    @Override
    public void onResume() {
        super.onResume();
        page.registerFragment(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        page.unregisterFragment();
    }
}
