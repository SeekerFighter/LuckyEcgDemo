package com.seeker.luckychart.strategy;

import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.provider.GestureProvider;
import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.doubletab.ECGDoubleTabImpl;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategy;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategyImpl;
import com.seeker.luckychart.strategy.press.ECGLongPressImpl;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.DefaultScaler;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.ECGScrollerImpl;
import com.seeker.luckychart.strategy.scroll.Scroller;

/**
 * @author Seeker
 * @date 2018/11/3/003  10:16
 * @describe ecg心电图手势相关策略生成工厂
 */
public class ECGStrategyFactory implements GestureProvider{

    private DoubleTap doubleTap;

    private Scroller scroller;

    private LongPress longPress;

    private ECGRenderStrategy renderStrategy;

    private ChartProvider chartProvider;

    private ECGStrategyFactory(ChartProvider provider){
        this.chartProvider = provider;
        initStrategies();
    }

    public static ECGStrategyFactory create(ChartProvider chartProvider){
        return new ECGStrategyFactory(chartProvider);
    }

    private void initStrategies(){
        doubleTap = ECGDoubleTabImpl.create(chartProvider);
        scroller = ECGScrollerImpl.create(chartProvider);
        longPress = ECGLongPressImpl.create(chartProvider);
        renderStrategy = ECGRenderStrategyImpl.create();
    }

    @Override
    public DoubleTap getDoubleTab() {
        return doubleTap;
    }

    @Override
    public Scroller getScrollImpl() {
        return scroller;
    }

    @Override
    public LongPress getLongpresser() {
        return longPress;
    }

    @Override
    public Scaler getScaler() {
        return null;
    }

    public ECGRenderStrategy getECGRenderStrategy() {
        return renderStrategy;
    }
}
