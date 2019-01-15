package com.seeker.luckychart.strategy.doubletab;

import android.view.MotionEvent;
import android.view.View;

import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/11/2/002  11:37
 * @describe 心电图双击实现
 */
public class DefaultDoubleTabImpl implements DoubleTap{

    private ChartProvider chartProvider;

    private DefaultDoubleTabImpl(ChartProvider provider){
        this.chartProvider = provider;
    }

    public static DefaultDoubleTabImpl create(ChartProvider provider){
        return new DefaultDoubleTabImpl(provider);
    }

    @Override
    public boolean doubleTap(MotionEvent e) {

        return true;
    }
}
