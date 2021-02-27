package com.example.campusinformationplatform;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class Global_Value extends Application {

    public String Host;
    public int Port;
    public String Cache_Path;
    public String Cache_Head_Path;
    public String Cache_Temp_PATH;


    public String UserName;

    public String SchoolName;

    public String Details_Releaseid;
    public ArrayList<Uri> EnlargeImage;

    public String Update_Releaseid;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        this.Host="192.168.31.139";
        this.Port=9999;
        Cache_Path=this.getExternalCacheDir().getPath();

        Cache_Head_Path=Cache_Path+"/head/";
        Cache_Temp_PATH=Cache_Path+"/temp/";

        try {
            File destDir = new File(Cache_Head_Path);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            destDir = new File(Cache_Temp_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            //Log.d("", "创建成功");

            UserName="";
            Details_Releaseid="";

            Update_Releaseid="";
        }catch (Exception e) {
            e.printStackTrace();
        }


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

    public String getCache_Head_Path(){
        return Cache_Head_Path;
    }

    public String getCache_Temp_PATH() {
        return Cache_Temp_PATH;
    }

    public ArrayList<Uri> getEnlargeImage(){
        return EnlargeImage;
    }

    public void setEnlargeImage(ArrayList<Uri> EnlargeImage){

        this.EnlargeImage=(ArrayList<Uri>)EnlargeImage.clone();
    }

    public void setUserName(String UserName){

        this.UserName=UserName;
    }
    public String getUserName(){
        return UserName;
    }

    public void setDetails_Releaseid(String Details_Releaseid){
        this.Details_Releaseid=Details_Releaseid;
    }
    public  String getDetails_Releaseid(){
        return Details_Releaseid;
    }

    public void setSchoolName(String Schoolname){

        this.SchoolName=Schoolname;
    }
    public String getSchoolName(){
        return SchoolName;
    }

    public void setUpdate_Releaseid(String s){
        Update_Releaseid=s;
    }
    public  String getUpdate_Releaseid(){
        return Update_Releaseid;
    }

}
