package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Brookman Holmes on 9/26/2016.
 */

public class CueBallHitView extends CueBallView {
    private static final String TAG = "CueBallHitView";

    private Paint hitPaint;
    private PointF hitPoint;

    private OnCueBallTouched onCueBallTouched;

    public CueBallHitView(Context context, AttributeSet attrs) {
        super(context, attrs);

        hitPoint = new PointF();
        hitPoint.x = 0;
        hitPoint.y = 0;

        hitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hitPaint.setColor(color);
        hitPaint.setStyle(Paint.Style.FILL);
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
                    if (Math.abs(eventX - getCenterX()) < 20)
                        eventX = (float) getWidth() / 2;
                    if (Math.abs(eventY - getCenterY()) < 20)
                        eventY = (float) getHeight() / 2;

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

        canvas.drawCircle(hitPoint.x == 0 ? getCenterX() : hitPoint.x,
                hitPoint.y == 0 ? getCenterY() : hitPoint.y, size, hitPaint);
        canvas.drawCircle(hitPoint.x == 0 ? getCenterX() : hitPoint.x,
                hitPoint.y == 0 ? getCenterY() : hitPoint.y, size / 3, hitPaint);
    }

    private double computeDistance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

    private boolean inRadius(double x, double y) {
        return computeDistance(getCenterX(), getCenterY(), x, y) < getRadius();
    }

    public int getCueBallX(float x) {
        return normalize((x - getRadius()));
    }

    public int getCueBallY(float y) {
        return normalize((y - getRadius()));
    }

    private int normalize(float point) {
        return (int) (point / getRadius() * 100);
    }

    public void addOnCueBueTouchedListener(OnCueBallTouched listener) {
        this.onCueBallTouched = listener;
    }
}
