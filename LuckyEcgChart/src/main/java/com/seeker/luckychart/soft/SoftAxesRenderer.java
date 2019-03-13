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
 * @date 2019/3/4/004  15:59
 * @describe TODO
 */
class SoftAxesRenderer extends RealRenderer{

    private static final int LINE_COLOR = Color.parseColor("#57C2FB");

    private Paint rowPaint;//行与行之间的画笔

    private Paint cellPaint;//网格画笔

    private Paint pointPaint;//点画笔

    SoftAxesRenderer(@NonNull Context context, @NonNull ECGPointValue[] values) {
        super(context, values);
        initPaint();
    }

    @Override
    public void draw(Canvas canvas) {

        int startX = mSoftStrategy.horizontalPadding();
        int endX = mSoftStrategy.pictureWidth() - mSoftStrategy.horizontalPadding();
        int startY = mSoftStrategy.VerticalPadding();
        int endY = mSoftStrategy.pictureHeight() - mSoftStrategy.VerticalPadding();

        drawHorizontalLine(canvas,startX,endX,startY,endY);
        drawVerticalLine(canvas,startX,endX,startY,endY);
    }

    private void drawHorizontalLine(Canvas canvas,int startX,int endX,int startY,int endY){

        int cellPixel = mSoftStrategy.pixelPerCell();
        int vCellCounts = (endY-startY)/cellPixel;

        for (int i = 0;i<=vCellCounts;i++){
            if (i == 0){
                canvas.drawLine(startX,startY+rowPaint.getStrokeWidth()/2,endX,startY+rowPaint.getStrokeWidth()/2,rowPaint);
            }else if (i == vCellCounts){
                canvas.drawLine(startX,endY-rowPaint.getStrokeWidth()/2,endX,endY-rowPaint.getStrokeWidth()/2,rowPaint);
            }else if (i % (mSoftStrategy.cellCountPerGrid()*mSoftStrategy.gridCountPerRow()) == 0){
                canvas.drawLine(startX,startY+i*cellPixel,endX,startY+i*cellPixel,rowPaint);
            }else if (i % mSoftStrategy.cellCountPerGrid() == 0){
                canvas.drawLine(startX,startY+i*cellPixel,endX,startY+i*cellPixel,cellPaint);
            }else {
                drawHorizontalPoint(canvas,startY+i*cellPixel,startX,endX);
            }
        }
    }

    private void drawHorizontalPoint(Canvas canvas,float y,int startX,int endX){
        int cellPixel = mSoftStrategy.pixelPerCell();
        int hCellCounts = (endX-startX)/cellPixel;
        for (int i = 0;i<=hCellCounts;i++){
            if (i % (mSoftStrategy.cellCountPerGrid()) == 0 || i == hCellCounts){
                continue;
            }
            canvas.drawPoint(startX+i*cellPixel,y,pointPaint);
        }
    }


    private void drawVerticalLine(Canvas canvas,int startX,int endX,int startY,int endY){

        int cellPixel = mSoftStrategy.pixelPerCell();
        int hCellCounts = (endX-startX)/cellPixel;

        for (int i = 0;i<=hCellCounts;i++){
            if (i == 0){
                canvas.drawLine(startX,startY,startX,endY,rowPaint);
            }else if (i == hCellCounts){
                canvas.drawLine(endX,startY,endX,endY,rowPaint);
            }else if (i % (mSoftStrategy.cellCountPerGrid()) == 0){
                canvas.drawLine(startX+i*cellPixel,startY,startX+i*cellPixel,endY,cellPaint);
            }
        }
    }

    private void initPaint(){
        rowPaint = new Paint();
        rowPaint.setAntiAlias(true);
        rowPaint.setColor(LINE_COLOR);
        rowPaint.setStrokeWidth(ChartUtils.dp2px(mDensity,2f));

        cellPaint = new Paint();
        cellPaint.setAntiAlias(true);
        cellPaint.setColor(LINE_COLOR);
        cellPaint.setAlpha(200);
        cellPaint.setStrokeWidth(ChartUtils.dp2px(mDensity,0.5f));

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(LINE_COLOR);
        pointPaint.setAlpha(200);
        pointPaint.setStrokeWidth(2);
    }

}
