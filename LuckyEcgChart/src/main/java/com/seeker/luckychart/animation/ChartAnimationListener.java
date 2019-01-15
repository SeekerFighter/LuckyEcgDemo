package com.seeker.luckychart.animation;

import java.util.EventListener;

/**
 * @author Seeker
 * @date 2017/10/17/017  15:57
 * @describe Listener used to listen for chart animation start and stop events. Implementations of this interface can be used for
 * all types of chart animations(data, viewport, PieChart rotation).
 */

public interface ChartAnimationListener extends EventListener {

    void onAnimationStarted();

    void onAnimationFinished();

}
