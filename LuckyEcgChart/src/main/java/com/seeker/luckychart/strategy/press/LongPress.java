package com.seeker.luckychart.strategy.press;

import android.view.MotionEvent;

/**
 * @author Seeker
 * @date 2018/11/3/003  10:13
 * @describe 长按
 */
public interface LongPress {

    void longPressed(MotionEvent e);

    void finish(MotionEvent e);

}
