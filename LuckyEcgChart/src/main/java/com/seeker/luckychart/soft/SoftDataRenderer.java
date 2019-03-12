package com.seeker.luckychart.soft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.utils.ChartUtils;

/**
 * @author Seeker
 * @date 2019/3/4/004  15:58
 * @describe TODO
 */
class SoftDataRenderer extends RealRenderer{

    private Transformer transformer;

    private int dataLeft;

    private int dataRight;

    private int rowHeight;

    private Paint linePaint;

    SoftDataRenderer(@NonNull Context context, @NonNull ECGPointValue[] values) {
        super(context, values);
        initPaint();
    }

    @Override
    void setSoftStrategy(@NonNull SoftStrategy softStrategy) {
        super.setSoftStrategy(softStrategy);
        this.transformer = mSoftStrategy.getTransformer();
        this.dataLeft = mSoftStrategy.horizontalPadding();
        this.dataRight = mSoftStrategy.pictureWidth()-mSoftStrategy.horizontalPadding();
        this.rowHeight = (mSoftStrategy.pictureHeight()-mSoftStrategy.VerticalPadding()*2)/mSoftStrategy.totalRows();
    }

    @Override
    public void draw(Canvas canvas) {
        transformer.setVisibleCoorport(0,maxDataValue/2,mSoftStrategy.pointsPerRow(),-maxDataValue/2);
        for (int i = 0,rows = mSoftStrategy.totalRows();i < rows;i++){
            transformer.setDataContentRect(dataLeft,i*rowHeight,dataRight,(i+1)*rowHeight);
            int start = i*mSoftStrategy.pointsPerRow();
            int end = Math.min((i+1)*mSoftStrategy.pointsPerRow(),mEcgData.length);
            for (int j = start;j < end-1;j++){
                final float currentX = transformer.computeRawX((j-start));
                final float currentY = transformer.computeRawY(mEcgData[j].getCoorY());
                final float nextX = transformer.computeRawX((j+1-start));
                final float nextY = transformer.computeRawY(mEcgData[j+1].getCoorY());
                if (!transformer.needDraw(currentY,nextY)){
                    continue;
                }
                canvas.drawLine(currentX,currentY,nextX,nextY,linePaint);
            }
        }
    }

    private void initPaint(){
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(ChartUtils.dp2px(mDensity, 1));
        linePaint.setColor(Color.parseColor("#021F52"));
    }
}
