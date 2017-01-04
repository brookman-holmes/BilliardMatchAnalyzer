package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

public class HorizontalBarView extends BaseView {
    String mText = "";
    float mValue = 30.0f;
    Paint mBarPaintEmpty = new Paint();
    Paint mBarPaintFill = new Paint();
    Paint mFillTextPaint = new Paint();
    Paint mEmptyTextPaint = new Paint();

    Rect mBarRect = new Rect();
    Rect mTextRect = new Rect();
    int mHalfStrokeWidth;

    int mEmptyColor;
    int mFillColor;
    int mEmptyTextColor;
    int mFillTextColor;
    int mTextSize = 15;
    int mTextWidth = 0;
    int mTextPadding = mTextSize;


    public HorizontalBarView(Context context) {
        super(context);
        init();
    }

    public HorizontalBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.PieChart, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HorizontalBarView,
                0, 0
        );

        String fontPath = null;

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.PieChart_* constants represent the index for
            // each custom attribute in the R.styleable.PieChart array.
            mEmptyColor = a.getColor(R.styleable.HorizontalBarView_bar_emptyColor, Color.GRAY);
            mFillColor = a.getColor(R.styleable.HorizontalBarView_bar_fillColor, Color.BLUE);
            mEmptyTextColor = a.getColor(R.styleable.HorizontalBarView_bar_emptyTextColor, Color.BLACK);
            mFillTextColor = a.getColor(R.styleable.HorizontalBarView_bar_fillTextColor, Color.WHITE);
            mTextSize = a.getDimensionPixelSize(R.styleable.HorizontalBarView_bar_textSize, mTextSize);
            mText = a.getString(R.styleable.HorizontalBarView_bar_text);
            mTextPadding = a.getDimensionPixelSize(R.styleable.HorizontalBarView_bar_textPadding, mTextPadding);
            mValue = a.getFloat(R.styleable.HorizontalBarView_bar_fillPercentage, mValue);
            fontPath = a.getString(R.styleable.HorizontalBarView_bar_fontPath);
            if (mText == null) {
                mText = "";
            }

        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }


        mBarPaintEmpty.setColor(mEmptyColor);
        mBarPaintEmpty.setStyle(Paint.Style.FILL);

        mBarPaintFill.setColor(mFillColor);
        mBarPaintFill.setStyle(Paint.Style.FILL);

        mEmptyTextPaint.setColor(mEmptyTextColor);
        mEmptyTextPaint.setStyle(Paint.Style.FILL);
        mEmptyTextPaint.setTextSize(mTextSize);
        mEmptyTextPaint.setAntiAlias(true);

        mFillTextPaint.setColor(mFillTextColor);
        mFillTextPaint.setStyle(Paint.Style.FILL);
        mFillTextPaint.setTextSize(mTextSize);
        mFillTextPaint.setAntiAlias(true);

        if (fontPath != null && fontPath.length() > 0) {
            Typeface t = Typeface.createFromAsset(context.getAssets(), fontPath);
            mFillTextPaint.setTypeface(t);
            mEmptyTextPaint.setTypeface(t);
        }

        mTextWidth = (int) mEmptyTextPaint.measureText(mText);

    }

    private void init() {

        mBarPaintEmpty.setColor(Color.GRAY);
        mBarPaintEmpty.setStyle(Paint.Style.FILL);

        mBarPaintFill.setColor(Color.BLUE);
        mBarPaintFill.setStyle(Paint.Style.FILL);

        mEmptyTextPaint.setColor(Color.BLACK);
        mEmptyTextPaint.setAntiAlias(true);

        mFillTextPaint.setColor(Color.WHITE);
        mFillTextPaint.setAntiAlias(true);

    }


    public void setText(String text) {
        this.mText = text;
        mTextWidth = (int) mEmptyTextPaint.measureText(text);
        invalidate();
    }

    public void setPercentage(float value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("value is a percentage and should be between 0 and 100 found -> " + value);
        }
        mValue = value;
        invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int minHeight = super.getSuggestedMinimumHeight();
        if (minHeight <= 0) {
            return mTextSize + (mTextPadding * 2);
        } else {
            return minHeight;
        }

    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int minWidth = super.getSuggestedMinimumHeight();
        if (minWidth <= 0) {
            return mTextSize + (mTextPadding * 4);
        } else {
            return minWidth;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //stroke is always centered, we want to bring it within the bounds
        //of the drawable area.
        mHalfStrokeWidth = mBarPaintEmpty.getStrokeWidth() < 2 ? (int) mBarPaintEmpty.getStrokeWidth() : (int) mBarPaintEmpty.getStrokeWidth() / 2;

        int ypad = getPaddingTop() + getPaddingBottom();
        int xpad = getPaddingLeft() + getPaddingRight();

        mBarRect.left = getPaddingLeft() + mHalfStrokeWidth;
        mBarRect.top = getPaddingTop() + mHalfStrokeWidth;
        mBarRect.right = getWidth() - getPaddingRight() - mHalfStrokeWidth;
        mBarRect.bottom = getHeight() - getPaddingBottom() - mHalfStrokeWidth;

        mEmptyTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        mTextRect.left = getPaddingLeft() + mHalfStrokeWidth; //recalculate this on draw, so it's close to the fill
        if (isInEditMode()) {
            mTextRect.top = mBarRect.centerY() + mTextRect.bottom + mHalfStrokeWidth;
        } else {
            mTextRect.top = mBarRect.centerY() + mTextRect.height() / 2 + mHalfStrokeWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Build the filled part first
        mBarRect.right = (int) (mBarRect.right * (mValue / 100f));
        int fillRight = mBarRect.right;
        //canvas.drawRect(mBarRect, mBarPaintFill);
        float rounding = ConversionUtils.convertDpToPx(getContext(), 6);
        // draw at the meeting point to fill around the rounded corners
        canvas.drawRect(mBarRect.right - (int) rounding, mBarRect.top, mBarRect.right + (int) rounding, mBarRect.bottom, mBarPaintEmpty);

        if (mBarRect.right > 0) // draw at the beginning to fill in the rounded corners
            canvas.drawRect(mBarRect.left, mBarRect.top, mBarRect.left + (int) rounding, mBarRect.bottom, mBarRect.right > 0 ? mBarPaintFill : mBarPaintEmpty);

        canvas.drawRoundRect(new RectF(mBarRect), rounding, rounding, mBarPaintFill);
        //Draw the empty part starting from the end of the filled part
        //this is to avoid overdraw.
        int originalLeft = mBarRect.left;
        mBarRect.left = mBarRect.right;
        mBarRect.right = getWidth() - getPaddingRight() - mHalfStrokeWidth;
        int emptyRight = mBarRect.right;
        canvas.drawRoundRect(new RectF(mBarRect), rounding, rounding, mBarPaintEmpty);
        mBarRect.left = originalLeft;

        //double the padding to account for the left and right sides.
        if (emptyRight - fillRight > mTextWidth + (mTextPadding * 2)) {
            //recalculate left for text to keep it close to the fill line.
            mTextRect.left = fillRight + mTextPadding;
            canvas.drawText(mText, mTextRect.left, mTextRect.top, mEmptyTextPaint);

        } else {
            //recalculate left for text to keep it close to the fill line.
            mTextRect.left = fillRight - mTextWidth - mTextPadding;
            canvas.drawText(mText, mTextRect.left, mTextRect.top, mFillTextPaint);
        }

    }
}