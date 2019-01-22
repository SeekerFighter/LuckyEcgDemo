package seeker.luckyecgdemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import android.widget.SeekBar;

import com.seeker.luckychart.charts.ECGChartView;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.model.chartdata.ECGChartData;
import com.seeker.luckychart.model.container.ECGPointContainer;
import com.seeker.luckychart.utils.ChartLogger;

import org.rajawali3d.view.ISurface;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Seeker
 * @date 2018/10/16/016  8:46
 */
public class ECGStaticActivity extends AppCompatActivity {

    private static final String TAG = "ECGStaticActivity";

    private ECGChartView ecgChartView;

    private SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        ecgChartView = findViewById(R.id.ecgChart);
        ecgChartView.setFrameRate(0);
        ecgChartView.setTouchable(true);
        ecgChartView.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);

        ((ViewStub)findViewById(R.id.staticVS)).inflate();

        seekBar = findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float percent = 1f * progress / seekBar.getMax();
                    ecgChartView.setProgress(percent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ecgChartView.setOnVisibleCoorPortChangedListener(new ECGChartView.OnVisibleCoorPortChangedListener() {
            @Override
            public void onChanged(Coordinateport visiblePort, Coordinateport maxPort) {
                float progress = visiblePort.left / (maxPort.width()-visiblePort.width());
                seekBar.setProgress((int) (seekBar.getMax() * progress));
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

    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.scaleUp:
                ecgChartView.scaleUp();
                break;
            case R.id.scaleDown:
                ecgChartView.scaleDown();
                break;
            case R.id.gainUp:
                ecgChartView.gainUp();
                break;
            case R.id.gainDown:
                ecgChartView.gainDown();
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class LoadTask extends AsyncTask<Void, Integer, ECGPointValue[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ECGPointValue[] doInBackground(Void... voids) {
            ECGDataParse dataParse = new ECGDataParse(ECGStaticActivity.this);
            return dataParse.getValues();
        }

        @Override
        protected void onPostExecute(ECGPointValue[] values) {
            ChartLogger.d("onPostExecute() called:"+values.length);

            ECGPointContainer container = ECGPointContainer.create(values);
            container.setDrawRpeak(true);
            container.setDrawNoise(true);
            ECGChartData chartData = ECGChartData.create(container);
            ecgChartView.setChartData(chartData);
            ecgChartView.applyRenderUpdate();
        }
    }

}
