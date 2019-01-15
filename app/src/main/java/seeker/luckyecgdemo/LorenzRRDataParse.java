package seeker.luckyecgdemo;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Seeker
 * @date 2018/6/27/027  15:09
 */
public class LorenzRRDataParse {

    private Data data;

    public LorenzRRDataParse(Context context){
        String json = parseJson(context,"scatter.json");
        Gson gson = new Gson();
        data = gson.fromJson(json,new TypeToken<Data>(){}.getType());
    }

    public Data.SubData getData(){
        return data.getData();
    }

    public List<float[]> parseLorenzz(){
        return data.getData().getRdList();
    }

    public List<long[]> parseTimeRR(){
        return data.getData().getRrList();
    }

    private static String parseJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static final class Data{

        private SubData data;

        public SubData getData() {
            return data;
        }

        public void setData(SubData data) {
            this.data = data;
        }

        public static final class SubData{

            private List<float[]> rdList;

            private List<long[]> rrList;

            public List<float[]> getRdList() {
                return rdList;
            }

            public void setRdList(List<float[]> rdList) {
                this.rdList = rdList;
            }

            public List<long[]> getRrList() {
                return rrList;
            }

            public void setRrList(List<long[]> rrList) {
                this.rrList = rrList;
            }
        }

    }

}
