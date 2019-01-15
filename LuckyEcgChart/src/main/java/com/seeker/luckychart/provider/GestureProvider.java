package com.seeker.luckychart.provider;

import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.Scroller;

/**
 * @author Seeker
 * @date 2018/11/2/002  11:42
 * @describe 各种单独策略提供器
 */
public interface GestureProvider {

    //双击策略
    DoubleTap getDoubleTab();

    //滑动策略
    Scroller getScrollImpl();

    //长按相关策略
    LongPress getLongpresser();

    //缩放功能
    Scaler getScaler();
}
