package com.seeker.luckychart.strategy;

import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.provider.GestureProvider;
import com.seeker.luckychart.strategy.doubletab.DefaultDoubleTabImpl;
import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.press.DefaultLongPressImpl;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.DefaultScaler;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.DefaultScrollerImpl;
import com.seeker.luckychart.strategy.scroll.Scroller;

/**
 * @author Seeker
 * @date 2018/11/3/003  10:16
 * @describe ecg心电图手势相关策略生成工厂
 */
public class DefaultStrategyFactory implements GestureProvider{

    private DoubleTap doubleTap;

    private Scroller scroller;

    private LongPress longPress;

    private Scaler scaler;

    private ChartProvider chartProvider;

    private DefaultStrategyFactory(ChartProvider provider){
        this.chartProvider = provider;
        initStrategies();
    }

    public static DefaultStrategyFactory create(ChartProvider chartProvider){
        return new DefaultStrategyFactory(chartProvider);
    }

    private void initStrategies(){
        doubleTap = DefaultDoubleTabImpl.create(chartProvider);
        scroller = DefaultScrollerImpl.create(chartProvider);
        longPress = DefaultLongPressImpl.create(chartProvider);
        scaler = DefaultScaler.create(chartProvider);
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
        return scaler;
    }

}
