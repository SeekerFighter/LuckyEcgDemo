package com.seeker.luckychart.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;

import com.seeker.luckychart.R;
import com.seeker.luckychart.annotation.UIMode;
import com.seeker.luckychart.computator.ECGRealtimeComputator;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.chartdata.ECGChartData;
import com.seeker.luckychart.model.container.ECGPointContainer;
import com.seeker.luckychart.render.ecg.ECGChartAxesRenderer;
import com.seeker.luckychart.render.ecg.ECGChartDataRender;
import com.seeker.luckychart.strategy.ECGStrategyFactory;
import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategy;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategyImpl;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.Scroller;

/**
 * @author Seeker
 * @date 2018/10/15/015  9:45
 * @describe 心电图图表
 */
public class ECGChartView extends AbstractChartView<ECGChartData> implements RealTime{

    private Coordinateport defaultCoordinateport = new Coordinateport();

    private int[] measureResult = new int[2];

    private ECGRealtimeComputator realtimeComputator;

    private OnVisibleCoorPortChangedListener visibleCoorPortChangedListener;

    private Boolean canScaleOrGain = true;//是否可以缩放或者增益

    private ECGStrategyFactory gestureFactory;

    private ECGRenderStrategy renderStrategy;

    public ECGChartView(Context context) {
        this(context,null);
    }

