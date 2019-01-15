package com.seeker.luckychart.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.seeker.luckychart.gesture.ChartTouchHandler;
import com.seeker.luckychart.model.chartdata.ScatterChartData;
import com.seeker.luckychart.render.CoorAxesRenderer;
import com.seeker.luckychart.render.ScatterChartDataRenderer;

/**
 * @author Seeker
 * @date 2018/6/7/007  14:13
 * @describe 散点图控件(可以用来绘制Lorenzz-RR散点图,时间间期RR散点图)
 */

public class ScatterChartView extends AbstractChartView<ScatterChartData>{

    public ScatterChartView(Context context) {
        this(context,null);
    }

    public ScatterChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ScatterChartDataRenderer getChartDataRenderer() {
        return ScatterChartDataRenderer.create(this);
    }

    @Override
    public CoorAxesRenderer getChartAxesRenderer() {
        return CoorAxesRenderer.create(this);
    }

    @Override
    public ScatterChartView getSelf() {
        return this;
    }
}
