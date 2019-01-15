package com.seeker.luckychart.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.provider.ChartProvider;

/**
 * @author Seeker
 * @date 2018/6/29/029  11:07
 * @describe 横向滚动的图表管理类
 */
public class HScrollChartContainerView extends HorizontalScrollView{

    private static final String TAG = "HScrollChartContainerVi";

    private FrameLayout childContainer;

    private LeftAxisView leftAxisView;

    private ChartProvider provider;

    public HScrollChartContainerView(Context context) {
        this(context,null);
    }

    public HScrollChartContainerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HScrollChartContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        transform();
    }

    private void transform(){

        childContainer = new FrameLayout(getContext());
        childContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        if (getChildCount() == 0){
            throw new IllegalStateException("HorizontalScrollView hasn't add one direct child.");
        }

        View child = getChildAt(0);

        if (!(child instanceof ChartProvider)){
            throw new IllegalStateException("direct child is't instanceof ChartProvider.");
        }

        provider = (ChartProvider) child;

        removeAllViews();
        childContainer.addView(child);

        addView(childContainer);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (leftAxisView == null) {
            ChartAxis leftAxis = provider.getChartData().getLeftAxis();
            if (leftAxis != null && leftAxis.getCoordinateValues() != null && leftAxis.getCoordinateValues().length > 0) {
                leftAxisView = new LeftAxisView(getContext(), provider);
                childContainer.addView(leftAxisView);
            }
        }
        if (leftAxisView != null) {
            leftAxisView.setX(l);
        }
    }
}
