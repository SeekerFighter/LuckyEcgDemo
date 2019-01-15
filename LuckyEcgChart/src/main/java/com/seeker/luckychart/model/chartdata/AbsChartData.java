package com.seeker.luckychart.model.chartdata;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.model.container.AbsContainer;
import com.seeker.luckychart.provider.DataProvider;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:38
 * @describe 图表数据抽象类
 */

public abstract class AbsChartData<Container extends AbsContainer> implements DataProvider<Container>{

    private Container container;

    private ChartAxis leftAxis;

    private ChartAxis rightAxis;

    private ChartAxis topAxis;

    private ChartAxis bottomAxis;

    AbsChartData() {

    }

    AbsChartData(Container container) {
        this.container = container;
    }

    @Override
    public void setLeftAxis(@Nullable ChartAxis leftAxis) {
        this.leftAxis = leftAxis;
    }

    @Override
    public void setRightAxis(@Nullable ChartAxis rightAxis) {
        this.rightAxis = rightAxis;
    }

    @Override
    public void setTopAxis(@Nullable ChartAxis topAxis) {
        this.topAxis = topAxis;
    }

    @Override
    public void setBottomAxis(@Nullable ChartAxis bottomAxis) {
        this.bottomAxis = bottomAxis;
    }

    @Override
    public ChartAxis getLeftAxis() {
        return this.leftAxis;
    }

    @Override
    public ChartAxis getRightAxis() {
        return this.rightAxis;
    }

    @Override
    public ChartAxis getTopAxis() {
        return this.topAxis;
    }

    @Override
    public ChartAxis getBottomAxis() {
        return this.bottomAxis;
    }

    @Override
    public boolean containDataContainer(Container container) {
        return this.container != null && this.container.hashCode() == container.hashCode();
    }

    @Override
    public void setDataContainer(Container container) {
        if (!containDataContainer(container)) {
            this.container = container;
        }
    }

    @Override
    public Container getDataContainer() {
        return this.container;
    }

    @CallSuper
    @Override
    public void clear() {
        if (container != null){
            container.clear();
        }
    }

}

