package com.seeker.luckychart.gesture;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Seeker
 * @date 2018/3/10/010  13:21
 * @describe TODO
 */

public class ChartGestureDetector extends GestureDetector{

    private OnGestureUpListener upListener;

    public ChartGestureDetector(Context context, ChartSimpleOnGestureListener listener) {
        super(context, listener);
        this.upListener = listener;
    }

    public ChartGestureDetector(Context context, ChartSimpleOnGestureListener listener, Handler handler) {
        super(context, listener, handler);
        this.upListener = listener;
    }

    public ChartGestureDetector(Context context, ChartSimpleOnGestureListener listener, Handler handler, boolean unused) {
        super(context, listener, handler, unused);
        this.upListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if (upListener != null && ev.getAction() == MotionEvent.ACTION_UP){
            upListener.onActionUp(ev);
        }
        return result;
    }

    /**
     * up事件没有回调，现在添加一个监听回调事件
     */
    public interface OnGestureUpListener{
        void onActionUp(MotionEvent event);
    }

    public static class ChartSimpleOnGestureListener extends SimpleOnGestureListener implements OnGestureUpListener{

        private boolean isFling = false;

        @Override
        public boolean onDown(MotionEvent e) {
            isFling = false;
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            isFling = true;
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onActionUp(MotionEvent event) {
            if (!isFling){
                onUpWithoutFling(event);
            }
        }

        //只有当不是fling的时候才回调
        public void onUpWithoutFling(MotionEvent event){

        }

    }

}
