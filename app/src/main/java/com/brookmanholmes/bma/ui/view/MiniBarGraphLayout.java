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

import java.text.DecimalFormat;

/**
 * Created by Brookman Holmes on 12/22/2016.
 */

public class MiniBarGraphLayout extends LinearLayout {
    TextView title, text;
    ImageView bar;
    float totalWeight;
    boolean useDistanceFormatter = false;

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
        useDistanceFormatter = a.getBoolean(R.styleable.MiniBarGraphLayout_mbg_formatter_feet, false);
        a.recycle();
    }

    public void setBarWeight(float weight) {
        if (weight >= 0) {
            if (Float.compare(weight, 0f) == 0)
                weight = .25f; // bump up the weight for 0 weighted ones so that you get a little graph
            setVisibility(View.VISIBLE);

            setWeight(bar, weight);
            setWeight(text, totalWeight - weight);

            if (useDistanceFormatter) {
                String string;
                if (Float.compare(weight, .25f) == 0)
                    string = "less than .5'";
                else
                    string = new DecimalFormat("#.#''").format(weight);
                text.setText(string);
            } else {
                String string = (int) weight + "/10";
                text.setText(string);
            }
        }
    }

    private void setWeight(View view, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
        params.weight = weight;
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
    }
}
