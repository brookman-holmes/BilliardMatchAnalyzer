package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 9/27/2016.
 */

public class HeatGraph extends View {
    private static final String TAG = "HeatGraph";

    List<Point> data = new ArrayList<>();

    private Paint paint;
    private Paint linePaint;

    public HeatGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paint.setAlpha(64);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(ConversionUtils.convertDpToPx(getContext(), 2));
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.dead_ball));

        setClipToOutline(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = (float) getWidth() / 2;
        float centerY = (float) getHeight() / 2;

        canvas.drawLine(centerX, 0, centerX, getHeight(), linePaint);
        canvas.drawLine(0, centerY, getWidth(), centerY, linePaint);
        canvas.drawCircle(centerX, centerY, Math.min(getWidth(), getHeight() / 2 - ConversionUtils.convertDpToPx(getContext(), 1)), linePaint);


        for (Point point : data) {
            canvas.drawCircle(convertPointXToFloat(point.x),
                    convertPointYToFloat(point.y),
                    ConversionUtils.convertDpToPx(getContext(), 15),
                    paint);
        }
    }

    public void setData(Point[] points) {
        data.clear();
        data.addAll(Arrays.asList(points));

        // redraw
        invalidate();
    }

    public void addDataPoint(Point point) {
        data.add(point);
    }

    public void reDraw() {
        invalidate();
    }

    private float convertPointXToFloat(float x) {
        return (x + 100) / 200 * getWidth();
    }

    private float convertPointYToFloat(float y) {
        return (y + 100) / 200 * getHeight();
    }
}
