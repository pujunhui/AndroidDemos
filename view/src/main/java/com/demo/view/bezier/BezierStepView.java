package com.demo.view.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 曲线绘制
 */
public class BezierStepView extends View {
    private final Paint mPaint, mLinePointPaint;
    private final Path mPath;

    //控制点集
    private final List<PointF> mControlPoints = new ArrayList<>();

    public BezierStepView(Context context) {
        this(context, null);
    }

    public BezierStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLinePointPaint = new Paint();
        mLinePointPaint.setAntiAlias(true);
        mLinePointPaint.setStrokeWidth(4);
        mLinePointPaint.setStyle(Paint.Style.FILL);
        mLinePointPaint.setColor(Color.GRAY);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        refreshPoint();
    }

    private void refreshPoint() {
        Random random = new Random();
        mControlPoints.clear();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(600);
            int y = random.nextInt(600);
            PointF pointF = new PointF(x, y);
            mControlPoints.add(pointF);
        }

        mPath.rewind();

        int order = mControlPoints.size() - 1;
        float delta = 1.0f / 1000;
        for (float t = 0; t <= 1; t += delta) {
            // Bezier点集
            float x = deCasteljauX(order, 0, t);
            float y = deCasteljauY(order, 0, t);
            if (t == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        // 控制点和控制点连线
        int size = mControlPoints.size();
        for (int i = 0; i < size; i++) {
            PointF point = mControlPoints.get(i);
            // 绘制控制点
            canvas.drawCircle(point.x, point.y, 12, mLinePointPaint);

            // 控制点连线
            if (i != size - 1) {
                PointF nextPoint = mControlPoints.get(i + 1);
                // 控制点连线
                canvas.drawLine(point.x, point.y, nextPoint.x, nextPoint.y, mLinePointPaint);
            }
        }

        //贝塞尔曲线绘制
        canvas.drawPath(mPath, mPaint);
    }


    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private float deCasteljauX(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).x + t * mControlPoints.get(j + 1).x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t) + t * deCasteljauX(i - 1, j + 1, t);
    }

    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private float deCasteljauY(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).y + t * mControlPoints.get(j + 1).y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t) + t * deCasteljauY(i - 1, j + 1, t);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            refreshPoint();
        }
        return true;
    }
}
