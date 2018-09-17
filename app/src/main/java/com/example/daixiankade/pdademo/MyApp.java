package com.example.daixiankade.pdademo;

import android.app.Application;
import android.content.Context;

/**
 * Created by daixiankade on 2018/9/10.
 */

public class MyApp extends Application {

    public static Context mctx;

    @Override
    public void onCreate() {
        super.onCreate();
        mctx = this;
    }

    public static Context getContext() {
        return mctx;
    }
}
