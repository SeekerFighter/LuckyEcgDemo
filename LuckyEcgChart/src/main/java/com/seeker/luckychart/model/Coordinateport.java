package com.seeker.luckychart.model;

/**
 * @author Seeker
 * @date 2017/10/14/014  11:52
 * @describe 保存坐标轴可视范围,与手机屏幕可绘制区域同比例缩放
 */

public class Coordinateport {

    public float left;
    public float top;
    public float right;
    public float bottom;

    public Coordinateport(){

    }

    public Coordinateport(float left, float top, float right, float bottom) {
        set(left,top,right,bottom);
    }

    public void set(float left, float top, float right, float bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void set(Coordinateport src){
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public final float width(){
        return right - left;
    }

    public final float height(){
        return top - bottom;
    }

    public final float centerX(){
        return (right+left)/2;
    }

    public final float centerY(){
        return (top+bottom)/2;
    }

    public boolean contains(float x,float y){
        return x >= left && x <= right && y >= bottom && y <= top;
    }

    @Override
    public String toString() {
        return "Coordinateport{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
