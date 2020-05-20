package com.seeker.luckychart.render.ecg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.util.TypedValue;
import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.glmodel.ECGLine3D;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.chartdata.ECGChartData;
import com.seeker.luckychart.model.container.ECGPointContainer;
import com.seeker.luckychart.render.AbstractChartDataRenderer;
import com.seeker.luckychart.strategy.ecgrender.ECGRenderStrategy;
import com.seeker.luckychart.utils.ChartUtils;
import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera2D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;
import java.util.List;

/**
 * @author Seeker
 * @date 2018/10/15/015  10:07
 * @describe 心电图绘制渲染器
 */
public class ECGChartDataRender extends AbstractChartDataRenderer<ECGChartData> {

    private ECGChartView chartView;

    private Plane bpmPlane;

    private Texture mBpmTexture;

    private Material bpmMaterial;

    private Paint paint;

    private Canvas canvas;

    private Bitmap bitmap;

    private float baseLine;

    private Object3D lineContainer;

    private ECGChartDataRender(ECGChartView chartView) {
        super(chartView);
        this.chartView = chartView;
    }

    public static ECGChartDataRender create(ECGChartView chartView){
        return new ECGChartDataRender(chartView);
    }

    @Override
    public void initScene() {
        initEcgLine();
        initAboutRPeak();
    }

    @Override
    public void onDataRender() {
        if (checkDataAvailable() && lineContainer != null){
            ECGChartData chartData = chartProvider.getChartData();
            ECGPointContainer[] containers = chartData.getDataContainer();
            ECGRenderStrategy renderStrategy = chartView.getECGRenderStrategy();
            int count = Math.min(containers.length,renderStrategy.getEcgLineCount());
            count = Math.min(count,lineContainer.getNumChildren());
            for (int i = 0;i< count;i++){
                ECGPointContainer container = containers[i];
                ECGLine3D ecgLine = (ECGLine3D) lineContainer.getChildAt(i);
                prepareEcgLine(container,ecgLine,i == 0);
                final ECGPointValue[] values = container.getValues();
                float top = chartComputator.getSingleEcgChartHeight()*i+renderStrategy.getEcgPortSpace()*i;
                float bottom = top + chartComputator.getSingleEcgChartHeight();
                drawOscillogram(ecgLine,values,container.isDrawNoise(),container.isDrawRpeak(),top,bottom);
            }
        }
    }

    private void drawOscillogram(ECGLine3D ecgLine,ECGPointValue[] values,boolean drawNoise,boolean drawRpeak,float top,float bottom){
        Coordinateport visiblePort = chartComputator.getVisibleCoorport();
        ECGRenderStrategy renderStrategy = chartView.getECGRenderStrategy();
        int len = renderStrategy.getXTotalPointCounts();
        int startIndex = (int) visiblePort.left;
        int endIndex = startIndex + len;
        if (endIndex > chartComputator.getMaxCoorport().right){
            endIndex = (int) chartComputator.getMaxCoorport().right;
            startIndex = endIndex - len;
            startIndex = Math.max(0,startIndex);
        }
        float preX = 0f,preY = 0f;
        int preColor = 0;
        float preRawY = 0;
        for (int i = startIndex; i < endIndex; ++i){
            ECGPointValue curPoint = values[i];
            if (curPoint == null || Float.isNaN(curPoint.getCoorY())){
                ecgLine.addVertexToBuffer(preX, preY,preColor,i-startIndex);
            }else {
                int drawColor = drawNoise || drawRpeak?curPoint.getDrawColor():curPoint.getDefaultColor();
                float rawX = chartComputator.computeRawX(i);
                float rawY = chartComputator.computeECGRawY(curPoint.getCoorY(),bottom);
                if (!renderStrategy.isCanLineBound()){
                    if (rawY > bottom){
                        if (preRawY > bottom){
                            drawColor = Color.TRANSPARENT;
                        }
                        preRawY = rawY;
                        rawY = bottom;
                    }else if (rawY < top){
                        if (preRawY < top){
                            drawColor = Color.TRANSPARENT;
                        }
                        preRawY = rawY;
                        rawY = top;
                    }
                }
                PointF pointF = chartComputator.screenToCartesian(rawX,rawY);
                ecgLine.addVertexToBuffer(pointF.x, pointF.y,drawColor,i-startIndex);
                preX = pointF.x;
                preY = pointF.y;
                preColor = curPoint.getDrawColor();
                if (drawRpeak && curPoint.isRPeak()){
                    paint.setColor(curPoint.getDrawColor());
                    float width = paint.measureText(curPoint.getTypeAnno());
                    canvas.drawText(curPoint.getTypeAnno(),rawX-width/2f,baseLine+top,paint);
                }
            }
        }
        ecgLine.updateData();
        if (drawRpeak) {
            mBpmTexture.setBitmap(bitmap);
            chartView.getChartGlRenderer().getTextureManager().replaceTexture(mBpmTexture);
        }
    }

