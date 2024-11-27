package com.demo.animator;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;

public class PathInterpolatorTest {

    @ViewCase(label = "PathInterpolator", description = "PathInterpolator测试")
    public static void tesPathInterpolator(Context context, ViewGroup parent) {
        TouchTrackView view = new TouchTrackView(context);
        parent.addView(view);
    }
}
