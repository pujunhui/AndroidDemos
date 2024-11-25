package com.demo.view.layout.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.demo.view.R;
import com.demo.view.arch.ViewCase;

public class CustomLayoutTest {
    @ViewCase(label = "CustomLayout", description = "自定义布局")
    public static void testFilterView(Context context, ViewGroup parent) {
        View.inflate(context, R.layout.test_custom_layout, parent);
    }
}
