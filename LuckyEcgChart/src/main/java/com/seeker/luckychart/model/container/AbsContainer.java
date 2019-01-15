package com.seeker.luckychart.model.container;

import android.graphics.Color;
import android.support.annotation.CallSuper;

import java.util.Arrays;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:43
 * @describe 基础数据容器
 */

public abstract class AbsContainer<Value>{

    private static final int DEFAULT_POINT_COLOR = Color.BLACK;

    private static final float DEFAULT_POINT_RADIUS = 3f;//默认原点半径

    private static final int DEFAULT_LINE_COLOR = 0xFF021F52;//折线默认颜色

    private static final float DEFAULT_LINE_STROKEWIDTH = 3.5f;//折线默认宽度

    private Value[] values;

    //点绘制颜色
    private int pointColor = DEFAULT_POINT_COLOR;

    //点回执半径大小
    private float pointRadius = DEFAULT_POINT_RADIUS;

    private int lineColor = DEFAULT_LINE_COLOR;

    private float lineStrokeWidth = DEFAULT_LINE_STROKEWIDTH;

    AbsContainer(){

    }

    AbsContainer(Value[] values ){
        this.values = values;
    }

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getLineStrokeWidth() {
        return lineStrokeWidth;
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
    }

    @CallSuper
    public void clear(){
        values = null;
    }

    /**
     * 更新新数据
     * @param newValues
     */
    public void updateNewValues(Value[] newValues){
        // TODO: 2018/10/24/024,do what you want to do
    }

}
