package com.example.campusinformationplatform;

import android.app.Application;
import android.util.Log;


public class Global_Value extends Application {

    public String Host;
    public int Port;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        this.Host="192.168.31.139";
        this.Port=9999;
    }

    public void setHost(String localhost) {
        this.Host = localhost;
    }

    public String getHost(){
        return Host;
    }

    public  void setPort(int Port){
        this.Port=Port;
    }

    public int getPort(){
        return Port;
    }
}
