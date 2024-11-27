package com.demo.view.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.view.R;

public class LoadingView extends View {
    private Bitmap mBitmap;                                  // 箭头图片
    private final Matrix mMatrix = new Matrix();             // 矩阵,用于对图片进行一些操作
    private Paint mDeafultPaint;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;

    private float mAnimatorValue; // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private final float[] pos = new float[2];                // 当前点的实际位置
    private final float[] tan = new float[2];                // 当前点的tangent值,用于计算图片所需旋转的角度

    private PathMeasure pathMeasure;

    private final Path outPath = new Path();


    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);

        mDeafultPaint = new Paint();
        mDeafultPaint.setColor(Color.RED);
        mDeafultPaint.setStrokeWidth(5);
        mDeafultPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        Path path = new Path();
        path.reset();
        //Path.Direction.CW顺时针方向
        path.addCircle(0, 0, 200, Path.Direction.CCW);

        pathMeasure = new PathMeasure(path, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        float length = pathMeasure.getLength();

        float distance = length * mAnimatorValue;
        pathMeasure.getPosTan(distance, pos, tan);

        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);

        mMatrix.reset();
        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);

        outPath.reset();

        float stop = length * mAnimatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * pathMeasure.getLength()));
        pathMeasure.getSegment(start, stop, outPath, true);

        canvas.drawPath(outPath, mDeafultPaint);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        invalidate();
    }
}
