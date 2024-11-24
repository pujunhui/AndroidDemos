package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.demo.view.R;

/**
 * Time:2019/3/13
 * Author:蒲俊辉
 * Description:仿QQ步数View
 */
public class QQStepView extends View {
    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20;  //20px
    private int mStepTextSize = 40;
    private int mStepTextColor = Color.BLACK;
    private Paint mOuterPaint, mInnerPaint, mTextPaint;

    //总共的，当前的步数
    private int mStepMax = 100;
    private int mCurrentStep = 0;


    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.QQStepView_borderWidth, mBorderWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.STROKE);   //画笔空心

        mInnerPaint = new Paint(mOuterPaint);//由外圆画笔直接copy一份属性出来
        mInnerPaint.setColor(mInnerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽高不一致时，取最小值确保是个正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(width, height);
        setMeasuredDimension(minSize, minSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - mBorderWidth / 2;
        //画外圆弧
        final RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mOuterPaint);

        //画内圆弧  不能写死
        if (mStepMax == 0) return;
        float sweepAngle = (float) mCurrentStep / mStepMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);

        //画文字
        String stepText = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        //基线baseLine
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    //添加动画
    //防止多线程访问错误
    public synchronized void setStepMax(int stepMax) {
        this.mStepMax = stepMax;
    }

    public void setCurrentStep(int currentStep) {
        if (currentStep > mStepMax) {
            currentStep = mStepMax;
        }
        this.mCurrentStep = currentStep;
        //不断绘制
        invalidate();
    }
}
