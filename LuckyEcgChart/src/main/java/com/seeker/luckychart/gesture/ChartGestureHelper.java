package com.seeker.luckychart.gesture;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.ScrollResult;
import com.seeker.luckychart.strategy.scroll.Scroller;

/**
 * @author Seeker
 * @date 2018/6/27/027  17:35
 * @describe 手势滑动辅助类
 */
public class ChartGestureHelper {

    private static final int GESTURE_NONE = -1;
    private static final int GESTURE_SCROLL = 0;
    private static final int GESTURE_DOUBLETAB = 1;
    private static final int GESTURE_LONGPRESS = 2;

    private ChartProvider chartProvider;

    private ChartComputator chartComputator;

    private OverScroller scroller;

    private Coordinateport scrollPort;

    private ScrollResult scrollResult = new ScrollResult();

    private Point surfaceSizeBuffer = new Point();// Used for scroll and flings

    private int gestureMode = GESTURE_NONE;

    ChartGestureHelper(ChartProvider provider){
        this.chartProvider = provider;
        this.scrollPort = new Coordinateport();
        this.chartComputator = provider.getChartComputator();
        this.scroller = new OverScroller(provider.getContexter());
    }

    public boolean prepareWithDownAction(){
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        scrollPort.set(chartComputator.getVisibleCoorport());
        gestureMode = GESTURE_NONE;
        return true;
    }

    public ScrollResult scroll(float startX, float startY, float distanceX, float distanceY){
        gestureMode = GESTURE_SCROLL;
        Scroller scroll = chartProvider.getScrollImpl();
        if (scroll == null){
            return scrollResult;
        }
        return scroll.scroll(startX,startY,distanceX,distanceY);
    }


    //响应双击
    public boolean doubleTap(MotionEvent e) {
        gestureMode = GESTURE_DOUBLETAB;
        DoubleTap doubleTap = chartProvider.getDoubleTab();
        return doubleTap != null && doubleTap.doubleTap(e);
    }

    //长按
    public void longPressed(MotionEvent e){
        gestureMode = GESTURE_LONGPRESS;
        LongPress longPress = chartProvider.getLongpresser();
        if (longPress != null){
            longPress.longPressed(e);
        }
    }

    public boolean scale(ScaleGestureDetector detector) {
        Scaler scaler = chartProvider.getScaler();
        return scaler != null && scaler.scale(detector);
    }

    //手势抬起
    public void onUp(MotionEvent e){
        switch (gestureMode){
            case GESTURE_LONGPRESS:
                LongPress longPress = chartProvider.getLongpresser();
                if (longPress != null){
                    longPress.finish(e);
                }
                break;
        }
    }

    public boolean computeScrollOffset(){
        if (scroller.computeScrollOffset()){
            final Coordinateport max = chartComputator.getMaxCoorport();
            chartComputator.computeScrollSurfaceSize(surfaceSizeBuffer);
            final float currXRange = max.left + max.width() * scroller.getCurrX() / surfaceSizeBuffer.x;
            final float currYRange = max.top - max.height() * scroller.getCurrY() / surfaceSizeBuffer.y;
            chartComputator.setViewportTopLeft(currXRange, currYRange);
            return true;
        }
        return false;
    }

}
