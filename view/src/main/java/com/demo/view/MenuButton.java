package com.demo.view;

import static android.animation.ValueAnimator.INFINITE;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MenuButton extends View {
    private float mProcess = 1.0f;
    private float mArrowStrokeWidth;
    private int mArrowColor;
    private int mArrowCount;
    private final Paint mArrowPaint = new Paint();
    private int currentIndex = 0;

    public MenuButton(Context context) {
        this(context, null);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuButton);
        mArrowStrokeWidth = typedArray.getDimension(R.styleable.MenuButton_arrowStrokeWidth, dip2px(2));
        mArrowColor = typedArray.getColor(R.styleable.MenuButton_arrowColor, Color.WHITE);
        mArrowCount = typedArray.getInteger(R.styleable.MenuButton_arrowCount, 4);

        typedArray.recycle();
        mArrowPaint.setStrokeWidth(mArrowStrokeWidth);
        mArrowPaint.setColor(mArrowColor);
        mArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        final ValueAnimator va = ValueAnimator.ofInt(mArrowCount - 1, -1);
        va.setInterpolator(new LinearInterpolator());
        va.setRepeatMode(ValueAnimator.RESTART);
        va.setRepeatCount(INFINITE);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentIndex = (int) animation.getAnimatedValue();
                Log.d("TAG", "currentIndex" + currentIndex);
                invalidate();
            }
        });
        va.setDuration(2000);
        va.start();
    }

    public void setProcess(float process) {
        mProcess = process;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int baseline = getHeight() / (mArrowCount + 2);
        int offset = (int) (baseline * 0.7);
        for (int i = 0; i < mArrowCount; i++) {
            if (currentIndex == i) {
                int color = mArrowPaint.getColor();
                int newColor = Color.argb((float) (0.5 * Color.alpha(color)), Color.red(color), Color.green(color), Color.blue(color));
                mArrowPaint.setColor(newColor);
                canvas.drawLine(0.2f * getWidth(), baseline + baseline * i + offset * mProcess, getWidth() / 2, baseline + baseline * i - offset * mProcess, mArrowPaint);
                canvas.drawLine(getWidth() / 2, baseline + baseline * i - offset * mProcess, 0.8f * getWidth(), baseline + baseline * i + offset * mProcess, mArrowPaint);
                mArrowPaint.setColor(color);
            } else {
                canvas.drawLine(0.2f * getWidth(), baseline + baseline * i + offset * mProcess, getWidth() / 2, baseline + baseline * i - offset * mProcess, mArrowPaint);
                canvas.drawLine(getWidth() / 2, baseline + baseline * i - offset * mProcess, 0.8f * getWidth(), baseline + baseline * i + offset * mProcess, mArrowPaint);
            }
        }
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}
