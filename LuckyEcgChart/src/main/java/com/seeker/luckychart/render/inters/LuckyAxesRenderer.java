package com.seeker.luckychart.render.inters;

/**
 * @author Seeker
 * @date 2018/10/15/015  16:25
 * @describe 坐标系渲染接口
 */
public interface LuckyAxesRenderer extends LuckyRenderer {

    /**
     * Prepare axes coordinates and draw axes lines(if enabled) in the background.
     */
    void drawInBackground();

    /**
     * Draw axes labels and names in the foreground.
     */
    void drawInForeground();

}
