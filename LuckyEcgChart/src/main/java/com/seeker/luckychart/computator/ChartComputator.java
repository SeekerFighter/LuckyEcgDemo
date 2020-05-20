package com.seeker.luckychart.computator;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.seeker.luckychart.charts.AbstractChartView;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategy;

import org.rajawali3d.cameras.Camera2D;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:39
 * @describe 图表参数处理器
 */

public final class ChartComputator {

    /**
     * Maximum chart zoom.
     */
    protected static final float DEFAULT_MAXIMUM_ZOOM = 20f;

    private int chartWidth = -1;//控件宽

    private int chartHeight = -1;//控件高

    /**
     * 整个控件绘制的区域，包含所有
     * 手机屏幕实际物理值大小
     */
    private Rect chartContentRect = new Rect();

    /**
     * 数据绘制区域，已经去掉坐标所占的区域
     * 手机屏幕实际物理值大小
     */
    private Rect dataContentRect = new Rect();

    /**
     * 缩放最大范围
     */
    private Coordinateport maxCoorport = new Coordinateport();

    /**
     * 实际可视范围,视图虚拟大小
     */
    private Coordinateport visibleCoorport = new Coordinateport();

    private float mDensity,mScaledDensity;

    private int deviceMin;//手机设备等小的一边大小

    //用于手势缩放功能的
    private float maxZoom = DEFAULT_MAXIMUM_ZOOM;
    private float minViewportWidth;
    private float minViewportHeight;

    private AbstractChartView.LuckyChartRenderer chartRenderer;

    private final PointF pointF = new PointF();

    private ECGRenderStrategy renderStrategy;

    private ChartComputator(Context context){
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        this.mDensity = dm.density;
        this.mScaledDensity = dm.scaledDensity;
        this.deviceMin = Math.min(dm.widthPixels,dm.heightPixels);
    }

    public boolean onChartSizeChanged(int width,int height){
        return !(chartWidth == width && chartHeight == height);
    }

    public void setChartFactSize(int width,int height){
        this.chartWidth = width;
        this.chartHeight = height;
        this.chartContentRect.set(0,0,width,height);
        this.dataContentRect.set(chartContentRect);
    }

    public void insetContentRect(int deltaLeft, int deltaTop, int deltaRight, int deltaBottom) {
        dataContentRect.left += deltaLeft;
        dataContentRect.top += deltaTop;
        dataContentRect.right -= deltaRight;
        dataContentRect.bottom -= deltaBottom;
    }

    public void setVisibleCoorport(@NonNull Coordinateport visible){
        visibleCoorport.set(visible);
    }

    public void setVisibleCoorport(float left, float top, float right, float bottom){
        constrainViewport(left, top, right, bottom);
    }

    public void setMaxCoorport(@NonNull Coordinateport max){
        maxCoorport.set(max);
        computeMinimumWidthAndHeight();
    }

    //缩放 now just for ecg
    public void scale(@NonNull Coordinateport scaleBasic){
        float visibleCenterX = visibleCoorport.centerX();
        float basicWidth = scaleBasic.width();
        float scaleLeft = Math.max(visibleCenterX - basicWidth/2,0);
        float scaleRight = Math.min(visibleCenterX + basicWidth/2,maxCoorport.right);
        visibleCoorport.set(scaleLeft,scaleBasic.top,scaleRight,scaleBasic.bottom);
        maxCoorport.top = scaleBasic.top;
        maxCoorport.bottom = scaleBasic.bottom;
    }

    //增益 now just for ecg
    public void gain(float top,float bottom){
        visibleCoorport.top = top;
        visibleCoorport.bottom = bottom;
        maxCoorport.top = top;
        maxCoorport.bottom = bottom;
    }

    //now just for ecg
    public void setProgress(@FloatRange(from = 0f,to = 1f) float progress){
        float left = (maxCoorport.width() - visibleCoorport.width()) * progress;
        float right = left + visibleCoorport.width();
        setViewportTopLeft(left,visibleCoorport.top);
    }

    public void setViewportTopLeft(float left,float top){
        final float width = visibleCoorport.width();
        final float height = visibleCoorport.height();
        left = Math.max(maxCoorport.left,Math.min(left,maxCoorport.right - width));
        top = Math.max(maxCoorport.bottom+height,Math.min(top,maxCoorport.top));
        constrainViewport(left,top,left+width,top-height);
    }

