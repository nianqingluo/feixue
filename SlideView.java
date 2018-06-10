package com.example.administrator.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/6/9 0009.
 */

public class SlideView extends RelativeLayout {

    private int ORIGIN_WIDTH = 200;
    private float SCALE = 0.8f;
    private float ANTI_SCALE = 1 / 0.8f;
    private int OVERLAP_DISTANCE = 10;

    //    -0.5f - 0.5f
    private float mMissmatch = 0;

    private Scroller mScroller;
    private Context mContext;
    private int mMaxFlintVelocity;
    private int mMinFlintVelocity;
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    private float lastX, lastY;
    private float mDownX;
    private float mDownY;
    //    private Scroller mScroller = new Scroller(mContext);

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScroller = new Scroller(mContext);
        initData(context);
    }

    private void initData(Context context) {
        mScroller = new Scroller(context, null, true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMaxFlintVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinFlintVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMinFlintVelocity = 600;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        System.out.println("onmeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {

        int childCount = getChildCount();
        RelativeLayout.LayoutParams para = (LayoutParams) params;
        float distance = getMoveDistance(childCount);

        float scale = getScale(childCount);

        float zoominTotalOffsetDistance = ORIGIN_WIDTH * scale - scale * ORIGIN_WIDTH * (1 - SCALE) / 2f - OVERLAP_DISTANCE;

        float zoominOffsetDistance = Math.abs(mMissmatch * zoominTotalOffsetDistance);

        float zoomoutTotalOffsetDistance = getCurrentScale(childCount);

        if (childCount % 2 == 0) { //往左边添加
            child.setTranslationX(distance - zoominOffsetDistance);

        } else {//往右边添加

        }


        float offsetScale = 1 - Math.abs(mMissmatch) * 0.2f;

        child.setScaleX(scale * offsetScale);
        child.setScaleY(scale * offsetScale);

        super.addView(child, 0, para);
    }

    private float getScale(int childCount) {
        float tempScale = 1;
        if (childCount % 2 == 0) { //往左边添加

            int multiple = childCount / 2;
            for (int j = 0; j < multiple; j++) {
                tempScale *= SCALE;
            }
        } else {//往右边添加
            int multiple = childCount / 2 + 1;
            for (int j = 0; j < multiple; j++) {
                tempScale *= SCALE;
            }
        }

        return tempScale;
    }

    private float getMoveDistance(int childCount) {
        if (childCount % 2 == 0) { //往左边添加
            int moveCount = (childCount / 2);
            float moveDistance = 0;
            for (int i = 1; i <= moveCount; i++) {
                float lastScale = getCurrentScale(i);
                moveDistance += ORIGIN_WIDTH * lastScale - lastScale * ORIGIN_WIDTH * (1 - SCALE) / 2f - OVERLAP_DISTANCE;
            }

            return -moveDistance;
        } else {//往右边添加
            int moveCount = childCount / 2 + 1;
            float moveDistance = 0;
            for (int i = 1; i <= moveCount; i++) {
                float lastScale = getCurrentScale(i);
                moveDistance += ORIGIN_WIDTH * lastScale - lastScale * ORIGIN_WIDTH * (1 - SCALE) / 2f - OVERLAP_DISTANCE;
            }

            return moveDistance;
        }
    }

    private float getCurrentScale(int i) {
        float totalScale = 1;
        for (int j = 0; j < i - 1; j++) {
            totalScale *= SCALE;
        }
        return totalScale;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float pointX = event.getX();
                float deltaX = pointX - mDownX;
                if (deltaX > 0) {  //往右滑
                    float ratio = deltaX / ORIGIN_WIDTH;


                } else {//往左滑动

                }


                break;
            case MotionEvent.ACTION_UP:
                //手指抬起，计算当前速率
                float up_x = event.getX();
                float up_y = event.getY();
                velocityTracker.computeCurrentVelocity(1000, mMaxFlintVelocity);
                int xVelocity = (int) velocityTracker.getXVelocity();
                System.out.println("xVelocity:" + xVelocity);
                int yVelocity = (int) velocityTracker.getYVelocity();
                //                LogUtil.e("mMinFlintVelocity=" + mMinFlintVelocity + ",xVelocity=" + xVelocity + ",yVelocity=" + yVelocity);
                if (Math.abs(xVelocity) > mMinFlintVelocity) {
                    mScroller.startScroll(50, 50, 600, 50, 1000);
                    //                    mScroller.fling((int) up_x,0,xVelocity,0,100,Integer.MAX_VALUE,0,0);
                    //                    LogUtil.e("up_x=" + up_x + ",up_y=" + up_y + "scrollX=" + scrollX + ",scrollY=" + scrollY + ", startX=" + mScroller.getStartX() + ", startY=" + mScroller.getStartY() + ", width=" + getWidth() + ",childWidth=" + chileView.getWidth() + ",height=" + getHeight() + ",childheight=" + chileView.getHeight());
                    int startX = mScroller.getStartX();
                    int startY = mScroller.getStartY();
                    int finalX = mScroller.getFinalX();
                    int finalY = mScroller.getFinalY();
                    //                    mChildeRectF.set(chileView.getLeft() - finalX, chileView.getTop() - finalY, chileView.getRight() - finalX, chileView.getBottom() - finalY);
                    int dex_x, dex_y;
                    //                    LogUtil.e("upx=" + up_x + ",upy=" + up_y + ",pontX=" + mDownPoint.x + ",pontY=" + mDownPoint.y);
                    //                    LogUtil.e("dex=" + dex_x + ",dey=" + dex_y + "left=" + mChildeRectF.left + ",top=" + mChildeRectF.top + "finalX=" + mScroller.getFinalX() + ",finalY=" + mScroller.getFinalY());
                    //                    mChildeRectF.set(mChildeRectF.left + dex_x, mChildeRectF.top + dex_y, mChildeRectF.right + dex_x, mChildeRectF.bottom + dex_y);
                    //                    LogUtil.e("left=" + mChildeRectF.left + ",top=" + mChildeRectF.top);
                    invalidate();
                } else {
                    //                    mChildeRectF.set(chileView.getLeft() - scrollX, chileView.getTop() - scrollY, chileView.getRight() - scrollX, chileView.getBottom() - scrollY);
                }

                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                //                LogUtil.e("-----ACTION_CANCEL");
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        boolean x = mScroller.computeScrollOffset();
        System.out.println(x);
        if (x) {
            System.out.println("currx" + mScroller.getCurrX());
            requestLayout();
            invalidate();
        }
    }
}
