package com.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义流式布局
 */
public class FlowLayout extends ViewGroup {
    private final List<LineInfo> flowLineInfo = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        flowLineInfo.clear();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //让每个子View计算自身最小的尺寸
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
        int parentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        measureChildren(parentWidthSpec, parentHeightSpec);

        int lineWidth = 0; //当前行累计宽度
        int lineHeight = 0; //当前行最大高度
        int lineChildCount = 0; //当前行容纳View个数
        int totalHeight = 0; //所有行累计高度

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                lineChildCount++;
                continue;
            }
            MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();
            if (lineWidth + child.getMeasuredWidth() + childParams.leftMargin <= widthSize) {
                //当前行还能容纳当前的子View
                lineWidth += child.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin);
                lineChildCount++;
            } else {
                //需要换行，先将上一行的行高和子元素个数保存起来
                totalHeight += lineHeight;
                flowLineInfo.add(new LineInfo(lineHeight, lineChildCount));
                lineWidth = child.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
                lineHeight = child.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;
                lineChildCount = 1;
            }
            //如果是最后一个
            if (i == getChildCount() - 1) {
                totalHeight += lineHeight;
                flowLineInfo.add(new LineInfo(lineHeight, lineChildCount));
            }
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, totalHeight);
        } else {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int baseHeight = 0; //当前行基础高度位置
        int childIndex = 0; //View累计序号
        int totalWidth = 0;

        for (LineInfo lineInfo : flowLineInfo) {
            int endIndex = childIndex + lineInfo.childCount;
            for (; childIndex < endIndex; childIndex++) {
                View child = getChildAt(childIndex);
                MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int left = totalWidth + childParams.leftMargin;
                int top = baseHeight + (lineInfo.lineHeight - childParams.topMargin - childParams.bottomMargin - height) / 2 + childParams.topMargin;
                int right = left + width;
                int bottom = top + height;
                child.layout(left, top, right, bottom);
                totalWidth += (width + childParams.leftMargin + childParams.rightMargin);
            }
            baseHeight += lineInfo.lineHeight;
            totalWidth = 0;
        }
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    private static final class LineInfo {
        private final int lineHeight;
        private final int childCount;

        private LineInfo(int lineHeight, int childCount) {
            this.lineHeight = lineHeight;
            this.childCount = childCount;
        }
    }
}
