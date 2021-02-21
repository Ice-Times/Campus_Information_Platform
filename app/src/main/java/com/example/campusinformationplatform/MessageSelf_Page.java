package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageSelf_Page extends AppCompatActivity {

    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private PersonalpageFunctionAdapt adapt;

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_self__page);


        listview =(ListView)findViewById(R.id.Self_Release_Listview);
        listItem = new ArrayList<HashMap<String, Object>>();


        HashMap<String, Object> Releaseitem= new HashMap<String, Object>();
        Releaseitem.put("Functionicon",R.mipmap.releaseicon);
        Releaseitem.put("Functiontext","我的发布");

        HashMap<String, Object> Messageitem= new HashMap<String, Object>();
        Messageitem.put("Functionicon",R.mipmap.messageicon);
        Messageitem.put("Functiontext","我的留言");

        HashMap<String, Object> Logoutitem= new HashMap<String, Object>();
        Logoutitem.put("Functionicon",R.mipmap.logouticon);
        Logoutitem.put("Functiontext","退出登录");

        listItem.add(Releaseitem);
        listItem.add(Messageitem);
        listItem.add(Logoutitem);

        adapt = new PersonalpageFunctionAdapt(this, listItem);
        //setListViewHeightBasedOnChildren(listview);
        listview.setAdapter(adapt);

    }
}
