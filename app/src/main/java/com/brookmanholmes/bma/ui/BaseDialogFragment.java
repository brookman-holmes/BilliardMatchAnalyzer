package com.brookmanholmes.bma.ui;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Brookman Holmes on 12/28/2016.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    @ColorInt
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }
}
