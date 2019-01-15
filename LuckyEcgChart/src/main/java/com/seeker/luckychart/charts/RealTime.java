package com.seeker.luckychart.charts;

/**
 * @author Seeker
 * @date 2018/10/30/030  11:14
 * @describe ecg实时模式辅助
 */
public interface RealTime {
    /**
     * R峰标识补助
     * @param rposition 位置
     * @param type 类型
     * @param tyoeAnno 描述
     * @param needRPeak 修复心博类型的时候，是否要判断当前是否是心博，主要是用于修复前前一个心博的
     */
    void repairPointRPeak(int rposition, int type, String tyoeAnno, boolean needRPeak);

}
