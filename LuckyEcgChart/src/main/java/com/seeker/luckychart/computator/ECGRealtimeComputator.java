package com.seeker.luckychart.computator;

import com.seeker.luckychart.annotation.UIMode;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.chartdata.ECGChartData;
import com.seeker.luckychart.model.container.ECGPointContainer;

import java.util.Arrays;
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





    //绘图区域的点
    private ECGPointValue[][] defaultRenderPoints;

    private ECGPointContainer[] defaultContainers;

    private Options[] defaultOptions;

    private ECGChartData defaultChartData;

    private boolean drawNoise = false;

    private boolean drawRPeak = false;

    private final ReentrantLock lock = new ReentrantLock();

    private int ecgLineContainerCount;

    private ECGRealtimeComputator(){

    }

    public static ECGRealtimeComputator create(){
        return new ECGRealtimeComputator();
    }

    public void setEcgLineContainerCount(int ecgLineContainerCount) {
        this.ecgLineContainerCount = ecgLineContainerCount;
        this.defaultContainers = new ECGPointContainer[this.ecgLineContainerCount];
        this.defaultOptions = new Options[this.ecgLineContainerCount];
        for (int i = 0;i < this.ecgLineContainerCount;i++){
            this.defaultContainers[i] = ECGPointContainer.create();
            this.defaultOptions[i] = new Options();
        }
        this.defaultChartData = ECGChartData.create(this.defaultContainers);
    }

    public void setPlotMaxPointCount(int pointCount){
        if (plotMaxPointCount == pointCount){
            return;
        }
        this.plotMaxPointCount = pointCount;
        this.defaultRenderPoints = new ECGPointValue[ecgLineContainerCount][plotMaxPointCount];
        for (int i = 0;i < ecgLineContainerCount;i++){
            this.defaultContainers[i].setValues(this.defaultRenderPoints[i]);
        }
    }

    /**
     * 把将要绘制的点转移到绘图区当中
     * @param points ，将要添加到绘图区的点的组合
     */
    public void updatePointsToRender(int targetLineIndex, ECGPointValue... points) {
        if (targetLineIndex >= ecgLineContainerCount){
            throw new ArrayIndexOutOfBoundsException("targetLineIndex("+targetLineIndex+") >= ecgLineContainerCount("+this.ecgLineContainerCount+")!!");
        }
        try {
            lock.lock();
            if (null != points && points.length > 0) {
                checkNull();
                if (mode == UIMode.TRANSLATE) {
                    translate(defaultRenderPoints[targetLineIndex],defaultOptions[targetLineIndex],points);
                } else if (mode == UIMode.ERASE) {
                    erase(defaultRenderPoints[targetLineIndex],defaultOptions[targetLineIndex],points);
                }
                defaultContainers[targetLineIndex].setValues(defaultRenderPoints[targetLineIndex]);
                defaultChartData.setDataContainer(defaultContainers);
//                defaultChartData.setDataContainer(defaultContainer);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 数据平移
     * @param points
     * @return
     */
    private void translate(ECGPointValue[] renderPoints,Options options,ECGPointValue... points){
        final int lengthIn = points.length;
        if (options.translateAppended){
            if (options.preAppendIndex + lengthIn <= plotMaxPointCount){
                copyFrom(renderPoints,options.preAppendIndex,lengthIn,points);
                options.preAppendIndex += lengthIn;
            }else {
                int destPos = Math.max(0,plotMaxPointCount-lengthIn);
                copyFrom(renderPoints,destPos,plotMaxPointCount-destPos,points);
                options.preAppendIndex = destPos;
                options.translateAppended = false;
            }
            if (!options.translateAppended){
                options.preAppendIndex = 0;
            }

        }else {
            int startIndex;
            if (lengthIn >= plotMaxPointCount){
                startIndex = 0;
                System.arraycopy(points,lengthIn-plotMaxPointCount,renderPoints,startIndex,plotMaxPointCount);
            }else {
                startIndex = plotMaxPointCount-lengthIn;
                ECGPointValue[] temp = Arrays.copyOfRange(renderPoints,0,lengthIn);
                System.arraycopy(renderPoints,lengthIn,renderPoints,0,startIndex);
                System.arraycopy(temp,0,renderPoints,startIndex,lengthIn);
                copyFrom(renderPoints,startIndex,lengthIn,points);
            }
        }
    }

    private void copyFrom(ECGPointValue[] renderPoints,int from,int lengthIn,ECGPointValue... values){
        int len = Math.min(lengthIn,values.length);
        int realIndex;
        for (int i = 0;i < len;++i){
            realIndex = from + i;
            if (realIndex >= 0 && realIndex < renderPoints.length){
                ECGPointValue ecgPointValue = renderPoints[realIndex];
                if (ecgPointValue == null){
                    ecgPointValue = new ECGPointValue();
                    renderPoints[realIndex] = ecgPointValue;
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
    @Deprecated
    public void repairPointRPeak(int rposition, int type, String typeAnno, boolean needRPeak){
//        synchronized (lock) {
//            int realIndex = -1;
//            if (mode == UIMode.TRANSLATE) {
//                if (translateAppended) {//这一个几乎不会进来
//                    realIndex = preAppendIndex + rposition;
//                } else {
//                    realIndex = defaultRenderPoints.length + rposition;
//                }
//            } else if (mode == UIMode.ERASE) {
//                realIndex = preAppendIndex + rposition;
//                realIndex = realIndex < 0 ? defaultRenderPoints.length + realIndex : realIndex;
//            }
//
//            if (realIndex >= 0 && realIndex < plotMaxPointCount) {
//                ECGPointValue bpm = defaultRenderPoints[realIndex];
//                if (!bpm.isNoise()) {
//                    if (needRPeak) {
//                        if (bpm.isRPeak()) {
//                            bpm.setType(type);
//                            bpm.setTypeAnno(typeAnno);
//                        }
//                    } else {
//                        bpm.setRPeak(true);
//                        bpm.setType(type);
//                        bpm.setTypeAnno(typeAnno);
//                    }
//
//                }
//            }
//        }
    }

    /**
     * 数据擦除刷新模式
     * @param points
     */
    private void erase(ECGPointValue[] renderPoints,Options options,ECGPointValue... points){
        final int lengthIn = points.length;
        if (lengthIn >= plotMaxPointCount){
            System.arraycopy(points,lengthIn-plotMaxPointCount,renderPoints,0,plotMaxPointCount);
            options.preAppendIndex = 0;
            options.translateAppended = false;
        }else if (lengthIn + options.preAppendIndex <= plotMaxPointCount){
            copyFrom(renderPoints,options.preAppendIndex,lengthIn,points);
            options.preAppendIndex += lengthIn;
        }else {
            int part1 = plotMaxPointCount - options.preAppendIndex;
            copyFrom(renderPoints,options.preAppendIndex,part1, Arrays.copyOfRange(points,0,part1));
            int part2 = lengthIn - part1;
            copyFrom(renderPoints,0,part2, Arrays.copyOfRange(points,part1,points.length));
            options.preAppendIndex = part2;
            options.translateAppended = false;
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
        defaultRenderPoints = new ECGPointValue[ecgLineContainerCount][plotMaxPointCount];
        for (int i = 0,len = defaultRenderPoints.length;i<len;i++){
            ECGPointValue[] values = defaultRenderPoints[i];
            Arrays.fill(values,null);
            defaultContainers[i].setValues(values);
        }
        for (Options options:defaultOptions){
            options.reset();
        }
    }

    private void checkNull(){
        if (null == defaultContainers || defaultContainers.length != ecgLineContainerCount){
            defaultContainers = new ECGPointContainer[ecgLineContainerCount];
            for (int i = 0;i<ecgLineContainerCount;i++){
                defaultContainers[i] = ECGPointContainer.create();
            }
        }
        if (null == defaultChartData){
            defaultChartData = ECGChartData.create();
        }

        if (null == defaultRenderPoints || defaultRenderPoints.length != ecgLineContainerCount){
            defaultRenderPoints = new ECGPointValue[ecgLineContainerCount][plotMaxPointCount];
        }

        for (ECGPointContainer container:defaultContainers){
            container.setDrawNoise(drawNoise);
            container.setDrawRpeak(drawRPeak);
        }
    }

    public void setMode(@UIMode int mode) {
        if (this.mode != mode){
            this.mode = mode;
            reset();
        }
//        if (mode == UIMode.ERASE && !translateAppended){
//            preAppendIndex = 0;
//        }
    }

    public ECGChartData getDefaultChartData() {
        return defaultChartData;
    }

    private static class Options{
        /**
         * 绘图区数据是否已经填满过
         */
        boolean translateAppended = true;

        int preAppendIndex = 0;

        void reset(){
            translateAppended = true;
            preAppendIndex = 0;
        }
    }

}
