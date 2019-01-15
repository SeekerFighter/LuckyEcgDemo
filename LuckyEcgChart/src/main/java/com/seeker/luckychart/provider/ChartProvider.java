package com.seeker.luckychart.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.model.Coordinateport;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:15
 * @describe 图表内容提供器
 */

public interface ChartProvider<ChartData extends DataProvider> extends RenderProvider {

    ChartComputator getChartComputator();

    /**
     * 设置图表数据
     * @param chartData
     */
    void setChartData(ChartData chartData);

    /**
     * 获取图表数据
     * @return
     */
    ChartData getChartData();

    /**
     * 设置图表绘制可视区域内坐标值范围
     * @param visible
     */
    void setChartVisibleCoordinateport(@NonNull Coordinateport visible);

    /**
     * 动画效果设置图表绘制可视区域内坐标值范围
     * @param target 可视区域
     * @param duration 动画时长
     */
    void setChartVisibleCoordinateportWithAnim(@NonNull Coordinateport target,long duration);

    /**
     * 设置图表绘制区域内最大坐标值范围
     * @param max
     */
    void setChartMaxCoordinateport(@NonNull Coordinateport max);

    /**
     * 获取上下文
     * @return
     */
    Context getContexter();

    /**
     * 清除图表数据
     */
    void clearChartData();

    /**
     * 返回自身
     * @return
     */
    View getSelf();

    /**
     * 是否响应触摸事件
     * @param isTouchable
     */
    void setTouchable(boolean isTouchable);
}