    public ECGChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        realtimeComputator = ECGRealtimeComputator.create();
        gestureFactory = ECGStrategyFactory.create(this);
        renderStrategy = gestureFactory.getECGRenderStrategy();
        applyAttributes(context,attrs);
        chartComputator.setRenderStrategy(renderStrategy);
        realtimeComputator.setEcgLineContainerCount(renderStrategy.getEcgLineCount());
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ECGChartView);
        final int count = array.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.ECGChartView_isTouchable){
                setTouchable(array.getBoolean(attr,false));
            }else if (attr == R.styleable.ECGChartView_yOuterCellCounts){
                renderStrategy.setYOuterCellCounts(array.getInt(attr,ECGRenderStrategyImpl.DEFAULT_OUTER_CELLCOUNTS_Y));
            }else if (attr == R.styleable.ECGChartView_ecgLineCount){
                renderStrategy.setEcgLineCount(array.getInt(attr,1));
            }else if (attr == R.styleable.ECGChartView_ecgportSpace){
                renderStrategy.setEcgPortSpace(array.getDimensionPixelSize(attr,30));
            }else if (attr == R.styleable.ECGChartView_markTextStyle){
                renderStrategy.setMarkTextStyle(array.getString(attr));
            }else if (attr == R.styleable.ECGChartView_canLineBound){
                renderStrategy.setCanLineBound(array.getBoolean(attr,false));
            }
        }
        array.recycle();
    }

    @Override
    public ECGChartDataRender getChartDataRenderer() {
        return ECGChartDataRender.create(this);
    }

    @Override
    public ECGChartAxesRenderer getChartAxesRenderer() {
        return ECGChartAxesRenderer.create(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredWithSize = getMeasuredWidth();
        final ECGRenderStrategy strategy = renderStrategy;
        strategy.onViewMeasured(measuredWithSize,getMeasuredHeight(),measureResult);
        int count = strategy.getEcgLineCount();
        setMeasuredDimension(measureResult[0], (int) (measureResult[1]*count+(count-1)*strategy.getEcgPortSpace()));
        defaultCoordinateport.set(0,strategy.getYMaxMvs()/2,
                strategy.getXTotalPointCounts(),-strategy.getYMaxMvs()/2);
        setChartVisibleCoordinateport(defaultCoordinateport);
        setChartMaxCoordinateport(defaultCoordinateport);
    }

    @Override
    public void setChartData(final ECGChartData chartData) {
        ECGPointContainer[] containers = chartData.getDataContainer();

        if (containers != null && containers.length > 0){
            int right = 0;
            for (ECGPointContainer c:containers){
                ECGPointValue[] values = c.getValues();
                if (values != null){
                    right = Math.max(right,values.length);
                }
            }
            chartComputator.getMaxCoorport().right = right;
            chartComputator.getVisibleCoorport().left = 0;
            chartComputator.getVisibleCoorport().right = renderStrategy.getXTotalPointCounts();
        }
//
//        if (container != null){
//            ECGPointValue[] values = container.getValues();
//            if (values != null){
//                chartComputator.getMaxCoorport().right = values.length;
//                chartComputator.getVisibleCoorport().left = 0;
//                chartComputator.getVisibleCoorport().right = renderStrategy.getXTotalPointCounts();
//            }
//        }
        super.setChartData(chartData);
    }

    @Override
    public void onAsyRenderUpdateLagWork() {
        if (visibleCoorPortChangedListener != null){
            Coordinateport visible = chartComputator.getVisibleCoorport();
            Coordinateport max = chartComputator.getMaxCoorport();
            visibleCoorPortChangedListener.onChanged(visible,max);
        }
        synchronized (canScaleOrGain){
            canScaleOrGain = true;
        }
    }

    @Override
    @Deprecated
    public void repairPointRPeak(int rposition,int type,String tyoeAnno,boolean needRPeak){
//        realtimeComputator.repairPointRPeak(rposition, type, tyoeAnno,needRPeak);
    }

    @Override
    public DoubleTap getDoubleTab() {
        return gestureFactory.getDoubleTab();
    }

    @Override
    public Scroller getScrollImpl() {
        return gestureFactory.getScrollImpl();
    }

    @Override
    public LongPress getLongpresser() {
        return gestureFactory.getLongpresser();
    }

    @Override
    public Scaler getScaler() {
        return gestureFactory.getScaler();
    }

    @Override
    public ECGChartView getSelf() {
        return this;
    }

    @Override
    public void applyRenderUpdate() {
        synchronized (canScaleOrGain) {
            if (!canScaleOrGain) {
                return;
            }
            canScaleOrGain = false;
            super.applyRenderUpdate();
        }
    }

    public ECGRenderStrategy getECGRenderStrategy() {
        return renderStrategy;
    }

    public void initDefaultChartData(final boolean drawRPeak, final boolean drawNoise){
        post(new Runnable() {
            @Override
            public void run() {
                realtimeComputator.setPlotMaxPointCount(renderStrategy.getXTotalPointCounts());
                setChartData(realtimeComputator.getDefaultChartData());
                realtimeComputator.setDrawRPeak(drawRPeak);
                realtimeComputator.setDrawNoise(drawNoise);
            }
        });
    }

    public void updatePointsToRender(int targetLineIndex, ECGPointValue... values){
        realtimeComputator.updatePointsToRender(targetLineIndex,values);
    }

    public void updatePointsToRender(ECGPointValue[]... values){
        for (int i = 0,len = values.length;i < len;i++) {
            realtimeComputator.updatePointsToRender(i,values[i]);
        }
    }

    /**
     * 是否绘制R峰
     * @param draw
     */
    public void setDrawRPeak(boolean draw){
        realtimeComputator.setDrawRPeak(draw);
    }

    /**
     * 是否绘制噪音
     * @param draw
     */
    public void setDrawNoise(boolean draw){
        realtimeComputator.setDrawNoise(draw);
    }

    public void setMode(@UIMode int mode){
        realtimeComputator.setMode(mode);
    }

    public void reset(){
        realtimeComputator.reset();
    }

    //放大
    public void scaleUp(){
        synchronized (canScaleOrGain){
            if (!canScaleOrGain){
                return;
            }
            if (renderStrategy.scale(renderStrategy.getYOuterCellCount()-2)) {
                defaultCoordinateport.set(0, renderStrategy.getYMaxMvs() / 2,
                        renderStrategy.getXTotalPointCounts(), -renderStrategy.getYMaxMvs() / 2);
                chartComputator.scale(defaultCoordinateport);
                layoutChanged();
            }
        }
    }

    //缩小
    public void scaleDown(){
        synchronized (canScaleOrGain){
            if (!canScaleOrGain){
                return;
            }
            if (renderStrategy.scale(renderStrategy.getYOuterCellCount()+2)) {
                defaultCoordinateport.set(0, renderStrategy.getYMaxMvs() / 2,
                        renderStrategy.getXTotalPointCounts(), -renderStrategy.getYMaxMvs() / 2);
                chartComputator.scale(defaultCoordinateport);
                layoutChanged();
            }
        }
    }

    //增益 +
    public void gainUp(){
        synchronized (canScaleOrGain){
            if (!canScaleOrGain){
                return;
            }
            if (renderStrategy.gain(renderStrategy.getYCellCountsPerMv()+1)) {
                chartComputator.gain(renderStrategy.getYMaxMvs() / 2, -renderStrategy.getYMaxMvs() / 2);
                layoutChanged();
            }
        }
    }

    //增益 -
    public void gainDown(){
        synchronized (canScaleOrGain){
            if (!canScaleOrGain){
                return;
            }
            if (renderStrategy.gain(renderStrategy.getYCellCountsPerMv()-1)) {
                chartComputator.gain(renderStrategy.getYMaxMvs() / 2, -renderStrategy.getYMaxMvs() / 2);
                layoutChanged();
            }
        }
    }

    //设置进度
    public void setProgress(@FloatRange(from = 0f,to = 1f) float progress){
        chartComputator.setProgress(progress);
        applyRenderUpdate();
    }

    public void setOnVisibleCoorPortChangedListener(OnVisibleCoorPortChangedListener visibleCoorPortChangedListener) {
        this.visibleCoorPortChangedListener = visibleCoorPortChangedListener;
    }

    private void layoutChanged(){
        if (axesRenderer != null){
            axesRenderer.onChartlayoutChanged();
        }
        if (dataRenderer != null){
            dataRenderer.onChartlayoutChanged();
        }
        applyRenderUpdate();
    }

    //可视范围变化监听
    public interface OnVisibleCoorPortChangedListener{
        void onChanged(Coordinateport visiblePort,Coordinateport maxPort);
    }

}
