package com.seeker.luckychart.soft;

/**
 * @author Seeker
 * @date 2019/3/4/004  15:01
 * @describe ecg数据转成图片参数设置
 */
public interface SoftStrategy {

    int pictureWidth();//生成图片的宽,像素

    int pictureHeight();//生成图片的高,像素

    int pointsPerRow();//一行总共显示几个点

    int secondsPerRow();//一行显示几秒

    int pointsPerSecond();//一秒多少个点

    int gridCountPerRow();//每行显示几个大格子

    int cellCountPerGrid();//每个大格子由几个小格组成

    float pixelPerPoint();//每个点占几个像素

    int pixelPerCell();//每个小格几个像素

    int totalRows();//总共几行

    int horizontalPadding();//水平方向边距

    int VerticalPadding();//竖直方向边距

    float maxDataValueForMv();//一行所表示的最大毫伏数

    int cellCountsPerMv();//一毫伏占几个小格子

    Transformer getTransformer();//获取坐标转换管理
}
