package seeker.luckyecgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.scatter_btn:
                intent.setClass(this,ScatterActivity.class);
                break;
            case R.id.ecgRealtime_btn:
                intent.setClass(this,ECGRealTimeActivity.class);
                break;
            case R.id.ecgStatic_btn:
                intent.setClass(this,ECGStaticActivity.class);
                break;
        }
        startActivity(intent);
    }

}
