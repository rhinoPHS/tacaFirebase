package com.taca.app.auth13;

import android.content.Context;
import android.content.SharedPreferences;
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
    //리모트 컨피그 : 원격저장소
    //저장소 처리
    //원격으로 들어오는 데이터가 늦게 도착하는것에 대비하여 최초 설정값을
    //기억하여 구동하고 늦게 오더라도 데이트를 받아서 갱신한다.
    String SAVED_NAME = "pref";

    public String getString(Context context, String key){
        return context.getSharedPreferences(SAVED_NAME,0).getString(key,"");
    }
    public void setString(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVED_NAME,0).edit();
        editor.putString(key,value);
        editor.commit();
    }
}
