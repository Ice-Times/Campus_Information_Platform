package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ReleaseSelf_Page extends AppCompatActivity {
    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private SelfReleaselistAdapt adapt;

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_self__page);

        listview =(ListView)findViewById(R.id.Self_Release_Listview);
        listItem = new ArrayList<HashMap<String, Object>>();


        HashMap<String, Object> Releaseitem= new HashMap<String, Object>();
        Releaseitem.put("SelfType","失物招领");
        Releaseitem.put("SelfTitle","一台崭新电脑");
        Releaseitem.put("SelfReleasedate","2021-02-19 23:28:25");
        Releaseitem.put("SelfDescribe","一台崭新电脑");



        listItem.add(Releaseitem);


        adapt = new SelfReleaselistAdapt(this, listItem);
        //setListViewHeightBasedOnChildren(listview);
        listview.setAdapter(adapt);
    }
}
