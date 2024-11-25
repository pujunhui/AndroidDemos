package com.demo.view.xfermode.test;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;
import com.demo.view.xfermode.XfermodeView;

public class XfermodeViewTest {
    @ViewCase(label = "XfermodeView", description = "Xfermode演示")
    public static void testXfermodeView(Context context, ViewGroup parent) {
        XfermodeView view = new XfermodeView(context);
        parent.addView(view);
    }
}
