package com.seeker.luckychart.render.ecg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
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

import org.rajawali3d.cameras.Camera2D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;

/**
 * @author Seeker
 * @date 2018/10/15/015  10:07
 * @describe 心电图绘制渲染器
 */
public class ECGChartDataRender extends AbstractChartDataRenderer<ECGChartData> {

    private ECGChartView chartView;

    private ECGLine3D ecgLine;

    private Plane bpmPlane;

    private Texture mBpmTexture;

    private Material bpmMaterial;

    private Paint paint;

    private Canvas canvas;

    private Bitmap bitmap;

    private float baseLine;

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
        if (checkDataAvailable() && ecgLine != null){
            ECGChartData chartData = chartProvider.getChartData();
            ECGPointContainer container = chartData.getDataContainer();
            prepareEcgLine(container);
            final ECGPointValue[] values = container.getValues();
            drawOscillogram(values,container.isDrawNoise(),container.isDrawRpeak());
        }
    }

    private void drawOscillogram(ECGPointValue[] values,boolean drawNoise,boolean drawRpeak){
        Coordinateport visiblePort = chartComputator.getVisibleCoorport();
        int len = chartView.getECGRenderStrategy().getXTotalPointCounts();
        int startIndex = (int) visiblePort.left;
        int endIndex = startIndex + len;
        if (endIndex > chartComputator.getMaxCoorport().right){
            endIndex = (int) chartComputator.getMaxCoorport().right;
            startIndex = endIndex - len;
        }
        float preX = 0f,preY = 0f;
        int preColor = 0;
        for (int i = startIndex; i < endIndex; ++i){
            ECGPointValue curPoint = values[i];
            if (curPoint == null || Float.isNaN(curPoint.getCoorY())){
                ecgLine.addVertexToBuffer(preX, preY,preColor,i-startIndex);
            }else {
                float rawX = chartComputator.computeRawX(i);
                float rawY = chartComputator.computeRawY(curPoint.getCoorY());
                PointF pointF = chartComputator.screenToCartesian(rawX,rawY);
                int drawColor = drawNoise || drawRpeak?curPoint.getDrawColor():curPoint.getDefaultColor();
                ecgLine.addVertexToBuffer(pointF.x, pointF.y,drawColor,i-startIndex);
                preX = pointF.x;
                preY = pointF.y;
                preColor = curPoint.getDrawColor();
                if (drawRpeak && curPoint.isRPeak()){
                    paint.setColor(curPoint.getDrawColor());
                    float width = paint.measureText(curPoint.getTypeAnno());
                    canvas.drawText(curPoint.getTypeAnno(),rawX-3*width/4,baseLine,paint);
                }
            }
        }
        ecgLine.updateData();
        if (drawRpeak) {
            mBpmTexture.setBitmap(bitmap);
            chartView.getChartGlRenderer().getTextureManager().replaceTexture(mBpmTexture);
        }
    }

    private void prepareEcgLine(ECGPointContainer container){
        ecgLine.setLineThickness(container.getLineStrokeWidth());
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    private void initEcgLine(){
        ECGRenderStrategy strategy = chartView.getECGRenderStrategy();
        ecgLine = new ECGLine3D(strategy.getXTotalPointCounts());
    }

    private void initAboutRPeak(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(ChartUtils.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14));
        paint.setStrokeWidth(ChartUtils.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4f));
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
        scene.addChild(ecgLine);

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
        scene.addChild(ecgLine);
        scene.addChild(bpmPlane);
    }

    @Override
    public void onChartDataChanged() {

    }

    private void destroyChild(){
        Scene scene = chartView.getChartGlRenderer().getCurrentScene();
        scene.removeChild(ecgLine);
        ecgLine.destroy();
        ecgLine = null;
        scene.removeChild(bpmPlane);
    }
}
