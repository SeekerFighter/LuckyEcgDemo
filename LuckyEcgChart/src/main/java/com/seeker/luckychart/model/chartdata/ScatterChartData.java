package com.seeker.luckychart.model.chartdata;

import com.seeker.luckychart.model.container.PointContainer;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:03
 * @describe 散点绘制数据
 */

public class ScatterChartData extends AbsChartData<PointContainer>{

    private ScatterChartData() {

    }

    private ScatterChartData(PointContainer container) {
        super(container);
    }

    public static ScatterChartData create(){
        return new ScatterChartData();
    }

    public static ScatterChartData create(PointContainer container){
        return new ScatterChartData(container);
    }

}
