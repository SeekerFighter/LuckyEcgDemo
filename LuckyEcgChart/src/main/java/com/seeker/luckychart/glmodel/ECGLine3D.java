package com.seeker.luckychart.glmodel;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.SparseArray;

import com.seeker.luckychart.utils.ChartLogger;

import org.rajawali3d.BufferInfo;
import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Seeker
 * @date 2018/10/20/020  10:26
 * @describe 心电图模型
 */
public class ECGLine3D extends Object3D{

    private static final int DEFAULT_LINE_COLOR = 0xFF021F52;//折线默认颜色

    private int numPoints;

    private float mLineThickness;

    private BufferInfo mDataVertexBufferInfo,mColorVertexBufferInfo, mIndexBufferInfo;

    private FloatBuffer mDataVertexBuffer,mColorVertexBuffer;

    private IntBuffer mIndexBuffer;

    private SparseArray<float[]> colorMaps = new SparseArray<>();

    public ECGLine3D(int numPoints){
        this.numPoints = numPoints;

        setDrawingMode(GLES20.GL_LINE_STRIP);
        isContainer(false);
        setDoubleSided(true);
        setTransparent(true);

        init();
    }

    private void init(){

        float[] vertices = new float[numPoints * 3];
        int[] indices = new int[numPoints];
        float[] mColors = new float[numPoints * 4];

        for (int i = 0;i<numPoints;i++){
            indices[i] = i;
        }

        setData(vertices, null, null, mColors, indices, true);

        Material material = new Material();
        material.useVertexColors(true);
        setMaterial(material);

        mDataVertexBufferInfo = getGeometry().getVertexBufferInfo();
        mDataVertexBuffer = getGeometry().getVertices();

        mColorVertexBufferInfo = getGeometry().getColorBufferInfo();
        mColorVertexBuffer = getGeometry().getColors();

        mIndexBufferInfo = getGeometry().getIndexBufferInfo();
        mIndexBuffer = getGeometry().getIndices();

        getGeometry().changeBufferUsage(mDataVertexBufferInfo,GLES20.GL_DYNAMIC_DRAW);
        getGeometry().changeBufferUsage(mColorVertexBufferInfo,GLES20.GL_DYNAMIC_DRAW);
        getGeometry().changeBufferUsage(mColorVertexBufferInfo,GLES20.GL_STATIC_DRAW);
    }

    public void addVertexToBuffer(float x,float y,int color,int index) {
        //更新数据源
        int vertIndex = index * 3;
        mDataVertexBuffer.put(vertIndex, x);
        mDataVertexBuffer.put(vertIndex + 1,y);
        mDataVertexBuffer.put(vertIndex + 2,0);

        //更新颜色
        float[] colors = getAssuredColors(color);
        int realIndex = index * 4;
        mColorVertexBuffer.put(realIndex,colors[0]);
        mColorVertexBuffer.put(realIndex+1,colors[1]);
        mColorVertexBuffer.put(realIndex+2,colors[2]);
        mColorVertexBuffer.put(realIndex+3,colors[3]);
    }

    public void addIndexToBuffer(int index){
        for (int i=0;i<index;i++){
            mIndexBuffer.put(i,i);
        }
        for (int i=index;i<numPoints;i++){
            mIndexBuffer.put(i,index-1);
        }
    }

    public void updateData(){
        updateData(numPoints);
    }

    public void updateData(int dataLength){
        getGeometry().changeBufferData(mDataVertexBufferInfo,mDataVertexBuffer,0,dataLength * 3);
        getGeometry().changeBufferData(mColorVertexBufferInfo,mColorVertexBuffer,0,dataLength * 4);
        getGeometry().changeBufferData(mIndexBufferInfo,mIndexBuffer,0,numPoints);
    }

    public void setLineThickness(final float lineThickness) {
        mLineThickness = lineThickness;
    }

    private float[] getAssuredColors(int color){
        float[] colors = colorMaps.get(color);
        if (colors == null){
            colors = new float[4];
            colors[0] = Color.red(color) / 255.f;
            colors[1] = Color.green(color) / 255.f;
            colors[2] = Color.blue(color) / 255.f;
            colors[3] = Color.alpha(color) / 255.f;
            colorMaps.put(color,colors);
        }
        return colors;
    }

    public void clearChild(){
        mChildren.clear();
    }

    @Override
    protected void preRender() {
        super.preRender();
        GLES20.glLineWidth(mLineThickness);
    }
}
