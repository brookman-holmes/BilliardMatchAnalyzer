package com.brookmanholmes.bma.ui;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.brookmanholmes.bma.MyApplication;
import com.squareup.leakcanary.RefWatcher;

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

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @ColorInt
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }
}
