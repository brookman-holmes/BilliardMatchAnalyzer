package com.brookmanholmes.bma.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

/**
 * Created by Brookman Holmes on 10/3/2016.
 */

public class CueBallView extends View {
    private static final int DEFAULT_SIZE = 25;
    protected Paint linePaint;
    protected int size;
    protected int color;

    public CueBallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CueBallView);

        size = a.getDimensionPixelSize(R.styleable.CueBallView_hit_indicator, getDefaultSize());

        color = a.getColor(R.styleable.CueBallView_hit_color, ContextCompat.getColor(getContext(), R.color.colorPrimarySuperTransparent));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(a.getDimension(R.styleable.CueBallView_line_width, ConversionUtils.convertDpToPx(getContext(), 2)));
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.dead_ball));

        a.recycle();

        setClipToOutline(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getCenterX(),
                getCenterY(), getRadius() - getPixels(1), linePaint);

        drawXLine(canvas, getCenterX());
        drawYLine(canvas, getCenterY());
    }

    protected float getPixels(int dp) {
        return ConversionUtils.convertDpToPx(getContext(), dp);
    }

    protected void drawXLine(Canvas canvas, float x) {
        canvas.drawLine(x, 0, x, getHeight(), linePaint);
    }

    protected void drawYLine(Canvas canvas, float y) {
        canvas.drawLine(0, y, getWidth(), y, linePaint);
    }

    protected float getCenterX() {
        return (float) getWidth() / 2;
    }

    protected float getCenterY() {
        return (float) getHeight() / 2;
    }

    protected float getRadius() {
        return Math.min(getWidth(), getHeight()) / 2;
    }

    private float getScaledTextSize(int size) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, metrics);
    }

    protected int getDefaultSize() {
        return (int) ConversionUtils.convertDpToPx(getContext(), DEFAULT_SIZE);
    }
}
