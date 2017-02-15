package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.util.Pair;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

import java.util.Locale;

/**
 * Created by Brookman Holmes on 1/19/2017.
 */

public class HowMissLayout extends LinearLayout {
    private static final String TAG = "HowMissLayout";

    private TextView title, leftLabel, rightLabel, centerLabel;
    private TextView left, right;

    private String titleText;
    private String leftLabelText;
    private String rightLabelText;

    public HowMissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HowMissLayout, 0, 0);
        titleText = a.getString(R.styleable.HowMissLayout_hml_title);
        if (titleText == null) titleText = "";
        leftLabelText = a.getString(R.styleable.HowMissLayout_hml_left_label);
        if (leftLabelText == null) leftLabelText = "";
        rightLabelText = a.getString(R.styleable.HowMissLayout_hml_right_label);
        if (rightLabelText == null) rightLabelText = "";

        a.recycle();
    }

    private void inflateView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.graph_item, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        leftLabel = (TextView) findViewById(R.id.leftLabel);
        rightLabel = (TextView) findViewById(R.id.rightLabel);
        centerLabel = (TextView) findViewById(R.id.noData);
        left = (TextView) findViewById(R.id.left);
        right = (TextView) findViewById(R.id.right);

        title.setText(titleText);
        leftLabel.setText(leftLabelText);
        rightLabel.setText(rightLabelText);
        centerLabel.setText(getContext().getString(R.string.no_data));

        setWeights(0, 0);
    }

    private void setWeight(View view, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
        params.weight = weight;
        params.gravity = Gravity.CENTER;

        int leftMargin = 0, rightMargin = 0;
        int defaultMarginSize = (int) ConversionUtils.convertDpToPx(getContext(), .5f);

        if (view.getId() == R.id.left) {
            rightMargin = defaultMarginSize;
        } else if (view.getId() == R.id.right) {
            leftMargin = defaultMarginSize;
        } else if (view.getId() == R.id.noData) {
            leftMargin = defaultMarginSize * 2;
            rightMargin = defaultMarginSize * 2;
        }

        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;

        view.setLayoutParams(params);
    }

    public void setWeights(int left, int right) {
        if (left + right > 0) {
            setWeight(centerLabel, 0);
            centerLabel.setText("");
        } else {
            setWeight(centerLabel, 1);
            centerLabel.setText(getContext().getString(R.string.no_data));
        }

        setLeftText(left);
        setRightText(right);

        if (left == 0)
            leftLabel.setBackgroundResource(R.drawable.graph_background);
        else
            leftLabel.setBackgroundResource(R.drawable.graph_background_left);

        if (right == 0)
            rightLabel.setBackgroundResource(R.drawable.graph_background);
        else
            rightLabel.setBackgroundResource(R.drawable.graph_background_right);

    }

    private void setLeftText(int count) {
        setText(left, count);
        setWeight(left, count);
    }

    private void setRightText(int count) {
        setText(right, count);
        setWeight(right, count);
    }

    private void setText(TextView textView, int count) {
        textView.setText(String.format(Locale.getDefault(), "%1$d", count));
    }

    public void setWeights(Pair<Integer, Integer> weights) {
        TransitionManager.beginDelayedTransition(this);
        setWeights(weights.first, weights.second);
    }
}
