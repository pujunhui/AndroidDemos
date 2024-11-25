package com.demo.view.xfermode.test;

import android.content.Context;
import android.view.ViewGroup;

import com.demo.view.arch.ViewCase;
import com.demo.view.xfermode.ScratchCard;

public class ScratchCardTest {
    @ViewCase(label = "ScratchCard", description = "刮刮卡")
    public static void testScratchCard(Context context, ViewGroup parent) {
        ScratchCard view = new ScratchCard(context);
        parent.addView(view);
    }
}
