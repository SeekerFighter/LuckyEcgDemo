package com.seeker.luckychart.model;

import android.support.annotation.NonNull;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:10
 * @describe 坐标刻度值
 */

public class CoorValue {

    private float value;//坐标值

    private String label;//刻度名

    private float rawValue;//手机屏幕物理值坐标

    public CoorValue(float value, @NonNull String label) {
        this.value = value;
        this.label = label;
    }

    public float getValue() {
        return value;
    }

    public float getRawValue() {
        return rawValue;
    }

    public void setRawValue(float rawValue) {
        this.rawValue = rawValue;
    }

    public char[] getLabelAsChar(){
        return label.toCharArray();
    }

}
