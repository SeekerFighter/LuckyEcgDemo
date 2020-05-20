package seeker.luckyecgdemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;

import com.seeker.luckychart.annotation.UIMode;
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

    private ECGPointValue[][] mValues;

    private int index = 0;

    private boolean ready = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        ((ViewStub)findViewById(R.id.realTimeVS)).inflate();
        ecgChartView = findViewById(R.id.ecgChart);
        ecgChartView.initDefaultChartData(true,true);
        ecgChartView.setFrameRenderCallback(new AbstractChartView.FrameRenderCallback() {
            @Override
            public void onPrepareNextFrame(long duration) {
                if (!ready || ecgChartView.getChartData() == null) {
                    return;
                }
                int count = 4;
                if (index+count < mValues[0].length) {
                    int lineCount = ecgChartView.getECGRenderStrategy().getEcgLineCount();
                    ECGPointValue[][] values = new ECGPointValue[lineCount][];
                    for (int i = 0;i < lineCount;i++){
                        ECGPointValue[] value = Arrays.copyOfRange(mValues[i], index, index + count);
                        values[i] = value;
                    }
                    ecgChartView.updatePointsToRender(values);
                    index += count;
                }
            }
        });

        findViewById(R.id.translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecgChartView.onPause();
                ecgChartView.setMode(UIMode.TRANSLATE);
                ecgChartView.onResume();
            }
        });

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecgChartView.onPause();
                ecgChartView.setMode(UIMode.ERASE);
                ecgChartView.onResume();
            }
        });

        new LoadTask().execute();

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
    private class LoadTask extends AsyncTask<Void, Integer, ECGPointValue[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ECGPointValue[][] doInBackground(Void... voids) {
            int count = ecgChartView.getECGRenderStrategy().getEcgLineCount();
            ECGPointValue[][] values = new ECGPointValue[count][];
            for (int i = 0;i<count;i++) {
                ECGDataParse dataParse = new ECGDataParse(ECGRealTimeActivity.this);
                values[i] = dataParse.getValues();
            }
            return values;
        }

        @Override
        protected void onPostExecute(ECGPointValue[][] values) {
            mValues = values;
            ready = true;
            ChartLogger.d("onPostExecute() called:"+mValues.length);
        }
    }

}
