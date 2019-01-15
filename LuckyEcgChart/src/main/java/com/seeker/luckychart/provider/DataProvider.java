package com.seeker.luckychart.provider;

/**
 * @author Seeker
 * @date 2018/6/7/007  11:34
 * @company zhengxinyiliao
 * @describe 图表数据提供器
 */

public interface DataProvider<Container> extends AxisProvider {

    /**
     * 设置一个数据容器，替换以前的
     * @param container
     */
    void setDataContainer(Container container);

    /**
     * 判断是否包含当前数据容器
     * @param container
     * @return
     */
    boolean containDataContainer(Container container);

    /**
     * 返回数据容器
     * @return
     */
    Container getDataContainer();

    /**
     * 清除数据
     */
    void clear();

}
