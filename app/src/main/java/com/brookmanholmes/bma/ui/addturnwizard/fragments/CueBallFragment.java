package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.CueBallPage;
import com.brookmanholmes.bma.ui.view.CueBallHitView;
import com.brookmanholmes.bma.ui.view.OnCueBallTouched;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.wizard.ui.BasePageFragment;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 9/27/2016.
 */
public class CueBallFragment extends BasePageFragment<CueBallPage> implements RangeBar.OnRangeBarTextListener,
        RangeBar.OnRangeBarChangeListener {
    private static final String TAG = "CueBallFragment";
    private static final String ARG_KEY = "key";
    @Bind(R.id.distanceLayout)
    ViewGroup distanceLayout;
    @Bind(R.id.speedLayout)
    ViewGroup speedLayout;
    @Bind(R.id.cueLayout)
    ViewGroup cueLayout;
    @Bind(R.id.distanceDivider)
    View distanceDivider;
    @Bind(R.id.cueDivider)
    View cueDivider;
    @Bind(R.id.rangeCb)
    RangeBar cbRange;
    @Bind(R.id.rangeOb)
    RangeBar obRange;
    @Bind(R.id.cbText)
    TextView cbText;
    @Bind(R.id.obText)
    TextView obText;
    @Bind(R.id.rangeSpeed)
    RangeBar speedRange;
    @Bind(R.id.speedText)
    TextView speedText;
    @Bind(R.id.hit)
    CueBallHitView hit;

    public static CueBallFragment create(String key) {
        CueBallFragment fragment = new CueBallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = (CueBallPage) callbacks.onGetPage(key);
        View view = inflater.inflate(R.layout.fragment_cue_ball, container, false);
        ButterKnife.bind(this, view);

        TextView title = (TextView) view.findViewById(android.R.id.title);
        TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
        title.setPadding(0, 0, 0, 0);
        title.setText(page.getTitle());

        if (page.isSpeed()) {
            speedRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));
            speedRange.setOnRangeBarChangeListener(this);
            speedRange.setSeekPinByIndex(page.getData().getInt(CueBallPage.SPEED_KEY, 4));
        } else {
            speedLayout.setVisibility(View.GONE);
        }

        if (page.isDistances()) {
            cbRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));
            obRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));
            cbRange.setPinTextListener(this);
            obRange.setPinTextListener(this);
            obRange.setOnRangeBarChangeListener(this);
            cbRange.setOnRangeBarChangeListener(this);
            obRange.setSeekPinByIndex(getIndexFromPinValue(page.getData().getFloat(CueBallPage.OB_DISTANCE_KEY, 8f)));
            cbRange.setSeekPinByIndex(getIndexFromPinValue(page.getData().getFloat(CueBallPage.CB_DISTANCE_KEY, 8f)));
        } else {
            distanceLayout.setVisibility(View.GONE);
        }

        if (page.isCueing()) {
            hit.addOnCueBueTouchedListener(new OnCueBallTouched() {
                @Override
                public void onTouch(int x, int y) {
                    page.getData().putInt(CueBallPage.CB_X_KEY, x);
                    page.getData().putInt(CueBallPage.CB_Y_KEY, y);
                    page.notifyDataChanged();
                }
            });
        } else {
            cueLayout.setVisibility(View.GONE);
        }

        if (!page.isCueing() && !page.isSpeed())
            distanceDivider.setVisibility(View.GONE);

        if (!page.isSpeed())
            cueDivider.setVisibility(View.GONE);

        return view;
    }

    @Override
    public String getPinValue(RangeBar rangeBar, int tickIndex) {
        DecimalFormat formatter = new DecimalFormat("#.#");
        switch (tickIndex) {
            case 0:
                return "<.5'";
            default:
                return formatter.format(((float) tickIndex / 2)) + "'";
        }
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex,
                                      String leftPinValue, String rightPinValue) {
        TextView textView;
        String stringToFormat;

        if (rangeBar.getId() == obRange.getId()) {
            textView = obText;
            stringToFormat = getString(R.string.html_object_pocket);
            page.getData().putFloat(CueBallPage.OB_DISTANCE_KEY, getPinValueFromIndex(rightPinIndex));
        } else if (rangeBar.getId() == cbRange.getId()) {
            textView = cbText;
            stringToFormat = getString(R.string.html_cue_object_ball);
            page.getData().putFloat(CueBallPage.CB_DISTANCE_KEY, getPinValueFromIndex(rightPinIndex));
        } else {
            textView = speedText;
            stringToFormat = getString(R.string.html_speed_range);
            page.getData().putInt(CueBallPage.SPEED_KEY, rightPinIndex + 1);
        }
        if (rightPinIndex == 0 && rangeBar.getId() != speedRange.getId()) // exclude the speed bar from using this
            textView.setText(Html.fromHtml(String.format(Locale.getDefault(), stringToFormat, "less than .5'"))); // TODO: 10/19/2016 change this to a string resource
        else
            textView.setText(Html.fromHtml(String.format(Locale.getDefault(), stringToFormat, rightPinValue)));

        if (isVisible()) // prevents a concurrent modification exception from being thrown
            page.notifyDataChanged();
    }

    private float getPinValueFromIndex(int index) {
        return ((float) index / 2);
    }

    private int getIndexFromPinValue(float value) {
        return (int) ((value - .5f));
    }
}
