package com.taca.app.auth13;

import android.util.Log;

/**
 * Created by a on 2016-12-05.
 */
public class U {
    private static U ourInstance = new U();

    public static U getInstance() {
        return ourInstance;
    }

    private U() {
    }
    //로그 출력용
    String TAG = "FB";
    public void log(String msg){
        Log.i(TAG,msg);
    }
}
