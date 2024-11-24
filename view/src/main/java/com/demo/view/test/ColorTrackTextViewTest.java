package com.demo.view.test;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.view.ColorTrackTextView;
import com.demo.view.R;
import com.demo.view.arch.ViewCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Time:2019/3/13
 * Author:蒲俊辉
 * Description:文字变色
 */
public class ColorTrackTextViewTest {

    @ViewCase(label = "ColorTrackTextView", description = "渐变文字")
    public static void testColorTrackTextView(Context context, ViewGroup parent) {
        final String[] items = {"直播", "推荐", "视频", "图片", "段子", "精华"};
        View view = View.inflate(context, R.layout.test_color_track_text_view, parent);

        LinearLayout indicatorContainer = view.findViewById(R.id.indicator_view);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        final List<ColorTrackTextView> indicators = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            // 动态添加颜色跟踪的TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(context);
            // 设置颜色
            colorTrackTextView.setTextSize(20);
            colorTrackTextView.setChangeColor(Color.RED);
            colorTrackTextView.setText(items[i]);
            colorTrackTextView.setLayoutParams(params);
            // 把新的加入LinearLayout容器
            indicatorContainer.addView(colorTrackTextView);
            indicators.add(colorTrackTextView);
        }
        viewPager.setAdapter(new TextPagerAdapter(context, items));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("TAG", "position -> " + position + "  positionOffset -> " + positionOffset);
                // position 代表当前的位置
                // positionOffset 代表滚动的 0 - 1 百分比

                // 1.左边  位置 position
                ColorTrackTextView left = indicators.get(position);
                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                left.setCurrentProgress(1 - positionOffset);
                try {
                    ColorTrackTextView right = indicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class TextPagerAdapter extends PagerAdapter {
        private final Context context;
        private final String[] texts;

        private TextPagerAdapter(Context context, String[] texts) {
            this.context = context;
            this.texts = texts;
        }

        @Override
        public int getCount() {
            return texts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[position]);
            textView.setTextSize(30);
            container.addView(textView, params);
            return textView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
