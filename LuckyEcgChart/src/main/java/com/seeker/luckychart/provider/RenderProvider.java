package com.seeker.luckychart.provider;

import com.seeker.luckychart.render.inters.LuckyAxesRenderer;
import com.seeker.luckychart.render.inters.LuckyDataRenderer;

import org.rajawali3d.renderer.Renderer;

/**
 * @author Seeker
 * @date 2018/6/7/007  13:17
 * @describe 渲染提供器
 */

public interface RenderProvider extends GestureProvider {

    LuckyDataRenderer getChartDataRenderer();

    LuckyAxesRenderer getChartAxesRenderer();

    Renderer getChartGlRenderer();

}
