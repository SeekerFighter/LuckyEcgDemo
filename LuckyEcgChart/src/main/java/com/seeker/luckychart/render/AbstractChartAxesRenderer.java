package com.seeker.luckychart.render;

import android.graphics.Rect;
import android.text.TextUtils;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.model.chartdata.AbsChartData;
import com.seeker.luckychart.provider.AxisProvider;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.render.inters.LuckyAxesRenderer;
import com.seeker.luckychart.utils.ChartUtils;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:21
 * @describe 图表坐标轴渲染抽象类
 */

public abstract class AbstractChartAxesRenderer<ChartData extends AbsChartData> implements LuckyAxesRenderer {

    protected ChartProvider<ChartData> chartProvider;

    protected ChartComputator chartComputator;

    AbstractChartAxesRenderer(ChartProvider<ChartData> provider){
        this.chartProvider = provider;
        this.chartComputator = provider.getChartComputator();
    }

    @Override
    public void onChartSizeChanged() {
        onChanged();
    }

    @Override
    public void onChartDataChanged() {
        onChanged();
    }

    @Override
    public void onChartlayoutChanged() {
        onChanged();
    }

    private void onChanged(){
        AxisProvider axisProvider = chartProvider.getChartData();
        if (axisProvider != null){
            initAxis(axisProvider.getLeftAxis(),ChartAxis.LEFT);
            initAxis(axisProvider.getTopAxis(),ChartAxis.TOP);
            initAxis(axisProvider.getRightAxis(),ChartAxis.RIGHT);
            initAxis(axisProvider.getBottomAxis(),ChartAxis.BOTTOM);
        }
    }

    private void initAxis(ChartAxis axis, @ChartAxis.Location int location){
        if (null == axis)return;
        initAxisPaints(axis,location);
        initAxisMargin(axis,location);
        initAxisMeasurements(axis,location);
        initContentRect(axis, location);
    }


    private void initAxisPaints(ChartAxis axis, @ChartAxis.Location int location){
        axis.initFontMetricsInt();
        if (isAxisVertical(location)){
            axis.setCoorDimensionForMargins(axis.getCoorWidth());
        }else {
            axis.setCoorDimensionForMargins(axis.getCoorTextAscent()+axis.getCoorTextDescent());
        }
    }

    private void initAxisMargin(ChartAxis axis, @ChartAxis.Location int location){
        int margin = ChartUtils.dp2px(chartComputator.getDensity(),axis.getAxisMargin())+axis.getCoorDimensionForMargins();
        margin += getAxisNameMargin(axis);
        insetContentRectWithAxesMargins(margin,location);
    }

    private void initAxisMeasurements(ChartAxis axis, @ChartAxis.Location int location){

        int axisMargin = ChartUtils.dp2px(chartComputator.getDensity(),axis.getAxisMargin());

        Rect dataContentRect = chartComputator.getDataContentRect();

        switch (location) {
            case ChartAxis.LEFT:
                axis.setCoorBaseLine(dataContentRect.left - axisMargin);
                axis.setNameBaseLine(axis.getCoorBaseLine() - axisMargin - axis.getCoorTextDescent()
                        - axis.getCoorDimensionForMargins());
                axis.setSeparationLine(dataContentRect.left);
                break;
            case ChartAxis.RIGHT:
                axis.setCoorBaseLine(dataContentRect.right + axisMargin);
                axis.setNameBaseLine(axis.getCoorBaseLine() + axisMargin + axis.getCoorTextAscent()
                        + axis.getCoorDimensionForMargins());
                axis.setSeparationLine(dataContentRect.right);
                break;
            case ChartAxis.TOP:
                axis.setCoorBaseLine(dataContentRect.top - axisMargin - axis.getCoorTextDescent());
                axis.setNameBaseLine(axis.getCoorBaseLine() - axisMargin - axis.getCoorDimensionForMargins());
                axis.setSeparationLine(dataContentRect.top);
                break;
            case ChartAxis.BOTTOM:
                axis.setCoorBaseLine(dataContentRect.bottom + axisMargin+axis.getCoorHeight());
                axis.setNameBaseLine(axis.getCoorBaseLine() + axisMargin + axis.getCoorDimensionForMargins());
                axis.setSeparationLine(dataContentRect.bottom);
                break;
        }
    }

    private void initContentRect(ChartAxis axis, @ChartAxis.Location int location){
        if (isAxisVertical(location)){
            chartComputator.insetContentRect(0,axis.getCoorTextAscent(),0,0);
        }else {
            chartComputator.insetContentRect(0,0,axis.getCoorWidth()/2,0);
        }
    }

    private int getAxisNameMargin(ChartAxis axis){
        int margin = 0;
        if (!TextUtils.isEmpty(axis.getName())){
            margin += axis.getNameTextAscent();
            margin += axis.getNameTextDescent();
            margin += ChartUtils.dp2px(chartComputator.getDensity(),axis.getAxisMargin());
        }
        return margin;
    }

    private void insetContentRectWithAxesMargins(int margin,@ChartAxis.Location int location){
        switch (location) {
            case ChartAxis.LEFT:
                chartComputator.insetContentRect(margin,0,0,0);
                break;
            case ChartAxis.BOTTOM:
                chartComputator.insetContentRect(0, 0, 0, margin);
                break;
            case ChartAxis.RIGHT:
                chartComputator.insetContentRect(0, 0, margin, 0);
                break;
            case ChartAxis.TOP:
                chartComputator.insetContentRect(0, margin, 0, 0);
                break;
        }
    }

    /**
     * 判断坐标轴方向
     * @param location
     * @return
     */
    protected boolean isAxisVertical(@ChartAxis.Location int location){
        if (location == ChartAxis.LEFT || location == ChartAxis.RIGHT){
            return true;
        }else if (location == ChartAxis.TOP || location == ChartAxis.BOTTOM){
            return false;
        }
        throw new IllegalArgumentException("Invalid axis location " + location);
    }
}
