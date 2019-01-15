package com.seeker.luckychart.strategy.ecgrender;

import com.seeker.luckychart.utils.ChartLogger;

/**
 * @author Seeker
 * @date 2018/10/23/023  13:01
 * @describe ecg心电图绘制策略
 */
public class ECGRenderStrategyImpl implements ECGRenderStrategy{

    private static final String TAG = "ECGRenderStrategyImpl";

    private static final int DEFAULT_CELLPIXEL = 10;//每个小格固定10像素

    public static final int DEFAULT_OUTER_CELLCOUNTS_Y = 8;//默认Y轴方向显示大格数

    private static final int DEFAULT_MAX_OUTERCELL_COUNT = 25;//默认最大有25个大格子

    private int xCellCounts;//x轴方向小格总数

    private float cellPixel = DEFAULT_CELLPIXEL;//一小格实际像素点

    private int yOuterCellCounts = DEFAULT_OUTER_CELLCOUNTS_Y;

    private int defaultYOuterCellCounts = DEFAULT_OUTER_CELLCOUNTS_Y;//just for scale

    private int yCellCountsPerMV = TOTALCELLS_PERMV;//纵向以mv包含几个小格子

    private boolean hasMeasured = false;

    private int measuredWith;

    private int measuredHeight;

    private ECGRenderStrategyImpl(){
    }

    public static ECGRenderStrategy create(){
        return new ECGRenderStrategyImpl();
    }

    @Override
    public void onViewMeasured(int measuredWithSize, int measuredHeightSize, int[] result) {
        if (!hasMeasured) {
            xCellCounts = (int) (measuredWithSize / getCellWidth());
            if (xCellCounts > defaultXMaxCellCounts()) {
                xCellCounts = defaultXMaxCellCounts();
                cellPixel = 1f * measuredWithSize / xCellCounts;
            }
            hasMeasured = true;
        }
        result[0] = this.measuredWith = (int) (xCellCounts * getCellWidth());
        result[1] = this.measuredHeight = (int) (getYCellCounts() * getCellWidth());
        ChartLogger.vTag(TAG,"onViewMeasured() called: xCellCounts = "+xCellCounts+",cellPixel = "+cellPixel
                +",width = "+result[0]+",height = "+result[1]
                +",measuredWithSize = "+measuredWithSize+",measuredHeightSize = "+measuredHeightSize);
    }

    @Override
    public int getXCellCounts() {
        return xCellCounts;
    }

    @Override
    public int getYCellCounts() {
        return yOuterCellCounts * getInnerCellCounts();
    }

    @Override
    public int getYOuterCellCount() {
        return yOuterCellCounts;
    }

    @Override
    public int getXTotalPointCounts() {
        return xCellCounts * POINTCOUNTS_PERCELL;
    }

    @Override
    public float getYMaxMvs() {
        return 1f * getYCellCounts() / yCellCountsPerMV;
    }

    @Override
    public int getYCellCountsPerMv() {
        return yCellCountsPerMV;
    }

    @Override
    public int getInnerCellCounts() {
        return DEFAULT_INNER_CELLCOUNTS;
    }

    @Override
    public float getInnerThinkLineWidth() {
        return INNER_STROKE_WIDTH;
    }

    @Override
    public float getOuterThinkLineWidth() {
        return OUTER_STROKE_WIDTH;
    }

    @Override
    public int getInnerColor() {
        return INNER_COLOR;
    }

    @Override
    public int getOuterColor() {
        return OUTER_COLOR;
    }

    @Override
    public float getCellWidth() {
        return cellPixel;
    }

    @Override
    public void setYOuterCellCounts(int yOuterCellCounts) {
        this.yOuterCellCounts = yOuterCellCounts;
        this.defaultYOuterCellCounts = yOuterCellCounts;
    }

    @Override
    public boolean scale(int outerCellYCount) {
        if (outerCellYCount < defaultYOuterCellCounts/2 || outerCellYCount > defaultYOuterCellCounts * 1.5){
            return false;
        }
        this.yOuterCellCounts = outerCellYCount;
        this.cellPixel = 1f * measuredHeight/getYCellCounts();
        this.xCellCounts = (int) (measuredWith / cellPixel);
        return true;
    }

    @Override
    public boolean gain(int yCellCountsPerMv) {
        if (yCellCountsPerMv > TOTALCELLS_PERMV * 2 || yCellCountsPerMv < TOTALCELLS_PERMV/2){
            return false;
        }
        this.yCellCountsPerMV = yCellCountsPerMv;
        return true;
    }

    /**
     * 返回x轴最大小格子数量
     * @return
     */
    private int defaultXMaxCellCounts(){
        return DEFAULT_MAX_OUTERCELL_COUNT * getInnerCellCounts();
    }
}
