package com.example.quangtv.xmppdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by QuangTV on 11/10/15.
 */
public class DummyApplication extends Application{
    public static Context mContext;

    /**
     * Static class to get app context everywhere.
     * Use it carefully if or you will get memory leak
     * @return Context
     */
    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
