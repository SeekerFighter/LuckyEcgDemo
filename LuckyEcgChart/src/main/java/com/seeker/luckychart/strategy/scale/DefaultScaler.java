package com.seeker.luckychart.strategy.scale;

import android.graphics.PointF;
import android.view.ScaleGestureDetector;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/11/3/003  14:41
 * @describe 默认图表缩放功能
 */
public class DefaultScaler implements Scaler{

    private PointF viewportFocus = new PointF();

    private ChartComputator chartComputator;

    private DefaultScaler(ChartProvider provider){
        this.chartComputator = provider.getChartComputator();
    }

    public static DefaultScaler create(ChartProvider provider){
        return new DefaultScaler(provider);
    }

    @Override
    public boolean scale(ScaleGestureDetector detector) {
        final float focusX = detector.getFocusX();
        final float focusY = detector.getFocusY();
        if (!chartComputator.computeVitual(focusX,focusY,viewportFocus)){
            return false;
        }
        float scale = 2.0f - detector.getScaleFactor();
        if (Float.isInfinite(scale)){
            scale = 1f;
        }
        final float newWidth = chartComputator.getVisibleCoorport().width() * scale;
        final float newHeight = chartComputator.getVisibleCoorport().height() * scale;
        float left = viewportFocus.x - (focusX - chartComputator.getDataContentRect().left)
                * (newWidth / chartComputator.getDataContentRect().width());
        float top = viewportFocus.y + (focusY - chartComputator.getDataContentRect().top)
                * (newHeight / chartComputator.getDataContentRect().height());
        float right = left + newWidth;
        float bottom = top - newHeight;
        chartComputator.setVisibleCoorport(left, top, right, bottom);
        return true;
    }
}
