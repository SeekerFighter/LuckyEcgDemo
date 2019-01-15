package com.seeker.luckychart.model.container;

import com.seeker.luckychart.model.ECGPointValue;

/**
 * @author Seeker
 * @date 2018/10/15/015  10:02
 * @describe 心电图点模型容器
 */
public class ECGPointContainer extends AbsContainer<ECGPointValue>{

    private boolean drawNoise;//是否绘制噪音

    private boolean drawRpeak;//是否绘制R波峰

    private int destIndex = 0;

    private ECGPointContainer(ECGPointValue[] values){
        super(values);
    }

    private ECGPointContainer(){
        super();
    }

    public static ECGPointContainer create(){
        return new ECGPointContainer();
    }

    public static ECGPointContainer create(ECGPointValue[] values){
        return new ECGPointContainer(values);
    }

    public boolean isDrawNoise() {
        return drawNoise;
    }

    public void setDrawNoise(boolean drawNoise) {
        this.drawNoise = drawNoise;
    }

    public boolean isDrawRpeak() {
        return drawRpeak;
    }

    public void setDrawRpeak(boolean drawRpeak) {
        this.drawRpeak = drawRpeak;
    }

    @Override
    public void updateNewValues(ECGPointValue[] newValues) {
        if (newValues == null || newValues.length == 0){
            return;
        }

        int addCount = newValues.length;

        ECGPointValue[] values = getValues();

        int len = values.length;

        if (destIndex + addCount <= len){
            System.arraycopy(newValues,0,values,destIndex,addCount);
            destIndex += addCount;
        }else {
            int subAdd = addCount + destIndex - len;
            System.arraycopy(values, subAdd, values, 0, len-subAdd);
            System.arraycopy(newValues, 0, values, len - addCount, addCount);
            destIndex = len;
        }

    }

    /**
     * 返回数组有效的长度， start 0
     * @return
     */
    public int getValidCount(){
        return destIndex;
    }

}
