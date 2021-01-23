package com.example.campusinformationplatform;

import android.app.Application;
import android.util.Log;


public class Global_Value extends Application {

    public String localhost;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        this.localhost="192.168.31.139";
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public String getLocalhost(){
        return localhost;
    }


}
