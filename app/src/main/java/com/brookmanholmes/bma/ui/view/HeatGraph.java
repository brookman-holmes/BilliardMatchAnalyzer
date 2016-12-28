package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

import com.brookmanholmes.bma.utils.ConversionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 9/27/2016.
 */

public class HeatGraph extends CueBallView {
    private static final String TAG = "HeatGraph";

    List<Point> data = new ArrayList<>();

    private Paint paint;

    public HeatGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Point point : data) {
            canvas.drawCircle(convertPointXToFloat(point.x),
                    convertPointYToFloat(point.y),
                    size,
                    paint);
        }
    }

    public void setData(Point[] points) {
        data.clear();
        data.addAll(Arrays.asList(points));

        // redraw
        invalidate();
    }

    public void setData(List<Point> points) {
        data.clear();
        data.addAll(points);

        invalidate();
    }

    public void setData(Point point) {
        data.clear();
        data.add(point);

        invalidate();
    }

    private float convertPointXToFloat(float x) {
        return (x + 100) / 200 * getWidth();
    }

    private float convertPointYToFloat(float y) {
        return (y + 100) / 200 * getHeight();
    }

    @Override
    protected int getDefaultSize() {
        return (int) ConversionUtils.convertDpToPx(getContext(), 15);
    }
}
