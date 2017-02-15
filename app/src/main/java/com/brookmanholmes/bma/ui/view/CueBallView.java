package com.brookmanholmes.bma.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

/**
 * Created by Brookman Holmes on 10/3/2016.
 */

public class CueBallView extends BaseView {
    private static final int DEFAULT_SIZE = 25;
    protected Paint linePaint;
    protected int size;
    protected int color;
    protected int alpha;

    public CueBallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CueBallView);

        size = a.getDimensionPixelSize(R.styleable.CueBallView_hit_indicator, getDefaultSize());

        color = a.getColor(R.styleable.CueBallView_hit_color, getColor(R.color.colorPrimary));
        alpha = a.getInt(R.styleable.CueBallView_hit_indicator_alpha, 128);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(a.getDimension(R.styleable.CueBallView_line_width, ConversionUtils.convertDpToPx(getContext(), 2)));
        linePaint.setColor(getColor(R.color.dead_ball));

        a.recycle();

        setClipToOutline(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getCenterX(), getCenterY(), getRadius() - getPixels(1), linePaint);

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

    protected int getDefaultSize() {
        return (int) ConversionUtils.convertDpToPx(getContext(), DEFAULT_SIZE);
    }
}
