package com.seeker.luckychart.model;

import android.support.annotation.CallSuper;

/**
 * @author Seeker
 * @date 2017/9/29/029  17:20
 */

public class PointValue implements IValueChanged {

    public boolean isIdle = true;

    protected float coorX;//坐标x

    protected float coorY;//坐标y

    protected float originX;
    protected float originY;
    protected float diffX;
    protected float diffY;

    public PointValue(){
        coorY = Float.NaN;
    }

    public PointValue(float coorX, float coorY) {
        set(coorX,coorY);
    }

    public float getCoorX() {
        return coorX;
    }

    public void setCoorX(float coorX) {
        this.coorX = coorX;
    }

    public float getCoorY() {
        return coorY;
    }

    public void setCoorY(float coorY) {
        this.coorY = coorY;
    }

    private PointValue set(float x,float y){
        this.coorX = x;
        this.coorY = y;
        this.originX = x;
        this.originY = y;
        return this;
    }

    public PointValue setTarget(float targetX, float targetY) {
        this.diffX = targetX - originX;
        this.diffY = targetY - originY;
        return this;
    }

    @CallSuper
    public void init(){
        coorX = 0;
        coorY = 0;
        originX = 0;
        originY = 0;
        diffX = 0;
        diffY = 0;
    }

    @Override
    public void update(float scale) {
        coorX = originX + diffX * scale;
        coorY = originY + diffY * scale;
    }

    @Override
    public void finish() {
        set(originX + diffX, originY + diffY);
    }

    @Override
    public String toString() {
        return "{" +
                "coorX=" + coorX +
                ", coorY=" + coorY +
                ", originX=" + originX +
                ", originY=" + originY +
                ", diffX=" + diffX +
                ", diffY=" + diffY +
                '}';
    }

    public void copyFrom(PointValue value){
        this.coorX = value.coorX;
        this.coorY = value.coorY;
        this.originX = value.originX;
        this.originY = value.originY;
        this.diffX = value.diffX;
        this.diffY = value.diffY;
    }

}
