package com.example.campusinformationplatform;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;


public class Global_Value extends Application {

    public String Host;
    public int Port;
    public String Cache_Path;

    public ArrayList<Uri> EnlargeImage;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        this.Host="192.168.31.139";
        this.Port=9999;
        Cache_Path=this.getExternalCacheDir().getPath();

        EnlargeImage=new ArrayList<Uri>();

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

    public String getCachePath(){
        return Cache_Path;
    }

    public ArrayList<Uri> getEnlargeImage(){

        return getEnlargeImage();
    }

    public void setEnlargeImage(ArrayList<Uri> EnlargeImage){

        this.EnlargeImage=(ArrayList<Uri>)EnlargeImage.clone();
    }
}
