package com.demo.view.bezier.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;
import com.demo.view.bezier.BezierStepView;
import com.demo.view.bezier.DragBubbleView;

public class BezierViewTest {
    @ViewCase(label = "BezierStepView", description = "贝塞尔曲线")
    public static void testBezierView(Context context, ViewGroup parent) {
        View view = new BezierStepView(context);
        parent.addView(view);
    }

    @ViewCase(label = "DragBubbleView", description = "仿QQ消息气泡")
    public static void testDragBubbleView(Context context, ViewGroup parent) {
        View view = new DragBubbleView(context);
        parent.addView(view);
    }
}
