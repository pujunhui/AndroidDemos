package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Time:2019/3/13
 * Author:蒲俊辉
 * Description:文字变色
 */
public class ColorTrackTextView extends AppCompatTextView {
    //绘制不变颜色字体的画笔
    private Paint mOriginPaint;
    //绘制变色字体的的画笔
    private Paint mChangePaint;
    //当前的进度
    private float mCurrentProgress = 0f;
    //不同朝向
    private Direction mDirection;

    //朝向的枚举类型
    public enum Direction {
        RIGHT_TO_LEFT, LEFT_TO_RIGHT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());
        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);
        array.recycle();
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        //设置颜色
        paint.setColor(color);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //设置字体的大小，也就是TextView的字体大小
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        mOriginPaint.setTextSize(sp2px(size));
        mChangePaint.setTextSize(sp2px(size));
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        //根据进度把中间值算出来
        int middle = (int) (mCurrentProgress * getWidth());

        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //绘制变色文字
            drawText(canvas, mChangePaint, 0, middle);
            //绘制默认文字
            drawText(canvas, mOriginPaint, middle, getWidth());
        } else {
            //绘制变色文字
            drawText(canvas, mOriginPaint, 0, getWidth() - middle);
            //绘制默认文字
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
        }
    }

    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        //裁剪区域
        canvas.clipRect(start, 0, end, getHeight());
        //自己绘制两种颜色的文字
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //获取字体的宽度
        int x = (getWidth() - bounds.width()) / 2;
        //基线baseLine
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, paint);
        canvas.restore();
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }

    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }
}
