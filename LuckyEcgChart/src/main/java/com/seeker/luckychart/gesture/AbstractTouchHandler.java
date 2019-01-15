package com.seeker.luckychart.gesture;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/6/27/027  17:25
 * @describe 手势滑动抽象类
 */
public abstract class AbstractTouchHandler {

    private static final int SCROLL_IDLE = 0;//空置状态

    private static final int SCROLL_PREPARE = 1;//滚动准备状态

    private static final int SCROLL_PARENT = 2;//父布局滚动

    private static final int SCROLL_CHILD = 3;//自身滚动

    private int scrollType = SCROLL_IDLE;//初始化滑动是否是子控件

    protected float mLastTouchX;

    protected float mLastTouchY;

    protected ChartGestureHelper chartGestureHelper;

    private int mTouchSlop;

    public AbstractTouchHandler(ChartProvider provider){
        final ViewConfiguration configuration = ViewConfiguration.get(provider.getContexter());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.chartGestureHelper = new ChartGestureHelper(provider);
    }

    public void dispatchTouchEvent(MotionEvent event,ViewParent viewParent){
        final float touchX = event.getX();
        final float touchY = event.getY();
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                scrollType = SCROLL_PREPARE;
                mLastTouchX = touchX;
                mLastTouchY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (SCROLL_PREPARE == scrollType){
                    float deltaX = Math.abs(touchX - mLastTouchX);
                    float deltaY = Math.abs(touchY - mLastTouchY);

                    if (deltaX > deltaY && Math.abs(deltaX) > mTouchSlop){
                        scrollType = SCROLL_CHILD;
                        allowParentInterceptTouchEvent(viewParent,true);
                    }else if(deltaX < deltaY && Math.abs(deltaX) > mTouchSlop){
                        scrollType = SCROLL_PARENT;
                        allowParentInterceptTouchEvent(viewParent,false);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
            default:
                allowParentInterceptTouchEvent(viewParent,false);
                break;
        }
    }

    public boolean canScroll(){
        return SCROLL_PARENT != scrollType && SCROLL_PREPARE != scrollType;
    }

    protected void allowParentInterceptTouchEvent(ViewParent viewParent,boolean allow){
        if (null != viewParent){
            viewParent.requestDisallowInterceptTouchEvent(allow);
        }
    }

    public abstract boolean handleTouchEvent(MotionEvent event,ViewParent viewParent);

    public abstract boolean computeScroll();

}
