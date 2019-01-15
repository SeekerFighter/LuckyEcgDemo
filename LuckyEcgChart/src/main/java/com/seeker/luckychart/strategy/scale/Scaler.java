package com.seeker.luckychart.strategy.scale;

import android.view.ScaleGestureDetector;

/**
 * @author Seeker
 * @date 2018/11/3/003  14:40
 * @describe 缩放
 */
public interface Scaler {

    boolean scale(ScaleGestureDetector detector);

}
