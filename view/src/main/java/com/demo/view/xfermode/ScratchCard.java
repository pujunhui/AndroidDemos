package com.demo.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.view.R;

public class ScratchCard extends View {
    private Paint mBitPaint;
    private Bitmap mBmpDST, mBmpSRC, mBmpText;
    private Path mPath;
    private float mPreX, mPreY;

    public ScratchCard(Context context) {
        this(context, null);
    }

    public ScratchCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        mBitPaint.setColor(Color.RED);
        mBitPaint.setStyle(Paint.Style.STROKE);
        mBitPaint.setStrokeWidth(45);

        mBmpText = BitmapFactory.decodeResource(getResources(), R.drawable.guaguaka_text1, null);
        mBmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.guaguaka, null);
        mBmpDST = Bitmap.createBitmap(mBmpSRC.getWidth(), mBmpSRC.getHeight(), Bitmap.Config.ARGB_8888);
        mPath = new Path();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBmpText, 0, 0, mBitPaint);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        //先把手指轨迹画到目标Bitmap上
        Canvas c = new Canvas(mBmpDST);
        c.drawPath(mPath, mBitPaint);

        //然后把目标图像画到画布上
        canvas.drawBitmap(mBmpDST, 0, 0, mBitPaint);

        //计算源图像区域
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(mBmpSRC, 0, 0, mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }
}
