package com.seeker.luckychart.model.container;

import android.graphics.Color;
import android.support.annotation.IntDef;

import com.seeker.luckychart.model.PointValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Seeker
 * @date 2018/6/7/007  12:54
 * @describe 点集合容器,点的绘制以后改为享元模式，减少内存的消耗
 */

public class PointContainer extends AbsContainer<PointValue> {

    private static final int SHAPE_CIRCLE = 0x01;//圆形

    private static final int SHAPE_SQUARE = 0x02;//方形

    @IntDef({SHAPE_CIRCLE,SHAPE_SQUARE})
    @Retention(RetentionPolicy.SOURCE)
    @interface PointShape{

    }

    //点绘制形状
    private int pointShape = SHAPE_CIRCLE;

    private PointContainer(){
        super();
    }

    private PointContainer(PointValue[] values){
        super(values);
    }

    public static PointContainer create(){
        return new PointContainer();
    }

    public static PointContainer create(PointValue[] values){
        return new PointContainer(values);
    }

    public int getPointShape() {
        return pointShape;
    }

    public void setPointShape(@PointShape int pointShape) {
        this.pointShape = pointShape;
    }

}
