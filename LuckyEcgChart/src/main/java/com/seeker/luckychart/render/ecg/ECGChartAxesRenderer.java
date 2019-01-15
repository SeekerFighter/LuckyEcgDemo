package com.seeker.luckychart.render.ecg;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.glmodel.ECGLine;
import com.seeker.luckychart.render.inters.LuckyAxesRenderer;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategy;
import com.seeker.luckychart.utils.ChartLogger;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.Scene;

import java.util.Stack;

/**
 * @author Seeker
 * @date 2018/10/15/015  16:19
 * @describe 心电图背景坐标绘制渲染器
 */
public class ECGChartAxesRenderer implements LuckyAxesRenderer {

    private static final String TAG = "ECGChartAxesRenderer";

    private ECGChartView chartView;

    private ECGLine outLine,innerLine;

    private Stack<Vector3> outVectors,innerVectors;

    private ECGChartAxesRenderer(ECGChartView chartView){
        this.chartView = chartView;
    }

    public static ECGChartAxesRenderer create(ECGChartView chartView){
        return new ECGChartAxesRenderer(chartView);
    }

    @Override
    public void initScene() {
        outLine = ECGLine.create(GLES20.GL_LINES);
        outLine.setLineThickness(chartView.getECGRenderStrategy().getOuterThinkLineWidth());
        outLine.setColor(chartView.getECGRenderStrategy().getOuterColor());
        outLine.setMaterial(new Material());
        outVectors = new Stack<>();

        innerLine = ECGLine.create(GLES20.GL_LINES);
        innerLine.setLineThickness(chartView.getECGRenderStrategy().getInnerThinkLineWidth());
        innerLine.setColor(chartView.getECGRenderStrategy().getInnerColor());
        innerLine.setMaterial(new Material());
        innerVectors = new Stack<>();
    }

    @Override
    public void drawInBackground() {
        drawHCellLine();
        drawVCellLine();
        outLine.setPoints(outVectors);
        innerLine.setPoints(innerVectors);
        chartView.getChartGlRenderer().getCurrentScene().addChildAt(innerLine.init(),0);
        chartView.getChartGlRenderer().getCurrentScene().addChildAt(outLine.init(),1);
    }

    /**
     * 绘制水平网格线
     */
    private void drawHCellLine(){
        ECGRenderStrategy renderStrategy = chartView.getECGRenderStrategy();
        int vCellCounts = renderStrategy.getYCellCounts();
        int innerCellCounts = renderStrategy.getInnerCellCounts();
        float cellWidth = renderStrategy.getCellWidth();
        ChartComputator chartComputator = chartView.getChartComputator();
        float startX = 0,startY,
                stopX = chartComputator.getChartContentRect().width(),stopY;
        for (int i = 0; i < vCellCounts+1;++i){
            startY = stopY = i * cellWidth;
            if (i == 0){
                startY = stopY = startY + renderStrategy.getOuterThinkLineWidth()/2;
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
            }else if (i == vCellCounts){
                startY = stopY = startY - renderStrategy.getOuterThinkLineWidth()/2;
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
            }else if (i % innerCellCounts == 0){
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
            }else {
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                innerVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                innerVectors.add(new Vector3(pointF.x,pointF.y,0));
            }
        }
    }

    /**
     * 绘制竖直网格线
     */
    private void drawVCellLine(){
        ECGRenderStrategy renderStrategy = chartView.getECGRenderStrategy();
        int hCellCounts = renderStrategy.getXCellCounts();
        int innerCellCounts = renderStrategy.getInnerCellCounts();
        float cellWidth = renderStrategy.getCellWidth();
        ChartComputator chartComputator = chartView.getChartComputator();
        float startX,startY = 0,stopX,
                stopY = chartComputator.getChartContentRect().height();
        for (int i = 0; i < hCellCounts+1;++i){
            startX = stopX = i * cellWidth;
            if (i == 0){
                startX = stopX = startX + renderStrategy.getOuterThinkLineWidth()/2;
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
            }else if (i % innerCellCounts == 0){
                if (i == hCellCounts){
                    startX = stopX = startX - renderStrategy.getOuterThinkLineWidth()/2;
                }
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                outVectors.add(new Vector3(pointF.x,pointF.y,0));
            }else {
                PointF pointF = chartComputator.screenToCartesian(startX,startY);
                innerVectors.add(new Vector3(pointF.x,pointF.y,0));
                pointF = chartComputator.screenToCartesian(stopX,stopY);
                innerVectors.add(new Vector3(pointF.x,pointF.y,0));
            }
        }
    }

    @Override
    public void drawInForeground() {

    }

    @Override
    public void onChartSizeChanged() {
        destroyChild();
        initScene();
        drawInBackground();
    }

    @Override
    public void onChartlayoutChanged() {
        destroyChild();
        initScene();
        drawInBackground();
    }

    @Override
    public void onChartDataChanged() {

    }

    private void destroyChild(){
        Scene scene = chartView.getChartGlRenderer().getCurrentScene();
        outVectors.clear();
        innerVectors.clear();
        outLine.destroy();
        innerLine.destroy();
        scene.removeChild(outLine);
        scene.removeChild(innerLine);
        outVectors = innerVectors = null;
        outLine = innerLine = null;
    }

}
