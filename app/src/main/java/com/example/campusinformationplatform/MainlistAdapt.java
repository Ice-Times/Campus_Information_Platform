package com.example.campusinformationplatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainlistAdapt extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    ArrayList<HashMap<String, Object>> listItem;

    public MainlistAdapt(Context context, ArrayList<HashMap<String, Object>> listItem) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }//声明构造函数


    @Override
    public int getCount() {
        return listItem.size();
    }//这个方法返回了在适配器中所代表的数据集合的条目数

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }//这个方法返回了数据集合中与指定索引position对应的数据项

    @Override
    public long getItemId(int position) {
        return position;
    }//这个方法返回了在列表中与指定索引对应的行id


    static class itemStruct
    {
        public TextView MainpageType;
        public ImageView MainpageUserheadImg;
        public TextView MainpageUsername;
        public TextView MainpageReleasedate;
        public TextView MainpageTitle;
        public TextView MainpageDescribe;
        public ImageView MainpagePicrelease;
    }//声明item结构


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MainlistAdapt.itemStruct zj ;
        if(convertView == null)
        {
            zj = new MainlistAdapt.itemStruct();
            convertView = mInflater.inflate(R.layout.mainlist_item, null);

            zj.MainpageType=(TextView)convertView.findViewById(R.id.Item_Type);
            zj.MainpageUserheadImg=(ImageView)convertView.findViewById(R.id.Item_UserHeadImg);
            zj.MainpageUsername=(TextView)convertView.findViewById(R.id.Item_UserName);
            zj.MainpageReleasedate=(TextView)convertView.findViewById(R.id.Item_ReleaseDate);
            zj.MainpageTitle=(TextView)convertView.findViewById(R.id.Item_Title);
            zj.MainpageDescribe=(TextView)convertView.findViewById(R.id.Item_Describe);
            zj.MainpagePicrelease=(ImageView)convertView.findViewById(R.id.Item_PicRelease1);


            convertView.setTag(zj);
        }
        else {
            zj = (MainlistAdapt.itemStruct)convertView.getTag();

        }

        zj.MainpageType.setText((String) listItem.get(position).get("Type"));
        zj.MainpageUserheadImg.setImageBitmap((Bitmap) listItem.get(position).get("UserHeadImg"));
        zj.MainpageUsername.setText((String) listItem.get(position).get("UserName"));
        zj.MainpageReleasedate.setText((String) listItem.get(position).get("ReleaseDate"));
        zj.MainpageDescribe.setText((String) listItem.get(position).get("Describe"));
        zj.MainpageTitle.setText((String) listItem.get(position).get("Title"));
        if(listItem.get(position).get("Picrelease")!=null)
            zj.MainpagePicrelease.setImageBitmap((Bitmap) listItem.get(position).get("Picrelease"));


        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

}
