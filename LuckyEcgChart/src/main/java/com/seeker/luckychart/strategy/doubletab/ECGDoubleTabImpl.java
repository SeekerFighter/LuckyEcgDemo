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
public class ECGDoubleTabImpl implements DoubleTap{

    private ChartProvider chartProvider;

    private ECGDoubleTabImpl(ChartProvider provider){
        this.chartProvider = provider;
    }

    public static ECGDoubleTabImpl create(ChartProvider provider){
        return new ECGDoubleTabImpl(provider);
    }

    @Override
    public boolean doubleTap(MotionEvent e) {
        View view = chartProvider.getSelf();
        if (view instanceof ECGChartView){
            ECGChartView chartView = (ECGChartView) view;
            if (e.getY() <= chartProvider.getChartComputator().getChartHeight()/2) {
                chartView.scaleUp();
            }else {
                chartView.scaleDown();
            }
        }
        return true;
    }
}
