package com.demo.view.path.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;
import com.demo.view.path.LoadingView;

public class PathTest {
    @ViewCase(label = "Path", description = "路径")
    public static void testPath(Context context, ViewGroup parent) {
        View view = new LoadingView(context);
        parent.addView(view);
    }
}
