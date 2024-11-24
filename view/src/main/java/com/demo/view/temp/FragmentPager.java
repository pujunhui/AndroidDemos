package com.demo.view.temp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import androidx.fragment.app.Fragment;

public class FragmentPager extends ViewGroup {
    private static final String TAG = "FragmentPager";

    public static final int SCROLL_DISABLE = 0;
    public static final int SCROLL_HORIZONTAL = 1;
    public static final int SCROLL_VERTICAL = 2;
    public static final int SCROLL_BOTH = SCROLL_HORIZONTAL | SCROLL_VERTICAL;

    public static final int ANIM_NULL = 0;
    public static final int ANIM_LEFT_TO_RIGHT = 1;
    public static final int ANIM_RIGHT_TO_LEFT = 2;
    public static final int ANIM_UP_TO_DOWN = 3;
    public static final int ANIM_DOWN_TO_UP = 4;

    /*
     * The direction in which the finger is allowed to slide
     */
    private int mScrollOrient = SCROLL_HORIZONTAL;
    /*
     * Animation direction when Fragment switches
     */
    private int mSwitchAnim = ANIM_NULL;
    private Fragment mCurrentFragment = null;
    private GestureDetector mGestureDetector = null;
    private Scroller mScroller = null;
    private Context mContext;

    public FragmentPager(Context context) {
        this(context, null);
    }

    public FragmentPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentPager(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FragmentPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;

        for (int i = 0; i < colors.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundColor(colors[i]);
            this.addView(imageView);
        }

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if ((mScrollOrient & SCROLL_BOTH) == SCROLL_BOTH) {
                    scrollBy((int) distanceX, (int) distanceY);
                } else if ((mScrollOrient & SCROLL_HORIZONTAL) == SCROLL_HORIZONTAL) {
                    scrollBy((int) distanceX, 0);
                } else if ((mScrollOrient & SCROLL_VERTICAL) == SCROLL_VERTICAL) {
                    scrollBy(0, (int) distanceY);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        mScroller = new Scroller(mContext);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(i * getWidth(), t, (i + 1) * getWidth(), b);
        }
    }

    public void setScrollOrient(int orient) {
        mScrollOrient = orient;
    }

    public void switchFragment(Fragment newFragment, String orientation) {

    }

    private int position = 0;
    private int scrollX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ACTION_MOVE", "scrollX=" + getScrollX());
                scrollX = getScrollX();//相对于初始位置滑动的距离
                //你滑动的距离加上屏幕的一半，除以屏幕宽度，就是当前图片显示的pos.如果你滑动距离超过了屏幕的一半，这个pos就加1
                position = (getScrollX() + getWidth() / 2) / getWidth();
                //滑到最后一张的时候，不能出边界
                if (position >= colors.length) {
                    position = colors.length - 1;
                }
                if (position < 0) {
                    position = 0;
                }
                if (mOnPageScrollListener != null) {
                    Log.e("TAG", "offset=" + (float) (getScrollX() * 1.0 / ((1) * getWidth())));
                    mOnPageScrollListener.onPageScrolled((float) (getScrollX() * 1.0 / (getWidth())), position);
                }
                break;

            case MotionEvent.ACTION_UP:
                //滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量
                mScroller.startScroll(scrollX, 0, -(scrollX - position * getWidth()), 0);
                invalidate();//使用invalidate这个方法会有执行一个回调方法computeScroll，我们来重写这个方法
                if (mOnPageScrollListener != null) {
                    mOnPageScrollListener.onPageSelected(position);
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            Log.e("CurrX", "mScroller.getCurrX()=" + mScroller.getCurrX());
            postInvalidate();
            if (mOnPageScrollListener != null) {
                Log.e("TAG", "offset=" + (float) (getScrollX() * 1.0 / (getWidth())));
                mOnPageScrollListener.onPageScrolled((float) (mScroller.getCurrX() * 1.0 / ((1) * getWidth())), position);
            }
        }
    }

    private int[] colors = new int[]{Color.CYAN, Color.BLUE, Color.YELLOW, Color.CYAN};

    public interface OnPageScrollListener {
        /**
         * @param offsetPercent offsetPercent：getScrollX滑动的距离占屏幕宽度的百分比
         * @param position
         */
        void onPageScrolled(float offsetPercent, int position);

        void onPageSelected(int position);
    }

    private OnPageScrollListener mOnPageScrollListener;

    public void setOnPageScrollListener(OnPageScrollListener onPageScrollListener) {
        this.mOnPageScrollListener = onPageScrollListener;
    }
}
