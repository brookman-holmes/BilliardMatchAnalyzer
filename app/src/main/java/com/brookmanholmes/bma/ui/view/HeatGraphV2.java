package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 1/11/2017.
 */

public class HeatGraphV2 extends CueBallView {
    private static final String TAG = "HeatGraphV2";
    private static final int DEFAULT_COLUMNS = 9;
    private static final int DEFAULT_ROWS = 9;

    List<Point> data = new ArrayList<>();

    private Paint goodPaint;
    private Paint almostGoodPaint;
    private Paint okayPaint;
    private Paint justAboveBadPaint;
    private Paint badPaint;

    private RectF rectF;
    private Path path;
    private Path circlePath;

    private int[][] pointCounts = new int[DEFAULT_COLUMNS][DEFAULT_ROWS];

    public HeatGraphV2(Context context, AttributeSet attrs) {
        super(context, attrs);

        goodPaint = getPaint(R.color.good);
        almostGoodPaint = getPaint(R.color.almost_good);
        okayPaint = getPaint(R.color.okay);
        justAboveBadPaint = getPaint(R.color.just_above_bad);
        badPaint = getPaint(R.color.bad);

        rectF = new RectF();
        path = new Path();
        circlePath = new Path();

        resetPointCounts();
    }

    private void resetPointCounts() {
        for (int i = 0; i < DEFAULT_COLUMNS; i++) {
            for (int j = 0; j < DEFAULT_ROWS; j++) {
                pointCounts[i][j] = 0;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circlePath.reset();
        circlePath.addCircle(getCenterX(), getCenterY(), getRadius(), Path.Direction.CCW);

        float height = getRectHeight();
        float width = getRectWidth();

        for (int column = 0; column < DEFAULT_COLUMNS; column++) {
            for (int row = 0; row < DEFAULT_ROWS; row++) {
                path.reset();
                rectF.left = column * width;
                rectF.top = row * height;
                rectF.bottom = (row + 1) * height;
                rectF.right = (column + 1) * width;
                path.addRect(rectF, Path.Direction.CCW);

                path.op(circlePath, Path.Op.INTERSECT);

                canvas.drawPath(path, getPaint(column, row));
            }
        }
    }

    private float getRectWidth() {
        return (float) getWidth() / DEFAULT_COLUMNS;
    }

    private float getRectHeight() {
        return (float) getHeight() / DEFAULT_ROWS;
    }

    private Paint getPaint(@ColorRes int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(getColor(color));
        paint.setStrokeWidth(ConversionUtils.convertDpToPx(getContext(), 1));
        return paint;
    }

    private Paint getPaint(int column, int row) {
        int pointCount = pointCounts[column][row];
        float percent = data.size() == 0 ? 0f : (float) pointCount / (float) data.size();

        Paint paint;

        if (percent < .04f)
            paint = goodPaint;
        else if (percent < .8f)
            paint = almostGoodPaint;
        else if (percent < .16f)
            paint = okayPaint;
        else if (percent < .25f)
            paint = justAboveBadPaint;
        else paint = badPaint;

        return paint;
    }

    private float convertPointXToFloat(float x) {
        return (x + 100) / 200 * getWidth();
    }

    private float convertPointYToFloat(float y) {
        return (y + 100) / 200 * getHeight();
    }

    public void setData(Point[] points) {
        setData(Arrays.asList(points));
    }

    public void setData(List<Point> points) {
        data.clear();
        data.addAll(points);
        resetPointCounts();

        for (Point point : data) {
            int row = convertPointXToGrid(point.x);
            int column = convertPointYToGrid(point.y);

            pointCounts[row][column] += 1;
        }

        invalidate();
    }

    private int convertPointYToGrid(int y) {
        float c = convertPointYToFloat(y);
        float dy = getRectHeight();
        for (int i = 0; i < DEFAULT_COLUMNS; i++) {
            if (c < dy)
                return i;
            else
                dy += getRectHeight();
        }

        return 0;
    }

    private int convertPointXToGrid(int x) {
        float c = convertPointXToFloat(x);
        float dy = getRectWidth();
        for (int i = 0; i < DEFAULT_ROWS; i++) {
            if (c < dy)
                return i;
            else
                dy += getRectWidth();
        }

        return 0;
    }
}
