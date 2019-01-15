package com.seeker.luckychart.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.model.CoorValue;
import com.seeker.luckychart.provider.AxisProvider;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.utils.ChartLogger;
import com.seeker.luckychart.utils.ChartUtils;

import org.rajawali3d.cameras.Camera2D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;

/**
 * @author Seeker
 * @date 2018/6/7/007  14:12
 * @describe 坐标轴绘制渲染器
 */

public class CoorAxesRenderer extends AbstractChartAxesRenderer{

    private static final String TAG = "CoorAxesRenderer";

    private Plane axesPlane;

    private Texture axesTexture;

    private Material axesMaterial;

    private Canvas axesCanvas;

    private Bitmap axesBitmap;

    private CoorAxesRenderer(ChartProvider chartProvider){
        super(chartProvider);
    }

    public static CoorAxesRenderer create(ChartProvider chartProvider){
        return new CoorAxesRenderer(chartProvider);
    }

    @Override
    public void initScene() {
        Camera2D camera2D = chartComputator.getChartRenderer().getCamera2D();
        axesPlane = new Plane((float) camera2D.getWidth(),(float) camera2D.getHeight(),2,1);
        axesPlane.setDoubleSided(true);
        axesPlane.setTransparent(true);
        axesPlane.isContainer(false);

        axesMaterial = new Material();
        axesMaterial.setColorInfluence(0);
        axesPlane.setMaterial(axesMaterial);
        axesCanvas = new Canvas();
    }

    @Override
    public void drawInBackground() {
        AxisProvider axisProvider = chartProvider.getChartData();
        if (axisProvider != null){
            ChartAxis axis = axisProvider.getLeftAxis();
            if (axis != null){
                prepareAxisToDraw(axis,ChartAxis.LEFT);
                drawAxisLines(axis,ChartAxis.LEFT);
            }

            axis = axisProvider.getTopAxis();
            if (axis != null){
                prepareAxisToDraw(axis,ChartAxis.TOP);
                drawAxisLines(axis,ChartAxis.TOP);
            }

            axis = axisProvider.getRightAxis();
            if (axis != null){
                prepareAxisToDraw(axis,ChartAxis.RIGHT);
                drawAxisLines(axis,ChartAxis.RIGHT);
            }

            axis = axisProvider.getBottomAxis();
            if (axis != null){
                prepareAxisToDraw(axis,ChartAxis.BOTTOM);
                drawAxisLines(axis,ChartAxis.BOTTOM);
            }
        }
    }

    @Override
    public void drawInForeground() {
        AxisProvider axisProvider = chartProvider.getChartData();
        if (axisProvider != null){
            ChartAxis axis = axisProvider.getLeftAxis();
            if (axis != null){
                drawAxisLabelsAndName(axis,ChartAxis.LEFT);
            }

            axis = axisProvider.getTopAxis();
            if (axis != null){
                drawAxisLabelsAndName(axis,ChartAxis.TOP);
            }

            axis = axisProvider.getRightAxis();
            if (axis != null){
                drawAxisLabelsAndName(axis,ChartAxis.RIGHT);
            }

            axis = axisProvider.getBottomAxis();
            if (axis != null){
                drawAxisLabelsAndName(axis,ChartAxis.BOTTOM);
            }
        }
        axesTexture.setBitmap(axesBitmap);
        chartProvider.getChartGlRenderer().getTextureManager().replaceTexture(axesTexture);
    }

