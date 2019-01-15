package com.seeker.luckychart.render;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.model.chartdata.AbsChartData;
import com.seeker.luckychart.model.container.AbsContainer;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.render.inters.LuckyDataRenderer;
import com.seeker.luckychart.utils.ChartLogger;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:21
 * @describe 图表数据渲染抽象类
 */

public abstract class AbstractChartDataRenderer<ChartData extends AbsChartData> implements LuckyDataRenderer {

    private static final String TAG = "AbstractChartDataRender";

    protected ChartProvider<ChartData> chartProvider;

    protected ChartComputator chartComputator;


    public AbstractChartDataRenderer(ChartProvider<ChartData> provider){
        this.chartProvider = provider;
        this.chartComputator = provider.getChartComputator();
    }

    @Override
    public void initScene() {

    }

    public boolean checkDataAvailable(){

        ChartData chartData = chartProvider.getChartData();
        if (chartData == null){
            ChartLogger.wTag(TAG,"checkDataAvailable(),chartData == null.");
            return false;
        }
        AbsContainer container = chartData.getDataContainer();
        if (container == null){
            ChartLogger.wTag(TAG,"checkDataAvailable(),container == null.");
            return false;
        }
        Object[] values = container.getValues();
        if (values == null || values.length == 0){
            ChartLogger.wTag(TAG,"checkDataAvailable(),empty values ");
            return false;
        }
        return true;
    }
}
