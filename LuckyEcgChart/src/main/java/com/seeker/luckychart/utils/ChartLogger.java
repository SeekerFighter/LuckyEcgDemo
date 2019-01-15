package com.seeker.luckychart.utils;

import android.util.Log;

/**
 * @author Seeker
 * @date 2018/6/27/027  10:18
 */
public final class ChartLogger {

    private static final String TAG = "ChartLogger";

    private static final boolean debug = true;

    public static void v(String msg){
        if (debug){
            Log.v(TAG, msg);
        }
    }

    public static void vTag(String tag,String msg){
        if (debug){
            Log.v(TAG+"—"+tag, msg);
        }
    }

    public static void d(String msg){
        if (debug){
            Log.d(TAG, msg);
        }
    }

    public static void dTag(String tag,String msg){
        if (debug){
            Log.d(TAG+"—"+tag, msg);
        }
    }

    public static void w(String msg){
        if (debug){
            Log.w(TAG, msg);
        }
    }

    public static void wTag(String tag,String msg){
        if (debug){
            Log.w(TAG+"—"+tag, msg);
        }
    }

}
