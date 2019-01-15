package com.seeker.luckychart.gesture;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;

import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.strategy.scroll.ScrollResult;

/**
 * @author Seeker
 * @date 2018/6/27/027  17:28
 * @describe 手势滑动实现类
 */
public class ChartTouchHandler extends AbstractTouchHandler{

    private ChartGestureDetector gestureDetector;

    private ScaleGestureDetector scaleGestureDetector;

    private boolean canAnswerScroll = true;//单手指触摸屏幕可以响应滑动

    public ChartTouchHandler(ChartProvider provider){
        super(provider);
        this.gestureDetector = new ChartGestureDetector(provider.getContexter(),new ChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(provider.getContexter(),new ChartScaleGestureListener());
    }

    @Override
    public boolean handleTouchEvent(MotionEvent event,ViewParent viewParent) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_POINTER_DOWN){
            canAnswerScroll = false;
        }else if (action == MotionEvent.ACTION_DOWN){
            canAnswerScroll = true;
        }
        boolean needInvalidate = gestureDetector.onTouchEvent(event);
        needInvalidate = scaleGestureDetector.onTouchEvent(event) || needInvalidate;
        if (scaleGestureDetector.isInProgress()){
            allowParentInterceptTouchEvent(viewParent,true);
        }
        return needInvalidate;
    }

    @Override
    public boolean computeScroll() {
        return chartGestureHelper.computeScrollOffset();
    }

    private class ChartGestureListener extends ChartGestureDetector.ChartSimpleOnGestureListener{

        ChartGestureListener(){}

        @Override
        public boolean onDown(MotionEvent e) {
            super.onDown(e);
            return chartGestureHelper.prepareWithDownAction();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (canAnswerScroll) {
                ScrollResult scrollResult = chartGestureHelper.scroll(e2.getX(), e2.getY(), distanceX, distanceY);
                return scrollResult.canScroll && canScroll();
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return chartGestureHelper.doubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            chartGestureHelper.longPressed(e);
        }

        @Override
        public void onUpWithoutFling(MotionEvent event) {
            chartGestureHelper.onUp(event);
        }
    }

    private class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return chartGestureHelper.scale(detector);
        }
    }

}
