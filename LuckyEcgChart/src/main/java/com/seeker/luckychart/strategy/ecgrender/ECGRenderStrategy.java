package com.seeker.luckychart.strategy.ecgrender;

/**
 * @author Seeker
 * @date 2018/10/23/023  11:20
 * @describe 心电图绘制策略
 */
public interface ECGRenderStrategy {

    int DEFAULT_INNER_CELLCOUNTS = 5;//每个大格包含5个小格子

    int POINTCOUNTS_PERCELL = 10;//横向一小格包含10个点

    int TOTALCELLS_PERMV = 10;//纵向1mv包含10个小格

    int OUTER_COLOR = 0xFF57C2FB;

    int INNER_COLOR = 0xFFC2E2F3;

    float INNER_STROKE_WIDTH = 1f;

    float OUTER_STROKE_WIDTH = 2f;

    void onViewMeasured(int measuredWithSize, int measuredHeightSize,int[] result);

    int getXCellCounts();//获取X轴方向小格子总数

    int getYCellCounts();//获取Y轴方向小格子总数

    void setYOuterCellCounts(int yOuterCellCounts);//设置Y轴大格子数目

    int getYOuterCellCount();//获取Y轴大格子个数

    int getInnerCellCounts();//获取每个大格包含几个小格子

    int getXTotalPointCounts();//获取X轴最多可绘制的点数

    float getYMaxMvs();//获取Y轴可表示的最大毫伏数

    int getYCellCountsPerMv();//1mv包含几个小格子

    float getInnerThinkLineWidth();//获取线的宽

    float getOuterThinkLineWidth();//获取线的宽

    int getInnerColor();//获取线颜色

    int getOuterColor();//获取线颜色

    float getCellWidth();//获取每个小格的宽

    /**
     * 缩放功能，以Y轴大格子数目为准，X轴从设
     * @param outerCellYCount 缩放后Y轴大格子数目
     */
    boolean scale(int outerCellYCount);

    /**
     * 增益功能，以Y轴为主
     * @param yCellCountsPerMv 设置的没mv包含几个小格子
     */
    boolean gain(int yCellCountsPerMv);

}
