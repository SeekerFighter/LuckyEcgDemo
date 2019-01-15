package com.seeker.luckychart.model;

import android.graphics.Paint;
import android.support.annotation.IntDef;

import com.seeker.luckychart.utils.ChartUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:02
 * @describe 坐标轴
 */

public class ChartAxis {

    //默认刻度与绘图区域的间距
    private static final float DEFAULT_AXIS_MARGIN_DP = 2f;

    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;//暂未做适配工作
    public static final int RIGHT = 0x03;//暂未做适配工作
    public static final int BOTTOM = 0x04;

    @IntDef({LEFT,TOP,RIGHT,BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Location{

    }

    private float axisMargin = DEFAULT_AXIS_MARGIN_DP;//刻度与绘图区域的间隔

    private String name;//坐标轴名称

    private Paint namePaint;//坐标轴名称画笔

    private Paint coorPaint;//刻度画笔

    private Paint lineMajorPaint;//外围主坐标线

    private Paint lineSubPaint;//内部次坐标线

    private CoorValue[] coordinateValues;//坐标刻度集合

    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    private int maxCoorchars = 5;//刻度值最大字符数

    /**
     * 绘制刻度间隔,如刻度为0,1,2,3,4 module = 1,则全部绘制， = 2，则为0,2,4
     */
    private int module = 1;

    private int coorTextAscent,nameTextAscent;

    private int coorTextDescent,nameTextDescent;

    private int coorTextBottom;

    private int coorTextTop;

    private int coorWidth;

    private int coorHeight;

    private int coorDimensionForMargins;//保存刻度值与绘图区域的间隔

    private float coorBaseLine;

    private float nameBaseLine;

    private float separationLine;

    public void initFontMetricsInt(){
        if (coorPaint == null){
            throw new NullPointerException("have you set draw coor paint?");
        }
        coorPaint.getFontMetrics(fontMetrics);
        coorTextAscent = (int) Math.abs(fontMetrics.ascent);
        coorTextDescent = (int) Math.abs(fontMetrics.descent);
        coorWidth = (int) coorPaint.measureText(ChartUtils.VALUEWIDTHCHARS,0,maxCoorchars);
        coorHeight = (int) Math.floor(fontMetrics.descent - fontMetrics.ascent);
        coorTextTop = (int) fontMetrics.top;
        coorTextBottom = (int) fontMetrics.bottom;

        if (namePaint != null){
            namePaint.getFontMetrics(fontMetrics);
            nameTextAscent = (int) Math.abs(fontMetrics.ascent);
            nameTextDescent = (int) Math.abs(fontMetrics.descent);
        }

    }

    public int getNameTextAscent() {
        return nameTextAscent;
    }

    public int getNameTextDescent() {
        return nameTextDescent;
    }

    public CoorValue[] getCoordinateValues() {
        return coordinateValues;
    }

    public void setCoordinateValues(CoorValue[] coordinateValues) {
        this.coordinateValues = coordinateValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Paint getNamePaint() {
        return namePaint;
    }

    public void setNamePaint(Paint namePaint) {
        this.namePaint = namePaint;
    }

    public Paint getCoorPaint() {
        return coorPaint;
    }

    public void setCoorPaint(Paint coorPaint) {
        this.coorPaint = coorPaint;
    }

    public Paint getLineMajorPaint() {
        return lineMajorPaint;
    }

    public void setLineMajorPaint(Paint lineMajorPaint) {
        this.lineMajorPaint = lineMajorPaint;
    }

    public Paint getLineSubPaint() {
        return lineSubPaint;
    }

    public void setLineSubPaint(Paint lineSubPaint) {
        this.lineSubPaint = lineSubPaint;
    }

    public void setMaxCoorchars(int maxCoorchars) {
        this.maxCoorchars = maxCoorchars;
    }

    public int getMaxCoorchars() {
        return maxCoorchars;
    }

    public int getCoorWidth() {
        return coorWidth;
    }

    public int getCoorHeight() {
        return coorHeight;
    }

    public int getCoorTextAscent() {
        return coorTextAscent;
    }

    public int getCoorTextDescent() {
        return coorTextDescent;
    }

    public int getCoorTextBottom() {
        return coorTextBottom;
    }

    public int getCoorTextTop() {
        return coorTextTop;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public float getAxisMargin() {
        return axisMargin;
    }

    public void setAxisMargin(float axisMargin) {
        this.axisMargin = axisMargin;
    }

    public int getCoorDimensionForMargins() {
        return coorDimensionForMargins;
    }

    public void setCoorDimensionForMargins(int coorDimensionForMargins) {
        this.coorDimensionForMargins = coorDimensionForMargins;
    }

    public float getCoorBaseLine() {
        return coorBaseLine;
    }

    public void setCoorBaseLine(float coorBaseLine) {
        this.coorBaseLine = coorBaseLine;
    }

    public float getNameBaseLine() {
        return nameBaseLine;
    }

    public void setNameBaseLine(float nameBaseLine) {
        this.nameBaseLine = nameBaseLine;
    }

    public float getSeparationLine() {
        return separationLine;
    }

    public void setSeparationLine(float separationLine) {
        this.separationLine = separationLine;
    }
}
