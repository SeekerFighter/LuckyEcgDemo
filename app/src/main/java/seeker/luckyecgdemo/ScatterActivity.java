package seeker.luckyecgdemo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.seeker.luckychart.charts.ScatterChartView;
import com.seeker.luckychart.model.ChartAxis;
import com.seeker.luckychart.model.CoorValue;
import com.seeker.luckychart.model.Coordinateport;
import com.seeker.luckychart.model.PointValue;
import com.seeker.luckychart.model.chartdata.ScatterChartData;
import com.seeker.luckychart.model.container.PointContainer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class ScatterActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    private ScatterChartView lorenzRRView,timeRRView;

    private Paint namePaint,coorPaint,majorPaint,subPaint;

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);//只显示出时分秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter);
        initPaint();
        lorenzRRView = findViewById(R.id.lorenzRR);
//        timeRRView = findViewById(R.id.timeRR);
        new LoadTask().execute();
    }

    private void initPaint(){
        namePaint = new Paint();
        namePaint.setAntiAlias(true);
        namePaint.setColor(Color.BLACK);
        namePaint.setTextSize(50);

        coorPaint = new Paint();
        coorPaint.setAntiAlias(true);
        coorPaint.setTextSize(25);
        coorPaint.setColor(0xFF5a5a5a);

        majorPaint = new Paint();
        majorPaint.setAntiAlias(true);
        majorPaint.setColor(Color.BLACK);
        majorPaint.setStrokeWidth(4);

        subPaint = new Paint();
        subPaint.setAntiAlias(true);
        subPaint.setColor(Color.GRAY);
        subPaint.setStrokeWidth(1f);
    }


    @SuppressLint("StaticFieldLeak")
    private class LoadTask extends AsyncTask<Void,Integer,LorenzRRDataParse.Data.SubData> {

        @Override
        protected LorenzRRDataParse.Data.SubData doInBackground(Void... voids) {
            LorenzRRDataParse dataParse = new LorenzRRDataParse(ScatterActivity.this);
            return dataParse.getData();
        }

        @Override
        protected void onPostExecute(LorenzRRDataParse.Data.SubData data) {
            lorenzRRtest(data);
//            timeRRtest(data);
        }
    }


    private void lorenzRRtest(LorenzRRDataParse.Data.SubData subData){

        int len = 5,step = 500;

        ScatterChartData pointChartData = ScatterChartData.create();
        ChartAxis left = new ChartAxis();
        left.setMaxCoorchars(4);
//        left.setName("leftAxis");
        left.setCoorPaint(coorPaint);
        left.setLineMajorPaint(majorPaint);
        left.setNamePaint(namePaint);

        CoorValue[] leftCoorValue = new CoorValue[len];
        for (int i = 0; i < len;++i){
            CoorValue coorValue = new CoorValue(i*step,""+i*step);
            leftCoorValue[i] = coorValue;
        }
        left.setCoordinateValues(leftCoorValue);
        pointChartData.setLeftAxis(left);

        ChartAxis bottom = new ChartAxis();
//        bottom.setName("bottomAxis");
        bottom.setMaxCoorchars(4);
        bottom.setCoorPaint(coorPaint);
        bottom.setLineMajorPaint(majorPaint);
        bottom.setNamePaint(namePaint);

        CoorValue[] bottomCoorValue = new CoorValue[len];
        for (int i = 0; i < len;++i){
            CoorValue coorValue = new CoorValue(i*step,""+i*step);
            bottomCoorValue[i] = coorValue;
        }
        bottom.setCoordinateValues(bottomCoorValue);
        pointChartData.setBottomAxis(bottom);

        // set data
        List<float[]> datas = subData.getRdList();
        PointValue[] values = new PointValue[datas.size()];
        Log.d(TAG, "lorenzRRtest: size = "+values.length);
        for (int i = 0,size = values.length;i < size;++i){
            float[] rd = datas.get(i);
            values[i] = new PointValue(rd[0],rd[1]);
        }
        PointContainer container = PointContainer.create(values);
        container.setPointColor(0xff3e7bff);
        pointChartData.setDataContainer(container);
        lorenzRRView.setChartVisibleCoordinateport(new Coordinateport(0,(len-1)*step,(len-1)*step,0));
        lorenzRRView.setChartData(pointChartData);
        lorenzRRView.applyRenderUpdate();
    }

    private void timeRRtest(LorenzRRDataParse.Data.SubData subData){

        int len = 11,step = 200;

        ScatterChartData pointChartData = ScatterChartData.create();
        ChartAxis left = new ChartAxis();
        left.setMaxCoorchars(4);
        left.setCoorPaint(coorPaint);
        left.setLineMajorPaint(majorPaint);
        left.setNamePaint(namePaint);

        CoorValue[] leftCoorValue = new CoorValue[len];
        for (int i = 0; i < len;++i){
            CoorValue coorValue = new CoorValue(i*step,""+i*step);
            leftCoorValue[i] = coorValue;
        }
        left.setCoordinateValues(leftCoorValue);
        pointChartData.setLeftAxis(left);

        ChartAxis bottom = new ChartAxis();
        bottom.setMaxCoorchars(2);
        bottom.setCoorPaint(coorPaint);
        bottom.setLineMajorPaint(majorPaint);
        bottom.setNamePaint(namePaint);

        CoorValue[] bottomCoorValue = new CoorValue[25];
        for (int i = 0; i < 25;++i){
            CoorValue coorValue = new CoorValue(i,""+i);
            bottomCoorValue[i] = coorValue;
        }
        bottom.setCoordinateValues(bottomCoorValue);
        pointChartData.setBottomAxis(bottom);

        // set data
        List<long[]> datas = subData.getRrList();
        PointValue[] values = new PointValue[datas.size()];
        Log.d(TAG, "timeRRtest: size = "+values.length);
        for (int i = 0,size = values.length;i < size;++i){
            long[] rr = datas.get(i);
            values[i] = new PointValue(getTime(rr,1),rr[1]);
        }
        PointContainer container = PointContainer.create(values);
        container.setPointColor(0xff3e7bff);
        pointChartData.setDataContainer(container);
        timeRRView.setChartVisibleCoordinateport(new Coordinateport(0,(len-1)*step,24,0));
        timeRRView.setChartData(pointChartData);
        timeRRView.applyRenderUpdate();
    }
    
    private float getTime(long []rr,int len){

        Date date = new Date(rr[0]);

        String t = format.format(date);

        String[] ts = t.split(":");

        int second = Integer.parseInt(ts[0]) * 60 * 60 + Integer.parseInt(ts[1]) * 60 + Integer.parseInt(ts[2]);

        float result = 1f * second / (len * 60*60);

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lorenzRRView.onResume();
//        timeRRView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lorenzRRView.onPause();
//        timeRRView.onPause();
    }
}
