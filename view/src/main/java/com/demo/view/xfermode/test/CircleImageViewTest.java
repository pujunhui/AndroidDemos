package com.demo.view.xfermode.test;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.R;
import com.demo.view.arch.ViewCase;
import com.demo.view.xfermode.CircleImageView;

public class CircleImageViewTest {
    @ViewCase(label = "CircleImageView", description = "圆形图片")
    public static void testCircleImageView(Context context, ViewGroup parent) {
        CircleImageView view = new CircleImageView(context);
        view.setImageResource(R.drawable.test);
        parent.addView(view);
    }
}