    private void constrainViewport(float left, float top, float right, float bottom) {

        if (right - left < minViewportWidth){
            right = left + minViewportWidth;
            if (left < maxCoorport.left){
                left = maxCoorport.left;
                right = left + minViewportWidth;
            }else if (right > maxCoorport.right){
                right = maxCoorport.right;
                left = right - minViewportWidth;
            }
        }

        if (top - bottom < minViewportHeight){
            bottom = top - minViewportHeight;
            if (top > maxCoorport.top){
                top = maxCoorport.top;
                bottom = top - minViewportHeight;
            }else if (bottom < maxCoorport.bottom){
                bottom = maxCoorport.bottom;
                top = bottom + minViewportHeight;
            }
        }

        visibleCoorport.left = Math.max(maxCoorport.left,left);
        visibleCoorport.top = Math.min(maxCoorport.top,top);
        visibleCoorport.right = Math.min(maxCoorport.right,right);
        visibleCoorport.bottom = Math.max(maxCoorport.bottom,bottom);

    }

    /**
     * 转化成opengl矢量坐标
     * @param x 实际手机物理x轴坐标
     * @param y 实际手机物理y轴坐标
     * @return
     */
    public final PointF screenToCartesian(float x,float y){
        Camera2D camera2D = chartRenderer.getCamera2D();
        float cameraWidth = (float) camera2D.getWidth();
        float cameraHeight = (float) camera2D.getHeight();
        pointF.x = (x / chartRenderer.getViewportWidth()) * cameraWidth - cameraWidth/2;
        pointF.y = ((chartRenderer.getViewportHeight() - y) / chartRenderer.getViewportHeight())*cameraHeight-cameraHeight/2;
        return pointF;
    }

    /**
     * 转化为实际手机物理坐标
     * @param x 虚拟坐标
     * @return
     */
    public final float computeRawX(float x){
        float pixelOffset = (x - visibleCoorport.left) *(dataContentRect.width() / visibleCoorport.width());
        return dataContentRect.left + pixelOffset;
    }

    /**
     * 转化为实际手机物理坐标
     * @param y 虚拟坐标
     * @return
     */
    public final float computeRawY(float y){
        float pixelOffset = (y - visibleCoorport.bottom)*(dataContentRect.height() / visibleCoorport.height());
        return dataContentRect.bottom - pixelOffset;
    }

    /**
     * 转化为实际手机物理坐标
     * @param y 虚拟坐标
     * @return
     */
    public final float computeECGRawY(float y,float bottom){
        float singleHeight = getSingleEcgChartHeight();
        float pixelOffset = (y - visibleCoorport.bottom)*(singleHeight / visibleCoorport.height());
        return bottom - pixelOffset;
    }


    public void computeScrollSurfaceSize(Point out) {
        out.set((int) (maxCoorport.width() * dataContentRect.width() / visibleCoorport.width()),
                (int) (maxCoorport.height() * dataContentRect.height() / visibleCoorport.height()));
    }

    //物理实际坐标转化成虚拟坐标
    public boolean computeVitual(float x,float y,PointF dest){
        if (!dataContentRect.contains((int) x,(int) y)){
            return false;
        }
        float virtualX = visibleCoorport.left +(x-dataContentRect.left)*visibleCoorport.width()/dataContentRect.width();
        float virtualY = visibleCoorport.bottom +(y-dataContentRect.bottom)*visibleCoorport.height()/-dataContentRect.height();
        dest.set(virtualX,virtualY);
        return true;
    }


    private void computeMinimumWidthAndHeight() {
        minViewportWidth = this.maxCoorport.width() / maxZoom;
        minViewportHeight = this.maxCoorport.height() / maxZoom;
    }

    public int getChartWidth() {
        return chartWidth;
    }

    public int getChartHeight() {
        return chartHeight;
    }

    public Rect getChartContentRect() {
        return chartContentRect;
    }

    public Rect getDataContentRect() {
        return dataContentRect;
    }

    public static ChartComputator create(Context context){
        return new ChartComputator(context);
    }

    public float getDensity() {
        return mDensity;
    }

    public float getScaledDensity() {
        return mScaledDensity;
    }

    public Coordinateport getMaxCoorport() {
        return maxCoorport;
    }

    public Coordinateport getVisibleCoorport() {
        return visibleCoorport;
    }

    public int getDeviceMin() {
        return deviceMin;
    }

    public float getMinViewportWidth() {
        return minViewportWidth;
    }

    public float getMinViewportHeight() {
        return minViewportHeight;
    }

    public void setChartRenderer(AbstractChartView.LuckyChartRenderer chartRenderer) {
        this.chartRenderer = chartRenderer;
    }

    public AbstractChartView.LuckyChartRenderer getChartRenderer() {
        return chartRenderer;
    }

    public void setRenderStrategy(ECGRenderStrategy renderStrategy) {
        this.renderStrategy = renderStrategy;
    }

    //返回单个ecg图波纹高度
    public float getSingleEcgChartHeight(){
        float height = getChartContentRect().height();
        float space = renderStrategy.getEcgPortSpace();
        int count = renderStrategy.getEcgLineCount();
        return (height-space*(count-1))/count;
    }

}
