package com.seeker.luckychart.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2017/10/17/017  17:00
 */

public class ChartCoordinateportAnimatorImpl
        implements ChartCoordinateportAnimator,ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener{

    private final ChartProvider chartProvider;

    private ValueAnimator animator;

    private Coordinateport startport = new Coordinateport();
    private Coordinateport targetport = new Coordinateport();
    private Coordinateport newport = new Coordinateport();
    private ChartAnimationListener animationListener;

    private ChartCoordinateportAnimatorImpl(ChartProvider chartProvider){
        this.chartProvider = chartProvider;
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.addListener(this);
        animator.addUpdateListener(this);
        animator.setDuration(FAST_ANIMATION_DURATION);
    }

    public static ChartCoordinateportAnimatorImpl create(ChartProvider chartProvider){
        return new ChartCoordinateportAnimatorImpl(chartProvider);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (null != animationListener){
            animationListener.onAnimationStarted();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        chartProvider.setChartVisibleCoordinateport(targetport);
        if (null != animationListener){
            animationListener.onAnimationFinished();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float scale = animation.getAnimatedFraction();
        float diffLeft = (targetport.left - startport.left) * scale;
        float diffTop = (targetport.top - startport.top) * scale;
        float diffRight = (targetport.right - startport.right) * scale;
        float diffBottom = (targetport.bottom - startport.bottom) * scale;
        newport.set(startport.left + diffLeft, startport.top + diffTop, startport.right + diffRight,
                startport.bottom + diffBottom);
        chartProvider.setChartVisibleCoordinateport(newport);
    }

    @Override
    public void startAnimation(Coordinateport startport, Coordinateport targetport, long duration) {
        this.startport.set(startport);
        this.targetport.set(targetport);
        animator.setDuration(duration);
        animator.start();
    }

    @Override
    public void cancelAnimation() {
        animator.cancel();
    }

    @Override
    public boolean isAnimationStarted() {
        return animator.isStarted();
    }

    @Override
    public void setChartAnimationListener(ChartAnimationListener animationListener) {
        this.animationListener = animationListener;
    }
}
