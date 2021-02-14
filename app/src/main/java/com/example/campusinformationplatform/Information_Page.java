package com.example.campusinformationplatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Information_Page  extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    private Context context;

    //下拉刷新view
    private PullToRefreshListView PullToRefreshListView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information_page, container,false);

        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path=gv.getCache_Head_Path();

        context=getContext();
        //获取下拉刷新信息
        PullToRefreshListView= (PullToRefreshListView)view.findViewById(R.id.InformationPage_Pull_Refresh_List);


        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> item = new HashMap<String, Object>();

        Bitmap headimg=openImage(Cache_Head_Path+"ice.jpg");

        item.put("Type","Type");
        item.put("UserHeadImg",headimg);
        item.put("UserName","UserName");
        item.put("ReleaseDate","2021-02-09 18:09:53");
        item.put("Describe","Describe");
        item.put("Picrelease",headimg);

        listItem.add(item);

        MainlistAdapt adapt=new MainlistAdapt(context,listItem);


        ListView mListView = PullToRefreshListView.getRefreshableView();
        mListView.setAdapter(adapt);


        return view;
    }

    public static Bitmap openImage(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
