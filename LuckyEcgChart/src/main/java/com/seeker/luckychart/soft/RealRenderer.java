package com.seeker.luckychart.soft;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.seeker.luckychart.model.ECGPointValue;

/**
 * @author Seeker
 * @date 2019/3/4/004  15:54
 * @describe 坐标背景绘制
 */
public abstract class RealRenderer {

    public SoftStrategy mSoftStrategy;

    public ECGPointValue[] mEcgData;

    public Context mContext;

    protected float mDensity;

    protected float mScaleDensity;

    public RealRenderer(@NonNull Context context, @NonNull ECGPointValue[] values){
        this.mContext = context;
        this.mEcgData = values;
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        this.mDensity = dm.density;
        this.mScaleDensity = dm.scaledDensity;
    }

    void setSoftStrategy(@NonNull SoftStrategy softStrategy){
        this.mSoftStrategy = softStrategy;
    }

    public abstract void draw(Canvas canvas);

}
