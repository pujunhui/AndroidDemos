package com.demo.view.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GalleryHorizontalScrollView extends HorizontalScrollView {
    private static final String TAG = "barry";
    private LinearLayout container;
    private int centerX;
    private int icon_width;

    public GalleryHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public GalleryHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //在ScrollView里面放置一个水平线性布局，再往里面放置很多ImageView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        container = new LinearLayout(getContext());
        container.setLayoutParams(params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //得到某一张图片的宽度
        View v = container.getChildAt(0);
        icon_width = v.getWidth();
        Log.d(TAG, "icon_width = " + icon_width);
        //得到hzv的中间x坐标
        centerX = getWidth() / 2;
        Log.d(TAG, "centerX = " + centerX);
        //处理下，中心坐标改为中心图片的左边界
        centerX = centerX - icon_width / 2;
        //给LinearLayout和hzv之间设置边框距离
        container.setPadding(centerX, 0, centerX, 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 渐变效果
        //得到hzv滑出去的距离
        int scrollX = l;
        //找到两张渐变的图片的下标--左，右
        int index_left = scrollX / icon_width;
        int index_right = index_left + 1;
        //设置图片的level
        for (int i = 0; i < container.getChildCount(); i++) {
            if (i == index_left || i == index_right) {
                //变化
                //比例：

                float ratio = 5000f / icon_width;
                ImageView iv_left = (ImageView) container.getChildAt(index_left);
                //scrollX%icon_width:代表滑出去的距离
                //滑出去了icon_width/2  icon_width/2%icon_width
                iv_left.setImageLevel(
                        //代表的是，我滑动之后的距离在5000份当中的份额
                        (int) (5000 - scrollX % icon_width * ratio)
                );
                //右边
                if (index_right < container.getChildCount()) {
                    ImageView iv_right = (ImageView) container.getChildAt(index_right);
                    //scrollX%icon_width:代表滑出去的距离
                    //滑出去了icon_width/2  icon_width/2%icon_width
                    iv_right.setImageLevel(
                            (int) (10000 - scrollX % icon_width * ratio)
                    );
                }
            } else {
                //灰色
                ImageView iv = (ImageView) container.getChildAt(i);
                iv.setImageLevel(0);
            }
        }
    }

    //添加图片的方法
    public void addImageViews(Drawable[] revealDrawables) {
        for (int i = 0; i < revealDrawables.length; i++) {
            ImageView img = new ImageView(getContext());
            img.setImageDrawable(revealDrawables[i]);
            container.addView(img);
            if (i == 0) {
                img.setImageLevel(5000);
            }
        }
        addView(container);
    }
}
