package com.seeker.luckychart.soft;

/**
 * @author Seeker
 * @date 2019/3/4/004  15:06
 * @describe TODO
 */
public class LuckySoftStrategy implements SoftStrategy {

    private int pointCount;

    private float maxDataValueForMv;//默认每行所表示的上下最大毫伏数 (maxDataValueForMv,-maxDataValueForMv)

    public LuckySoftStrategy(int pointCount){
        this.pointCount = pointCount;
        this.maxDataValueForMv = 1.5f;
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
        if (maxDataValueForMv > 2f){
            return 10;
        }else if (maxDataValueForMv > 1.5f){
            return 8;
        }else {
            return 6;
        }
    }

    @Override
    public int cellCountPerGrid() {
        return 5;
    }

    @Override
    public int pointsPerRow() {
        return pointsPerSecond() * secondsPerRow();// 250/s,10s数据
    }

    @Override
    public int secondsPerRow() {
        return 10;
    }

    @Override
    public int pointsPerSecond() {
        return 250;
    }

    @Override
    public int pixelPerCell() {
        return 10;//一个小格占5像素
    }

    @Override
    public float pixelPerPoint() {
        return 1f;//0.5像素/每点
    }

    @Override
    public int totalRows() {
        return pointCount%pointsPerRow() == 0?pointCount/pointsPerRow():pointCount/pointsPerRow()+1;//总共12行，2分钟数据
    }

    @Override
    public int horizontalPadding() {
        return 20;//水平方向，左右边距20个像素
    }

    @Override
    public int VerticalPadding() {
        return 0;
    }

    @Override
    public float maxDataValueForMv() {
        return maxDataValueForMv;
    }

    @Override
    public int cellCountsPerMv() {
        return 10;
    }

    @Override
    public Transformer getTransformer() {
        return new Transformer() {};
    }

    public void setMaxDataValueForMv(float maxDataValueForMv) {
        this.maxDataValueForMv = maxDataValueForMv;
    }
}
