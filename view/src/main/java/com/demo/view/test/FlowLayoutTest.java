package com.demo.view.test;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.view.FlowLayout;
import com.demo.view.arch.ViewCase;

import java.util.Random;

public class FlowLayoutTest {

    @ViewCase(label = "FlowLayout", description = "流式布局")
    public static void testFlowLayout(Context context, ViewGroup parent) {
        FlowLayout layout = new FlowLayout(parent.getContext());
        final String[] randomTexts = {
                "Hello, World!",
                "This is a random text.",
                "Android development is fun!",
                "Keep coding!",
                "Randomness is key.",
                "Explore the world of Android.",
                "TextView example.",
                "Learn, build, and grow.",
                "Happy coding!",
                "Embrace the chaos.",
                "Every line of code counts.",
                "Stay curious!",
                "Innovation through code.",
                "Dream in code.",
                "Create your own path.",
                "The future is bright.",
                "Code is poetry.",
                "Let’s build something great!",
                "Coding is an adventure.",
                "Find joy in coding.",
                "Random text for your view."
        };
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            TextView textView = new TextView(context);
            textView.setTextSize(18);
            //随机文字
            String randomText = randomTexts[random.nextInt(randomTexts.length)];
            textView.setText(randomText);
            //随机背景颜色
            int randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            textView.setBackgroundColor(randomColor);
            //随机边距
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = random.nextInt(10);
            params.rightMargin = random.nextInt(10);
            params.topMargin = random.nextInt(30);
            params.bottomMargin = random.nextInt(30);
            layout.addView(textView, params);
        }
        parent.addView(layout);
    }
}
