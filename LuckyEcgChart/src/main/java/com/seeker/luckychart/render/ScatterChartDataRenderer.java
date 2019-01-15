package com.seeker.luckychart.render;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.model.PointValue;
import com.seeker.luckychart.model.chartdata.ScatterChartData;
import com.seeker.luckychart.model.container.PointContainer;
import com.seeker.luckychart.provider.ChartProvider;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;

import java.util.Stack;

/**
 * @author Seeker
 * @date 2018/6/7/007  14:11
 * @describe 散点图绘制渲染器
 */

public class ScatterChartDataRenderer extends AbstractChartDataRenderer<ScatterChartData>{

    private ScatterChartDataRenderer(ChartProvider<ScatterChartData> provider){
        super(provider);
    }

    public static ScatterChartDataRenderer create(ChartProvider<ScatterChartData> provider){
        return new ScatterChartDataRenderer(provider);
    }

    @Override
    public void onChartSizeChanged() {

    }

    @Override
    public void onChartDataChanged() {

    }

    @Override
    public void onChartlayoutChanged() {

    }

    @Override
    public void onDataRender() {
        if (checkDataAvailable()){
            ScatterChartData chartData = chartProvider.getChartData();
            PointContainer container = chartData.getDataContainer();
            final PointValue[] values = container.getValues();
            drawPoint(values,container.getPointColor(),container.getPointRadius());
        }
    }

    private void drawPoint(PointValue[] values,int color,float radius){
        float rawX,rawY;
        final Coordinateport visible = chartComputator.getVisibleCoorport();
        Stack<Vector3> points = new Stack<>();
        for (PointValue value:values){
            if (visible.contains(value.getCoorX(),value.getCoorY())) {
                rawX = chartComputator.computeRawX(value.getCoorX());
                rawY = chartComputator.computeRawY(value.getCoorY());
                PointF pointF = chartComputator.screenToCartesian(rawX,rawY);
                points.add(new Vector3(pointF.x,pointF.y,0));
            }
        }
        Line3D whirl = new Line3D(points,radius,color);
        whirl.setDrawingMode(GLES20.GL_POINTS);
        Material material = new Material();
        whirl.setMaterial(material);
        chartComputator.getChartRenderer().getCurrentScene().addChild(whirl);
    }
}
