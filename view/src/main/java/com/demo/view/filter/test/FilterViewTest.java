package com.demo.view.filter.test;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;
import com.demo.view.filter.FilterView;

public class FilterViewTest {

    @ViewCase(label = "FilterView", description = "过滤器")
    public static void testFilterView(Context context, ViewGroup parent) {
        FilterView filterView = new FilterView(context);
        parent.addView(filterView);
    }
}
