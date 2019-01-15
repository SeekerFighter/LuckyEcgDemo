package com.seeker.luckychart.model.chartdata;

import android.support.annotation.NonNull;

import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.container.ECGPointContainer;

/**
 * @author Seeker
 * @date 2018/10/15/015  10:03
 * @describe 心电图绘制数据
 */
public class ECGChartData extends AbsChartData<ECGPointContainer>{

    private ECGChartData() {

    }

    private ECGChartData(ECGPointContainer container) {
        super(container);
    }

    public static ECGChartData create(){
        return new ECGChartData();
    }

    public static ECGChartData create(ECGPointContainer container){
        return new ECGChartData(container);
    }

    /**
     * 添加需要更新的点数,平移模式下数据添加
     * @param adds
     */
    public void addNewDatas(@NonNull ECGPointValue[] adds){
        getDataContainer().updateNewValues(adds);
    }
}
