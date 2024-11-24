package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;

public class LetterSideBar extends View {
    private final Paint mNormalPaint;
    private final Paint mFocusPaint;
    private static final String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    //当前触摸位置字母
    private String mCurrentTouchLetter;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);
        float textSize = array.getDimension(R.styleable.LetterSideBar_barTextSize, sp2px(12));
        int textColor = array.getColor(R.styleable.LetterSideBar_barTextColor, Color.BLUE);
        array.recycle();
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setTextSize(textSize);
        mNormalPaint.setColor(textColor);
        mNormalPaint.setTextAlign(Paint.Align.CENTER);

        mFocusPaint = new Paint(mNormalPaint);
        mFocusPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算指定宽度
        float textWidth = mNormalPaint.measureText("W");
        int width = (int) (getPaddingLeft() + getPaddingRight() + textWidth);
        //高度可以直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;

        for (int i = 0; i < mLetters.length; i++) {
            int letterCenterY = itemHeight * i + itemHeight / 2;
            //基线
            Paint.FontMetrics fontMetrics = mNormalPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
            int baseLine = letterCenterY + dy;
            int x = getWidth() / 2;
            if (mLetters[i].equals(mCurrentTouchLetter)) {
                canvas.drawText(mLetters[i], x, baseLine, mFocusPaint);
            } else {
                canvas.drawText(mLetters[i], x, baseLine, mNormalPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前触摸字母
                float currentMoveY = event.getY();
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int currentPosition = (int) ((currentMoveY - getPaddingTop()) / itemHeight);

                currentPosition = MathUtils.clamp(currentPosition, 0, mLetters.length - 1);
                String currentTouchLetter = mLetters[currentPosition];
                if (!currentTouchLetter.equals(mCurrentTouchLetter)) {
                    if (mListener != null) {
                        mListener.touch(currentTouchLetter);
                    }
                    mCurrentTouchLetter = currentTouchLetter;
                    //重新绘制
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentTouchLetter = null;
                if (mListener != null) {
                    mListener.touch(null);
                }
                invalidate();
                break;
        }
        return true;
    }

    private LetterTouchListener mListener;

    public void setOnLetterTouchListener(LetterTouchListener listener) {
        this.mListener = listener;
    }

    public interface LetterTouchListener {
        void touch(@Nullable CharSequence letter);
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
