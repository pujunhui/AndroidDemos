package com.demo.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
public class LightBookView extends View {
    private Paint mBitPaint;
    private Bitmap mBmpDST, mBmpSRC;

    public LightBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        mBmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.book_bg, null);
        mBmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.book_light, null);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(mBmpDST, 0, 0, mBitPaint);
        //目标
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        //源
        canvas.drawBitmap(mBmpSRC, 0, 0, mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

    }
}
