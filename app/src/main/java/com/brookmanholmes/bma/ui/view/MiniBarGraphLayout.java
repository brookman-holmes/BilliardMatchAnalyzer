package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Brookman Holmes on 12/22/2016.
 */

public class MiniBarGraphLayout extends LinearLayout {
    TextView title, text;
    ImageView bar;
    float totalWeight;
    private IAxisValueFormatter valueFormatter = new DefaultAxisValueFormatter(0);

    public MiniBarGraphLayout(Context context) {
        super(context);
        initViews(context);
    }

    public MiniBarGraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setup(attrs);
    }

    public MiniBarGraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        setup(attrs);
    }

    public MiniBarGraphLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
        setup(attrs);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.graph_bar, this);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.number);
        bar = (ImageView) findViewById(R.id.bar);
    }

    private void setup(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MiniBarGraphLayout);

        title.setText(a.getText(R.styleable.MiniBarGraphLayout_mbg_title));
        text.setText(a.getText(R.styleable.MiniBarGraphLayout_mbg_text));
        bar.setColorFilter(a.getColor(R.styleable.MiniBarGraphLayout_mbg_bar_color, ContextCompat.getColor(getContext(), R.color.colorAccent)));
        totalWeight = a.getFloat(R.styleable.MiniBarGraphLayout_mbg_weight, 10f);

        a.recycle();
    }

    public void setBarWeight(float weight) {
        if (weight >= 0) {
            setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams barLayoutParams =
                    new LinearLayout.LayoutParams(bar.getLayoutParams());
            barLayoutParams.weight = weight;
            barLayoutParams.gravity = Gravity.CENTER;
            LinearLayout.LayoutParams textLayoutParams =
                    new LinearLayout.LayoutParams(text.getLayoutParams());
            textLayoutParams.weight = totalWeight - weight;
            textLayoutParams.gravity = Gravity.CENTER;
            bar.setLayoutParams(barLayoutParams);
            text.setLayoutParams(textLayoutParams);
            text.setText(valueFormatter.getFormattedValue(weight, null));
        } else {
            setVisibility(View.GONE);
        }
    }

    public void setValueFormatter(IAxisValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }
}
