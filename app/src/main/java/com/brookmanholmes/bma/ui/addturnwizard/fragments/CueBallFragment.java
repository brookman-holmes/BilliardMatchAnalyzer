package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.content.Context;
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
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.addturnwizard.model.CueBallPage;
import com.brookmanholmes.bma.ui.view.CueBallHitView;
import com.brookmanholmes.bma.ui.view.OnCueBallTouched;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 9/27/2016.
 */
public class CueBallFragment extends BaseFragment implements RangeBar.OnRangeBarTextListener,
        RangeBar.OnRangeBarChangeListener {
    private static final String TAG = "CueBallFragment";
    private static final String ARG_KEY = "key";
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
    private String key;
    private Page page;
    private PageFragmentCallbacks callbacks;

    public static CueBallFragment create(String key) {
        CueBallFragment fragment = new CueBallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        } else
            callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = callbacks.onGetPage(key);
        View view = inflater.inflate(R.layout.fragment_cue_ball, container, false);
        ButterKnife.bind(this, view);

        TextView title = (TextView) view.findViewById(android.R.id.title);
        TextViewCompat.setTextAppearance(title, R.style.WizardPageTitle2);
        title.setPadding(0, 0, 0, 0);
        title.setText(page.getTitle());

        speedRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));
        cbRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));
        obRange.setBarWeight(ConversionUtils.convertDpToPx(getContext(), 2));

        cbRange.setPinTextListener(this);
        obRange.setPinTextListener(this);

        obRange.setOnRangeBarChangeListener(this);
        cbRange.setOnRangeBarChangeListener(this);
        speedRange.setOnRangeBarChangeListener(this);

        obRange.setSeekPinByIndex(getIndexFromPinValue(page.getData().getFloat(CueBallPage.OB_DISTANCE_KEY, 4f)));
        cbRange.setSeekPinByIndex(getIndexFromPinValue(page.getData().getFloat(CueBallPage.CB_DISTANCE_KEY, 4f)));
        speedRange.setSeekPinByIndex(page.getData().getInt(CueBallPage.SPEED_KEY, 5));


        hit.addOnCueBueTouchedListener(new OnCueBallTouched() {
            @Override
            public void onTouch(int x, int y) {
                page.getData().putInt(CueBallPage.CB_X_KEY, x);
                page.getData().putInt(CueBallPage.CB_Y_KEY, y);
                page.notifyDataChanged();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callbacks = null;
    }

    @Override
    public String getPinValue(RangeBar rangeBar, int tickIndex) {
        switch (tickIndex) {
            case 0:
                return "6\"";
            case 1:
                return "1'";
            case 2:
                return "2'";
            case 3:
                return "3'";
            case 4:
                return "4'";
            case 5:
                return "5'";
            case 6:
                return "6'";
            case 7:
                return "7'";
            case 8:
                return "8'";
            default:
                return "9'";
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
            page.notifyDataChanged();
        } else if (rangeBar.getId() == cbRange.getId()) {
            textView = cbText;
            stringToFormat = getString(R.string.html_cue_object_ball);
            page.getData().putFloat(CueBallPage.CB_DISTANCE_KEY, getPinValueFromIndex(rightPinIndex));
            page.notifyDataChanged();
        } else {
            textView = speedText;
            stringToFormat = getString(R.string.html_speed_range);
            page.getData().putInt(CueBallPage.SPEED_KEY, rightPinIndex);
            page.notifyDataChanged();
        }
        textView.setText(Html.fromHtml(String.format(Locale.getDefault(), stringToFormat, rightPinValue)));
    }

    private float getPinValueFromIndex(int index) {
        if (index == 0)
            return .5f;
        else
            return index;
    }

    private int getIndexFromPinValue(float value) {
        if (value == .5f)
            return 0;
        else return (int) value;
    }
}
