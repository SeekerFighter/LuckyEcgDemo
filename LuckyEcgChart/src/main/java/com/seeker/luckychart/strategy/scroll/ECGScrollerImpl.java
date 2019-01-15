package com.seeker.luckychart.strategy.scroll;

import android.graphics.Rect;

import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/11/2/002  11:51
 * @describe ecg心电图滑动效果
 */
public class ECGScrollerImpl implements Scroller {

    private ChartProvider chartProvider;

    private ScrollResult scrollResult = new ScrollResult();

    private ECGScrollerImpl(ChartProvider provider){
        this.chartProvider = provider;
    }

    public static ECGScrollerImpl create(ChartProvider provider){
        return new ECGScrollerImpl(provider);
    }

    @Override
    public ScrollResult scroll(float startX, float startY, float distanceX, float distanceY) {
        final ChartComputator chartComputator = chartProvider.getChartComputator();
        final Coordinateport maxPort = chartComputator.getMaxCoorport();
        final Coordinateport visiblePort = chartComputator.getVisibleCoorport();
        final Rect contentRect = chartComputator.getDataContentRect();
        boolean canScrollLeft = visiblePort.getLeft() > maxPort.getLeft();
        boolean canScrollRight = visiblePort.getRight() < maxPort.getRight();
        boolean canScrollX = false;
        if ((canScrollLeft && distanceX <= 0) || (canScrollRight && distanceX >= 0)) {
            canScrollX = true;
        }
        if (canScrollX) {
            float viewportOffsetX = distanceX * visiblePort.width() / contentRect.width();
            chartComputator.setViewportTopLeft(visiblePort.left + viewportOffsetX, visiblePort.top);
        }
        scrollResult.canScrollX = canScrollX;
        scrollResult.canScroll = canScrollX;
        return scrollResult;
    }
}
