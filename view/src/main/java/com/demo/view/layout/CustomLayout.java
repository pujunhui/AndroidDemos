package com.demo.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.demo.view.R;

public class CustomLayout extends ViewGroup {
    public CustomLayout(Context context) {
        this(context, null);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得父容器给予的mode和size
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int layoutWidth = 0;
        int layoutHeight = 0;
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount();
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果布局容器的宽度模式是确定的（具体的size或者match_parent），直接使用父窗体建议的宽度
            layoutWidth = sizeWidth;
        } else {
            //如果是未指定或者wrap_content，我们都按照包裹内容做，宽度方向上只需要拿到所有子控件中宽度最大的作为布局宽度
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int width = child.getMeasuredWidth();
                //获取子控件最大宽度
                layoutWidth = Math.max(width, layoutWidth);
            }
        }
        //高度很宽度处理思想一样
        if (heightMode == MeasureSpec.EXACTLY) {
            layoutHeight = sizeHeight;
        } else {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int height = child.getMeasuredHeight();
                layoutHeight = Math.max(height, layoutHeight);
            }
        }

        // 测量并保存layout的宽高
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0, top = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // 注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            CustomLayoutParams params = (CustomLayoutParams) child.getLayoutParams();
            switch (params.position) {
                case CustomLayoutParams.POSITION_LEFT_TOP:
                case CustomLayoutParams.POSITION_LEFT_CENTER:
                case CustomLayoutParams.POSITION_LEFT_BOTTOM:
                    left = 0;
                    break;
                case CustomLayoutParams.POSITION_TOP_CENTER:
                case CustomLayoutParams.POSITION_CENTER:
                case CustomLayoutParams.POSITION_BOTTOM_CENTER:
                    left = (getWidth() - childWidth) / 2;
                    break;
                case CustomLayoutParams.POSITION_RIGHT_TOP:
                case CustomLayoutParams.POSITION_RIGHT_CENTER:
                case CustomLayoutParams.POSITION_RIGHT_BOTTOM:
                    left = getWidth() - childWidth;
                    break;
            }
            switch (params.position) {
                case CustomLayoutParams.POSITION_LEFT_TOP:
                case CustomLayoutParams.POSITION_TOP_CENTER:
                case CustomLayoutParams.POSITION_RIGHT_TOP:
                    top = 0;
                    break;
                case CustomLayoutParams.POSITION_LEFT_CENTER:
                case CustomLayoutParams.POSITION_CENTER:
                case CustomLayoutParams.POSITION_RIGHT_CENTER:
                    top = (getHeight() - childHeight) / 2;
                    break;
                case CustomLayoutParams.POSITION_LEFT_BOTTOM:
                case CustomLayoutParams.POSITION_BOTTOM_CENTER:
                case CustomLayoutParams.POSITION_RIGHT_BOTTOM:
                    top = getHeight() - childHeight;
                    break;
            }
            // 确定子控件的位置，四个参数分别代表（左上右下）点的坐标值
            child.layout(left, top, left + childWidth, top + childHeight);
        }
    }

    public static class CustomLayoutParams extends MarginLayoutParams {
        public static final int POSITION_LEFT_TOP = 0; // 左上
        public static final int POSITION_TOP_CENTER = 1; // 上中
        public static final int POSITION_RIGHT_TOP = 2; // 右上

        public static final int POSITION_LEFT_CENTER = 3; // 左中
        public static final int POSITION_CENTER = 4; // 中间
        public static final int POSITION_RIGHT_CENTER = 5; // 右中

        public static final int POSITION_LEFT_BOTTOM = 6; // 左下
        public static final int POSITION_BOTTOM_CENTER = 7; // 下中
        public static final int POSITION_RIGHT_BOTTOM = 8; // 右下

        private int position = POSITION_LEFT_TOP;  // 默认左上角

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.CustomLayout_Layout);
            position = array.getInt(R.styleable.CustomLayout_Layout_layout_position, position);
            array.recycle();
        }

        public CustomLayoutParams(int width, int height) {
            super(width, height);
        }

        public CustomLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(LayoutParams source) {
            super(source);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}