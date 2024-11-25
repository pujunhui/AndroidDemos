package com.demo.view.drawable.test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.demo.view.R;
import com.demo.view.arch.ViewCase;
import com.demo.view.drawable.GalleryHorizontalScrollView;
import com.demo.view.drawable.RevealDrawable;

public class GalleryHorizontalScrollViewTest {
    private static int[] mImgIds = new int[]{ //7个
            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline,

            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline
    };
    private static int[] mImgIds_active = new int[]{
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active,
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active
    };

    public static Drawable[] revealDrawables = new Drawable[mImgIds.length];

    @ViewCase(label = "GalleryHorizontalScrollView", description = "横向滚动View")
    public static void testGalleryHorizontalScrollView(Context context, ViewGroup parent) {
        for (int i = 0; i < mImgIds.length; i++) {
            RevealDrawable rd = new RevealDrawable(
                    context.getResources().getDrawable(mImgIds[i]),
                    context.getResources().getDrawable(mImgIds_active[i]),
                    RevealDrawable.HORIZONTAL);
            revealDrawables[i] = rd;
        }
        GalleryHorizontalScrollView hzv = new GalleryHorizontalScrollView(context);
        hzv.addImageViews(revealDrawables);
        parent.addView(hzv);
    }
}
