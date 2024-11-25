package com.demo.view.xfermode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CircleImageView extends ImageView {
    private final Paint paint = new Paint();

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        int saveCount1 = canvas.saveLayer(centerX - radius, centerY - radius, centerX + radius, centerY + radius, null);
        canvas.drawCircle(centerX, centerY, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int saveCount2 = canvas.saveLayer(null, paint);
        super.onDraw(canvas);
        paint.setXfermode(null);
        canvas.restoreToCount(saveCount2);

        canvas.restoreToCount(saveCount1);
    }
}
