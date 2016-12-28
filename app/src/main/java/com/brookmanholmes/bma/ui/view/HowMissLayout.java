package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 12/22/2016.
 */

public class HowMissLayout extends LinearLayout {
    private TextView left, right;

    public HowMissLayout(Context context) {
        super(context);
        initViews(context);
    }

    public HowMissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setTexts(attrs);
    }

    public HowMissLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        setTexts(attrs);
    }

    public HowMissLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
        setTexts(attrs);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.graph_mini, this);
        left = (TextView) findViewById(R.id.left);
        right = (TextView) findViewById(R.id.right);
    }

    private void setTexts(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HowMissLayout);

        left.setText(a.getText(R.styleable.HowMissLayout_hml_left_label));
        right.setText(a.getText(R.styleable.HowMissLayout_hml_right_label));

        int background = a.getColor(R.styleable.HowMissLayout_hml_background, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        int textColor = a.getColor(R.styleable.HowMissLayout_hml_text_color, ContextCompat.getColor(getContext(), R.color.white));
        left.getBackground().setColorFilter(background, PorterDuff.Mode.SRC_IN);
        right.getBackground().setColorFilter(background, PorterDuff.Mode.SRC_IN);
        left.setTextColor(textColor);
        right.setTextColor(textColor);
        a.recycle();
    }

    public void setHowMiss(boolean selectLeft, boolean selectRight) {
        if (selectLeft)
            select(this.left);
        else unSelect(this.left);

        if (selectRight)
            select(this.right);
        else unSelect(this.right);

        if (!selectLeft && !selectRight)
            this.setVisibility(View.GONE);
        else this.setVisibility(View.VISIBLE);
    }

    private void select(TextView textView) {
        textView.setVisibility(View.VISIBLE);
    }

    private void unSelect(TextView textView) {
        textView.setVisibility(View.GONE);
    }
}
