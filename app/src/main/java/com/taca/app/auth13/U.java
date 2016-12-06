package com.taca.app.auth13;

import android.provider.Settings;
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
        Log.i(TAG,""+msg);
    }

    //현재시간,클라이언트의 시간
    public String curTm(){
        return System.currentTimeMillis()+"";
    }
    public long curTmEx(){
        return System.currentTimeMillis();
    }
}
