package com.seeker.luckychart.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;

import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.model.CoorValue;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.utils.ChartUtils;

/**
 * @author Seeker
 * @date 2018/6/29/029  13:17
 * @describe 绘制坐标的控件,针对横向滑动
 */
@SuppressLint("ViewConstructor")
public class LeftAxisView extends View{

    private ChartProvider provider;

    private ChartAxis leftAxis;

    private  char[] drawed;

    private Rect dataContent;

    public LeftAxisView(Context context, @NonNull ChartProvider provider) {
        super(context);
        this.provider = provider;
        this.leftAxis = provider.getChartData().getLeftAxis();
        this.drawed = new char[leftAxis.getMaxCoorchars()];
        this.dataContent = provider.getChartComputator().getDataContentRect();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (provider.getChartComputator().getDataContentRect().left
                        +provider.getChartData().getLeftAxis().getLineMajorPaint().getStrokeWidth()/2f),-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAxisCoor(canvas);

        drawMajorLine(canvas);

    }

    /**
     * 绘制刻度值
     */
    private void drawAxisCoor(Canvas canvas){
        canvas.save();
        final CoorValue[] coorValues = leftAxis.getCoordinateValues();
        float cooX = leftAxis.getCoorBaseLine();
        final Paint coorPaint = leftAxis.getCoorPaint();
        float realX,realY;
        for (int i = 0,len = coorValues.length;i<len;++i){
            if (i % leftAxis.getModule() == 0){
                CoorValue coorValue = coorValues[i];
                ChartUtils.copyof(coorValue.getLabelAsChar(),drawed);
                if (i == 0){
                    realY = (int) (coorValue.getRawValue() - leftAxis.getCoorHeight());
                }else {
                    realY = (int) (coorValue.getRawValue() - leftAxis.getCoorHeight() / 2f);
                }
                realX = (int) (cooX - ChartUtils.measureText(drawed,coorPaint));
                if (coorValue.getRawValue() >= dataContent.top-ChartUtils.CONTAIN_OFFSET
                        && coorValue.getRawValue() <= dataContent.bottom+ChartUtils.CONTAIN_OFFSET){
                    Bitmap bitmap = ChartUtils.drawBitmapText(drawed,0,drawed.length,coorPaint);
                    canvas.drawBitmap(bitmap,realX,realY,null);
                    bitmap.recycle();
                }
            }
        }
        canvas.restore();
    }

    private void drawMajorLine(Canvas canvas){
        canvas.save();
        canvas.drawLine(leftAxis.getSeparationLine(),dataContent.bottom,leftAxis.getSeparationLine(),dataContent.top,leftAxis.getLineMajorPaint());
        canvas.restore();
    }
}
