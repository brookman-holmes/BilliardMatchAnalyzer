package com.brookmanholmes.bma.ui;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/31/2016.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @ColorInt
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }
}
