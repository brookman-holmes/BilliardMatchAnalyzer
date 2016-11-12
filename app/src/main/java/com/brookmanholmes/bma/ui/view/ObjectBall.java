package com.brookmanholmes.bma.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

/**
 * Created by Brookman Holmes on 11/9/2016.
 */

public class ObjectBall extends View {
    @ColorRes
    private static final int DEFAULT_BACKGROUND_COLOR = R.color.one_ball;
    private static final String DEFAULT_BALL_NUMBER = "1";
    private static final boolean DEFAULT_IS_DEAD = false;
    private static final int DEFAULT_VIEW_SIZE = 96;
    private static final float TEXT_SIZE = 24;

    @ColorInt
    private int backgroundColor;
    @ColorInt
    private int deadColor;
    private String ballNumber = DEFAULT_BALL_NUMBER;
    private boolean isDead = DEFAULT_IS_DEAD;

    private Paint backgroundPaint;
    private float textSize = TEXT_SIZE;
    private TextPaint textPaint;
    private Paint textBackgroundPaint;
    private Paint strokePaint;
    private int mViewSize;
    private RectF mInnerRectF;

    public ObjectBall(Context context) {
        super(context);
        init(null, 0);
    }

    public ObjectBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ObjectBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext()
                .obtainStyledAttributes(attrs, R.styleable.ObjectBall, defStyle, 0);

        if (a.hasValue(R.styleable.ObjectBall_ob_ball_number))
            ballNumber = a.getString(R.styleable.ObjectBall_ob_ball_number);

        backgroundColor = a.getColor(R.styleable.ObjectBall_ob_color, getColor(DEFAULT_BACKGROUND_COLOR));
        deadColor = getColor(R.color.dead_ball);
        isDead = a.getBoolean(R.styleable.ObjectBall_ob_dead, DEFAULT_IS_DEAD);
        textSize = a.getDimension(R.styleable.ObjectBall_ob_text_size, TEXT_SIZE);
        a.recycle();

        // text paint
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setLinearText(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(TEXT_SIZE);

        // stroke paint
        strokePaint = new Paint();
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStrokeWidth(ConversionUtils.convertDpToPx(getContext(), 1));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);

        // background color
        backgroundPaint = new Paint();
        backgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(isDead ? deadColor : backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);

        // center paint
        textBackgroundPaint = new Paint();
        textBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textBackgroundPaint.setColor(Color.WHITE);
        textBackgroundPaint.setStyle(Paint.Style.FILL);

        mInnerRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = resolveSize(DEFAULT_VIEW_SIZE, widthMeasureSpec);
        int height = resolveSize(DEFAULT_VIEW_SIZE, heightMeasureSpec);
        mViewSize = Math.min(width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mInnerRectF.set(0, 0, mViewSize, mViewSize);
        mInnerRectF.offset((getWidth() - mViewSize) / 2, (getHeight() - mViewSize) / 2);

        float centerX = mInnerRectF.centerX();
        float centerY = mInnerRectF.centerY();

        int xPos = (int) centerX;
        int yPos = (int) (centerY - (textPaint.descent() + textPaint.ascent()) / 2);

        canvas.drawCircle(centerX, centerY, (mViewSize / 2) - ConversionUtils.convertDpToPx(getContext(), .5f), backgroundPaint);
        canvas.drawCircle(centerX, centerY, (mViewSize / 2) - ConversionUtils.convertDpToPx(getContext(), .5f), strokePaint);
        canvas.drawCircle(centerX, centerY, textSize / 1.2f, textBackgroundPaint);
        canvas.drawCircle(centerX, centerY, textSize / 1.2f, strokePaint);
        canvas.drawText(ballNumber, xPos, yPos, textPaint);
    }

    private
    @ColorInt
    int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }
}
