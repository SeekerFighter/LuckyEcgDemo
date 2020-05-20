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

    private Container[] containers;

    private ChartAxis leftAxis;

    private ChartAxis rightAxis;

    private ChartAxis topAxis;

    private ChartAxis bottomAxis;

    AbsChartData() {

    }

    @SafeVarargs
    AbsChartData(Container... container) {
        this.containers = container;
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
        if (this.containers == null || this.containers.length == 0){
            return false;
        }
        for (Container c:containers){
            if (c.hashCode() == container.hashCode()){
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final void setDataContainer(Container... container) {
        this.containers = container;
    }

    @Override
    public Container[] getDataContainer() {
        return this.containers;
    }

    @CallSuper
    @Override
    public void clear() {
        if (containers != null){
            for (Container c:containers){
                c.clear();
            }
        }
    }

}

