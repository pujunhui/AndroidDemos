package com.demo.view.compass;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.demo.view.R;

public class CompassView extends View {
    private float mBearing;
    private Paint markerPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private String northString;
    private String eastString;
    private String southString;
    private String westString;
    private int textHeight;

    public CompassView(Context context) {
        this(context, null);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CompassView, defStyleAttr, 0);
        if (array.hasValue(R.styleable.CompassView_bearing)) {
            setBearing(array.getFloat(R.styleable.CompassView_bearing, 0));
        }
        array.recycle();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.background_color));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Resources resources = getResources();
        northString = resources.getString(R.string.cardinal_north);
        eastString = resources.getString(R.string.cardinal_east);
        southString = resources.getString(R.string.cardinal_south);
        westString = resources.getString(R.string.cardinal_west);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(context, R.color.text_color));

        textHeight = (int) textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(ContextCompat.getColor(context, R.color.marker_color));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);
        int d = Math.min(measureWidth, measureHeight);
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    public void setBearing(float bearing) {
        mBearing = bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    public float getBearing() {
        return mBearing;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.dispatchPopulateAccessibilityEvent(event);
        if (isShown()) {
            String bearingStr = String.valueOf(mBearing);
            event.getText().add(bearingStr);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();

        int px = measureWidth / 2;
        int py = measureHeight / 2;

        int radius = Math.min(px, py);

        canvas.drawCircle(px, py, radius, circlePaint);

        canvas.save();
        canvas.rotate(-mBearing, px, py);

        int textWidth = (int) textPaint.measureText("W");
        int cardinalX = px - textWidth / 2;
        int cardinalY = py - radius + textHeight;

        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);
            canvas.save();
            canvas.translate(0, textHeight);

            if (i % 6 == 0) {
                String dirString = "";
                switch (i) {
                    case 0: {
                        dirString = northString;
                        int arrowY = 2 * textHeight;
                        canvas.drawLine(px, arrowY, px - 5, 3 * textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px + 5, 3 * textHeight, markerPaint);
                        break;
                    }
                    case 6:
                        dirString = eastString;
                        break;
                    case 12:
                        dirString = southString;
                        break;
                    case 18:
                        dirString = westString;
                        break;
                }
                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
            } else if (i % 3 == 0) {
                String angle = String.valueOf(i * 15);
                float angleTextWidth = textPaint.measureText(angle);

                int angleTextX = (int) (px - angleTextWidth / 2);
                int angleTextY = py - radius + textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            canvas.restore();
            canvas.rotate(15, px, py);
        }
        canvas.restore();
    }
}
