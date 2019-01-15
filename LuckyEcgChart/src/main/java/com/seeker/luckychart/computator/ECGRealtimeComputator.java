package com.seeker.luckychart.computator;

import com.seeker.luckychart.annotation.UIMode;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.chartdata.ECGChartData;
import com.seeker.luckychart.model.container.ECGPointContainer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Seeker
 * @date 2017/9/30/030  9:33
 * @describe 实时心电图数据添加辅助类
 */

public final class ECGRealtimeComputator {

    private @UIMode int mode = UIMode.TRANSLATE;

    /**
     * 绘图区域要绘制的点的个数
     */
    private int plotMaxPointCount;

    private int preAppendIndex = 0;

    /**
     * 绘图区数据是否已经填满过
     */
    private boolean translateAppended = true;

    //绘图区域的点
    private ECGPointValue[] defaultRenderPoints;

    private ECGPointContainer defaultContainer;

    private ECGChartData defaultChartData;

    private boolean drawNoise = false;

    private boolean drawRPeak = false;

    private final ReentrantLock lock = new ReentrantLock();

    private ECGRealtimeComputator(){
        this.defaultContainer = ECGPointContainer.create();
        this.defaultChartData = ECGChartData.create(defaultContainer);
    }

    public static ECGRealtimeComputator create(){
        return new ECGRealtimeComputator();
    }

    public void setPlotMaxPointCount(int pointCount){
        if (plotMaxPointCount == pointCount){
            return;
        }
        this.plotMaxPointCount = pointCount;
        this.defaultRenderPoints = new ECGPointValue[plotMaxPointCount];
        this.defaultContainer.setValues(defaultRenderPoints);
    }

    /**
     * 把将要绘制的点转移到绘图区当中
     * @param points，将要添加到绘图区的点的组合
     * @return 返回绘图区的点的集合
     */
    public ECGChartData updatePointsToRender(ECGPointValue... points) {
        try {
            lock.lock();
            if (null != points && points.length > 0) {
                checkNull();
                if (mode == UIMode.TRANSLATE) {
                    translate(points);
                } else if (mode == UIMode.ERASE) {
                    erase(points);
                }
                defaultContainer.setValues(defaultRenderPoints);
                defaultChartData.setDataContainer(defaultContainer);
            }
        } finally {
            lock.unlock();
        }
        return defaultChartData;
    }

    /**
     * 数据平移
     * @param points
     * @return
     */
    private void translate(ECGPointValue... points){
        final int lengthIn = points.length;
        if (translateAppended){
            if (preAppendIndex + lengthIn <= plotMaxPointCount){
                copyFrom(preAppendIndex,lengthIn,points);
                preAppendIndex += lengthIn;
            }else {
                int destPos = Math.max(0,plotMaxPointCount-lengthIn);
                copyFrom(destPos,plotMaxPointCount-destPos,points);
                preAppendIndex = destPos;
                translateAppended = false;
            }
            if (!translateAppended){
                preAppendIndex = 0;
            }

        }else {
            int startIndex;
            if (lengthIn >= plotMaxPointCount){
                startIndex = 0;
                System.arraycopy(points,lengthIn-plotMaxPointCount,defaultRenderPoints,startIndex,plotMaxPointCount);
            }else {
                startIndex = plotMaxPointCount-lengthIn;
                ECGPointValue[] temp = Arrays.copyOfRange(defaultRenderPoints,0,lengthIn);
                System.arraycopy(defaultRenderPoints,lengthIn,defaultRenderPoints,0,startIndex);
                System.arraycopy(temp,0,defaultRenderPoints,startIndex,lengthIn);
                copyFrom(startIndex,lengthIn,points);
            }
        }
    }

