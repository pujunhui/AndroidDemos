package com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class RatingBar extends View {
    private Bitmap mStartNormalBitmap;
    private Bitmap mStartFocusBitmap;
    private int mGradeNumber = 5;
    private int currentGrade = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int startNormalId = array.getResourceId(R.styleable.RatingBar_startNormal, android.R.drawable.star_off);
        mStartNormalBitmap = BitmapFactory.decodeResource(getResources(), startNormalId);
        int startFocusId = array.getResourceId(R.styleable.RatingBar_startFocus, android.R.drawable.star_on);
        mStartFocusBitmap = BitmapFactory.decodeResource(getResources(), startFocusId);
        mGradeNumber = array.getInt(R.styleable.RatingBar_gradeNumber, mGradeNumber);
        array.recycle();
    }

    @Override
    protected void onMeasure(@NonNull int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度    一张图片的高度
        int height = mStartFocusBitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        int width = (mStartFocusBitmap.getWidth() + getPaddingLeft() + getPaddingRight()) * mGradeNumber;//还需要加间隔
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mGradeNumber; i++) {
            int x = i * mStartFocusBitmap.getWidth() + getPaddingLeft() * (i + 1) + getPaddingRight() * i;
            if (i < currentGrade)
                canvas.drawBitmap(mStartFocusBitmap, x, getPaddingTop(), null);
            else
                canvas.drawBitmap(mStartNormalBitmap, x, getPaddingTop(), null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();

                int grade = (int) Math.ceil(moveX / (mStartFocusBitmap.getWidth() + getPaddingLeft() + getPaddingRight()));
                if (currentGrade != grade) {
                    currentGrade = grade;
                    invalidate();
                }
        }
        return true;
    }
}
