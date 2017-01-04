package com.brookmanholmes.bma.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

/**
 * Created by Brookman Holmes on 12/27/2016.
 */

public class BarGraphView extends BaseView {
    private final float width;
    private final float smallWidth;
    private final float spacing;
    private final int currentPaintColor;
    private final int prevPaintColor;
    private final int nextPaintColor;
    private final Paint prevPaint;
    private final Paint currentPaint;
    private final Paint nextPaint;
    private final Paint textPaint;
    private final int count;
    private int current;

    public BarGraphView(Context context) {
        this(context, null, 0);
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarGraphView);

        width = a.getDimension(R.styleable.BarGraphView_bgv_width, ConversionUtils.convertDpToPx(context, 8));
        smallWidth = width / 2;
        spacing = a.getDimension(R.styleable.BarGraphView_bgv_spacing, ConversionUtils.convertDpToPx(context, 4));
        current = a.getInt(R.styleable.BarGraphView_bgv_selected, 0);
        currentPaintColor = a.getColor(R.styleable.BarGraphView_bgv_current_color, getColor(R.color.colorAccent));
        prevPaintColor = a.getColor(R.styleable.BarGraphView_bgv_prev_color, getColor(R.color.colorAccentLight));
        nextPaintColor = a.getColor(R.styleable.BarGraphView_bgv_next_color, getColor(R.color.dead_ball));
        count = a.getInt(R.styleable.BarGraphView_bgv_count, 10);
        float textSize = a.getDimension(R.styleable.BarGraphView_bgv_text_size, ConversionUtils.convertSpToPx(context, 12));
        a.recycle();

        prevPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        prevPaint.setColor(prevPaintColor);
        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setColor(currentPaintColor);
        nextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nextPaint.setColor(nextPaintColor);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getColor(R.color.primary_text));
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setLinearText(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                View.resolveSize(
                        (int) (count * (width + spacing) - spacing)
                                + getPaddingLeft() + getPaddingRight(),
                        widthMeasureSpec),
                View.resolveSize(
                        (int) width
                                + getPaddingTop() + getPaddingBottom(),
                        heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (count == 0)
            return;

        float totalcx = getPaddingLeft();
        float cy = (int) (getHeight() - smallWidth) / 2;
        float center = cy + smallWidth / 2;

        float tabWidth = this.smallWidth;

        for (int i = 0; i < count; i++) {
            float cx = totalcx + (i * (tabWidth + spacing));
            if (i >= current) { // add extra spacing after current selection
                cx += spacing / 2;
            }
            if (i > current)
                cx += spacing / 2;

            Paint tempPaint;

            if (i < current)
                tempPaint = prevPaint;
            else if (i > current)
                tempPaint = nextPaint;
            else
                tempPaint = currentPaint;

            canvas.drawCircle(cx,
                    center,
                    i == current ? width : smallWidth,
                    tempPaint);

            if (current == i) {
                int xPos = (int) cx;
                int yPos = (int) (center - (textPaint.descent() + textPaint.ascent()) / 2);
                //canvas.drawText(valueFormatter.getFormattedValue(i + 1, null), xPos, yPos, textPaint);
            }
        }
    }

    public void setSelection(int selection) {
        current = selection;
        invalidate();
    }

    public void setSelection(float selection) {
        current = Math.round(selection * 2);
        invalidate();
    }
}
