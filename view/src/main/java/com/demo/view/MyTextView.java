package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class MyTextView extends View {
    private String text;
    private int textColor = Color.BLACK;
    private int textSize = 15;
    private Rect textBounds = new Rect();
    private Paint paint;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextView);
        text = array.getString(R.styleable.TextView_text);
        textColor = array.getColor(R.styleable.TextView_textColor, textColor);
        textSize = array.getDimensionPixelSize(R.styleable.TextView_textSize, sp2px(textSize));
        array.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST | heightMode == MeasureSpec.AT_MOST) {
            paint.getTextBounds(text, 0, text.length(), textBounds);
            if (widthMode == MeasureSpec.AT_MOST) {
                widthSize = textBounds.width() + getPaddingLeft() + getPaddingRight();
            }
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = textBounds.height() + getPaddingTop() + getPaddingBottom();
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        int dx = getPaddingLeft();
        int dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
        int baseline = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 + dy + getPaddingTop();
        canvas.drawText(text, dx, baseline, paint);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}
