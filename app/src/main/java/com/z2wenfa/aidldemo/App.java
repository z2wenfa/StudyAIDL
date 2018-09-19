package com.z2wenfa.aidldemo;

import android.app.Application;

public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static Application getInstance(){
        return application;
    }

}
