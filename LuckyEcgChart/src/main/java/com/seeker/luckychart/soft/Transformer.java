package com.seeker.luckychart.soft;

import android.graphics.Rect;

import com.seeker.luckychart.model.Coordinateport;

/**
 * @author Seeker
 * @date 2019/3/5/005  8:49
 * @describe 坐标系转换
 */
public abstract class Transformer {

    /**
     * 实际可视范围,视图虚拟大小
     */
    private Coordinateport visibleCoorport = new Coordinateport();

    /**
     * 数据绘制区域，已经去掉坐标所占的区域
     * 手机屏幕实际物理值大小
     */
    private Rect dataContentRect = new Rect();

    public Transformer(){

    }

    public final void setVisibleCoorport(float left,float top,float right,float bottom){
        this.visibleCoorport.set(left, top, right, bottom);
    }

    public final void setDataContentRect(int left, int top, int right, int bottom){
        this.dataContentRect.set(left,top,right,bottom);
    }

    public float computeRawX(int index){
        float pixelOffset = (index - visibleCoorport.left) *(dataContentRect.width() / visibleCoorport.width());
        return dataContentRect.left + pixelOffset;
    }

    public float computeRawY(float value){
        float pixelOffset = (value - visibleCoorport.bottom)*(dataContentRect.height() / visibleCoorport.height());
        return dataContentRect.bottom - pixelOffset;
    }

    public boolean needDraw(float currentY,float nextY){
        if (currentY == nextY){
            return !(currentY == dataContentRect.top) && !(currentY == dataContentRect.bottom);
        }
        return true;
    }

}
