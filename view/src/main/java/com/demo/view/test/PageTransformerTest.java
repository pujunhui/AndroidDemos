package com.demo.view.test;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.view.R;
import com.demo.view.arch.ViewCase;

public class PageTransformerTest {
    private static final int[] images = {
            R.drawable.test_1,
            R.drawable.test_2,
            R.drawable.test_3,
            R.drawable.test_4,
            R.drawable.test_5
    };

    @ViewCase(label = "PageTransformer", description = "Page动画")
    public static void testPageTransformer(Context context, ViewGroup parent) {
        ViewPager viewPager = new ViewPager(context);
        parent.addView(viewPager);
        viewPager.setAdapter(new ImagePagerAdapter(context, images));
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.9f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                ViewParent parent = page.getParent();
                if (parent instanceof ViewPager) {
                    ViewPager viewPager = (ViewPager) parent;
                    int paddingLeft = viewPager.getPaddingLeft();
                    int paddingRight = viewPager.getPaddingRight();
                    int childWidth = viewPager.getMeasuredWidth() - paddingLeft - paddingRight;
                    int scrollX = viewPager.getScrollX();
                    int childLeft = (int) (position * childWidth + scrollX);
                    float newPosition = (float) (childLeft - paddingLeft - scrollX) / childWidth;
                    Log.d("PUJH", "page:" + page + ",pos:" + newPosition);
                    if (-1 < newPosition && newPosition < 1) {
                        float scaleX = Math.max(MIN_SCALE, 1 - Math.abs(newPosition));
                        page.setScaleX(scaleX);
                        page.setScaleY(scaleX);
                        page.setAlpha(Math.max(MIN_ALPHA, 1 - Math.abs(newPosition)));
                    } else {
                        page.setScaleX(MIN_SCALE);
                        page.setScaleY(MIN_SCALE);
                        page.setAlpha(MIN_ALPHA);
                    }
                }

            }
        });
    }

    private static class ImagePagerAdapter extends PagerAdapter {
        private final Context context;
        private final int[] images;

        private ImagePagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[position]);
            container.addView(imageView, params);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
