package com.demo.view.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.demo.view.R;
import com.demo.view.WaveBitmapDrawable;
import com.demo.view.arch.ViewCase;

public class WaveBitmapDrawableTest {

    @ViewCase(label = "WaveBitmapDrawableTest", description = "波浪进度图片")
    public static void testWaveBitmapDrawable(Context context, ViewGroup parent) {
        View view = View.inflate(context, R.layout.test_wave_bitmap_drawable, parent);
        Resources resources = view.getResources();
        ImageView imageView = view.findViewById(R.id.image_view);
        final WaveBitmapDrawable drawable = new WaveBitmapDrawable(resources);
        drawable.setCornerRadius(20);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.test);
        drawable.setBackground(new BitmapDrawable(resources, bitmap));
        drawable.setWaveColor(Color.BLACK);
        drawable.setWaveAlpha(100);
        drawable.setReverse(true);
        imageView.setImageDrawable(drawable);

        ValueAnimator animator = ValueAnimator.ofFloat(1f)
                .setDuration(10000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                drawable.start();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                drawable.stop();
            }

            @Override
            public void onAnimationResume(Animator animation) {
                drawable.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drawable.stop();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                drawable.setProgress(progress);
            }
        });
        view.findViewById(R.id.start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animator.isPaused()) {
                    animator.resume();
                } else if (!animator.isStarted()) {
                    animator.start();
                }
            }
        });
        view.findViewById(R.id.pause_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.pause();
            }
        });
    }
}
