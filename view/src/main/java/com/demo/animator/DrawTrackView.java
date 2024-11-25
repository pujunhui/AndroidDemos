package com.demo.animator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawTrackView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private float lastX = -1;
    private float lastY = -1;
    private ObjectAnimator pathAnimator;
    private float trackX = -1;
    private float trackY = -1;

    public DrawTrackView(Context context) {
        this(context, null);
    }

    public DrawTrackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (pathAnimator != null) {
                pathAnimator.cancel();
                pathAnimator = null;
            }
            trackX = -1;
            trackY = -1;
            path.reset();

            path.moveTo(x, y);
            lastX = x;
            lastY = y;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.quadTo(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //start Animator
            pathAnimator = ObjectAnimator.ofFloat(this, "trackX", "trackY", path);
            pathAnimator.setDuration(5000);
            pathAnimator.start();
        }
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        if (trackX != -1 && trackY != -1) {
            canvas.drawCircle(trackX, trackY, 10, paint);
        }
    }

    public float getTrackX() {
        return trackX;
    }

    public void setTrackX(float trackX) {
        this.trackX = trackX;
        postInvalidate();
    }

    public float getTrackY() {
        return trackY;
    }

    public void setTrackY(float trackY) {
        this.trackY = trackY;
        postInvalidate();
    }
}