    private void copyFrom(int from,int lengthIn,ECGPointValue... values){
        int len = Math.min(lengthIn,values.length);
        int realIndex;
        for (int i = 0;i < len;++i){
            realIndex = from + i;
            if (realIndex >= 0 && realIndex < defaultRenderPoints.length){
                ECGPointValue ecgPointValue = defaultRenderPoints[realIndex];
                if (ecgPointValue == null){
                    ecgPointValue = new ECGPointValue();
                    defaultRenderPoints[realIndex] = ecgPointValue;
                }
                ecgPointValue.copyFrom(values[i]);
            }
            values[i].init();
        }
    }

    /**
     * R峰标识补助
     * @param rposition 位置
     * @param type 类型
     * @param typeAnno 描述
     * @param needRPeak 修复心博类型的时候，是否要判断当前是否是心博，主要是用于修复前前一个心博的
     */
    public void repairPointRPeak(int rposition, int type, String typeAnno, boolean needRPeak){
        synchronized (lock) {
            int realIndex = -1;
            if (mode == UIMode.TRANSLATE) {
                if (translateAppended) {//这一个几乎不会进来
                    realIndex = preAppendIndex + rposition;
                } else {
                    realIndex = defaultRenderPoints.length + rposition;
                }
            } else if (mode == UIMode.ERASE) {
                realIndex = preAppendIndex + rposition;
                realIndex = realIndex < 0 ? defaultRenderPoints.length + realIndex : realIndex;
            }

            if (realIndex >= 0 && realIndex < plotMaxPointCount) {
                ECGPointValue bpm = defaultRenderPoints[realIndex];
                if (!bpm.isNoise()) {
                    if (needRPeak) {
                        if (bpm.isRPeak()) {
                            bpm.setType(type);
                            bpm.setTypeAnno(typeAnno);
                        }
                    } else {
                        bpm.setRPeak(true);
                        bpm.setType(type);
                        bpm.setTypeAnno(typeAnno);
                    }

                }
            }
        }
    }

    /**
     * 数据擦除刷新模式
     * @param points
     */
    private void erase(ECGPointValue... points){
        final int lengthIn = points.length;
        if (lengthIn >= plotMaxPointCount){
            System.arraycopy(points,lengthIn-plotMaxPointCount,defaultRenderPoints,0,plotMaxPointCount);
            preAppendIndex = 0;
            translateAppended = false;
        }else if (lengthIn + preAppendIndex <= plotMaxPointCount){
            copyFrom(preAppendIndex,lengthIn,points);
            preAppendIndex += lengthIn;
        }else {
            int part1 = plotMaxPointCount - preAppendIndex;
            copyFrom(preAppendIndex,part1, Arrays.copyOfRange(points,0,part1));
            int part2 = lengthIn - part1;
            copyFrom(0,part2, Arrays.copyOfRange(points,part1,points.length));
            preAppendIndex = part2;
            translateAppended = false;
        }
    }

    public void setDrawNoise(boolean isDraw){
        this.drawNoise = isDraw;
        checkNull();
    }

    public void setDrawRPeak(boolean isDraw){
        this.drawRPeak = isDraw;
        checkNull();
    }

    public void reset(){
        translateAppended = true;
        preAppendIndex = 0;
        defaultRenderPoints = new ECGPointValue[plotMaxPointCount];
        defaultContainer.setValues(defaultRenderPoints);
    }

    private void checkNull(){
        if (null == defaultContainer){
            defaultContainer = ECGPointContainer.create();
        }
        if (null == defaultChartData){
            defaultChartData = ECGChartData.create();
        }

        if (null == defaultRenderPoints){
            defaultRenderPoints = new ECGPointValue[plotMaxPointCount];
        }

        defaultContainer.setDrawNoise(drawNoise);
        defaultContainer.setDrawRpeak(drawRPeak);
    }

    public void setMode(@UIMode int mode) {
        this.mode = mode;
        if (mode == UIMode.ERASE && !translateAppended){
            preAppendIndex = 0;
        }
    }

    public ECGChartData getDefaultChartData() {
        return defaultChartData;
    }
}
