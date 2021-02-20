package com.example.campusinformationplatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonalpageFunctionAdapt extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    ArrayList<HashMap<String, Object>> listItem;

    public PersonalpageFunctionAdapt(Context context, ArrayList<HashMap<String, Object>> listItem) {
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
        public ImageView Functionicon;
        public TextView Functiontext;

    }//声明item结构


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        PersonalpageFunctionAdapt.itemStruct zj ;
        if(convertView == null)
        {
            zj = new PersonalpageFunctionAdapt.itemStruct();
            convertView = mInflater.inflate(R.layout.personalfunction_item, null);

            zj.Functionicon=(ImageView)convertView.findViewById(R.id.functionimg_item);
            zj.Functiontext=(TextView)convertView.findViewById(R.id.functiontext_item);


            convertView.setTag(zj);
        }
        else {
            zj = (PersonalpageFunctionAdapt.itemStruct)convertView.getTag();

        }

        zj.Functionicon.setImageResource( (int)listItem.get(position).get("Functionicon"));
        //zj.Functionicon.setImageBitmap((Bitmap));
        zj.Functiontext.setText((String) listItem.get(position).get("Functiontext"));

        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

}


