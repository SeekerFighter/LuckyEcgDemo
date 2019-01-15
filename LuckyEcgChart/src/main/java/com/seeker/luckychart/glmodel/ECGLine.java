package com.seeker.luckychart.glmodel;

import android.graphics.Color;
import android.opengl.GLES20;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;

import java.util.Stack;

/**
 * @author Seeker
 * @date 2018/10/20/020  10:26
 * @describe 心电图模型
 */
public class ECGLine extends Object3D {

    private Stack<Vector3> mPoints;
    private float mLineThickness;

    private ECGLine(int drawingMode) {
        setDoubleSided(true);
        setTransparent(true);
        isContainer(false);
        setDrawingMode(drawingMode);
    }

    public static ECGLine create(){
        return new ECGLine(GLES20.GL_LINE_STRIP);
    }

    public static ECGLine create(int drawingMode){
        return new ECGLine(drawingMode);
    }

    public void setLineThickness(final float lineThickness) {
        this.mLineThickness = lineThickness;
    }

    public void setPoints(Stack<Vector3> points) {
        this.mPoints = points;
    }

    public void setColor(int color) {
        mColor[RED] = Color.red(color) / 255.f;
        mColor[GREEN] = Color.green(color) / 255.f;
        mColor[BLUE] = Color.blue(color) / 255.f;
        mColor[ALPHA] = Color.alpha(color) / 255.f;
        mOverrideMaterialColor = true;
    }

    public ECGLine init() {

        int numVertices = mPoints.size();

        float[] vertices = new float[numVertices * 3];
        int[] indices = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            Vector3 point = mPoints.get(i);
            int index = i * 3;
            vertices[index] = (float) point.x;
            vertices[index + 1] = (float) point.y;
            vertices[index + 2] = (float) point.z;
            indices[i] = i;
        }

        setData(vertices, null, null, null, indices, true);

        return this;
    }

    @Override
    protected void preRender() {
        super.preRender();
        GLES20.glLineWidth(mLineThickness);
    }
}
