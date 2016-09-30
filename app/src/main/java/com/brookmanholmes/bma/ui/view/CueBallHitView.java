package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

/**
 * Created by Brookman Holmes on 9/26/2016.
 */

public class CueBallHitView extends View {
    private static final String TAG = "CueBallHitView";
    private static final int SIZE = 25;

    private Paint hitPaint;
    private PointF hitPoint;

    private Paint linePaint;
    private OnCueBallTouched onCueBallTouched;

    public CueBallHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setClipToOutline(true);

        hitPoint = new PointF();
        hitPoint.x = 0;
        hitPoint.y = 0;

        hitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hitPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        hitPaint.setAlpha(128);
        hitPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(ConversionUtils.convertDpToPx(getContext(), 2));
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.dead_ball));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (inRadius(eventX, eventY)) {
                    hitPoint.x = eventX;
                    hitPoint.y = eventY;
                    if (onCueBallTouched != null)
                        onCueBallTouched.onTouch(getCueBallX(eventX), getCueBallY(eventY));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (inRadius(eventX, eventY)) {
                    hitPoint.x = eventX;
                    hitPoint.y = eventY;
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (inRadius(eventX, eventY)) {
                    // snap to x/y axis if within 20 pixels
                    if (Math.abs(eventX - ((float) getWidth() / 2)) < 20)
                        eventX = 0;
                    if (Math.abs(eventY - ((float) getHeight() / 2)) < 20)
                        eventY = 0;

                    hitPoint.x = eventX;
                    hitPoint.y = eventY;
                    if (onCueBallTouched != null)
                        onCueBallTouched.onTouch(getCueBallX(eventX), getCueBallY(eventY));
                }
                break;
            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = (float) getWidth() / 2;
        float centerY = (float) getHeight() / 2;

        canvas.drawLine(centerX, 0, centerX, getHeight(), linePaint);
        canvas.drawLine(0, centerY, getWidth(), centerY, linePaint);
        canvas.drawCircle(centerX, centerY, Math.min(getWidth(), getHeight() / 2 - ConversionUtils.convertDpToPx(getContext(), 1)), linePaint);

        canvas.drawCircle(hitPoint.x == 0 ? centerX : hitPoint.x, hitPoint.y == 0 ? centerY : hitPoint.y, ConversionUtils.convertDpToPx(getContext(), SIZE), hitPaint);
        canvas.drawCircle(hitPoint.x == 0 ? centerX : hitPoint.x, hitPoint.y == 0 ? centerY : hitPoint.y, ConversionUtils.convertDpToPx(getContext(), SIZE / 3), hitPaint);
    }

    private double computeDistance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

    private boolean inRadius(double x, double y) {
        float centerX = (float) getWidth() / 2;
        float centerY = (float) getHeight() / 2;
        float radius = Math.min(getWidth(), getHeight()) / 2;
        return computeDistance(centerX, centerY, x, y) < radius;
    }

    public int getCueBallX(float x) {
        float radius = Math.min(getWidth(), getHeight()) / 2;
        return normalize((x - radius));
    }

    public int getCueBallY(float y) {
        float radius = Math.min(getWidth(), getHeight()) / 2;
        return normalize((y - radius));
    }

    private int normalize(float point) {
        float radius = Math.min(getWidth(), getHeight()) / 2;
        return (int) (point / radius * 100);
    }

    public void addOnCueBueTouchedListener(OnCueBallTouched listener) {
        this.onCueBallTouched = listener;
    }
}
