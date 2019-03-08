package com.seeker.luckychart.soft;

/**
 * @author Seeker
 * @date 2019/3/4/004  15:06
 * @describe TODO
 */
class LuckySoftStrategy implements SoftStrategy {

    private int sumPointCounts;

    LuckySoftStrategy(int counts){
        this.sumPointCounts = counts;
    }

    @Override
    public int pictureWidth() {
        return Math.round(pointsPerRow() * pixelPerPoint() + horizontalPadding() * 2);//左右边距
    }

    @Override
    public int pictureHeight() {
        return pixelPerCell() * cellCountPerGrid() * gridCountPerRow() * totalRows() + VerticalPadding() * 2;//，一小格是1像素,5个小格组成一个大格，总共6个大格每行
    }

    @Override
    public int gridCountPerRow() {
        return 6;
    }

    @Override
    public int cellCountPerGrid() {
        return 5;
    }

    @Override
    public int pointsPerRow() {
        return 250 * 10;// 250/s,10s数据
    }

    @Override
    public int pixelPerCell() {
        return 5;//一个小格占5像素
    }

    @Override
    public float pixelPerPoint() {
        return 0.5f;//0.5像素/每点
    }

    @Override
    public int totalRows() {
        return sumPointCounts%pointsPerRow() == 0?sumPointCounts/pointsPerRow():sumPointCounts/pointsPerRow()+1;
    }

    @Override
    public int horizontalPadding() {
        return 50;//水平方向，左右边距50个像素
    }

    @Override
    public int VerticalPadding() {
        return 0;
    }

    @Override
    public float maxDataValueForMv() {
        return cellCountPerGrid()*gridCountPerRow()/cellCountsPerMv()*1.5f;
    }

    @Override
    public int cellCountsPerMv() {
        return 15;
    }

    @Override
    public Transformer getTransformer() {
        return new Transformer() {};
    }
}
