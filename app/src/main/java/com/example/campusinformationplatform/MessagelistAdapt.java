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

public class MessagelistAdapt extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    ArrayList<HashMap<String, Object>> listItem;

    public MessagelistAdapt(Context context, ArrayList<HashMap<String, Object>> listItem) {
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
        public ImageView MessageUserheadImg;
        public TextView MessageUsername;
        public TextView MessageReleasedate;
        public TextView Message;

    }//声明item结构


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MessagelistAdapt.itemStruct zj ;
        if(convertView == null)
        {
            zj = new MessagelistAdapt.itemStruct();
            convertView = mInflater.inflate(R.layout.messagelist_item, null);

            zj.MessageUserheadImg=(ImageView)convertView.findViewById(R.id.MessageItem_UserHeadImg);
            zj.MessageUsername=(TextView)convertView.findViewById(R.id.MessageItem_UserName);
            zj.MessageReleasedate=(TextView)convertView.findViewById(R.id.MessageItem_ReleaseDate);
            zj.Message=(TextView)convertView.findViewById(R.id.MessageItem_Message);

            convertView.setTag(zj);
        }
        else {
            zj = (MessagelistAdapt.itemStruct)convertView.getTag();

        }

        if( listItem.get(position).get("MessageUserheadImg")!=null)
            zj.MessageUserheadImg.setImageBitmap((Bitmap) listItem.get(position).get("MessageUserheadImg"));
        zj.MessageUsername.setText((String) listItem.get(position).get("MessageUsername"));
        zj.MessageReleasedate.setText((String) listItem.get(position).get("MessageReleasedate"));
        zj.Message.setText((String) listItem.get(position).get("Message"));

        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

}