    private void drawAxisLabelsAndName(ChartAxis axis,@ChartAxis.Location int location){
        final CoorValue[] coorValues = axis.getCoordinateValues();
        if (coorValues == null || coorValues.length == 0){
            return;
        }
        boolean isAxisVertical = isAxisVertical(location);
        int cooX = 0,cooY = 0;
        if (isAxisVertical){
            cooX = (int) axis.getCoorBaseLine();
        }else {
            cooY = (int) axis.getCoorBaseLine();
        }

        char[] drawed = new char[axis.getMaxCoorchars()];
        final Rect dataContent = chartComputator.getDataContentRect();
        float textWidth;
        int realX = cooX,realY = cooY;
        boolean contains = false;
        for (int i = 0,len = coorValues.length;i < len;++i){
            if (i % axis.getModule() == 0){
                CoorValue coorValue = coorValues[i];
                ChartUtils.copyof(coorValue.getLabelAsChar(),drawed);
                textWidth = ChartUtils.measureText(drawed,axis.getCoorPaint());
                switch (location) {
                    case ChartAxis.BOTTOM:
                        if (i == 0){
                            realX = Math.round(coorValue.getRawValue());
                        }else {
                            realX = Math.round(coorValue.getRawValue() - textWidth / 2f);
                        }
                        contains = coorValue.getRawValue() >= dataContent.left-ChartUtils.CONTAIN_OFFSET && coorValue.getRawValue() <= dataContent.right+ChartUtils.CONTAIN_OFFSET;
                        break;
                    case ChartAxis.LEFT:
                        if (i == 0){
                            realY = Math.round(coorValue.getRawValue());
                        }else {
                            realY = Math.round(coorValue.getRawValue() + axis.getCoorHeight()/2f);
                        }
                        realX = Math.round(cooX - textWidth);
                        contains = coorValue.getRawValue() >= dataContent.top-ChartUtils.CONTAIN_OFFSET && coorValue.getRawValue() <= dataContent.bottom+ChartUtils.CONTAIN_OFFSET;
                        break;
                    case ChartAxis.RIGHT:

                        break;
                    case ChartAxis.TOP:

                        break;
                }
                if (contains) {
                    axesCanvas.drawText(drawed, 0, drawed.length, realX, realY, axis.getCoorPaint());
                }
            }
        }

        //draw axis name
        final Rect content = chartComputator.getDataContentRect();
        String name = axis.getName();
        Paint namePaint = axis.getNamePaint();
        if (!TextUtils.isEmpty(name) && namePaint != null){
            switch (location) {
                case ChartAxis.BOTTOM:
                    textWidth = ChartUtils.measureText(name.toCharArray(),namePaint);
                    axesCanvas.drawText(name, (int) (content.centerX()-textWidth/2f),Math.round(axis.getNameBaseLine()),namePaint);
                    break;
                case ChartAxis.LEFT:
                    axesCanvas.save();
                    axesCanvas.rotate(90,content.centerX(),content.centerY());
                    axesCanvas.translate(content.centerX()-ChartUtils.measureText(name.toCharArray(),namePaint)/2f,content.centerY()+axis.getCoorHeight());
                    axesCanvas.drawText(name, (int) axis.getNameBaseLine(),content.centerY(),namePaint);
                    axesCanvas.restore();
                    break;
                case ChartAxis.RIGHT:
                    break;
                case ChartAxis.TOP:
                    break;
            }
        }
    }

    private void prepareAxisToDraw(ChartAxis axis, @ChartAxis.Location int location) {
        CoorValue[] coorValues = axis.getCoordinateValues();
        if (coorValues != null) {
            boolean isAxisVertical = isAxisVertical(location);
            float rawValue;
            float value;
            for (CoorValue coorValue : coorValues) {
                value = coorValue.getValue();
                if (isAxisVertical) {
                    rawValue = chartComputator.computeRawY(value);
                } else {
                    rawValue = chartComputator.computeRawX(value);
                }
                coorValue.setRawValue(rawValue);
            }
        }
    }

    private void drawAxisLines(ChartAxis axis, @ChartAxis.Location int location){
        final Rect dataContent = chartComputator.getDataContentRect();
        float separationX1, separationY1, separationX2, separationY2;
        float lineX1, lineY1, lineX2, lineY2;
        lineX1 = lineY1 = lineX2 = lineY2 = 0;
        boolean isAxisVertical = isAxisVertical(location);
        if (isAxisVertical){
            separationX1 = separationX2 = axis.getSeparationLine();
            separationY1 = dataContent.bottom;
            separationY2 = dataContent.top;
            lineX1 = dataContent.left;
            lineX2 = dataContent.right;
        }else {
            separationX1 = dataContent.left;
            separationX2 = dataContent.right;
            separationY1 = separationY2 = axis.getSeparationLine();
            lineY1 = dataContent.top;
            lineY2 = dataContent.bottom;
        }

        Paint majorPaint = axis.getLineMajorPaint();
        if (majorPaint != null){
            axesCanvas.drawLine(separationX1,separationY1,separationX2,separationY2,majorPaint);
        }

        Paint subPaint = axis.getLineSubPaint();
        if (subPaint != null){
            CoorValue[] coorValues = axis.getCoordinateValues();
            if (coorValues  == null || coorValues.length == 0){
                return;
            }
            for (int i = 0,len = coorValues.length;i < len;++i){
                if (i != 0 && i % axis.getModule() == 0){
                    CoorValue coorValue = coorValues[i];
                    if (isAxisVertical){
                        lineY1 = lineY2 = coorValue.getRawValue();
                    }else {
                        lineX1 = lineX2 = coorValue.getRawValue();
                    }
                    axesCanvas.drawLine(lineX1,lineY1,lineX2,lineY2,subPaint);
                }
            }
        }
    }


    @Override
    public void onChartSizeChanged() {
        super.onChartSizeChanged();
        Scene scene = chartProvider.getChartGlRenderer().getCurrentScene();
        scene.removeChild(axesPlane);
        if (axesBitmap == null){
            axesBitmap = Bitmap.createBitmap(chartComputator.getChartWidth(), chartComputator.getChartHeight(), Bitmap.Config.ARGB_8888);
            axesCanvas.setBitmap(axesBitmap);
            axesTexture = new Texture("bpmTexture", axesBitmap);
            try {
                axesTexture.setMipmap(false);
                axesMaterial.addTexture(axesTexture);
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        }
        scene.addChildAt(axesPlane,0);
    }

    @Override
    public void onChartDataChanged() {
        super.onChartDataChanged();
        drawInBackground();
        drawInForeground();
    }
}
