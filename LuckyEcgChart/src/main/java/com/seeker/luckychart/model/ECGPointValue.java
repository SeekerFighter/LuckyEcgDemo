package com.seeker.luckychart.model;

import android.graphics.Color;

/**
 * @author Seeker
 * @date 2018/10/15/015  10:01
 * @describe 心电图点模型
 */
public class ECGPointValue extends PointValue implements Cloneable{

    private static final int DEFAULT_COLOR = Color.parseColor("#021F52");

    private int drawColor = DEFAULT_COLOR;

    public static float INVALID_Y = Float.NaN;

    private boolean isNewStart;//是否是噪音之后新的开始

    private boolean isNoise;//是否是噪音

    private boolean isRPeak;//是否是R峰

    private int type =Integer.MIN_VALUE;//当前点的类型

    private String typeAnno;

    private int index;

    @Override
    public void init(){
        super.init();
        isNewStart = false;
        isNoise = false;
        isRPeak = false;
        type = Integer.MIN_VALUE;
        typeAnno = "";
        index = 0;
        isIdle = true;
        drawColor = DEFAULT_COLOR;
    }

    /**
     * 数据的完全复制
     * @param value
     */
    public void copyFrom(ECGPointValue value){
        super.copyFrom(value);
        this.isNewStart = value.isNewStart;
        this.isNoise = value.isNoise;
        this.isRPeak = value.isRPeak;
        this.type = value.type;
        this.typeAnno = value.typeAnno;
        this.index = value.index;
        this.drawColor = value.drawColor;
    }

    public int getDefaultColor(){
        return DEFAULT_COLOR;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public boolean isNewStart() {
        return isNewStart;
    }

    public void setNewStart(boolean newStart) {
        isNewStart = newStart;
    }

    public boolean isNoise() {
        return isNoise;
    }

    public void setNoise(boolean noise) {
        isNoise = noise;
    }

    public boolean isRPeak() {
        return isRPeak;
    }

    public void setRPeak(boolean RPeak) {
        isRPeak = RPeak;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeAnno() {
        return typeAnno;
    }

    public void setTypeAnno(String typeAnno) {
        this.typeAnno = typeAnno;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
