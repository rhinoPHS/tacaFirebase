package com.taca.app.auth13;

import android.app.Application;

import com.miguelbcr.ui.rx_paparazzo.RxPaparazzo;

/**
 * Created by a on 2016-12-06.
 */

public class MyApplication extends Application{
    @Override public void onCreate() {
        super.onCreate();
        RxPaparazzo.register(this);
    }
}
