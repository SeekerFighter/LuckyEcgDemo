package com.seeker.luckychart.charts;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.seeker.luckychart.animation.ChartCoordinateportAnimatorImpl;
import com.seeker.luckychart.computator.ChartComputator;
import com.seeker.luckychart.gesture.AbstractTouchHandler;
import com.seeker.luckychart.gesture.ChartTouchHandler;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.provider.ChartProvider;
import com.seeker.luckychart.provider.DataProvider;
import com.seeker.luckychart.render.inters.LuckyAxesRenderer;
import com.seeker.luckychart.render.inters.LuckyDataRenderer;
import com.seeker.luckychart.strategy.DefaultStrategyFactory;
import com.seeker.luckychart.strategy.doubletab.DoubleTap;
import com.seeker.luckychart.strategy.press.LongPress;
import com.seeker.luckychart.strategy.scale.Scaler;
import com.seeker.luckychart.strategy.scroll.Scroller;
import com.seeker.luckychart.utils.ChartLogger;

import org.rajawali3d.cameras.Camera2D;
import org.rajawali3d.scene.ASceneFrameCallback;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Seeker
 * @date 2018/10/18/018  16:38
 * @describe TODO
 */
public abstract class AbstractChartView<ChartData extends DataProvider> extends SurfaceView
        implements IDisplay,ChartProvider<ChartData>{

    private static final String TAG = "AbstractChartView";

    protected Context mContext;

    protected ChartComputator chartComputator;

    protected ChartData chartData;

    protected LuckyDataRenderer dataRenderer;

    protected LuckyAxesRenderer axesRenderer;

    protected AbstractTouchHandler touchHandler;

    protected boolean isTouchable = false;//是否响应触摸事件

    protected FrameRenderCallback frameRenderCallback = new DummpyFrameRenderCallback();

    protected LuckyChartRenderer chartRenderer;

    private ChartCoordinateportAnimatorImpl chartCoordinateportAnimator;

    private DefaultStrategyFactory defaultStrategyFactory;

    public AbstractChartView(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    public AbstractChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initialize();
    }

    private void initialize(){
        setAntiAliasingMode(ANTI_ALIASING_CONFIG.MULTISAMPLING);
        setSampleCount(2);
        chartCoordinateportAnimator = ChartCoordinateportAnimatorImpl.create(this);
        chartComputator = ChartComputator.create(mContext);
        chartRenderer = createRenderer();
        axesRenderer = getChartAxesRenderer();
        dataRenderer = getChartDataRenderer();
        chartComputator.setChartRenderer(chartRenderer);
        touchHandler = new ChartTouchHandler(this);
        setSurfaceRenderer(chartRenderer);
        defaultStrategyFactory = DefaultStrategyFactory.create(this);
    }

    @Override
    public LuckyChartRenderer createRenderer() {
        return new LuckyChartRenderer(mContext,getASceneFrameCallback());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != touchHandler){
            touchHandler.dispatchTouchEvent(event,getParent());
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (isTouchable){
            if (touchHandler.handleTouchEvent(event,getParent())) {
                applyRenderUpdate();
            }
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isTouchable){
            if (touchHandler.computeScroll()) {
                applyRenderUpdate();
            }
        }
    }

    @Override
    public void setChartData(ChartData data) {
        this.chartData = data;
        if (axesRenderer != null){
            axesRenderer.onChartDataChanged();
        }
        if (dataRenderer != null){
            dataRenderer.onChartDataChanged();
        }
    }

    @Override
    public void setChartVisibleCoordinateport(@NonNull Coordinateport visible) {
        chartComputator.setVisibleCoorport(visible);
    }

    @Override
    public void setChartVisibleCoordinateportWithAnim(@NonNull Coordinateport target, long duration) {
        chartCoordinateportAnimator.cancelAnimation();
        chartCoordinateportAnimator.startAnimation(chartComputator.getVisibleCoorport(),target,duration);
    }

    @Override
    public void setChartMaxCoordinateport(@NonNull Coordinateport max) {
        chartComputator.setMaxCoorport(max);
    }

    @Override
    public ChartData getChartData() {
        return this.chartData;
    }

    @Override
    public void clearChartData() {
        if (null != chartData){
            chartData.clear();
        }
    }

    @Override
    public ChartComputator getChartComputator() {
        return chartComputator;
    }

    @Override
    public Context getContexter() {
        return getContext();
    }

    @Override
    public void setTouchable(boolean isTouchable){
        this.isTouchable = isTouchable;
    }

    @Override
    public org.rajawali3d.renderer.Renderer getChartGlRenderer() {
        return chartRenderer;
    }

    @Override
    public DoubleTap getDoubleTab() {
        return defaultStrategyFactory.getDoubleTab();
    }

    @Override
    public Scroller getScrollImpl() {
        return defaultStrategyFactory.getScrollImpl();
    }

    @Override
    public LongPress getLongpresser() {
        return defaultStrategyFactory.getLongpresser();
    }

    @Override
    public Scaler getScaler() {
        return defaultStrategyFactory.getScaler();
    }

    public ASceneFrameCallback getASceneFrameCallback(){
        return null;
    }

    /**
     * 主动去申请视图重绘
     */
    public void applyRenderUpdate(){
        queueEvent(ASYNTASK);
        requestRenderUpdate();
    }

    //绘制完成之后的操作 work in render thread
    public void onAsyRenderUpdateLagWork(){
        // TODO: 2018/11/1/001
    }

    /**
     * gl线程里面，为下一次的屏幕刷新准备工作
     */
    @CallSuper
    public void onAsynWorkForNextRender(){
        frameRenderCallback.onPrepareNextFrame(0);
        if (dataRenderer != null) {
            dataRenderer.onDataRender();
        }
    }

    public final class LuckyChartRenderer extends org.rajawali3d.renderer.Renderer{

        private Camera2D camera2D;

        LuckyChartRenderer(Context context,ASceneFrameCallback frameCallback) {
            this(context,false,frameCallback);
        }

        LuckyChartRenderer(Context context, boolean registerForResources,ASceneFrameCallback frameCallback) {
            super(context, registerForResources);
            camera2D = new Camera2D();
            camera2D.setWidth(2);
            camera2D.setHeight(1);
            camera2D.setPosition(0,0,2);
            camera2D.enableLookAt();
            getCurrentScene().switchCamera(camera2D);
            getCurrentScene().setBackgroundColor(Color.WHITE);
            if (frameCallback != null) {
                getCurrentScene().registerFrameCallback(frameCallback);
            }
        }

        public Camera2D getCamera2D(){
            return camera2D;
        }

        @Override
        protected void initScene() {
            ChartLogger.vTag(TAG,"initScene() called...");
            if (axesRenderer != null) {
                axesRenderer.initScene();
            }

            if (dataRenderer != null){
                dataRenderer.initScene();
            }
        }

        @Override
        public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
            super.onRenderSurfaceSizeChanged(gl, width, height);
            ChartLogger.vTag(TAG,"onRenderSurfaceSizeChanged() called：width = "+width+",height = "+height);
            if (chartComputator.onChartSizeChanged(width, height)) {
                chartComputator.setChartFactSize(width, height);
                if (axesRenderer != null) {
                    axesRenderer.onChartSizeChanged();
                }
                if (dataRenderer != null) {
                    dataRenderer.onChartSizeChanged();
                }
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            if (getRenderMode() == ISurface.RENDERMODE_CONTINUOUSLY) {
                onAsynWorkForNextRender();
            }
            super.onRender(ellapsedRealtime, deltaTime);
            onAsyRenderUpdateLagWork();
        }
    }

    public void setFrameRenderCallback(FrameRenderCallback frameRenderCallback) {
        this.frameRenderCallback = frameRenderCallback;
    }

    public interface FrameRenderCallback{
        /**
         * 帧回调，需要在这里处理准备下一帧绘制所需的数据
         * @param duration 帧间隔 单位毫秒
         */
       void onPrepareNextFrame(long duration);
    }

    private static final class DummpyFrameRenderCallback implements FrameRenderCallback{

        @Override
        public void onPrepareNextFrame(long durationMs) {

        }
    }

    private final Runnable ASYNTASK = new Runnable() {
        @Override
        public void run() {
            onAsynWorkForNextRender();
        }
    };

}
