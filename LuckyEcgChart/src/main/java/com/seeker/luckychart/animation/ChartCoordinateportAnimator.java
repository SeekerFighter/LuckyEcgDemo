package com.seeker.luckychart.animation;

import com.seeker.luckychart.model.Coordinateport;

/**
 * @author Seeker
 * @date 2017/10/17/017  16:58
 */

public interface ChartCoordinateportAnimator {

    int FAST_ANIMATION_DURATION = 300;

    void startAnimation(Coordinateport startViewport, Coordinateport targetViewport, long duration);

    void cancelAnimation();

    boolean isAnimationStarted();

    void setChartAnimationListener(ChartAnimationListener animationListener);

}
