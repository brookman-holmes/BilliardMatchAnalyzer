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

package com.brookmanholmes.billiardmatchanalyzer.wizard.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;

public class StepPagerStrip extends View {
    private static final int[] ATTRS = new int[]{
            android.R.attr.gravity
    };
    private int pageCount;
    private int currentPage;

    private int gravity = Gravity.LEFT | Gravity.TOP;
    private float tabWidth;
    private float tabHeight;
    private float tabSpacing;

    private Paint prevTabPaint;
    private Paint selectedTabPaint;
    private Paint selectedLastTabPaint;
    private Paint nextTabPaint;

    private RectF tempRectF = new RectF();

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

        final Resources res = getResources();
        tabWidth = res.getDimensionPixelSize(R.dimen.step_pager_tab_width);
        tabHeight = res.getDimensionPixelSize(R.dimen.step_pager_tab_height);
        tabSpacing = res.getDimensionPixelSize(R.dimen.step_pager_tab_spacing);

        prevTabPaint = new Paint();
        prevTabPaint.setColor(res.getColor(R.color.step_pager_previous_tab_color));

        selectedTabPaint = new Paint();
        selectedTabPaint.setColor(res.getColor(R.color.step_pager_selected_tab_color));

        selectedLastTabPaint = new Paint();
        selectedLastTabPaint.setColor(res.getColor(R.color.step_pager_selected_last_tab_color));

        nextTabPaint = new Paint();
        nextTabPaint.setColor(res.getColor(R.color.step_pager_next_tab_color));
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

        float totalWidth = pageCount * (tabWidth + tabSpacing) - tabSpacing;
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

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.CENTER_VERTICAL:
                tempRectF.top = (int) (getHeight() - tabHeight) / 2;
                break;
            case Gravity.BOTTOM:
                tempRectF.top = getHeight() - getPaddingBottom() - tabHeight;
                break;
            default:
                tempRectF.top = getPaddingTop();
        }

        tempRectF.bottom = tempRectF.top + tabHeight;

        float tabWidth = this.tabWidth;
        if (fillHorizontal) {
            tabWidth = (getWidth() - getPaddingRight() - getPaddingLeft()
                    - (pageCount - 1) * tabSpacing) / pageCount;
        }

        for (int i = 0; i < pageCount; i++) {
            tempRectF.left = totalLeft + (i * (tabWidth + tabSpacing));
            tempRectF.right = tempRectF.left + tabWidth;
            canvas.drawRect(tempRectF, i < currentPage
                    ? prevTabPaint
                    : (i > currentPage
                    ? nextTabPaint
                    : (i == pageCount - 1
                    ? selectedLastTabPaint
                    : selectedTabPaint)));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                View.resolveSize(
                        (int) (pageCount * (tabWidth + tabSpacing) - tabSpacing)
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

        float totalWidth = pageCount * (tabWidth + tabSpacing) - tabSpacing;
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
                    - (pageCount - 1) * tabSpacing) / pageCount;
        }

        float totalRight = totalLeft + (pageCount * (tabWidth + tabSpacing));
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

        // TODO: Set content description appropriately
    }

    private void scrollCurrentPageIntoView() {
        // TODO: only works with left gravity for now
//
//        float widthToActive = getPaddingLeft() + (currentPage + 1) * (tabWidth + tabSpacing)
//                - tabSpacing;
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

        // TODO: Set content description appropriately
    }

    public interface OnPageSelectedListener {
        void onPageStripSelected(int position);
    }

//
//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            setScrollX(mScroller.getCurrX());
//        }
//    }
}
