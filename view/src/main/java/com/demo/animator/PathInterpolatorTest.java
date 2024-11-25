package com.demo.animator;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;

public class PathInterpolatorTest {

    @ViewCase(label = "PathInterpolator", description = "PathInterpolator测试")
    public static void tesPathInterpolator(Context context, ViewGroup parent) {
        DrawTrackView view = new DrawTrackView(context);
        parent.addView(view);
    }
}
