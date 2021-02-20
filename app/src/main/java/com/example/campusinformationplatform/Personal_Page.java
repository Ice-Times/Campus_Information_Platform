package com.example.campusinformationplatform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Personal_Page  extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private PersonalpageFunctionAdapt adapt;

    private ListView listview;

    private ImageView PersonalPage_Headimg;
    private TextView PersonalPage_Username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_page, container,false);


        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path=gv.getCache_Head_Path();

        System.out.println("进入person");
        //view.setClickable(true);

        listview =(ListView)view.findViewById(R.id.PersonalPage_Listview);
        listItem = new ArrayList<HashMap<String, Object>>();

        initItem();

        adapt = new PersonalpageFunctionAdapt(getContext(), listItem);
        //setListViewHeightBasedOnChildren(listview);
        listview.setAdapter(adapt);


        PersonalPage_Headimg=(ImageView)view.findViewById(R.id.PersonalPage_Headimg);
        PersonalPage_Username=(TextView) view.findViewById(R.id.PersonalPage_Username);

        PersonalPage_Headimg.setImageBitmap(openImage(Cache_Head_Path+gv.getUserName()+".jpg"));



        return view;
    }

    private void initItem(){
        HashMap<String, Object> Releaseitem= new HashMap<String, Object>();
        Releaseitem.put("Functionicon",R.mipmap.ic1);
        Releaseitem.put("Functiontext","我的发布");

        HashMap<String, Object> Messageitem= new HashMap<String, Object>();
        Messageitem.put("Functionicon",R.mipmap.ic2);
        Messageitem.put("Functiontext","我的留言");

        listItem.add(Releaseitem);
        listItem.add(Messageitem);

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
