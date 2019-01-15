package com.seeker.luckychart.provider;

import android.support.annotation.Nullable;

import com.seeker.luckychart.model.ChartAxis;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:28
 * @describe 坐标轴提供器
 */

public interface AxisProvider {

    void setLeftAxis(@Nullable ChartAxis leftAxis);

    void setRightAxis(@Nullable ChartAxis rightAxis);

    void setTopAxis(@Nullable ChartAxis topAxis);

    void setBottomAxis(@Nullable ChartAxis bottomAxis);

    ChartAxis getLeftAxis();

    ChartAxis getRightAxis();

    ChartAxis getTopAxis();

    ChartAxis getBottomAxis();

}
