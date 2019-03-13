package seeker.luckyecgdemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.seeker.luckychart.model.ECGPointValue;
import com.seeker.luckychart.soft.LuckySoftRenderer;
import com.seeker.luckychart.utils.ChartLogger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Seeker
 * @date 2019/3/8/008  15:39
 * @describe TODO
 */
public class EcgTransformImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_image_layout);
        imageView = findViewById(R.id.image);
        new LoadTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadTask extends AsyncTask<Void, Integer, ECGPointValue[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ECGPointValue[] doInBackground(Void... voids) {
            ECGDataParse dataParse = new ECGDataParse(EcgTransformImageActivity.this);
            return dataParse.getValues();
        }

        @Override
        protected void onPostExecute(ECGPointValue[] values) {
            ChartLogger.d("onPostExecute() called:" + values.length);
            imageView.setImageBitmap(
                    LuckySoftRenderer.instantiate(EcgTransformImageActivity.this, values)
//                            .setMaxDataValue(2f)
                            .startRender());
        }
    }
}
