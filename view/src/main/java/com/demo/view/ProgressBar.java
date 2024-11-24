package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.demo.view.R;

/**
 * Time:2019/3/23
 * Author:蒲俊辉
 * Email:pujh@idste.cn
 * Description:自定义圆形进度条
 */
public class ProgressBar extends View {
    private int mInnerBackground = Color.RED;
    private int mOuterBackground = Color.RED;
    private int mRoundWidth = 10;//10px
    private float mProgressTextSize = 15;
    private int mProgressTextColor = Color.RED;

    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    private int mMax = 100;
    private int mProgress = 0;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获得自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mInnerBackground = array.getColor(R.styleable.ProgressBar_innerBackground, mInnerBackground);
        mOuterBackground = array.getColor(R.styleable.ProgressBar_outerBackground, mOuterBackground);
        mRoundWidth = array.getDimensionPixelOffset(R.styleable.ProgressBar_roundWidth, dip2px(mRoundWidth));
        mProgressTextSize = array.getDimension(R.styleable.ProgressBar_progressTextSize, sp2px(mProgressTextSize));
        mProgressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor, mProgressTextColor);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mRoundWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setColor(mOuterBackground);

        mInnerPaint = new Paint(mOuterPaint);
        mInnerPaint.setColor(mInnerBackground);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);

    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(width, height);
        setMeasuredDimension(minSize, minSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先画内圆
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, center - mRoundWidth / 2, mOuterPaint);

        //再画外圆
        final RectF rectF = new RectF(mRoundWidth / 2, mRoundWidth / 2, getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);
        canvas.drawArc(rectF, 0, (int) (360.0 * mProgress / mMax), false, mInnerPaint);

        //最后画文字
        String text = (int) (100.0 * mProgress / mMax) + "%";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        //基线baseLine
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, mTextPaint);
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException();
        }
        this.mMax = max;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException();
        }
        if (progress > mMax) {
            progress = mMax;
        }
        this.mProgress = progress;
        invalidate();
    }
}
