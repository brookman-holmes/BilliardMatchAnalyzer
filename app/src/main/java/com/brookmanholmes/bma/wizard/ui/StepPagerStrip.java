/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brookmanholmes.bma.wizard.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.brookmanholmes.bma.R;

public class StepPagerStrip extends View {
    private static final int[] ATTRS = new int[]{
            android.R.attr.gravity
    };
    private final float tabWidth;
    private final float tabHeight;
    private final float indicatorSpacing;
    private final float radius;
    private final float nonCurrentRadius;
    private final Paint prevTabPaint;
    private final Paint nextTabPaint;
    private final Paint selectedTabPaint;
    private int pageCount;
    private int currentPage;
    private int gravity = Gravity.LEFT | Gravity.TOP;

    //private Scroller mScroller;
    private OnPageSelectedListener onPageSelectedListener;

    public StepPagerStrip(Context context) {
        this(context, null, 0);
    }

    public StepPagerStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepPagerStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        gravity = a.getInteger(0, gravity);
        a.recycle();

        Resources res = getResources();
        tabWidth = res.getDimensionPixelSize(R.dimen.step_pager_tab_width);
        tabHeight = res.getDimensionPixelSize(R.dimen.step_pager_tab_height);
        indicatorSpacing = res.getDimensionPixelSize(R.dimen.step_pager_tab_spacing);
        radius = res.getDimension(R.dimen.step_pager_tab_width);
        nonCurrentRadius = res.getDimension(R.dimen.step_pager_tab_width_non_current);

        nextTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nextTabPaint.setColor(ContextCompat.getColor(getContext(), R.color.step_pager_next_tab_color));

        selectedTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedTabPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        prevTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        prevTabPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
    }

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pageCount == 0) {
            return;
        }

        float totalWidth = pageCount * (tabWidth + indicatorSpacing) - indicatorSpacing;
        float totalcx;
        float cy;
        boolean fillHorizontal = false;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                totalcx = (getWidth() - totalWidth) / 2;
                break;
            case Gravity.RIGHT:
                totalcx = getWidth() - getPaddingRight() - totalWidth;
                break;
            case Gravity.FILL_HORIZONTAL:
                totalcx = getPaddingLeft();
                fillHorizontal = true;
                break;
            default:
                totalcx = getPaddingLeft();
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.CENTER_VERTICAL:
                cy = (int) (getHeight() - tabHeight) / 2;
                break;
            case Gravity.BOTTOM:
                cy = getHeight() - getPaddingBottom() - tabHeight;
                break;
            default:
                cy = getPaddingTop();
        }

        float center = cy + tabHeight / 2;

        float tabWidth = this.tabWidth;
        if (fillHorizontal) {
            tabWidth = (getWidth() - getPaddingRight() - getPaddingLeft()
                    - (pageCount - 1) * indicatorSpacing) / pageCount;
        }

        for (int i = 0; i < pageCount; i++) {
            float cx = totalcx + (i * (tabWidth + indicatorSpacing));

            Paint tempPaint;

            if (i < currentPage)
                tempPaint = prevTabPaint;
            else if (i > currentPage)
                tempPaint = nextTabPaint;
            else
                tempPaint = selectedTabPaint;

            canvas.drawCircle(cx, center,
                    i == currentPage ? radius : nonCurrentRadius,
                    tempPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                View.resolveSize(
                        (int) (pageCount * (tabWidth + indicatorSpacing) - indicatorSpacing)
                                + getPaddingLeft() + getPaddingRight(),
                        widthMeasureSpec),
                View.resolveSize(
                        (int) tabHeight
                                + getPaddingTop() + getPaddingBottom(),
                        heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        scrollCurrentPageIntoView();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onPageSelectedListener != null) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int position = hitTest(event.getX());
                    if (position >= 0) {
                        onPageSelectedListener.onPageStripSelected(position);
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private int hitTest(float x) {
        if (pageCount == 0) {
            return -1;
        }

        float totalWidth = pageCount * (tabWidth + indicatorSpacing) - indicatorSpacing;
        float totalLeft;
        boolean fillHorizontal = false;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                totalLeft = (getWidth() - totalWidth) / 2;
                break;
            case Gravity.RIGHT:
                totalLeft = getWidth() - getPaddingRight() - totalWidth;
                break;
            case Gravity.FILL_HORIZONTAL:
                totalLeft = getPaddingLeft();
                fillHorizontal = true;
                break;
            default:
                totalLeft = getPaddingLeft();
        }

        float tabWidth = this.tabWidth;
        if (fillHorizontal) {
            tabWidth = (getWidth() - getPaddingRight() - getPaddingLeft()
                    - (pageCount - 1) * indicatorSpacing) / pageCount;
        }

        float totalRight = totalLeft + (pageCount * (tabWidth + indicatorSpacing));
        if (x >= totalLeft && x <= totalRight && totalRight > totalLeft) {
            return (int) (((x - totalLeft) / (totalRight - totalLeft)) * pageCount);
        } else {
            return -1;
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        invalidate();
        scrollCurrentPageIntoView();
    }

    private void scrollCurrentPageIntoView() {
//
//        float widthToActive = getPaddingLeft() + (currentPage + 1) * (tabWidth + indicatorSpacing)
//                - indicatorSpacing;
//        int viewWidth = getWidth();
//
//        int startScrollX = getScrollX();
//        int destScrollX = (widthToActive > viewWidth) ? (int) (widthToActive - viewWidth) : 0;
//
//        if (mScroller == null) {
//            mScroller = new Scroller(getContext());
//        }
//
//        mScroller.abortAnimation();
//        mScroller.startScroll(startScrollX, 0, destScrollX - startScrollX, 0);
//        postInvalidate();
    }

    public void setPageCount(int count) {
        pageCount = count;
        invalidate();
    }

    public interface OnPageSelectedListener {
        void onPageStripSelected(int position);
    }

//
//    @Override //    public void computeScroll() {
//        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            setScrollX(mScroller.getCurrX());
//        }
//    }
}
