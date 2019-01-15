package com.seeker.luckychart.strategy.press;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.utils.ChartLogger;

/**
 * @author Seeker
 * @date 2018/11/3/003  10:13
 * @describe ecg心电图长按
 */
public class ECGLongPressImpl implements LongPress,Handler.Callback{

    private static final int LONGPRESSED = 1;

    private ChartProvider chartProvider;

    private Handler handler;

    private boolean gainUp = false;

    private ECGChartView chartView;

    private ECGLongPressImpl(ChartProvider provider){
        this.chartProvider = provider;
        this.chartView = (ECGChartView) chartProvider.getSelf();
        this.handler = new Handler(Looper.getMainLooper(),this);
    }

    public static ECGLongPressImpl create(ChartProvider provider){
        return new ECGLongPressImpl(provider);
    }

    @Override
    public void longPressed(MotionEvent e) {
        gainUp = e.getY() <= chartProvider.getChartComputator().getChartHeight()/2;
        handler.sendEmptyMessageDelayed(LONGPRESSED,16);
    }

    @Override
    public void finish(MotionEvent e) {
        handler.removeMessages(LONGPRESSED);
    }

    @Override
    public boolean handleMessage(Message msg) {
        handler.sendEmptyMessageDelayed(LONGPRESSED,16);
        if (gainUp) {
            chartView.gainUp();
        }else {
            chartView.gainDown();
        }
        return true;
    }
}
