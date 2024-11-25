package com.demo.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.demo.view.R;

/**
 * @author barry
 * @version V1.0
 * @time 2018-6-20
 */
public class InvertedImageView_SRCATOP extends View {
    private Paint mBitPaint;
    private Bitmap mBmpDST, mBmpSRC, mBmpRevert;

    public InvertedImageView_SRCATOP(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        mBmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.invert_shade, null);
        mBmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.test, null);

        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        // 生成倒影图
        mBmpRevert = Bitmap.createBitmap(mBmpSRC, 0, 0, mBmpSRC.getWidth(), mBmpSRC.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        //先画出小狗图片
        canvas.drawBitmap(mBmpSRC, 0, 0, mBitPaint);

        //再画出倒影
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.translate(0, mBmpSRC.getHeight());

        canvas.drawBitmap(mBmpDST, 0, 0, mBitPaint);
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBmpRevert, 0, 0, mBitPaint);

        mBitPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }
}
