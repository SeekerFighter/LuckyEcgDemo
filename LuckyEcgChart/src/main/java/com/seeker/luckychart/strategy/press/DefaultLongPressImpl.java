package com.seeker.luckychart.strategy.press;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/11/3/003  10:13
 * @describe ecg心电图长按
 */
public class DefaultLongPressImpl implements LongPress,Handler.Callback{

    private ChartProvider chartProvider;

    private Handler handler;

    private DefaultLongPressImpl(ChartProvider provider){
        this.chartProvider = provider;
        this.handler = new Handler(Looper.getMainLooper(),this);
    }

    public static DefaultLongPressImpl create(ChartProvider provider){
        return new DefaultLongPressImpl(provider);
    }

    @Override
    public void longPressed(MotionEvent e) {
        handler.sendEmptyMessageDelayed(0,50);
    }

    @Override
    public void finish(MotionEvent e) {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(Message msg) {

        handler.sendEmptyMessageDelayed(0,50);
        return true;
    }
}