    private void prepareEcgLine(ECGPointContainer container,ECGLine3D ecgLine,boolean clear){
        ecgLine.setLineThickness(container.getLineStrokeWidth());
        if (clear) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
    }

    private void initEcgLine(){
        lineContainer = new Object3D();
        ECGRenderStrategy strategy = chartView.getECGRenderStrategy();
        int count = strategy.getEcgLineCount();
        for (int i = 0;i < count;i++){
            ECGLine3D ecgLine3D = new ECGLine3D(strategy.getXTotalPointCounts());
            lineContainer.addChild(ecgLine3D);
        }
    }

    private void initAboutRPeak(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        float[] markStyle = chartView.getECGRenderStrategy().getMarkTextStyle();
        paint.setTextSize(ChartUtils.applyDimension(TypedValue.COMPLEX_UNIT_SP, markStyle[0]));
        paint.setStrokeWidth(ChartUtils.applyDimension(TypedValue.COMPLEX_UNIT_SP, markStyle[1]));
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int height = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        baseLine = (height - fontMetrics.bottom - fontMetrics.top) / 2.0f;

        Camera2D camera2D = chartComputator.getChartRenderer().getCamera2D();
        bpmPlane = new Plane((float) camera2D.getWidth(),(float) camera2D.getHeight(),2,1);
        bpmPlane.setDoubleSided(true);
        bpmPlane.setTransparent(true);
        bpmPlane.isContainer(false);

        bpmMaterial = new Material();
        bpmMaterial.setColorInfluence(0);
        bpmPlane.setMaterial(bpmMaterial);
        canvas = new Canvas();
    }

    @Override
    public void onChartSizeChanged() {
        destroyChild();
        initEcgLine();
        Scene scene = chartView.getChartGlRenderer().getCurrentScene();
        scene.addChild(lineContainer);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(chartComputator.getChartWidth(), chartComputator.getChartHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            mBpmTexture = new Texture("bpmTexture", bitmap);
            try {
                mBpmTexture.setMipmap(false);
                bpmMaterial.addTexture(mBpmTexture);
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        }
        scene.addChild(bpmPlane);
    }

    @Override
    public void onChartlayoutChanged() {
        destroyChild();
        initEcgLine();
        Scene scene = chartView.getChartGlRenderer().getCurrentScene();
        scene.addChild(lineContainer);
        scene.addChild(bpmPlane);
    }

    @Override
    public void onChartDataChanged() {

    }

    private void destroyChild(){
        Scene scene = chartView.getChartGlRenderer().getCurrentScene();
        List<Object3D> children = scene.getChildrenCopy();
        if (children.contains(lineContainer)) {
            scene.removeChild(lineContainer);
            lineContainer.destroy();
            lineContainer = null;
        }
        if (children.contains(bpmPlane)) {
            scene.removeChild(bpmPlane);
        }
    }
}
