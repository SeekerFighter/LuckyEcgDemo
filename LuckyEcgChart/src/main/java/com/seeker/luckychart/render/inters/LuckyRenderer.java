package com.seeker.luckychart.render.inters;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:19
 * @describe 渲染器
 */

public interface LuckyRenderer {

    void initScene();

    /**
     * 图表大小发生变化
     */
    void onChartSizeChanged();

    /**
     * 图表数据发生变化
     */
    void onChartDataChanged();

    /**
     * 图表布局方式发生变化，例如 心电图缩放、增益等功能
     */
    void onChartlayoutChanged();
}
