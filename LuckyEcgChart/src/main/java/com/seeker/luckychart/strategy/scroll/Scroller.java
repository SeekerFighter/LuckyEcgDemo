package com.seeker.luckychart.strategy.scroll;

/**
 * @author Seeker
 * @date 2018/11/2/002  11:49
 * @describe 滚动
 */
public interface Scroller {

    ScrollResult scroll(float startX,float startY,float distanceX,float distanceY);

}
