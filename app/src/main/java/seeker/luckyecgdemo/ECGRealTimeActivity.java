package seeker.luckyecgdemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.seeker.luckychart.charts.AbstractChartView;
import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.utils.ChartLogger;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Seeker
 * @date 2018/10/16/016  8:46
 */
public class ECGRealTimeActivity extends AppCompatActivity {

    private ECGChartView ecgChartView;

    private ECGPointValue[] mValues;

    private int index = 0;

    private boolean ready = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        findViewById(R.id.btn_group).setVisibility(View.GONE);
        ecgChartView = findViewById(R.id.ecgChart);
        new LoadTask().execute();
        ecgChartView.initDefaultChartData(true,true);
        ecgChartView.setDrawNoise(true);
        ecgChartView.setDrawRPeak(true);
        ecgChartView.setFrameRenderCallback(new AbstractChartView.FrameRenderCallback() {
            @Override
            public void onPrepareNextFrame(long duration) {
                if (!ready || ecgChartView.getChartData() == null) {
                    return;
                }
                int count = 4;
                ecgChartView.updatePointsToRender(Arrays.copyOfRange(mValues,index,index+count));
                index += count;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ecgChartView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ecgChartView.onPause();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadTask extends AsyncTask<Void, Integer, ECGPointValue[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ECGPointValue[] doInBackground(Void... voids) {
            ECGDataParse dataParse = new ECGDataParse(ECGRealTimeActivity.this);
            return dataParse.getValues();
        }

        @Override
        protected void onPostExecute(ECGPointValue[] values) {
            mValues = values;
            ready = true;
            ChartLogger.d("onPostExecute() called:"+mValues.length);
        }
    }

}
